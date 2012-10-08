package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.Project;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author andy
 */
public class ProjectDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDAO.class);

    public static List<Project> getAllProjects() {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProjectsMapper projectsMapper = session.getMapper(ProjectsMapper.class);
            return projectsMapper.selectAll();

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("selectAll() failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    /**
     * @return {@code true} iff project has been successfully created.
     */
    public static boolean newProject(String name, String description) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProjectsMapper projectsMapper = session.getMapper(ProjectsMapper.class);
            projectsMapper.insertProject(name, description == null ? "" : description);
            session.commit();

        } catch (PersistenceException e) {
            logger.error("Cannot create project, name = " + name + ", description = " + description, e);
            return false;

        } finally {
            session.close();
        }

        return true;
    }

    public static void delete(Project project) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProjectsMapper projectsMapper = session.getMapper(ProjectsMapper.class);

            // Remove all associated tasks (we've got foreign keys to track this).
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            tasksMapper.deleteAll(project.getId());

            // Finally, remove the project itself.
            projectsMapper.delete(project.getId());

            session.commit();

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("delete() failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    /**
     * Updates name of the specified project (both in the DB and in the project instance).
     *
     * @return {@code true} iff name has been successfully changed.
     */
    public static boolean updateName(Project project, String newName) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProjectsMapper projectsMapper = session.getMapper(ProjectsMapper.class);
            projectsMapper.updateName(project.getId(), newName);
            session.commit();
            project.setName(newName);
            return true;

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("updateName() for projectId=" + project.getId() + " failed", e);
            }
            return false;

        } finally {
            session.close();
        }
    }

}
