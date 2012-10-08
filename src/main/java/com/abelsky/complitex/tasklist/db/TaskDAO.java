package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.Profile;
import com.abelsky.complitex.tasklist.model.Project;
import com.abelsky.complitex.tasklist.model.Task;
import com.abelsky.complitex.tasklist.model.TaskState;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author andy
 */
public class TaskDAO {

    private static final Logger logger = LoggerFactory.getLogger(TaskDAO.class);

    public static List<Task> getTasks(Project project, String filter, int start, int limit) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            if (Strings.isEmpty(filter)) {
                return tasksMapper.selectAll(project.getId(), start, limit);

            } else {
                filter = "%" + filter + "%";
                return tasksMapper.select(project.getId(), start, limit, filter, filter);
            }

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("getTasks() for project id=" + project.getId() +
                        ", start=" + start +
                        ", limit=" + limit +
                        ", filter" + filter + " failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public static void setState(Task task, TaskState newState) {
        if (newState.equals(task.getState())) {
            return;
        }

        task.setState(newState);

        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            tasksMapper.updateState(task.getId(), newState.toString());
            session.commit();

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("setState() for taskId=" + task.getId() + ", newState=" + newState + " failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public static Profile getOwner(Task task) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProfileMapper profileMapper = session.getMapper(ProfileMapper.class);
            return profileMapper.selectProfile(task.getOwnerId());

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("getOwner() for taskId=" + task.getId() + " failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public static void setOwner(Task task, Profile profile) {
        if (task.getOwnerId() == profile.getId()) {
            return;
        }

        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            tasksMapper.updateOwner(task.getId(), profile.getId());
            session.commit();
            task.setOwnerId(profile.getId());

        } finally {
            session.close();
        }
    }

    public static void addTask(String taskDescription, Project ownerProject, Profile creator, Profile owner) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            tasksMapper.insert(taskDescription, ownerProject.getId(), creator.getId(), owner.getId());
            session.commit();

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("addTask() for taskDescription=" + taskDescription +
                        ", ownerProjectId=" + ownerProject.getId() +
                        ", creatorId=" + creator.getId() +
                        ", ownerId=" + owner.getId() + " failed", e);
            }
            throw e;

        } finally {
            session.close();
        }

    }

    public static void delete(Task task) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            tasksMapper.delete(task.getId());
            session.commit();

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("delete() failed for taskId=" + task.getId(), e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public static int getCount(Project project, String filter) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            if (Strings.isEmpty(filter)) {
                return tasksMapper.selectCount(project.getId());

            } else {
                filter = "%" + filter + "%";
                return tasksMapper.selectCountFiltered(project.getId(), filter, filter);
            }

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("getCount() for projectId=" + project.getId() +
                        ", filter=" + filter + " failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public static void updateDescription(Task task, String newDescription) {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final TasksMapper tasksMapper = session.getMapper(TasksMapper.class);
            tasksMapper.updateDescription(task.getId(), newDescription);
            session.commit();
            task.setDescription(newDescription);

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("updateDescription() for taskId=" + task.getId() + " failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }
}
