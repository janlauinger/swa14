--PostgreSQL


CREATE TABLE IF NOT EXISTS kunde(
	kunden_id BIGSERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(32) NOT NULL,
	vorname VARCHAR(32) NOT NULL,
	geschlecht char(1) NOT NULL,
	registrierdatum DATE NOT NULL,
	kundenart CHAR(1) NOT NULL,
	newsletter BOOLEAN NOT NULL,
	email VARCHAR(128) NOT NULL UNIQUE,
	passwort VARCHAR(256) NOT NULL,
	--adress_fk Bigint NOT NULL REFERENCES adresse(adress_id),
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL
);
CREATE TABLE adresse(
	adress_id BIGSERIAL NOT NULL PRIMARY KEY,
	plz VARCHAR(5) NOT NULL,
	strasse VARCHAR(64),
	ort VARCHAR(64),
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL,
	kunde_fk bigint not null references kunde(kunden_id)
);

CREATE TABLE IF NOT EXISTS bestellung(
	bestell_id BIGSERIAL NOT NULL PRIMARY KEY,
	bezahlart varchar(32) NOT NULL,
	lieferart varchar(32) NOT NULL,
	gesamtpreis integer NOT NULL,
	status VARCHAR(32),
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL,
	idx SMALLINT,
--	kunde_fk bigint NOT NULL REFERENCES kunde(kunden_id)
	kunde_fk bigint not null references kunde(kunden_id)
);

CREATE TABLE IF NOT EXISTS produkt(
	produkt_id BIGSERIAL NOT NULL PRIMARY KEY,
	bezeichnung VARCHAR(256) NOT NULL,
	preis integer NOT NULL,
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS bestellposition(
	position_id BIGSERIAL NOT NULL PRIMARY KEY,
	anzahl SMALLINT NOT NULL,
	idx SMALLINT,
	erzeugt TIMESTAMP NOT NULL,
	aktualisiert TIMESTAMP NOT NULL,
	bestell_fk BIGINT NOT NULL REFERENCES bestellung(bestell_id),
	produkt_fk BIGINT NOT NULL REFERENCES produkt(produkt_id)
);
CREATE INDEX adresse_kunde_index ON adresse(kunde_fk);
CREATE INDEX bestellung_kunde_index ON bestellung(kunde_fk);
CREATE INDEX bestpos_best_index ON bestellposition(bestell_fk);
CREATE INDEX bestpos_produkt_index ON bestellposition(produkt_fk);

