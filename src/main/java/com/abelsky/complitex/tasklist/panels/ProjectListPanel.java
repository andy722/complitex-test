package com.abelsky.complitex.tasklist.panels;

import com.abelsky.complitex.tasklist.Session;
import com.abelsky.complitex.tasklist.components.models.ProjectTitleModel;
import com.abelsky.complitex.tasklist.db.ProjectDAO;
import com.abelsky.complitex.tasklist.model.Project;
import com.abelsky.complitex.tasklist.pages.OverviewPage;
import com.abelsky.complitex.tasklist.util.ResourceReader;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import java.util.List;

/**
 * Displays panel with the list of project tabs.
 *
 * @author andy
 */
public class ProjectListPanel extends AjaxTabbedPanel {

    public ProjectListPanel(String id, List<ITab> tabs) {
        super(id, tabs);

        final AddProjectForm addProjectForm = new AddProjectForm("newProjectForm");
        add(addProjectForm);

        add(new AjaxSubmitLink("newProjectSubmit", addProjectForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                final Project project = (Project) form.getModelObject();
                if (!ProjectDAO.newProject(project.getName(), project.getDescription())) {
                    error(getLocalizer().getString("project.already.exists", this, "Project with the same name already exists. Please, try again."));
                    target.add(getForm().get("newProjectFeedback"));

                } else {
                    Session.get().setAttribute(Session.KEY_PROJECT_NAME, project.getName());
                    setResponsePage(OverviewPage.class);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getForm().get("newProjectFeedback"));
            }
        });

        // If the current project is stored in the session, jump to the corresponding tab.
        setActiveTab(tabs);
    }

    private void setActiveTab(List<ITab> tabs) {
        final String projectName = (String) Session.get().getAttribute(Session.KEY_PROJECT_NAME);
        if (projectName == null) {
            return;
        }

        // TODO: use project ID for this.
        for (int i = 0, tabsSize = tabs.size(); i < tabsSize; i++) {
            if (Strings.isEqual(getProject(i).getName(), projectName)) {
                setSelectedTab(i);
                break;
            }
        }
    }

    /**
     * @return Project associated with the selected tab.
     */
    private Project getSelectedProject() {
        return getProject(getSelectedTab());
    }

    /**
     * @param index Tab index
     * @return Project associated with the specified tab.
     */
    private Project getProject(int index) {
        if (getTabs().size() <= index) {
            return null;

        } else {
            final ProjectTitleModel projectModel = (ProjectTitleModel) getTabs().get(index).getTitle();
            return projectModel.getProject();
        }
    }

    @Override
    public TabbedPanel setSelectedTab(int index) {
        final TabbedPanel panel = super.setSelectedTab(index);

        final Project selectedProject = getSelectedProject();
        if (selectedProject != null) {
            // Store project name in the session for future page refreshes.
            Session.get().setAttribute(Session.KEY_PROJECT_NAME, selectedProject.getName());
        } else {
            // No projects are available yet.
        }

        return panel;
    }

    @Override
    protected WebMarkupContainer newLink(String originalLinkId, final int index) {
        final WebMarkupContainer item = new AjaxFallbackLink<Void>(originalLinkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (index != getSelectedTab()) {
                    setSelectedTab(index);
                    if (target != null) {
                        target.add(ProjectListPanel.this);
                    }
                    onAjaxUpdate(target);

                } else {
                    final ManuallyTriggeredAjaxEditableLabel label = (ManuallyTriggeredAjaxEditableLabel) get("title");
                    label.onEditManual(target);
                }
            }
        };

        // Need to provide elements in each row with unique IDs to reference them from JS.
        final String linkId = originalLinkId + index;
        final String deleteMarkupId = linkId + "-delete";

        item.setMarkupId(linkId);
        final String script = String.format(ResourceReader.getResource(ResourceReader.HOVER_SUPPORT),
                deleteMarkupId, linkId);

        //noinspection WicketForgeJavaIdInspection
        item.add(new Label("hoverSupport", script).setEscapeModelStrings(false));

        //noinspection WicketForgeJavaIdInspection
        item.add(new AjaxLink("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ProjectDAO.delete(getProject(index));
                setResponsePage(OverviewPage.class);
            }
        }.setMarkupId(deleteMarkupId));

        return item;
    }

    @Override
    protected Component newTitle(String titleId, IModel<?> titleModel, final int index) {
        return new ManuallyTriggeredAjaxEditableLabel<String>(titleId, new ProjectTitleModel(getProject(index))).setRequired(true);
    }

    @Override
    protected LoopItem newTabContainer(final int tabIndex) {
        return new LoopItem(tabIndex) {
            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);

                String cssClass = tag.getAttribute("class");
                if (cssClass == null) {
                    cssClass = " ";
                }
                cssClass += " tab" + getIndex();

                if (getIndex() == getSelectedTab()) {
                    cssClass += " selected active";
                }

                if (getIndex() == getTabs().size() - 1) {
                    cssClass += " last";
                }
                tag.put("class", cssClass.trim());
            }

            @Override
            public boolean isVisible() {
                return getTabs().get(tabIndex).isVisible();
            }

        };
    }

    private static class AddProjectForm extends StatelessForm<Project> {
        private final Project newProject = new Project();

        public AddProjectForm(String id) {
            super(id);

            add(new ErrorPanel("newProjectFeedback").setOutputMarkupId(true));

            setModel(new CompoundPropertyModel<Project>(newProject));

            add(new RequiredTextField<String>("name"));
            add(new TextArea<String>("description"));
        }
    }

    /**
     * Same as {@link AjaxEditableLabel}, but shows an editor on
     * {@link #onEditManual(org.apache.wicket.ajax.AjaxRequestTarget)} instead of handling the default action.
     */
    private static class ManuallyTriggeredAjaxEditableLabel<T> extends AjaxEditableLabel<T> {
        public ManuallyTriggeredAjaxEditableLabel(String id, IModel<T> model) {
            super(id, model);
        }

        @Override
        public void onEdit(AjaxRequestTarget target) {
            // Nothing here.
        }

        public void onEditManual(AjaxRequestTarget target) {
            if (!getEditor().isVisible()) {
                super.onEdit(target);
            }
        }
    }

}
