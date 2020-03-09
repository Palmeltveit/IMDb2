

INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (1, 'Knut', 1984, 'Indonesia') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (2, 'Ove', 1942, 'Norge') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (3, 'Brad Pitt', 1970, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (4, 'Quentin Tarantino', 1963, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (5, 'John Travolta', 1954, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (6, 'Uma Thurman', 1970, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (7, 'Matthew Broderick', 1970, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (8, 'Roger Allers', 1949, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (9, 'Steven Lisberger', 1951, 'USA') ;
INSERT INTO Person (ID, Navn, Fødselsår, Fødselsland) VALUES (10, 'Jeff Bridges', 1949, 'USA') ;

INSERT INTO Bruker (ID, Brukernavn, PassordHash) VALUES (1, 'usr', 'D') ; -- pass=A

INSERT INTO Kategori (ID, Navn, Beskrivelse) VALUES (1, 'Thriller', 'For en thrillende opplevelse') ;
INSERT INTO Kategori (ID, Navn, Beskrivelse) VALUES (2, 'Comedy', 'En beskrivelse av livet ditt') ;
INSERT INTO Kategori (ID, Navn, Beskrivelse) VALUES (3, 'Tragedy', 'Også en beskrivelse av livet ditt') ;
INSERT INTO Kategori (ID, Navn, Beskrivelse) VALUES (4, 'Romcom', 'Absolutt ikke livet ditt') ;
INSERT INTO Kategori (ID, Navn, Beskrivelse) VALUES (5, 'Adventure', 'Heller ikke livet ditt, taper') ;
INSERT INTO Kategori (ID, Navn, Beskrivelse) VALUES (6, 'Crime', 'Muligens livet ditt, narkis') ;

INSERT INTO Produksjonsselskap (ID, Navn, Opprettet) VALUES (1, 'Miramax Films', '1979-01-01') ;
INSERT INTO Produksjonsselskap (ID, Navn, Opprettet) VALUES (2, 'Disney', '1944-01-01') ;
INSERT INTO Produksjonsselskap (ID, Navn, Opprettet) VALUES (3, 'Pixar', '1986-01-01') ;
INSERT INTO Produksjonsselskap (ID, Navn, Opprettet) VALUES (4, 'Universal Pictures', '1912-01-01') ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (1, 1, 'Pulp Fiction', 154, 1994, '1994-04-21', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmKategori (Film, Kategori) VALUES (1, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (1, 6) ;

INSERT INTO FilmSkuespiller(Film, Person, Rolle) VALUES (1, 5, 'Vincent Vega') ;
INSERT INTO FilmRegissør(Film, Person) VALUES (1, 4) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (2, 1, 'Kill Bill', 111, 2003, '2003-10-10', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmSkuespiller(Film, Person, Rolle) VALUES (2, 6, 'The Bride') ;
INSERT INTO FilmRegissør(Film, Person) VALUES (2, 4) ;

INSERT INTO FilmKategori (Film, Kategori) VALUES (2, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (2, 3) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (2, 6) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (3, 2, 'The Lion King', 89, 1994, '1994-01-01', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmSkuespiller(Film, Person, Rolle) VALUES (3, 7, 'Simba') ;
INSERT INTO FilmRegissør(Film, Person) VALUES (3, 8) ;

INSERT INTO FilmKategori (Film, Kategori) VALUES (3, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (3, 4) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (4, 2, 'Tron', 102, 1995, '1995-01-01', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmSkuespiller(Film, Person, Rolle) VALUES (4, 10, 'Kevin Flynn') ;
INSERT INTO FilmRegissør(Film, Person) VALUES (4, 9) ;

INSERT INTO FilmKategori (Film, Kategori) VALUES (4, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (4, 4) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (5, 3, 'WALL-E', 101, 2008, '2008-01-01', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmKategori (Film, Kategori) VALUES (5, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (5, 4) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (6, 3, 'Monsters Inc', 90, 2001, '2001-01-01', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmKategori (Film, Kategori) VALUES (6, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (6, 4) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (7, 3, 'Back to the Future', 116, 1985, '1985-01-01', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmKategori (Film, Kategori) VALUES (7, 2) ;
INSERT INTO FilmKategori (Film, Kategori) VALUES (7, 4) ;

INSERT INTO Film (ID, Produksjonsselskap, Tittel, Lengde, Utgivelsesår, LanseringsDato,
                  Beskrivelse, OpprinneligLagetFor)
                  VALUES (8, 3, 'Schindlers List', 116, 1993, '1993-01-01', 'Bra film', 3) ; -- 1=TV, 2=FILM, 3 = KINO

INSERT INTO FilmKategori (Film, Kategori) VALUES (8, 2) ;
