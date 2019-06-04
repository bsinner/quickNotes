package com.blakesinner.quickNotes.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Generic DAO with CRUD methods.
 *
 * @param <T> the entity type
 */
public class GenericDAO<T> {

    private final SessionFactory FACTORY = SessionFactoryProvider.getSessionFactory();
    private final Pattern UUID_FORMAT = Pattern.compile("^\\p{Alnum}{8}"
            + "-\\p{Alnum}{4}-\\p{Alnum}{4}-\\p{Alnum}{4}"
            + "-\\p{Alnum}{12}$");
    private Class<T> type;

    /**
     * Instantiates a new Generic dao.
     *
     * @param type the type of entity to use
     */
    public GenericDAO(Class<T> type) { this.type = type; }

    /**
     * No argument constructor.
     */
    public GenericDAO() { }

    /**
     * Gets all entities.
     *
     * @return all found entities
     */
    public List<T> getAll() {
        Session session = FACTORY.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);

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
        Session session = FACTORY.openSession();

        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();

        session.close();
    }

    /**
     * Inserts new entity.
     *
     * @param entity entity to insert
     * @return       id of inserted entity
     */
    public int insert(T entity) {
        Session session = FACTORY.openSession();

        int id;

        Transaction transaction = session.beginTransaction();
        id = (int)session.save(entity);
        transaction.commit();

        session.close();
        return id;
    }

    /**
     * Inserts new entity with UUID primary key.
     *
     * @param entity entity to insert
     * @return id of inserted entity
     */
    public UUID insertWithUUID(T entity) {
        Session session = FACTORY.openSession();
        UUID id;

        Transaction transaction = session.beginTransaction();
        id = (UUID)session.save(entity);
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
        Session session = FACTORY.openSession();

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
     * @return             found entities
     */
    public List<T> getByPropertyEqual(String propertyName, String value) {
        Session session = FACTORY.openSession();

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
     * @return             found entities
     */
    public List<T> getByPropertyLike(String propertyName, String value) {
        Session session = FACTORY.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);

        Expression<String> propertyPath = root.get(propertyName);
        query.where(cb.like(propertyPath, "%" + value + "%"));

        List<T> results = session.createQuery(query).getResultList();
        session.close();
        return results;
    }

    /**
     * Search for entities by multiple properties equal.
     *
     * @param properties map of properties to search for
     * @return           found entities
     */
    public List<T> getByPropertiesEqual(Map<String, String> properties) {
        Session session = FACTORY.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(type);
        Root<T> root = query.from(type);

        List<Predicate> predicates = new ArrayList<>();

        for(Map.Entry item : properties.entrySet()) {
            predicates.add(cb.equal(root.get((String) item.getKey()), item.getValue()));
        }

        query.select(root).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return session.createQuery(query).getResultList();
    }

    /**
     * Gets by UUID, checks if the id is a valid UUID to prevent exceptions.
     *
     * @param id the id
     * @return   the entity
     */
    public T getByUUID(String id) {
        UUID uuid = UUID.fromString(id);

        if (!UUID_FORMAT.matcher(id).matches()) {
            return null;
        }

        Session session = FACTORY.openSession();

        T entity = session.get(type, uuid);

        session.close();
        return entity;
    }

    /**
     * Gets by id.
     *
     * TODO: replace usages of getByPropertyEqual("id", id) with getById(id)
     *
     * @param id the id
     * @return   the entity
     */
    public T getById(int id) {
        Session session = FACTORY.openSession();

        T entity = session.get(type, id);

        session.close();
        return entity;
    }

    /**
     * Set the dao type.
     *
     * @param type the dao entity type
     */
    public void setType(Class<T> type) { this.type = type; }
}
