package com.abelsky.complitex.tasklist.pages;

import com.abelsky.complitex.tasklist.Session;
import com.abelsky.complitex.tasklist.components.SignOutLink;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

/**
 * Base class for all the pages which require authentication.
 *
 * @author andy
 */
@AuthorizeInstantiation(Session.DEFAULT_ROLE)
abstract class BaseAuthenticatedPage extends WebPage {
    protected BaseAuthenticatedPage() {
        final String userName = Session.get().getProfile().getName();
        add(new SignOutLink("signout").setBody(new Model<String>(userName)));
    }
}
