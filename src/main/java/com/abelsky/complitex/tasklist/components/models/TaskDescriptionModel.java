package com.abelsky.complitex.tasklist.components.models;

import com.abelsky.complitex.tasklist.db.TaskDAO;
import com.abelsky.complitex.tasklist.model.Task;
import org.apache.wicket.model.Model;

/**
 * Provides task description, persists on change.
 *
* @author andy
*/
public class TaskDescriptionModel extends Model<String> {
    private final Task task;

    public TaskDescriptionModel(Task task) {
        this.task = task;
    }

    @Override
    public String getObject() {
        return task.getDescription();
    }

    @Override
    public void setObject(String newValue) {
        TaskDAO.updateDescription(task, newValue);
    }
}
