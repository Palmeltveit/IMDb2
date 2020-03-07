kiUSE
SELECT * FROM `EpisodeSkuespiller` WHERE `Person` = 21;

SELECT ID, Tittel FROM `Film` INNER JOIN `FilmKategori` ON ID = FILM;

INSERT INTO `Kategori` (`Beskrivelse`, `Navn`) VALUES ('Ekle filmer', 'Grøsser');
INSERT INTO `Kategori` (`Beskrivelse`, `Navn`) VALUES ('Ufyselige filmer', 'Romantisk komedie');
INSERT INTO `Kategori` (`Beskrivelse`, `Navn`) VALUES ('Bra', 'Thriller');
INSERT INTO `Kategori` (`Beskrivelse`, `Navn`) VALUES ('Artig', 'Komedie');


SELECT Film.ID, Tittel, Kategori.Navn FROM `Film` INNER JOIN `FilmKategori` ON `Film`.ID = `FilmKategori`.Film INNER JOIN `Kategori` ON `FilmKategori`.Kategori = `Kategori`.ID;

INSERT INTO `FilmKategori` (`Kategori`, `Film`) VALUE (3, 1)


SELECT * From `Kategori`;

