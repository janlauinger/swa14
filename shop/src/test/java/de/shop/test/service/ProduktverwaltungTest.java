package de.shop.test.service;

//import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Collection;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shop.artikelverwaltung.domain.Produkt;
import de.shop.artikelverwaltung.service.Produktverwaltung;
import de.shop.test.util.AbstractTest;

@RunWith(Arquillian.class)
public class ProduktverwaltungTest extends AbstractTest {
	private static final Integer PREIS_VORHANDEN = 40;
	private static final Long PRODUKT_VORHANDEN = Long.valueOf(1);
	@Inject
	private Produktverwaltung av;
	
	@Ignore
	@Test
	public void findProduktByPreis() {
		
		final Integer preis = PREIS_VORHANDEN;
		
		final Collection<Produkt> produkte = av.findProduktByPreis(preis);
		
		assertThat(produkte, is(notNullValue()));
		assertThat(produkte.isEmpty(), is(false));
		
		for (Produkt p : produkte) {
			assertThat(p.getPreis() >= PREIS_VORHANDEN, is(true));
		}
		
	}
	
	@Test
	public void updateProdukt() {
		
		final Long prid = PRODUKT_VORHANDEN;
		
		Produkt produkt = av.findProduktById(prid);
		
		produkt = av.updateProdukt(produkt, LOCALE);
		
		produkt = av.findProduktById(prid);
	}
}
