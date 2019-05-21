package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.ActivationToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.UUID;

public class ActivationTokenDAO extends GenericDAO<ActivationToken> {

    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public ActivationTokenDAO() {
        setType(ActivationToken.class);
    }

    public UUID insertToken(ActivationToken token) {
        Session session = sessionFactory.openSession();
        UUID id = null;

        Transaction transaction = session.beginTransaction();
        id = (UUID)session.save(token);
        transaction.commit();

        session.close();
        return id;
    }
}
