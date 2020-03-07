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
        Optional<Person> personOptional = findPerson();
        if (personOptional.isEmpty()) {
            pl("Unable to find a matching person...");
            return;
        }

        pl("==== Roles ====");
        Person p = personOptional.get();
        p.findAllActorRoles(conn.getConn()).stream().forEach(s -> System.out.println(s.getRolle()));
    }

    void findAllActorMovieAppearences() {
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
                .forEach(film -> pl(film.getTittel()));
    }

    void findProductionCompanyWithMostMoviesPerGenre() {
        Map<Kategori, Produksjonsselskap> m = new HashMap<>();

        List<Kategori> categories = Kategori.findAllCategories(conn.getConn());
        categories.forEach(category -> {
            List<Film> filmsByCategory = category.findAllFilmsByCategory(conn.getConn());
            Map<Produksjonsselskap, Integer> countMap = new HashMap<>();
            filmsByCategory.forEach(film -> {
                Produksjonsselskap p = film.getProduksjonsselskap();
                final int newCount = countMap.getOrDefault(p, 1);
                countMap.put(p, newCount);
            });

            Produksjonsselskap highestFilmCountProducer = null;
            for (Map.Entry<Produksjonsselskap, Integer> e : countMap.entrySet()) {
                Produksjonsselskap key = e.getKey();
                if (countMap.getOrDefault(highestFilmCountProducer, 0)
                        < countMap.get(key)) {
                    highestFilmCountProducer = key;
                }
            }
            m.put(category, highestFilmCountProducer);
        });

        pl("==== Genre --> Production company with most films of the genre =====");
        m.entrySet().stream().forEach(e -> {
            pl(e.getKey().getNavn() + " -- " + e.getValue().getNavn());
        });
    }

    class FindBy<T> {
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
                        return new Person(rs.getString("Navn"),
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
            directors.add(findOrCreatePerson(input));
        }
        directors.forEach(director ->
                film.addCrewMember(conn.getConn(), new CrewMember(director, film, CrewTypes.REGISSOR)));

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

    void insertNewReview() {

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
                case 5: insertNewMovie(); break;
                case 6: quit = true; break;
                default: pl("Not a valid option, try again."); break;
            }
        } while (!quit);

    }

}
