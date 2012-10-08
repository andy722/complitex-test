package com.abelsky.complitex.tasklist.components;

import com.abelsky.complitex.tasklist.Session;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.link.Link;

/**
 * Signs out and redirects to login page.
 *
 * @author andy
 */
public class SignOutLink extends Link<String> {
    public SignOutLink(String id) {
        super(id);
    }

    @Override
    public void onClick() {
        Session.get().signOut();
        throw new RestartResponseException(getApplication().getHomePage());
    }
}
