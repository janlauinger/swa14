SET search_path TO ${schema.postgresql};

ALTER SEQUENCE kunde_kunden_id_seq RESTART WITH 6;
ALTER SEQUENCE adresse_adress_id_seq RESTART WITH 106;
ALTER SEQUENCE bestellung_bestell_id_seq RESTART WITH 16;
ALTER SEQUENCE produkt_produkt_id_seq RESTART WITH 510;
ALTER SEQUENCE bestellposition_position_id_seq RESTART WITH 210;