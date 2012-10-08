package com.abelsky.complitex.tasklist.components.state;

import com.abelsky.complitex.tasklist.model.TaskState;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import java.util.Map;

/**
 * Renders state title.
 *
 * @author andy
 */
class StateRenderer implements IChoiceRenderer<TaskState> {

    private final Map<TaskState, String> stateNames;

    public StateRenderer(Map<TaskState, String> stateNames) {
        this.stateNames = stateNames;
    }

    @Override
    public Object getDisplayValue(TaskState state) {
        return stateNames.get(state);
    }

    @Override
    public String getIdValue(TaskState object, int index) {
        return object.toString();
    }
}
