package com.blakesinner.quickNotes.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
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
    private Class<T> TYPE;

    /**
     * Instantiates a new Generic dao.
     *
     * @param TYPE the type of entity to use
     */
    public GenericDAO(Class<T> TYPE) { this.TYPE = TYPE; }

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
        return accessData(session -> {

           CriteriaBuilder cb = session.getCriteriaBuilder();
           CriteriaQuery<T> query = cb.createQuery(TYPE);
           query.from(TYPE);

           return session.createQuery(query).getResultList();
        });
    }

    /**
     * Save or update.
     *
     * @param entity entity to save or update
     */
    public void saveOrUpdate(T entity) { alterDataVoid(s -> s.saveOrUpdate(entity)); }

    /**
     * Inserts new entity.
     *
     * @param entity entity to insert
     * @return       id of inserted entity
     */
    public int insert(T entity) { return alterData(s -> (int)s.save(entity)); }

    /**
     * Inserts new entity with UUID primary key.
     *
     * @param entity entity to insert
     * @return id of inserted entity
     */
    public UUID insertWithUUID(T entity) { return alterData(s -> (UUID)s.save(entity)); }

    /**
     * Delete entity.
     *
     * @param entity deleted entity
     */
    public void delete(T entity) { alterDataVoid(s -> s.delete(entity)); }

    /**
     * Gets by property equal.
     *
     * @param propertyName property name
     * @param value        value to search for
     * @return             found entities
     */
    public List<T> getByPropertyEqual(String propertyName, String value) {
        return accessData(session -> {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(TYPE);
            Root<T> root = query.from(TYPE);

            query.select(root).where(cb.equal(root.get(propertyName), value));

            return session.createQuery(query).getResultList();
        });
    }

    /**
     * Gets by property like.
     *
     * @param propertyName property name
     * @param value        value to search for
     * @return             found entities
     */
    public List<T> getByPropertyLike(String propertyName, String value) {
        return accessData(session -> {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(TYPE);
            Root<T> root = query.from(TYPE);

            Expression<String> propPath = root.get(propertyName);
            query.where(cb.like(propPath, "%" + value + "%"));

            return session.createQuery(query).getResultList();
        });
    }

    /**
     * Search for entities by multiple properties equal.
     *
     * @param properties map of properties to search for
     * @return           found entities
     */
    public List<T> getByPropertiesEqual(Map<String, String> properties) {
        return accessData(session -> {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(TYPE);
            Root<T> root = query.from(TYPE);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry item : properties.entrySet()) {
                predicates.add(cb.equal(root.get((String) item.getKey()), item.getValue()));
            }

            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            return session.createQuery(query).getResultList();
        });
    }

    /**
     * Gets by UUID, checks if the id is a valid UUID to prevent exceptions.
     *
     * @param id the id
     * @return   the entity
     */
    public T getByUUID(String id) {
        if (!UUID_FORMAT.matcher(id).matches()) {
            return null;
        }

        UUID uuid = UUID.fromString(id);

        return accessData(s -> s.get(TYPE, uuid));
    }

    /**
     * Gets by id.
     *
     * @param id the id
     * @return   the entity
     */
    public T getById(int id) { return accessData(s -> s.get(TYPE, id)); }

    /**
     * Gets by string id.
     *
     * @param id the id
     * @return   the entity
     */
    public T getById(String id) { return getById(Integer.valueOf(id)); }

    private <T2> T2 accessData(Function<Session, T2> callback) {
        Session session = FACTORY.openSession();

        T2 value = callback.apply(session);

        session.close();
        return value;
    }

    private <T2> T2 alterData(Function<Session, T2> callback) {
        Session session = FACTORY.openSession();

        Transaction transaction = session.beginTransaction();

        T2 value = callback.apply(session);

        transaction.commit();
        session.close();

        return value;
    }

    private void alterDataVoid(Consumer<Session> callback) {
        alterData(session -> {
            callback.accept(session);
            return null;
        });
    }

    /**
     * Set the dao type.
     *
     * @param TYPE the dao entity type
     */
    public void setType(Class<T> TYPE) { this.TYPE = TYPE; }
}
