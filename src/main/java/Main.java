import DB.DBConnection;
import models.Film;
import models.Produksjonsselskap;

import java.sql.Date;

public class Main {

    public static void main(String[] args){
        DBConnection connection = new DBConnection();
        connection.connect();

        Produksjonsselskap produksjonsselskap = new Produksjonsselskap("Pål Productions", new Date(0));
        Film film = new Film(produksjonsselskap, "XXX - Pizza guy getting the job DONE! Hot Stuff!",
                13, 2020, Date.valueOf("2020-01-01"),
                "Pizza guy Pål having a great day at the job!", 1);

        produksjonsselskap.save(connection.getConn());
        film.save(connection.getConn());
    }

}
