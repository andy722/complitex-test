package com.abelsky.complitex.tasklist.components;

import com.abelsky.complitex.tasklist.db.ProfileDAO;
import com.abelsky.complitex.tasklist.db.TaskDAO;
import com.abelsky.complitex.tasklist.model.Profile;
import com.abelsky.complitex.tasklist.model.Task;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

/**
 * Displays task owner, i.e. one of the registered user names.
 * Provides state drop-down on click, persists on select.
 *
 * @author andy
 */
public class EditableUserLabel extends AjaxEditableChoiceLabel<Profile> {
    private final Task task;

    public EditableUserLabel(String id, Task task, IModel<List<Profile>> allProfiles) {
        super(id, Model.<Profile>of(TaskDAO.getOwner(task)), allProfiles.getObject(), new ProfileRenderer());
        this.task = task;
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target) {
        final Profile profile = (Profile) getDefaultModelObject();

        // Also updates corresponding task field.
        TaskDAO.setOwner(task, profile);

        super.onSubmit(target);
    }

    /**
     * Renders user name.
     */
    private static class ProfileRenderer implements IChoiceRenderer<Profile> {

        @Override
        public Object getDisplayValue(Profile profile) {
            return profile.getName();
        }

        @Override
        public String getIdValue(Profile object, int index) {
            return object.toString();
        }
    }
}
