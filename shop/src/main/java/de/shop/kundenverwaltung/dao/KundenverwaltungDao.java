package de.shop.kundenverwaltung.dao;

import static de.shop.util.Dao.QueryParameter.with;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.util.Dao;
import de.shop.util.Log;

@Named
@Log
public class KundenverwaltungDao extends Dao {
	private static final long serialVersionUID = -6166455307123578665L;

	public enum FetchType {
		NUR_KUNDE,
		MIT_BESTELLUNGEN,
		MIT_BESTELLUNGEN_UND_LIEFERUNGEN
	}
	
	public enum Order {
		KEINE,
		ID
	}

	public List<Kunde> findAllKunden(FetchType fetch, Order order) {
		List<Kunde> kunden = null;
		
		if (fetch == null || FetchType.NUR_KUNDE.equals(fetch)) {
			if (Order.ID.equals(order)) {
				kunden = find(Kunde.class, Kunde.FIND_KUNDEN_ORDER_BY_ID);
			}
			else {
				kunden = find(Kunde.class, Kunde.FIND_KUNDEN);
			}
		}
		
		else if (FetchType.MIT_BESTELLUNGEN.equals(fetch)) {
			kunden = find(Kunde.class, Kunde.FIND_KUNDEN_FETCH_BESTELLUNGEN);
		}
		return kunden;
	}
	
	public List<Kunde> findKundenByName(String name, FetchType fetch) {
		List<Kunde> kunden = null;
		if (fetch == null || FetchType.NUR_KUNDE.equals(fetch)) {
			kunden = find(Kunde.class, Kunde.FIND_KUNDE_BY_NAME,
        	              with(Kunde.PARAM_KUNDE_NAME, name).build());
		}
		else if (FetchType.MIT_BESTELLUNGEN.equals(fetch)) {
			kunden = find(Kunde.class, Kunde.FIND_KUNDEN_BY_NAME_FETCH_BESTELLUNGEN,
			              with(Kunde.PARAM_KUNDE_NAME, name).build());
		}
		else if (FetchType.MIT_BESTELLUNGEN_UND_LIEFERUNGEN.equals(fetch)) {
			kunden = find(Kunde.class, Kunde.FIND_KUNDEN_BY_NAME_FETCH_BESTELLUNGEN,
        	              with(Kunde.PARAM_KUNDE_NAME, name).build());
			
			final CriteriaBuilder builder = em.getCriteriaBuilder();
			final CriteriaQuery<Bestellung> criteriaQuery = builder.createQuery(Bestellung.class);
			final Root<Bestellung> b = criteriaQuery.from(Bestellung.class);
			b.fetch("lieferungen", JoinType.LEFT);
			
			final Path<Long> idPath = b.get("id");
			
			final List<Predicate> predList = new ArrayList<>();
			for (Kunde k : kunden) {
				final List<Bestellung> bestellungen = k.getBestellungen();
				for (Bestellung best : bestellungen) {
					final Predicate equal = builder.equal(idPath, best.getBestellId());
					predList.add(equal);
				}
			}
			
			if (!predList.isEmpty()) {
				final Predicate[] predArray = new Predicate[predList.size()];
				final Predicate pred = builder.or(predList.toArray(predArray));
				criteriaQuery.where(pred).distinct(true);
				
				final TypedQuery<Bestellung> queryObj = em.createQuery(criteriaQuery);
				queryObj.getResultList();
			}
		}
		return kunden;
	}

	public Kunde findKundeById(Long id, FetchType fetch) {
		Kunde kunde = null;
		if (fetch == null || FetchType.NUR_KUNDE.equals(fetch)) {
			kunde = find(Kunde.class, id);
		}
		else if (FetchType.MIT_BESTELLUNGEN.equals(fetch)) {
			kunde = findSingle(Kunde.class, Kunde.FIND_KUNDEN_FETCH_BESTELLUNGEN,
                               with(Kunde.PARAM_KUNDE_ID, id).build());
		}
		return kunde;
	}

//	public List<Kunde> findKundenMitMinBestMenge(short minMenge) {
//		final CriteriaBuilder builder = em.getCriteriaBuilder();
//		final CriteriaQuery<Kunde> criteriaQuery  = builder.createQuery(Kunde.class);
//		final Root<Kunde> k = criteriaQuery.from(Kunde.class);
//
//		final Join<Kunde, Bestellung> b = k.join(Kunde_.bestellungen);
//		final Join<Bestellung, Bestellposition> bp = b.join(Bestellung_.bestellpositionen);
//		criteriaQuery.where(builder.gt(bp.<Short>get(Bestellposition_.anzahl), minMenge))
//		             .distinct(true);
//		//final Join<Kunde, Bestellung> b = k.join("bestellungen");
//		//final Join<Bestellung, Bestellposition> bp = b.join("bestellpositionen");
//		//criteriaQuery.where(builder.gt(bp.<Short>get("anzahl"), minMenge))
//		//             .distinct(true);
//
//		final TypedQuery<Kunde> query = em.createQuery(criteriaQuery);
//		final List<Kunde> kunden = query.getResultList();
//		return kunden;
//	}

	public List<Kunde> findKundenByName(String name) {
		final CriteriaBuilder builder = em.getCriteriaBuilder();
		final CriteriaQuery<Kunde> criteriaQuery = builder.createQuery(Kunde.class);
		final Root<Kunde> k = criteriaQuery.from(Kunde.class);

		final Path<String> namePath = k.get("name");
		final Predicate pred = builder.equal(namePath, name);
		criteriaQuery.where(pred);

		final TypedQuery<Kunde> query = em.createQuery(criteriaQuery);
		final List<Kunde> kunden = query.getResultList();
		return kunden;
	}
}