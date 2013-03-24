package de.shop.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * Prinzip: wenn nichts gefunden wird, dann wird entweder eine leere Liste oder
 * null (bei singulaeren Queries) zurueck geliefert
 */
@Named
public class Dao implements Serializable {
	private static final long serialVersionUID = -7246779329244314242L;
	
	@PersistenceContext
	protected transient EntityManager em;
	
	/**
	 */
	public <T> T find(Class<T> clazz, Object id) {
		final T result = em.find(clazz, id);
		return result;
	}
	
	/**
	 */
	public <T> List<T> find(Class<T> clazz, String namedQuery) {
		final List<T> result = em.createNamedQuery(namedQuery, clazz)
		                         .getResultList();
		return result;
	}
	
	/**
	 */
	public <T> List<T> find(Class<T> clazz, String namedQuery, Map<String, Object> parameters) {
		final TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);

		// Map in Set konvertieren fuer for-Schleife
		final Set<Entry<String, Object>> paramSet = parameters.entrySet();
		for (Entry<String, Object> entry : paramSet) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		final List<T> result = query.getResultList();
		return result;
	}
	
	/**
	 */
	public <T> List<T> find(Class<T> clazz, String namedQuery, int resultLimit) {
		final List<T> result = em.createNamedQuery(namedQuery, clazz)
		                         .setMaxResults(resultLimit)
	                             .getResultList();
		return result;
	}
	
	/**
	 */
	public <T> List<T> find(Class<T> clazz, String namedQuery, Map<String, Object> parameters, int resultLimit) {
		final TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
		query.setMaxResults(resultLimit);

		// Map in Set konvertieren fuer for-Schleife
		final Set<Entry<String, Object>> paramSet = parameters.entrySet();
		for (Entry<String, Object> entry : paramSet) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		final List<T> result = query.getResultList();
		return result;
	}

	
	/**
	 */
	public <T> T findSingle(Class<T> clazz, String namedQuery) {
		try {
			return em.createNamedQuery(namedQuery, clazz)
			         .getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 */
	public <T> T findSingle(Class<T> clazz, String namedQuery, Map<String, Object> parameters) {
		final TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);

		// Map in Set konvertieren fuer for-Schleife
		final Set<Entry<String, Object>> paramSet = parameters.entrySet();
		for (Entry<String, Object> entry : paramSet) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		try {
			return query.getSingleResult();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	
	/**
	 */
	public List<?> findUsingSQL(String sql, String resultSetMapping) {
		return em.createNativeQuery(sql, resultSetMapping)
		         .getResultList();
	}

	/**
	 */
	public <T> T create(T obj) {
		em.persist(obj);
		return obj;
	}
	
	/**
	 */
	public <T> T update(T obj) {
		return em.merge(obj);
	}
	
	/**
	 */
	public void delete(Object obj) {
		if (!em.contains(obj)) {
			final Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(obj);
			obj = em.find(obj.getClass(), id);
		}
		em.remove(obj);
	}
	
	/**
	 */
	public void delete(Class<?> clazz, Object id) {
		final Object obj = em.find(clazz, id);
		em.remove(obj);
	}
	
	/**
	 * Beispiel:
	 * public List<Bestellung> findBestellungen(Long kundeId, Bestellung.class) {
	 *    return dao.find(Bestellung.FIND_BY_KUNDEID,
	 *                    with(Bestellung.PARAM_KUNDEID, kundeId).parameters(),
	 *                    Bestellung.class);
	 */
	public static final class QueryParameter {
		private final Map<String, Object> params;
			
		private QueryParameter(String name, Object value) {
			params = new HashMap<>();
			params.put(name, value);
		}
		
		public static QueryParameter with(String name, Object value) {
			return new QueryParameter(name, value);
		}
	
		public QueryParameter and(String name, Object value) {
			params.put(name, value);
			return this;
		}
		
		public Map<String, Object> build() {
			return params;
		}
	}
}
