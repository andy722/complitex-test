package com.abelsky.complitex.tasklist.pages;

import com.abelsky.complitex.tasklist.Session;
import com.abelsky.complitex.tasklist.panels.ErrorPanel;
import com.abelsky.complitex.tasklist.beans.LoginBean;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Sign-in page.
 *
 * @author andy
 */
public class LoginPage extends WebPage {

    public LoginPage() {
        // Prohibit multiple log-ins.
        if (Session.get().isSignedIn()) {
            throw new RestartResponseAtInterceptPageException(getApplication().getHomePage());
        }

        add(new ErrorPanel("feedback"));
        add(new LoginForm("login_form"));
    }

    private static class LoginForm extends StatelessForm<LoginBean> {

        public LoginForm(String id) {
            super(id, new CompoundPropertyModel<LoginBean>(new LoginBean()));

            add(new RequiredTextField<String>("username"));
            add(new PasswordTextField("password"));
        }

        @Override
        protected void onSubmit() {
            final LoginBean formData = getModelObject();

            if (Session.get().signIn(formData.getUsername(), formData.getPassword())) {
                setResponsePage(getApplication().getHomePage());

            } else {
                error(getLocalizer().getString("login.failed", this, "Invalid username or password"));
            }
        }
    }
}
