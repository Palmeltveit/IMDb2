// 1. Finne navnet på alle rollene en gitt skuespiller har.
// 2. Finne hvilke filmer som en gitt skuespiller opptrer i.
// 3. Finne hvilke filmselskap som lager flest filmer inne hver sjanger (grøssere, familie, o.l.).
// 4. Sette inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
// 5. Sette inn ny anmeldelse av en episode av en serie.

import DB.DBConnection;
import com.mysql.cj.protocol.ResultsetRow;
import jdk.jfr.Category;
import models.*;
import models.crew.CrewMember;
import models.crew.CrewTypes;
import models.crew.Skuespiller;
import models.reactions.Rating;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;


public class TextUserInterface {
    private DBConnection conn;
    private Scanner scanner;

    public TextUserInterface(DBConnection connection) {
        conn = connection;
        scanner = new Scanner(System.in);
    }

    static private void p(String s) { System.out.print(s); }
    static private void pl(String s) { System.out.println(s); }
    static private void pl() { System.out.println(); }

    Optional<Person> findPerson() {
        p("Enter name of person: ");
        String name = scanner.nextLine();
        pl("You entered: " + name);

        return Person.findByName(conn.getConn(), name);
    }

    void findAllRolesForActor() {
        pl("> Find all roles for actor");
        Optional<Person> personOptional = findPerson();
        if (personOptional.isEmpty()) {
            pl("Unable to find a matching person...");
            return;
        }

        pl("==== Roles ====");
        Person p = personOptional.get();
        p.findAllActorRoles(conn.getConn()).stream().forEach(s -> {
            IFilm f = s.getFilm().orElseGet(() -> s.getEpisode().get());
            System.out.println(s.getRolle() + " in " + (f == null ? "NONE" : f.toString()));
        });
    }

    void findAllActorMovieAppearences() {
        pl("> Find all actor movie appearences");
        Optional<Person> personOptional = findPerson();
        if (personOptional.isEmpty()) {
            pl("Unable to find a matching person...");
            return;
        }

        pl("==== Movie appearences ====");
        Person p = personOptional.get();
        p.findAllActorRoles(conn.getConn()).stream()
                .map(Skuespiller::getFilm)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(film -> pl(film.toString()));
    }

    // Shitty code
    void findProductionCompanyWithMostMoviesPerGenre() {
        pl("> Find production company with most movies produced per genre");
        Map<Kategori, Long> m = new HashMap<>();
        Map<Kategori, Integer> mCount = new HashMap<>();

        List<Kategori> categories = Kategori.findAllCategories(conn.getConn());
        categories.forEach(category -> {
            List<Film> filmsByCategory = category.findAllFilmsByCategory(conn.getConn());
            Map<Long, Integer> countMap = new HashMap<>();
            filmsByCategory.forEach(film -> {
                Produksjonsselskap p = film.getProduksjonsselskap();
                final int newCount = countMap.getOrDefault(p.getID(), 0) + 1;
                countMap.put(p.getID(), newCount);
            });

            Long highestFilmCountProducerID = null;
            for (Map.Entry<Long, Integer> e : countMap.entrySet()) {
                Long key = e.getKey();
                if (countMap.getOrDefault(highestFilmCountProducerID, 0)
                        < countMap.get(key)) {
                    highestFilmCountProducerID = key;
                }
            }
            if (highestFilmCountProducerID != null) {
                m.put(category, highestFilmCountProducerID);
                mCount.put(category, countMap.get(highestFilmCountProducerID));
            }
        });

        pl("==== Genre --> Production company with most films of the genre =====");
        m.entrySet().stream().forEach(e -> {
            Produksjonsselskap p = new Produksjonsselskap(e.getValue());
            p.initialize(conn.getConn());
            pl(e.getKey().getNavn() + " -- " + p.getNavn() + " (" + mCount.get(e.getKey()) + ")");
        });
    }

    class FindBy<T> {
        public List<T> findAllByIds(Connection conn, String query, long[] ids, Function<ResultSet, T> fromRow) {
            List<T> elems = new ArrayList<>();
            try ( PreparedStatement stmt = conn.prepareStatement(query); ) {
                for (int i = 0; i < ids.length; i++)
                    stmt.setLong(i+1, ids[i]);
                System.out.println("SQL: " + stmt.toString());
                try ( ResultSet rs = stmt.executeQuery(); ) {
                    while (rs.next()) {
                        T e = fromRow.apply(rs);
                        assert e != null;
                        System.out.println("got row: " + e);
                        elems.add(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return elems;
        }

        public Optional<T> findByIds(Connection conn, String query, long[] ids, Function<ResultSet, T> fromRow) {
            return findAllByIds(conn, query, ids, fromRow).stream().findFirst();
        }

        public List<T> findAllByLike(Connection conn, String query, String likeString, Function<ResultSet, T> fromRow) {
            List<T> elems = new ArrayList<>();
            try ( PreparedStatement stmt = conn.prepareStatement(query); ) {
                stmt.setString(1, likeString);
                System.out.println("SQL: " + stmt.toString());
                try ( ResultSet rs = stmt.executeQuery(); ) {
                    while (rs.next()) {
                        T e = fromRow.apply(rs);
                        assert e != null;
                        System.out.println("got row: " + e);
                        elems.add(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return elems;
        }

        public Optional<T> findByLike(Connection conn, String query, String likeString, Function<ResultSet, T> fromRow) {
            return findAllByLike(conn, query, likeString, fromRow).stream().findFirst();
        }
    }


    static private List<Integer> findIDsByLike(Connection conn, String query, String likeString, String IDField) {
        List<Integer> ids = new ArrayList<>();
        try ( PreparedStatement stmt = conn.prepareStatement(query); ) {
            stmt.setString(1, likeString);
            System.out.println("SQL: " + stmt.toString());
            try ( ResultSet rs = stmt.executeQuery(); ) {
                while (rs.next()) {
                    System.out.println("got row");
                    ids.add(rs.getInt(IDField));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    Person findOrCreatePerson(String name) {
        FindBy<Person> findByPerson = new FindBy<>();
        Optional<Person> personOptional = findByPerson.findByLike(conn.getConn(),
                //"SELECT `Person`.ID, `Person`.Navn, `Person`.Fødselsland, `Person`.Fødselsår, Film FROM `FilmRegissør` " +
                //                       "INNER JOIN `Person` ON `FilmRegissør`.Person = `Person`.ID " +
                //                       "WHERE `Person`.Navn LIKE ?",
                "SELECT `Person`.ID, `Person`.Navn, `Person`.Fødselsland, `Person`.Fødselsår FROM `Person`" +
                                       "WHERE `Person`.Navn LIKE ?",
                name,
                rs -> {
                    try {
                        return new Person(rs.getLong("ID"), rs.getString("Navn"),
                                rs.getString("Fødselsland"),
                                rs.getInt("Fødselsår"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        Person p = personOptional.orElseGet(() -> {
            pl("No such person, creating...");
            p("Enter birth country: ");
            String birthCountry = scanner.nextLine();
            p("Enter birth year: ");
            int birthYear = scanner.nextInt();
            scanner.nextLine();
            Person person = new Person(name, birthCountry, birthYear);
            person.save(conn.getConn());
            return person;
        });
        pl(p.getID() + ": " + p.getNavn() + " " + p.getFodselsland() + " " + p.getFodselsar());
        return p;
    }

    Kategori findOrCreateCategory(String name) {
        FindBy<Kategori> findByCategory = new FindBy<>();
        Optional<Kategori> kategoriOptional = findByCategory.findByLike(conn.getConn(),
                "SELECT `Kategori`.ID, `Kategori`.Navn, `Kategori`.Beskrivelse FROM `Kategori`" +
                        "WHERE `Kategori`.Navn LIKE ?",
                name,
                rs -> {
                    try {
                        return new Kategori(rs.getInt("ID"),
                                rs.getString("Navn"),
                                rs.getString("Beskrivelse"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        Kategori k = kategoriOptional.orElseGet(() -> {
            pl("No such category, creating...");
            p("Enter description: ");
            String description = scanner.nextLine();
            Kategori kategori = new Kategori(name, description);
            kategori.save(conn.getConn());
            return kategori;
        });
        pl(k.getID() + ": " + k.getNavn() + " " + k.getBeskrivelse());
        return k;
    }

    Produksjonsselskap findOrCreateProductionCompany(String name) {
        FindBy<Produksjonsselskap> findByProductionCompany = new FindBy<>();
        Optional<Produksjonsselskap> produksjonsselskapOptional = findByProductionCompany.findByLike(conn.getConn(),
                "SELECT ID, Navn, Opprettet FROM `Produksjonsselskap`" +
                        "WHERE Navn LIKE ?",
                name,
                rs -> {
                    try {
                        return new Produksjonsselskap(rs.getInt("ID"),
                                rs.getString("Navn"),
                                Date.valueOf(rs.getString("Opprettet")));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        Produksjonsselskap p = produksjonsselskapOptional.orElseGet(() -> {
            pl("No such production company, creating...");
            p("Enter date of creation: ");
            String creationDate = scanner.nextLine();
            Produksjonsselskap produksjonsselskap = new Produksjonsselskap(name, Date.valueOf(creationDate));
            produksjonsselskap.save(conn.getConn());
            return produksjonsselskap;
        });
        pl(p.getID() + ": " + p.getNavn() + " " + p.getOpprettet());
        return p;
    }


    void insertNewMovie() {
        pl("> Insert new movie");
        p("Ener movie title: ");
        String title = scanner.nextLine();

        p("Ener movie description: ");
        String description = scanner.nextLine();

        p("Enter movie duration: ");
        int duration = scanner.nextInt();
        scanner.nextLine();

        p("Enter movie release year: ");
        int releaseYear = scanner.nextInt();
        scanner.nextLine();

        p("Enter movie release date: ");
        String releaseDateString = scanner.nextLine();
        Date releaseDate = Date.valueOf(releaseDateString);

        p("Enter production company name: ");
        String productionCompanyName = scanner.nextLine();
        Produksjonsselskap produksjonsselskap = findOrCreateProductionCompany(productionCompanyName);


        Map<String, FilmLagetFor> originallyMadeForMap = new HashMap<>();
        originallyMadeForMap.put("CINEMA", FilmLagetFor.KINO);
        originallyMadeForMap.put("TV", FilmLagetFor.TV);
        originallyMadeForMap.put("FILM", FilmLagetFor.FILM);
        String inpMadeFor = null;
        do {
            if (inpMadeFor == null) p("Not a valid choice (one of CINEMA, TV, FILM): ");
            else p("Enter film made for originally (one of: CINEMA, TV, FILM): ");
            inpMadeFor = scanner.nextLine();
        } while (!originallyMadeForMap.containsKey(inpMadeFor.toUpperCase()));

        FilmLagetFor madeFor = originallyMadeForMap.get(inpMadeFor);

        pl("Creating movie entry...");
        Film film = new Film(produksjonsselskap, title, duration,
                releaseYear, releaseDate, description, madeFor.ord());
        film.save(conn.getConn());


        List<Person> directors = new ArrayList<>();
        while (true) {
            pl(directors.size() == 0 ? "Enter director name" : "Enter another director name or NEXT");
            String input = scanner.nextLine().strip();
            System.out.println("READ: '" + input + "'");
            if (input.equals("NEXT")) break;
            // directors.add(findOrCreatePerson(input));
            Person p = findOrCreatePerson(input);
            System.out.println("Person: " + p.getNavn() + ", " + p.getID());
            CrewMember member = new CrewMember(p, film, CrewTypes.REGISSOR);
            System.out.println(member.getPerson().getNavn() + ", " + member.getPerson().getID());

        }
        // directors.forEach(director ->
        //         (new CrewMember(director, film, CrewTypes.REGISSOR)).save(conn.getConn()));

        List<Kategori> categories = new ArrayList<>();
        while (true) {
            pl(categories.size() == 0 ? "Enter category name" : "Enter another category name or NEXT");
            String input = scanner.nextLine().strip();
            System.out.println("READ: '" + input + "'");
            if (input.equals("NEXT")) break;
            categories.add(findOrCreateCategory(input));
        }
        categories.forEach(category ->
                film.addCategory(conn.getConn(), category));

        List<Person> scriptWriters = new ArrayList<>();
        while (true) {
            pl(scriptWriters.size() == 0 ? "Enter scriptwriter name" : "Enter another scriptwriter name or NEXT");
            String input = scanner.nextLine().strip();
            System.out.println("READ: '" + input + "'");
            if (input.equals("NEXT")) break;
            scriptWriters.add(findOrCreatePerson(input));
        }
        scriptWriters.forEach(scriptWriter ->
                film.addCrewMember(conn.getConn(), new CrewMember(scriptWriter, film, CrewTypes.MANUSFORFATTER)));

        List<Person> actors = new ArrayList<>();
        while (true) {
            pl(actors.size() == 0 ? "Enter actor name" : "Enter another actor name or NEXT");
            String input = scanner.nextLine().strip();
            System.out.println("READ: '" + input + "'");
            if (input.equals("NEXT")) break;
            actors.add(findOrCreatePerson(input));
        }
        actors.forEach(actor -> {
                Skuespiller s = new Skuespiller(actor, film, "Han spilte der");
                s.save(conn.getConn());
            }
        );

        film.refresh(conn.getConn());
    }

    Optional<Serie> findSerie(String seriesTitle) {
        FindBy<Serie> findBySerie = new FindBy<>();
        Optional<Serie> serieOptional = findBySerie.findByLike(conn.getConn(),
                "SELECT `Serie`.ID FROM `Serie` " +
                        "INNER JOIN `Film` on `Serie`.FilmID = `Film`.ID " +
                        "WHERE Tittel LIKE ?",
                seriesTitle,
                rs -> {
                    try {
                        Serie e = new Serie(rs.getInt("ID"));
                        e.initialize(conn.getConn());
                        return e;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        return serieOptional;
    }

    Optional<Episode> findEpisode(long serieID, int seasonNumber, int episodeNumber) {
        FindBy<Episode> findByEpisode = new FindBy<>();
        Optional<Episode> episodeOptional = findByEpisode.findByIds(conn.getConn(),
                "SELECT `Episode`.ID FROM `Episode` " +
                        "INNER JOIN `Serie` on `Serie`.ID = `Episode`.SerieID " +
                        "INNER JOIN `Film` ON `Serie`.FilmID = `Film`.ID " +
                        // "WHERE `Serie`.ID = ?",
                        "WHERE `Serie`.ID = ? AND `Episode`.SesongNr = ? AND `Episode`.EpisodeNr = ?",
                // new long[] {serieID},
                new long[] {serieID, seasonNumber, episodeNumber},
                rs -> {
                    try {
                        Episode e = new Episode(rs.getInt("ID"));
                        e.initialize(conn.getConn());
                        return e;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        return episodeOptional;
    }

    void insertNewReview() {
        pl("> Insert new review");

        pl("Authenticate to proceed");
        p("Enter username: ");
        String username = scanner.nextLine();
        p("Enter password: ");
        String password = scanner.nextLine();
        Bruker user = new Bruker(username, password);
        user.initialize(conn.getConn());

        if (!user.isLoggedIn()) {
            pl("Invalid credentials");
            return;
        }
        pl("Logged in as: " + user.getBrukernavn());


        p("Find series by name: ");
        String seriesNameLike = scanner.nextLine();

        Optional<Serie> serieOptional = findSerie(seriesNameLike);
        if (serieOptional.isEmpty()) {
            pl("No such series... returning to main menu");
            return;
        }
        Serie s = serieOptional.get();
        pl("Found series: " + s.getID() + ": " + s.getTittel() + " " + s.getBeskrivelse());

        p("Enter season number: ");
        int seasonNumber = scanner.nextInt();
        scanner.nextLine();
        p("Enter episode number: ");
        int episodeNumber = scanner.nextInt();
        scanner.nextLine();

        Optional<Episode> episodeOptional = findEpisode(s.getID(), seasonNumber, episodeNumber);
        if (episodeOptional.isEmpty()) {
            pl("No such episode... returning to main menu");
            return;
        }
        Episode e = episodeOptional.get();
        pl("Found episode: " + e.getEpisodeNr() + " " + e.getSesongNr() + " " + e.getID());

        p("Enter rating title: ");
        String title = scanner.nextLine();

        p("Enter rating content: ");
        String content = scanner.nextLine();

        p("Enter rating number: ");
        int ratingNumber = scanner.nextInt();
        scanner.nextLine();

        Rating episodeRating = new Rating(user, e, title, content, ratingNumber);
        episodeRating.save(conn.getConn());

        episodeRating.refresh(conn.getConn());

        pl("Added new episode rating with ID " + episodeRating.getID() + " " + episodeRating.getTittel());
    }

    public void showMenu() {
        boolean quit = false;
        do {
            pl("IMDB2 Management Utility v0.0.1 - Copyright IMDB2 Management Inc. 1985");
            pl(" 1. Find all roles for a given actor");
            pl(" 2. Find all movies a given actor appear in");
            pl(" 3. Find which movie production company produce the most movies in each genre");
            pl(" 4. Insert new move with director, scriptwriters, actors,...");
            pl(" 5. Insert a new review for an episode of a series");
            pl(" 6. Quit");
            pl();
            pl();

            p("Choose option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: findAllRolesForActor(); break;
                case 2: findAllActorMovieAppearences(); break;
                case 3: findProductionCompanyWithMostMoviesPerGenre(); break;
                case 4: insertNewMovie(); break;
                case 5: insertNewReview(); break;
                case 6: quit = true; break;
                default: pl("Not a valid option, try again."); break;
            }
        } while (!quit);

    }

}
