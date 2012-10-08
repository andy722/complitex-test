package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.Task;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * @author andy
 */
interface TasksMapper {
    @Select("SELECT * FROM tasks WHERE project_id = #{project_id} LIMIT #{start},#{limit}")
    @Results({
            @Result(column = "creator_id", property = "creatorId"),
            @Result(column = "owner_id", property = "ownerId"),
            @Result(column = "project_id", property = "projectId")
    })
    List<Task> selectAll(@Param("project_id") int projectId,
                         @Param("start") int start,
                         @Param("limit") int limit) throws PersistenceException;

    @Select("SELECT COUNT(*) FROM tasks WHERE project_id=#{project_id}")
    int selectCount(@Param("project_id") int projectId) throws PersistenceException;

    @Select("SELECT COUNT(*) FROM tasks t " +
            "  JOIN profiles p" +
            "  ON p.id = t.owner_id" +
            "  WHERE t.project_id = #{project_id} AND (t.description LIKE #{description_filter} OR p.name LIKE #{user_filter})")
    int selectCountFiltered(@Param("project_id") int projectId,
                            @Param("description_filter") String descriptionFilter,
                            @Param("user_filter") String userFilter) throws PersistenceException;

    @Select("SELECT t.* FROM tasks t " +
            "  JOIN profiles p" +
            "  ON p.id = t.owner_id" +
            "  WHERE t.project_id = #{project_id} AND (t.description LIKE #{description_filter} OR p.name LIKE #{user_filter})" +
            "  LIMIT #{start},#{limit}")
    @Results({
            @Result(column = "creator_id", property = "creatorId"),
            @Result(column = "owner_id", property = "ownerId"),
            @Result(column = "project_id", property = "projectId")
    })
    List<Task> select(@Param("project_id") int projectId,
                      @Param("start") int start,
                      @Param("limit") int limit,
                      @Param("description_filter") String descriptionFilter,
                      @Param("user_filter") String userFilter) throws PersistenceException;

    @Update("UPDATE tasks SET state=#{state} WHERE id=#{id}")
    void updateState(@Param("id") int id, @Param("state") String state) throws PersistenceException;

    @Update("UPDATE tasks SET owner_id=#{profileId} WHERE id=#{id}")
    void updateOwner(@Param("id") int id, @Param("profileId") int profileId) throws PersistenceException;

    @Update("UPDATE tasks SET description=#{description} WHERE id=#{id}")
    void updateDescription(@Param("id") int id, @Param("description") String description) throws PersistenceException;

    @Insert("INSERT INTO tasks (description, creator_id, owner_id, project_id) VALUES (#{description}, #{creator_id}, #{owner_id}, #{project_id})")
    int insert(@Param("description") String description,
               @Param("project_id") int projectId,
               @Param("creator_id") int creatorId,
               @Param("owner_id") int ownerId) throws PersistenceException;

    @Delete("DELETE FROM tasks WHERE id=#{id}")
    void delete(@Param("id") int id) throws PersistenceException;

    @Delete("DELETE FROM tasks WHERE project_id=#{project_id}")
    void deleteAll(@Param("project_id") int projectId) throws PersistenceException;
}