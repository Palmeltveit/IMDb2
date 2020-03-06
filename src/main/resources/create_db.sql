CREATE TABLE Person(
	ID						INTEGER 		NOT NULL AUTO_INCREMENT,
	Navn					TEXT			NOT NULL,
	Fødselsår 				INTEGER 		NOT NULL,
	Fødselsland 			TEXT			NOT NULL, #TODO: FK?

	PRIMARY KEY (ID)
);

CREATE TABLE Bruker(
	ID 						INTEGER 		NOT NULL AUTO_INCREMENT,
	Brukernavn 				TEXT 			NOT NULL,
	PassordHash				VARCHAR(128)	NOT NULL,

	PRIMARY KEY (ID)
);

CREATE TABLE Kategori(
	ID 						INTEGER 		NOT NULL AUTO_INCREMENT,
	Navn 					TEXT 			NOT NULL,
	Beskrivelse				TEXT			NOT NULL,

	PRIMARY KEY (ID)
);

CREATE TABLE Produksjonsselskap(
	ID						INTEGER 		NOT NULL AUTO_INCREMENT,
	Navn					TEXT			NOT NULL,
	Opprettet				DATE			NOT NULL,
		
	PRIMARY KEY (ID)
);

CREATE TABLE Film(	
	ID 						INTEGER 		NOT NULL AUTO_INCREMENT,
	Produksjonsselskap		INTEGER			NOT NULL,
	Tittel 					TEXT 			NOT NULL,
	Lengde					INTEGER,
	Utgivelsesår			INTEGER,
	LanseringsDato 			DATE,
	Beskrivelse 			TEXT,
	OpprinneligLagetFor		INTEGER,

	PRIMARY KEY (ID),
	FOREIGN KEY (Produksjonsselskap) REFERENCES Produksjonsselskap(ID)
);

CREATE TABLE FilmKategori(
	Film 					INTEGER 		NOT NULL,
	Kategori 				INTEGER 		NOT NULL,

	PRIMARY KEY (Film, Kategori),
	FOREIGN KEY (Film) 		REFERENCES Film(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Kategori) 	REFERENCES Kategori(ID) 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE FilmSkuespiller(
	Film 					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,
	Rolle					TEXT			NOT NULL,

	PRIMARY KEY (Film, Person),
	FOREIGN KEY (Film) 		REFERENCES Film(ID) 		ON DELETE CASCADE ON UPDATE CASCADE ,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE FilmRegissør(
	Film 					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Film, Person),
	FOREIGN KEY (Film) 		REFERENCES Film(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE FilmManusforfatter(
	Film 					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Film, Person),
	FOREIGN KEY (Film) 		REFERENCES Film(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE 
);

CREATE TABLE FilmKomponist(
	Film 					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Film, Person),
	FOREIGN KEY (Film) 		REFERENCES Film(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE FilmMusiker(
	Film 					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Film, Person),
	FOREIGN KEY (Film) 		REFERENCES Film(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Serie(
	ID 						INTEGER 		NOT NULL AUTO_INCREMENT,
	FilmID 					INTEGER			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (FilmID) REFERENCES Film(ID) 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Episode(
	ID 						INTEGER 		NOT NULL AUTO_INCREMENT,
	SerieID					INTEGER			NOT NULL,
	SesongNr				INTEGER			NOT NULL,
	EpisodeNr				INTEGER			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (SerieID) REFERENCES Serie(ID) 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeSkuespiller(
	Episode					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,
	Rolle					TEXT			NOT NULL,

	PRIMARY KEY (Episode, Person),
	FOREIGN KEY (Episode) 	REFERENCES Episode(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeRegissør(
	Episode					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Episode, Person),
	FOREIGN KEY (Episode) 	REFERENCES Episode(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeManusforfatter(
	Episode					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Episode, Person),
	FOREIGN KEY (Episode) 	REFERENCES Episode(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeKomponist(
	Episode					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Episode, Person),
	FOREIGN KEY (Episode) 	REFERENCES Episode(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeMusiker(
	Episode					INTEGER 		NOT NULL,
	Person 					INTEGER 		NOT NULL,

	PRIMARY KEY (Episode, Person),
	FOREIGN KEY (Episode) 	REFERENCES Episode(ID) 		ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (Person) 	REFERENCES Person(ID) 		ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeKommentar(
	ID 						INTEGER			NOT NULL AUTO_INCREMENT,
	BrukerID				INTEGER			NOT NULL,
	EpisodeID				INTEGER			NOT NULL,
	Tittel					TEXT			NOT NULL,
	Innhold 				TEXT			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (BrukerID)  REFERENCES Bruker(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EpisodeID) REFERENCES Episode(ID) 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE SerieKommentar(
	ID 						INTEGER			NOT NULL AUTO_INCREMENT,
	BrukerID				INTEGER			NOT NULL,
	SerieID					INTEGER			NOT NULL,
	Tittel					TEXT			NOT NULL,
	Innhold 				TEXT			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (BrukerID)  REFERENCES Bruker(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (SerieID) 	REFERENCES Serie(ID) 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE FilmKommentar(
	ID 						INTEGER			NOT NULL AUTO_INCREMENT,
	BrukerID				INTEGER			NOT NULL,
	FilmID					INTEGER			NOT NULL,
	Tittel					TEXT			NOT NULL,
	Innhold 				TEXT			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (BrukerID)  REFERENCES Bruker(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (FilmID) 	REFERENCES Film(ID) 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE EpisodeRating(
	ID 						INTEGER			NOT NULL AUTO_INCREMENT,
	BrukerID				INTEGER			NOT NULL,
	EpisodeID				INTEGER			NOT NULL,
	Tittel					TEXT			NOT NULL,
	Innhold 				TEXT			NOT NULL,
	Rating 					INTEGER			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (BrukerID)  REFERENCES Bruker(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (EpisodeID) REFERENCES Episode(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT CheckEpisodeRating CHECK (Rating BETWEEN 1 AND 10)

);

CREATE TABLE SerieRating(
	ID 						INTEGER			NOT NULL AUTO_INCREMENT,
	BrukerID				INTEGER			NOT NULL,
	SerieID					INTEGER			NOT NULL,
	Tittel					TEXT			NOT NULL,
	Innhold 				TEXT			NOT NULL,
	Rating 					INTEGER			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (BrukerID)  REFERENCES Bruker(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (SerieID) 	REFERENCES Serie(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT CheckSeriesRating CHECK (Rating BETWEEN 1 AND 10)
);

CREATE TABLE FilmRating(
	ID 						INTEGER			NOT NULL AUTO_INCREMENT,
	BrukerID				INTEGER			NOT NULL,
	FilmID					INTEGER			NOT NULL,
	Tittel					TEXT			NOT NULL,
	Innhold 				TEXT			NOT NULL,
	Rating 					INTEGER			NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (BrukerID)  REFERENCES Bruker(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (FilmID) 	REFERENCES Film(ID) 	ON DELETE CASCADE ON UPDATE CASCADE,

	CONSTRAINT CheckRating CHECK (Rating BETWEEN 1 AND 10)
);