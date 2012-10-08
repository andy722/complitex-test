package com.abelsky.complitex.tasklist.components.models;

import com.abelsky.complitex.tasklist.db.ProjectDAO;
import com.abelsky.complitex.tasklist.model.Project;
import org.apache.wicket.model.Model;

/**
 * @author andy
 */
public class ProjectTitleModel extends Model<String> {

    private final Project project;

    public ProjectTitleModel(Project project) {
        this.project = project;
    }

    @Override
    public String getObject() {
        return project.getName();
    }

    @Override
    public void setObject(String newValue) {
        ProjectDAO.updateName(project, newValue);
    }

    public Project getProject() {
        return project;
    }
}
