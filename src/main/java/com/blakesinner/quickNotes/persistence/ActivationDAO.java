package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.entity.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sun.net.www.content.text.Generic;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DAO for setting up activation tokens, and using them to activate users.
 *
 * @author bsinner
 */
public class ActivationDAO extends GenericDAO<ActivationToken> {

    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * No argument constructor for ActivationDAO.
     */
    public ActivationDAO() {
        setType(ActivationToken.class);
    }

    /**
     * Assign id to token, save to database, return id of created token.
     *
     * @param token the token
     * @return      the id
     */
    public String insertToken(ActivationToken token) {
        Session session = sessionFactory.openSession();
        String id = null;
        token.setId(UUID.randomUUID().toString());

        Transaction transaction = session.beginTransaction();
        id = (String)session.save(token);
        transaction.commit();

        session.close();
        return id;
    }

    public User activateUser(ActivationToken token) {
        User user = token.getUser();

        User activatedUser = updateUser(user);
        GenericDAO<User> userDao = new GenericDAO<>(User.class);

        userDao.saveOrUpdate(activatedUser);
        List<User> results = userDao
                .getByPropertyEqual("id", String.valueOf(activatedUser.getId()));

        super.delete(token);

        return results.isEmpty() ? null : results.get(0);
    }

    private User updateUser(User user) {
        Set<UserRole> roles = user.getUserRoles();
        roles.add(new UserRole("USER"));

        Set<UserRole> updatedRoles = roles.stream()
                .filter(r -> !r.getRole().equals("UNACTIVATED"))
                .collect(Collectors.toSet());

        user.setUserRoles(updatedRoles);
        return user;
    }


}
