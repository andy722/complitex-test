package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.Project;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * @author andy
 */
interface ProjectsMapper {
    @Select("SELECT * FROM projects")
    List<Project> selectAll() throws PersistenceException;

    @Insert("INSERT INTO projects (name, description) VALUES (#{name}, #{description})")
    int insertProject(@Param("name") String name, @Param("description") String description) throws PersistenceException;

    @Delete("DELETE FROM projects WHERE id=#{id}")
    void delete(@Param("id") int id) throws PersistenceException;

    @Select("UPDATE projects SET name=#{name} WHERE id=#{id}")
    void updateName(@Param("id") int id, @Param("name") String newName) throws PersistenceException;
}
