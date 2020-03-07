// 1. Finne navnet på alle rollene en gitt skuespiller har.
// 2. Finne hvilke filmer som en gitt skuespiller opptrer i.
// 3. Finne hvilke filmselskap som lager flest filmer inne hver sjanger (grøssere, familie, o.l.).
// 4. Sette inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
// 5. Sette inn ny anmeldelse av en episode av en serie.

import DB.DBConnection;
import jdk.jfr.Category;
import models.Film;
import models.Kategori;
import models.Person;
import models.Produksjonsselskap;
import models.crew.Skuespiller;

import javax.swing.text.html.Option;
import java.util.*;


public class TextUserInterface {
    private DBConnection conn;
    private Scanner scanner;

    public TextUserInterface(DBConnection connection) {
        conn = connection;
        scanner = new Scanner(System.in);
    }

    private void p(String s) { System.out.print(s); }
    private void pl(String s) { System.out.println(s); }
    private void pl() { System.out.println(); }

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

    void insertNewMovie() {

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
