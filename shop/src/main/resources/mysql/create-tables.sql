USE ${dbname.mysql};
SHOW WARNINGS;

DROP TABLE IF EXISTS kunde;
CREATE TABLE kunde(
	kunden_id SERIAL NOT NULL PRIMARY KEY,
	name NVARCHAR(32) NOT NULL,
	vorname NVARCHAR(32),
	geschlecht INTEGER,
	kundenart integer,
	newsletter TINYINT(1),
	email NVARCHAR(128) NOT NULL UNIQUE,
	passwort NVARCHAR(256),
	adress_fk bigint NOT NULL REFERENCES adresse(adresse_id),
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX kunde_email ON kunde(email);

DROP TABLE IF EXISTS adresse;
CREATE TABLE adresse(
	adresse_id SERIAL NOT NULL PRIMARY KEY,
	plz CHAR(5) NOT NULL,
	strasse NVARCHAR(32),
	ort NVARCHAR(32) NOT NULL,
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL
);
CREATE INDEX adresse_kunde_index ON kunde(adress_fk);


DROP TABLE IF EXISTS bestellung;
CREATE TABLE bestellung(
	bestell_id SERIAL NOT NULL PRIMARY KEY,
	bestelldatum Date not Null,
	bezahlart integer,
	lieferart integer,
	gesamtpreis double not null,
	status integer,
	kunde_fk integer NOT NULL REFERENCES kunde(kunden_id),
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL

);
CREATE INDEX bestellung_kunde_index ON bestellung(kunde_fk);



DROP TABLE IF EXISTS produkt;
CREATE TABLE produkt(
	produkt_id SERIAL NOT NULL PRIMARY KEY,
	bezeichnung NVARCHAR(32) NOT NULL,
	preis DOUBLE,
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS bestellposition;
CREATE TABLE bestellposition(
	position_id SERIAL NOT NULL PRIMARY KEY,
	anzahl SMALLINT NOT NULL,
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL,
	bestell_fk BIGINT NOT NULL REFERENCES bestellung(bestell_id),
	produkt_fk BIGINT NOT NULL REFERENCES produkt(produkt_id)
);


CREATE INDEX bestpos_bestellung_index ON bestellposition(bestell_fk);
CREATE INDEX bestpos_produkt_index ON bestellposition(produkt_fk);