package de.shop.kundenverwaltung.service;

import static de.shop.util.Constants.KEINE_ID;
import static de.shop.util.Dao.QueryParameter.with;
import static javax.ejb.TransactionAttributeType.MANDATORY;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
//import javax.enterprise.event.Event;

import org.jboss.logging.Logger;

//import de.shop.bestellverwaltung.service.Bestellverwaltung;
import de.shop.kundenverwaltung.dao.KundenverwaltungDao;
import de.shop.kundenverwaltung.dao.KundenverwaltungDao.FetchType;
import de.shop.kundenverwaltung.dao.KundenverwaltungDao.Order;
import de.shop.kundenverwaltung.domain.Kunde;
import de.shop.kundenverwaltung.domain.PasswordGroup;
import de.shop.util.IdGroup;
import de.shop.util.ValidationService;

@Stateless
@TransactionAttribute(MANDATORY)
public class Kundenverwaltung implements Serializable {
	private static final long serialVersionUID = -5520738420154763865L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private KundenverwaltungDao dao;
	
//	@Inject
//	private Bestellverwaltung bv;
	
//	@Inject
//	private transient Event<Kunde> event;
	
	@Inject
	private ValidationService validationService;

	public List<Kunde> findAllKunden(FetchType fetch, Order order) {
		final List<Kunde> kunden = dao.findAllKunden(fetch, order);
		return kunden;
	}
	
	public List<Kunde> findKundenByName(String name, FetchType fetch) {
		final List<Kunde> kunden = dao.findKundenByName(name, fetch);
		return kunden;
	}

	public Kunde findKundeById(Long id, FetchType fetch) {
		final Kunde kunde = dao.findKundeById(id, fetch);
		return kunde;
	}
	
	public Kunde findKundeByEmail(String email) {
		final Kunde kunde = dao.findSingle(Kunde.class, Kunde.FIND_KUNDE_BY_EMAIL,
                                                   with(Kunde.PARAM_KUNDE_EMAIL, email).build());
		return kunde;
	}

	public Kunde createKunde(Kunde kunde, Locale locale) {
		if (kunde == null) {
			return kunde;
		}

		validateKunde(kunde, locale, Default.class, PasswordGroup.class);

		final Kunde vorhandenerKunde = dao.findSingle(Kunde.class,
				                                              Kunde.FIND_KUNDE_BY_EMAIL,
                                                              with(Kunde.PARAM_KUNDE_EMAIL, kunde.getEmail())
                                                              .build());
		if (vorhandenerKunde != null) {
			throw new EmailExistsException(kunde.getEmail());
		}
		LOGGER.trace("Email-Adresse existiert noch nicht");
		
//		kunde = findKundeByEmail(kunde.getEmail());
//		kunde.setKundenId(KEINE_ID);
//		dao.create(kunde);
//		event.fire(kunde);
////		kunde = dao.create(kunde);
		
		kunde.setKundenId(KEINE_ID);
		kunde = dao.create(kunde);
		
		return kunde;
	}
	
	private void validateKunde(Kunde kunde, Locale locale, Class<?>... groups) {
		final Validator validator = validationService.getValidator(locale);
		
		final Set<ConstraintViolation<Kunde>> violations = validator.validate(kunde, groups);
		if (!violations.isEmpty()) {
			throw new KundeValidationException(kunde, violations);
		}
	}

	public Kunde updateKunde(Kunde kunde, Locale locale) {
		if (kunde == null) {
			return null;
		}

		validateKunde(kunde, locale, Default.class, PasswordGroup.class, IdGroup.class);
		
		final Kunde vorhandenerKunde = dao.findSingle(Kunde.class,
				                                              Kunde.FIND_KUNDE_BY_EMAIL,
                                                              with(Kunde.PARAM_KUNDE_EMAIL,
                                                            	   kunde.getEmail()).build());
		if (vorhandenerKunde != null && vorhandenerKunde.getKundenId().longValue() != kunde.getKundenId().longValue()) {
			throw new EmailExistsException(kunde.getEmail());
		}
		LOGGER.trace("Email-Adresse existiert noch nicht");

		kunde = dao.update(kunde);
		return kunde;
	}


	public void deleteKunde(Kunde kunde) {
		if (kunde == null) {
			return;
		}
		kunde = findKundeById(kunde.getKundenId(), FetchType.MIT_BESTELLUNGEN);
		if (kunde == null) {
			return;
		}
		if (!kunde.getBestellungen().isEmpty()) {
			throw new KundeDeleteBestellungException(kunde);
		}

		dao.delete(kunde);
	}

	public List<Kunde> findKundenByPLZ(String plz) {
		final List<Kunde> kunden = dao.find(Kunde.class, Kunde.FIND_KUNDE_BY_PLZ,
                                                    with(Kunde.PARAM_KUNDE_ADRESSE_PLZ, plz).build());
		return kunden;
	}

	public List<Kunde> findKundenBySeit(Date seit) {
		final List<Kunde> kunden = dao.find(Kunde.class, Kunde.FIND_KUNDEN_BY_DATE,
                                                    with(Kunde.PARAM_KUNDE_SEIT, seit).build());
		return kunden;
	}
	
	public List<Kunde> findKunden() {
		final List<Kunde> kunden = dao.find(Kunde.class,
				                                    Kunde.FIND_KUNDEN);
		return kunden;
	}
	
	/**
	 */
	public List<Kunde> findKundenByNameCriteria(String name) {
		final List<Kunde> kunden = dao.findKundenByName(name);
		return kunden;
	}
	
	/**
	 */
//	public List<Kunde> findKundenMitMinBestMenge(short minMenge) {
//		final List<Kunde> kunden = dao.findKundenMitMinBestMenge(minMenge);
//		return kunden;
//	}
	
	/**
	 */
//	public List<Wartungsvertrag> findWartungsvertraege(Long kundeId) {
//		final List<Wartungsvertrag> wartungsvertraege =
//			                        dao.find(Wartungsvertrag.class,
//			                        		 Wartungsvertrag.FIND_WARTUNGSVERTRAEGE_BY_KUNDE_ID,
//                                             with(Wartungsvertrag.PARAM_KUNDE_ID, kundeId).build());
//		return wartungsvertraege;
//	}
	
	/**
	 */
//	public Wartungsvertrag createWartungsvertrag(Wartungsvertrag wartungsvertrag,
//			                                     Kunde kunde) {
//		if (wartungsvertrag == null || kunde == null) {
//			return null;
//		}
//		
//		if (!em.contains(kunde)) {
//			final Long id = kunde.getId();
//			kunde = em.find(Kunde.class, id);
//		}
//		
//		wartungsvertrag.setKunde(kunde);
//		kunde.addWartungsvertrag(wartungsvertrag);
//		
//		wartungsvertrag = dao.create(wartungsvertrag);
//		return wartungsvertrag;
//	}
}
