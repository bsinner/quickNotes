package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Data access object for the user entity.
 *
 * @author bsinner
 */
public class UserDAO {

    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    // TODO clean up repeated code in user search methods

    /**
     * Get a list of all users in database.
     * @return list of all users
     */
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);

        Root<User> root = query.from(User.class);

        List<User> users = session.createQuery(query).getResultList();
        session.close();

        return users;
    }

    /**
     * Updates passed in user.
     * @param user user to update
     */
    public void saveOrUpdate(User user) {
        Session session = sessionFactory.openSession();
        session.saveOrUpdate(user);
        session.close();
    }

    /**
     * Insert or update user.
     * @param user user to update
     */
    public int insert(User user) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        id = (int)session.save(user);
        transaction.commit();
        session.close();
        return id;
    }

    /**
     * Deletes a user.
     * @param user user to delete
     */
    public void delete(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    /**
     * Get user by exact property match.
     * @return the found user
     * @param propertyName property to search by
     * @param value value to search for
     */
    public List<User> getByPropertyEqual(String propertyName, String value) {
       Session session = sessionFactory.openSession();

       logger.debug("Searching for user with " + propertyName + "=" + value);

       CriteriaBuilder builder = session.getCriteriaBuilder();
       CriteriaQuery<User> query = builder.createQuery(User.class);
       Root<User> root = query.from(User.class);
       query.select(root).where(builder.equal(root.get(propertyName), value));
       List<User> users = session.createQuery(query).getResultList();

       session.close();
       return users;

    }

    /**
     * Get users where with properties like search string.
     * @return found users
     * @param propertyName property to search by
     * @param value value to search for
     */
    public List<User> getByPropertyLike(String propertyName, String value) {
        Session session = sessionFactory.openSession();

        logger.debug("Searching for user with {} = {}", propertyName, value);

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        Expression<String> propertyPath = root.get(propertyName);

        query.where(builder.like(propertyPath, "%" + value + "%"));

        List<User> users = session.createQuery(query).getResultList();

        session.close();
        return users;
    }

}
