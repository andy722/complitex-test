package com.abelsky.complitex.tasklist.panels;

import com.abelsky.complitex.tasklist.Session;
import com.abelsky.complitex.tasklist.components.EditableUserLabel;
import com.abelsky.complitex.tasklist.components.TasksProvider;
import com.abelsky.complitex.tasklist.components.models.TaskDescriptionModel;
import com.abelsky.complitex.tasklist.components.models.UpdateableModel;
import com.abelsky.complitex.tasklist.components.state.StateLabel;
import com.abelsky.complitex.tasklist.db.ProfileDAO;
import com.abelsky.complitex.tasklist.db.TaskDAO;
import com.abelsky.complitex.tasklist.model.Profile;
import com.abelsky.complitex.tasklist.model.Project;
import com.abelsky.complitex.tasklist.model.Task;
import com.abelsky.complitex.tasklist.model.TaskState;
import com.abelsky.complitex.tasklist.pages.OverviewPage;
import com.abelsky.complitex.tasklist.util.MultipleEventHandler;
import com.abelsky.complitex.tasklist.util.ResourceReader;
import org.apache.wicket.Localizer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Displays a list of tasks.
 *
 * @author andy
 */
public class TaskListPanel extends Panel {

    /**
     * Number of simultaneously displayed tasks.
     */
    private static final int ITEMS_PER_PAGE = 15;

    /**
     * Maps states to localized names.
     */
    private final Map<TaskState, String> stateNames = Collections.unmodifiableMap(
            new EnumMap<TaskState, String>(TaskState.class) {{
                for (TaskState state : TaskState.values()) {
                    put(state, Localizer.get().getString("task.state." + state.name().toLowerCase(), TaskListPanel.this));
                }
            }}
    );

    private final UpdateableModel<String> filter;

    public TaskListPanel(String id, final Project project) {
        super(id);

        // Save current project in session so that it could stay selected on page reload.
        Session.get().setAttribute(Session.KEY_PROJECT_NAME, project.getName());

        setVersioned(false);

        filter = new UpdateableModel<String>();
        final TasksProvider dataProvider = new TasksProvider(project, filter);

        // Set up a (hidden) modal form for adding new tasks.
        final AddTaskForm addTaskForm = new AddTaskForm("newTaskForm");
        {
            // Submit button is outside the form (to simplify layout), so handling it here.
            add(new AjaxSubmitLink("newTaskSubmit", addTaskForm) {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    final Task task = (Task) form.getModelObject();
                    TaskDAO.addTask(task.getDescription(), project, Session.get().getProfile(), Session.get().getProfile());

                    // Don't bother updating the list via Ajax, just reopen.
                    setResponsePage(OverviewPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(getForm().get("newTaskFeedback"));
                }
            });
        }
        add(addTaskForm);

        final TransparentWebMarkupContainer wrapper = new TransparentWebMarkupContainer("tasksWrapper");
        wrapper.setOutputMarkupId(true);
        {
            final TasksList list = new TasksList("tasks", dataProvider, ITEMS_PER_PAGE);
            list.setOutputMarkupId(true);
            wrapper.add(list);

            final AjaxPagingNavigator navigator = new AjaxPagingNavigator("navigator", list);
            wrapper.add(navigator);
        }
        add(wrapper);

        // Handle any type of input events in the filter field.
        final TextField<String> filterField = new TextField<String>("filter", filter);
        MultipleEventHandler.attachHandler(filterField, new MultipleEventHandler.UpdateHandler() {
            @Override
            public void onUpdate(AjaxRequestTarget target) {
                if (filter.isUpdated()) {
                    // Renew the list, data provider already contains updated filter.
                    target.add(wrapper);
                    filter.setUpdated();
                }
            }
        }, "onpropertychange", "onkeyup", "oninput", "onpaste", "onchange");
        add(filterField);
    }

    private class TasksList extends DataView<Task> {

        public TasksList(String id, IDataProvider<Task> model, int itemsPerPage) {
            super(id, model, itemsPerPage);
        }

        // Great that we can't delete users:-]
        public IModel<List<Profile>> allProfiles = new ListModel<Profile>(ProfileDAO.getAllProfiles());

        private final Map<TaskState, Behavior> labelClasses = Collections.unmodifiableMap(
                new EnumMap<TaskState, Behavior>(TaskState.class) {{
                    put(TaskState.NEW,          new AttributeAppender("class", " label-important"));
                    put(TaskState.IN_PROGRESS,  new AttributeAppender("class", " label-info"));
                    put(TaskState.DONE,         new AttributeAppender("class", " label-success"));
                }}
        );

        @Override
        protected void populateItem(Item<Task> item) {
            final Task currentTask = item.getModel().getObject();

            //
            // Layout of the row is like this:
            //
            // <Task name label>  <Task state drop-down> <Task owner drop-down>          <Delete button>
            //

            item.add(new AjaxEditableLabel<String>("task_name", new TaskDescriptionModel(currentTask)).setRequired(true));

            item.add(new StateLabel("state", currentTask, labelClasses, TaskListPanel.this.stateNames));
            item.add(new EditableUserLabel("owner", currentTask, allProfiles));

            // Need to provide elements in each row with unique IDs to reference them from JS.
            final String itemMarkupId = "task-" + currentTask.getId();
            final String deleteMarkupId = "task-delete-" + currentTask.getId();

            item.setMarkupId(itemMarkupId);

            // Emit JS to control delete button visibility based on task item hovering.
            final String script = String.format(ResourceReader.getResource(ResourceReader.HOVER_SUPPORT),
                    deleteMarkupId, itemMarkupId);
            item.add(new Label("hoverSupport", script).setEscapeModelStrings(false));

            item.add(new AjaxLink("delete") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    TaskDAO.delete(currentTask);
                    setResponsePage(OverviewPage.class);
                }
            }.setMarkupId(deleteMarkupId));
        }
    } // TasksList

    private static class AddTaskForm extends StatelessForm<Task> {

        public AddTaskForm(String id) {
            super(id, new CompoundPropertyModel<Task>(new Task()));

            add(new ErrorPanel("newTaskFeedback").setOutputMarkupId(true));
            add(new RequiredTextField<String>("description"));
        }
    }

}
