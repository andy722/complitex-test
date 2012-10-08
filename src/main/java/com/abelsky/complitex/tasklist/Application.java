package com.abelsky.complitex.tasklist;

import com.abelsky.complitex.tasklist.pages.InternalErrorPage;
import com.abelsky.complitex.tasklist.pages.LoginPage;
import com.abelsky.complitex.tasklist.pages.OverviewPage;
import com.abelsky.complitex.tasklist.pages.RegistrationPage;
import com.abelsky.complitex.tasklist.util.MountedMapperWithoutPageComponentInfo;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IExceptionSettings;

//
// Some implementation notes (not related to this class, actually - just leaving them near an entry point).
//
//    1. An obvious one: the number of DB requests can be reduced.
//       For instance, we can select tasks and corresponding user profiles at once (for a single page of the list).
//       Haven't fixed this as requests are small and simple, and performance doesn't matter for this kind of task.
//
//    2. We may not handle concurrent modifications well. Haven't worked on this for the same reasons.
//
//    3. Haven't tested the markup in all the browsers, Google Chrome only. But it should work OK,
//       as almost all CSS is from bootstrap.
//
//    4. Data i18n - not sure if it works for Cyrillic etc.
//
//    5. Caching, data prefetching etc - I'd better implement these before putting it all into production
//

/**
 * @author andy
 */
public class Application extends AuthenticatedWebApplication {

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return Session.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return OverviewPage.class;
    }

    @Override
    protected void init() {
        super.init();

        // Show internal error page on unhandled exceptions.
        getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
        getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);

        // Remove markup output (mainly to ease localization as remaining wicket tags are shown in <title>).
        getMarkupSettings().setStripWicketTags(true);

        mountPage("/login", LoginPage.class);
        mountPage("/register", RegistrationPage.class);
        mount(new MountedMapperWithoutPageComponentInfo("/tasks", OverviewPage.class));
        mountPage("/error", InternalErrorPage.class);
    }


}
