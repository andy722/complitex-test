package com.abelsky.complitex.tasklist.components.state;

import com.abelsky.complitex.tasklist.db.TaskDAO;
import com.abelsky.complitex.tasklist.model.Task;
import com.abelsky.complitex.tasklist.model.TaskState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;
import org.apache.wicket.model.Model;

import java.util.Arrays;
import java.util.Map;

/**
 * Displays task state, e.g. new, in progress or done.
 * Provides state drop-down on click, persists on select.
 *
 * @author andy
 */
public class StateLabel extends AjaxEditableChoiceLabel<TaskState> {

    private final Task task;
    private final Map<TaskState, Behavior> labelClasses;

    public StateLabel(String id, Task task, Map<TaskState, Behavior> labelClasses, Map<TaskState, String> stateNames) {
        super(id, Model.<TaskState>of(task.getState()), Arrays.asList(TaskState.values()), new StateRenderer(stateNames));

        this.task = task;
        this.labelClasses = labelClasses;

        add(labelClasses.get(task.getState()));
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target) {
        final TaskState previousState = task.getState();
        final TaskState state = (TaskState) getDefaultModelObject();

        if (state != previousState) {
            // This also updates state field.
            TaskDAO.setState(task, state);

            // Change label markup class.
            remove(labelClasses.get(previousState));
            add(labelClasses.get(state));
        }

        super.onSubmit(target);
    }

}
