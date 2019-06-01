package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.ActivationToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.UUID;

/**
 * DAO with activation token specific functionality.
 *
 * @author bsinner
 */
public class ActivationTokenDAO extends GenericDAO<ActivationToken> {

    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * No argument constructor for ActivationTokenDAO.
     */
    public ActivationTokenDAO() {
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


}
