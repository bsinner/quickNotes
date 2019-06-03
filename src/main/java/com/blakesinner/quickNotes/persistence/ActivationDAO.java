package com.blakesinner.quickNotes.persistence;

import com.blakesinner.quickNotes.entity.ActivationToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.entity.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashSet;
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

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * No argument constructor for ActivationDAO.
     */
    public ActivationDAO() {
        setType(ActivationToken.class);
    }

    /**
     * Activate a user's account and delete the activation token.
     *
     * @param token the account activation token
     * @return      the activated user
     */
    public User activateUser(ActivationToken token) {
        User user = token.getUser();

        if (isActivated(user)) {
            logger.info(String.format("%nAccount activation token was issued to already activated user"
                    + "%nUser ID:" + user.getId()
                    + "%nToken ID:" + token.getId() + "%n"
            ));
            return null;
        }

        User activatedUser = updateUser(user);
        GenericDAO<User> userDao = new GenericDAO<>(User.class);

        userDao.saveOrUpdate(activatedUser);
        User results = userDao.getById(activatedUser.getId());

        super.delete(token);

        return results;

    }

    /**
     * Delete a users UNACTIVATED role and add the USER role to show
     * that they are activated.
     *
     * @param user the user to activate
     * @return     the activated user with updated roles
     */
    private User updateUser(User user) {
        user.addRole(new UserRole("USER"));

        Set<UserRole> toDelete = findRole(user.getRoles(), "UNACTIVATED");

        toDelete.forEach(user::removeRole);

        return user;
    }

    /**
     * Get if the user is already activated
     *
     * @param user the user to check
     * @return     true if the user is activated, false if they are unactivated
     */
    private boolean isActivated(User user) { return !findRole(user.getRoles(), "USER").isEmpty(); }

    /**
     * Find a user's UserRole object.
     *
     * @param roles the user's roles
     * @param role  the role to search for
     * @return      the found role
     */
    private Set<UserRole> findRole(Set<UserRole> roles, String role) {
        return roles.stream()
                .filter(r -> r.getRole().equals(role))
                .collect(Collectors.toCollection(HashSet::new));
    }
}
