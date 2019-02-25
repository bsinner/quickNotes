package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    public List<User> getUsersByUsername(String username) {
        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Expression<String> propertyPath = root.get("username");
        query.where(builder.like(propertyPath, "%" + username + "%"));

        List<User> users = session.createQuery(query).getResultList();
        session.close();
        return users;
    }

}
