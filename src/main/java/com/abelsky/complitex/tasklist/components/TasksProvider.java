package com.abelsky.complitex.tasklist.components;

import com.abelsky.complitex.tasklist.db.TaskDAO;
import com.abelsky.complitex.tasklist.model.Project;
import com.abelsky.complitex.tasklist.model.Task;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Iterator;

/**
 * Provides filtered list of tasks in the specified project.
 *
 * @author andy
 */
public class TasksProvider extends SortableDataProvider<Task> {
    private final Project project;
    private final IModel<String> filter;

    public TasksProvider(Project project, IModel<String> filter) {
        this.project = project;
        this.filter = filter;
    }

    @Override
    public Iterator<? extends Task> iterator(int first, int count) {
        return TaskDAO.getTasks(project, filter.getObject(), first, count).iterator();
    }

    @Override
    public int size() {
        return TaskDAO.getCount(project, filter.getObject());
    }

    @Override
    public IModel<Task> model(Task object) {
        return new Model<Task>(object);
    }
}
