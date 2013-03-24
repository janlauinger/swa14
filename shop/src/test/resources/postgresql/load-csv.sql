SET search_path TO ${schema.postgresql};
SET datestyle TO 'DMY';

--   Voraussetzungen fuer COPY:
--   1) Superuser der Datenbank
--   2) Absoluter Pfadname fuer Datei oder relativ zu %PGDATA%
		
COPY kunde
	FROM 'C:/Software/eclipse-git-repository/swa08/shop/src/test/resources/postgresql/kunde.csv'
	WITH (
		DELIMITER ';',
		FORMAT csv,
		HEADER);
		
COPY adresse
	FROM 'C:/Software/eclipse-git-repository/swa08/shop/src/test/resources/postgresql/adresse.csv'
	WITH (
		DELIMITER ';',
		FORMAT csv,
		HEADER);

COPY bestellung
	FROM 'C:/Software/eclipse-git-repository/swa08/shop/src/test/resources/postgresql/bestellung.csv'
	WITH (
		DELIMITER ';',
		FORMAT csv,
		HEADER);

COPY produkt
	FROM 'C:/Software/eclipse-git-repository/swa08/shop/src/test/resources/postgresql/produkt.csv'
	WITH (
		DELIMITER ';',
		FORMAT csv,
		HEADER);

COPY bestellposition
	FROM 'C:/Software/eclipse-git-repository/swa08/shop/src/test/resources/postgresql/bestellposition.csv'
	WITH (
		DELIMITER ';',
		FORMAT csv,
		HEADER);