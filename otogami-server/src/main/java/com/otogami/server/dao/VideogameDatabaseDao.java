package com.otogami.server.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.otogami.core.model.Platform;
import com.otogami.core.model.Videogame;
import com.otogami.server.model.VideogameEntity;

public class VideogameDatabaseDao implements VideogameDao {

	private final static Logger LOG = LoggerFactory.getLogger(VideogameDatabaseDao.class);

	@Override
	public VideogameEntity findById(Long id) {
		throw new NotImplementedException("The medthod findById(Long id) is not yet implemented.");
	}

	@Override
	public VideogameEntity findByStoreGameId(String storeId, String id) {
		throw new NotImplementedException("The medthod findByStoreGameId(String storeId, String id) is not yet implemented.");
	}

	@Override
	public List<VideogameEntity> findByPlatform(Platform platform) {
		throw new NotImplementedException("The medthod findByPlatform(Platform platform) is not yet implemented.");
	}

	@Override
	public void update(List<VideogameEntity> videogameList) throws Exception {
		LOG.info("Updating {} videogames", videogameList.size());
		EntityManager em = createEntityManager();

		Session session = (Session) em.getDelegate();

		Transaction tx = session.getTransaction();

		try {
			tx = session.beginTransaction();
			int count = 0;
			for (VideogameEntity videogameEntity : videogameList) {
				LOG.info("Persisting " + videogameEntity);
				session.saveOrUpdate(videogameEntity);

				if (++count % 20 == 0) {
					// flush a batch of updates and release memory:
					em.flush();
					em.clear();
				}
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			session.close();
		}

	}

	private EntityManager createEntityManager() {
		return Persistence.createEntityManagerFactory("videogame").createEntityManager();
	}

	@Override
	// TODO Refactor
	public List<VideogameEntity> findByCriteria(Videogame criteria) {
		EntityManager em = createEntityManager();

		// TODO - Use the simple Hibernate Criteria!
		// Session session = (Session) em.getDelegate();
		// Criteria criteria2 = session.createCriteria(VideogameEntity.class);
		// criteria2.add(Restrictions.ilike("title", criteria.getTitle()));
		// criteria2.addOrder(Order.asc("price"));
		// criteria2.setFetchSize(XXX);

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<VideogameEntity> select = cb.createQuery(VideogameEntity.class);
		Root<VideogameEntity> videogameEntityRoot = select.from(VideogameEntity.class);

		List<Predicate> criteriaList = new ArrayList<Predicate>();

		addPlatformWhereClause(criteriaList, videogameEntityRoot, criteria, cb);
		addTitleWhereClause(criteriaList, videogameEntityRoot, criteria, cb);
		addPriceWhereClause(criteriaList, videogameEntityRoot, criteria, cb);
		addAvailabilityWhereClause(criteriaList, videogameEntityRoot, criteria, cb);

		select.where(cb.and(criteriaList.toArray(new Predicate[0])));

		select.orderBy(cb.asc(videogameEntityRoot.get("price")));
		TypedQuery<VideogameEntity> query = em.createQuery(select);
		List<VideogameEntity> resultList = query.getResultList();
		em.close();
		return resultList;
	}

	private void addAvailabilityWhereClause(List<Predicate> criteriaList, Root<VideogameEntity> videogameEntityRoot, Videogame criteria,
			CriteriaBuilder cb) {
		if (criteria.getAvailability() != null) {
			Predicate availabiliyPredicate = cb.equal(videogameEntityRoot.get("availability"), criteria.getAvailability().name());
			criteriaList.add(availabiliyPredicate);
		}
	}

	private void addPriceWhereClause(List<Predicate> criteriaList, Root<VideogameEntity> videogameEntityRoot, Videogame criteria, CriteriaBuilder cb) {
		BigDecimal price = criteria.getPrice();
		if (price != null && price.intValue() > 0) {
			Expression<Integer> priceExpression = videogameEntityRoot.get("price");
			Predicate pricePredicate = cb.ge(priceExpression, criteria.getPrice());
			criteriaList.add(pricePredicate);
		}
	}

	private void addTitleWhereClause(List<Predicate> criteriaList, Root<VideogameEntity> videogameEntityRoot, Videogame criteria, CriteriaBuilder cb) {
		if (StringUtils.isNotBlank(criteria.getTitle())) {
			Expression<String> titleExpression = videogameEntityRoot.get("title");
			Predicate titlePredicate = cb.like(cb.lower(titleExpression), "%" + criteria.getTitle().toLowerCase() + "%");
			criteriaList.add(titlePredicate);
		}
	}

	private void addPlatformWhereClause(List<Predicate> criteriaList, Root<VideogameEntity> videogameEntityRoot, Videogame criteria,
			CriteriaBuilder cb) {
		if (criteria.getPlatform() != null) {
			Predicate platformPredicate = cb.equal(videogameEntityRoot.get("platform"), criteria.getPlatform().name());
			criteriaList.add(platformPredicate);
		}
	}
}
