package com.blakesinner.quickNotes.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Generic DAO with CRUD methods.
 *
 * @param <T> the entity type
 */
public class GenericDAO<T> {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();
    private Class<T> type;

    /**
     * Instantiates a new Generic dao.
     *
     * @param type the type of entity to use
     */
    public GenericDAO(Class<T> type) {
        this.type = type;
    }

    /**
     * Gets all entities.
     *
     * @return all found entities
     */
    public List<T> getAll() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);

        List<T> list = session.createQuery(query).getResultList();
        session.close();
        return list;
    }

    /**
     * Save or update.
     *
     * @param entity entity to save or update
     */
    public void saveOrUpdate(T entity) {
        Session session = sessionFactory.openSession();

        session.saveOrUpdate(entity);
        session.close();
    }

    /**
     * Inserts new entity.
     *
     * @param entity entity to insert
     * @return id of inserted entity
     */
    public int insert(T entity) {
        Session session = sessionFactory.openSession();

        int id = 0;

        Transaction transaction = session.beginTransaction();
        id = (int)session.save(entity);
        transaction.commit();

        session.close();
        return id;
    }

    /**
     * Delete entity.
     *
     * @param entity deleted entity
     */
    public void delete(T entity) {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();

        session.close();
    }

    /**
     * Gets by property equal.
     *
     * @param propertyName property name
     * @param value        value to search for
     * @return found entities
     */
    public List<T> getByPropertyEqual(String propertyName, String value) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);

        query.select(root).where(cb.equal(root.get(propertyName), value));

        List<T> results = session.createQuery(query).getResultList();
        session.close();
        return results;
    }

    /**
     * Gets by property like.
     *
     * @param propertyName property name
     * @param value        value to search for
     * @return found entities
     */
    public List<T> getByPropertyLike(String propertyName, String value) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);

        Expression<String> propertyPath = root.get(propertyName);
        query.where(cb.like(propertyPath, "%" + value + "%"));

        List<T> results = session.createQuery(query).getResultList();
        session.close();
        return results;
    }
}