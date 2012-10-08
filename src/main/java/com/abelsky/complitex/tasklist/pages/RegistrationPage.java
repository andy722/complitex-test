package com.abelsky.complitex.tasklist.pages;

import com.abelsky.complitex.tasklist.Session;
import com.abelsky.complitex.tasklist.beans.RegistrationBean;
import com.abelsky.complitex.tasklist.db.ProfileDAO;
import com.abelsky.complitex.tasklist.panels.ErrorPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Sign-up page.
 *
 * @author andy
 */
public class RegistrationPage extends WebPage {

    public RegistrationPage() {
        add(new ErrorPanel("feedback"));
        add(new RegistrationForm("registration_form"));
    }

    private class RegistrationForm extends StatelessForm<RegistrationBean> {

        public RegistrationForm(String id) {
            super(id, new CompoundPropertyModel<RegistrationBean>(new RegistrationBean()));

            add(new RequiredTextField<String>("username"));
            add(new EmailTextField("email"));
            add(new PasswordTextField("password"));
        }

        @Override
        protected void onSubmit() {
            final RegistrationBean registrationBean = getModelObject();
            final String username = registrationBean.getUsername();
            final String email = registrationBean.getEmail();
            final String password = registrationBean.getPassword();

            if (!ProfileDAO.addProfile(username, email, password)) {
                error(getLocalizer().getString("registration.name.duplicate", this, "This name is already used"));

            } else {
                if (Session.get().signIn(username, password)) {
                    setResponsePage(getApplication().getHomePage());

                } else {
                    error(getLocalizer().getString("registration.failed", this, "Registration failed"));
                }
            }
        }
    }
}
