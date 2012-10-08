package com.abelsky.complitex.tasklist.pages;

import com.abelsky.complitex.tasklist.components.models.ProjectTitleModel;
import com.abelsky.complitex.tasklist.db.ProjectDAO;
import com.abelsky.complitex.tasklist.model.Project;
import com.abelsky.complitex.tasklist.panels.ProjectListPanel;
import com.abelsky.complitex.tasklist.panels.TaskListPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Main page, displaying projects and tasks.
 *
 * @author andy
 */
public class OverviewPage extends BaseAuthenticatedPage {

    public OverviewPage() {
        final List<Project> projects = ProjectDAO.getAllProjects();
        add(new ProjectListPanel("projects_panel", createTabs(projects)));
    }

    /**
     * Creates tabs for provided projects. Tab label is the project name.
     */
    private List<ITab> createTabs(List<Project> projects) {
        final List<ITab> projectTabs = new ArrayList<ITab>();

        for (final Project project : projects) {
            projectTabs.add(new AbstractTab(new ProjectTitleModel(project)) {
                @Override
                public WebMarkupContainer getPanel(String panelId) {
                    return new TaskListPanel(panelId, project);
                }
            });
        }

        return projectTabs;
    }

}
