package com.abelsky.complitex.tasklist;

import com.abelsky.complitex.tasklist.db.ProfileDAO;
import com.abelsky.complitex.tasklist.model.Profile;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

/**
 * @author andy
 */
public class Session extends AuthenticatedWebSession {

    /**
     * Role of an authorized user.
     */
    public static final String DEFAULT_ROLE = Roles.USER;

    /**
     * Every user gets a single role of an authenticated one after sign in.
     */
    private static final Roles DEFAULT_ROLES = new Roles(DEFAULT_ROLE);

    /**
     * Key for storing currently displayed project in session.
     */
    public static final String KEY_PROJECT_NAME = "project-name";

    private Profile profile;

    /**
     * @return The current session.
     */
    public static Session get() {
        return (Session) org.apache.wicket.Session.get();
    }

    public Session(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String name, String password) {
        profile = ProfileDAO.getProfile(name, password);
        return profile != null;
    }

    @Override
    public Roles getRoles() {
        return isSignedIn() ? DEFAULT_ROLES : null;

    }

    /**
     * Returns profile of the current user, or {@code null} if not signed in.
     */
    public Profile getProfile() {
        return profile;
    }
}
