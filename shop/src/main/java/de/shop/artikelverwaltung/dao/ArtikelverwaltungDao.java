package de.shop.artikelverwaltung.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.shop.artikelverwaltung.domain.Produkt;
import de.shop.util.Dao;

public class ArtikelverwaltungDao extends Dao {
	
private static final long serialVersionUID = 1L;
	
	public List<Produkt> findProduktByBez(List<Long> ids) {
		//TODO
		return null;
	}
	
	public List<Produkt> findProduktByMaxPreis(List<Integer> preis) {
		if (preis == null || preis.isEmpty()) {
			return null;
		}
		
		final CriteriaBuilder builder = em.getCriteriaBuilder();
		final CriteriaQuery<Produkt> criteriaQuery = builder.createQuery(Produkt.class);
		final Root<Produkt> b = criteriaQuery.from(Produkt.class);
		
		final Path<Integer> maxpreisPath = b.get("maxpreis");
		final List<Predicate> predList = new ArrayList<>();
		for (Integer gp : preis) {
			final Predicate equal = builder.equal(maxpreisPath, gp);
			predList.add(equal);
		}
		
		final Predicate[] predArray = new Predicate[predList.size()];
		final Predicate pred = builder.or(predList.toArray(predArray));
		criteriaQuery.where(pred).distinct(true);
		
		final TypedQuery<Produkt> query = em.createQuery(criteriaQuery);
		final List<Produkt> produkte = query.getResultList();
		return produkte;
			
	}

}
