for x in drop.sql create_db.sql testdata.sql; do mysql --user=jbr --password=`pass mysql` IMDB2 < "$x"; done
