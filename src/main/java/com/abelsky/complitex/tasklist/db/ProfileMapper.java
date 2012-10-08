package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.Profile;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * @author andy
 */
interface ProfileMapper {

    @Select("SELECT * FROM profiles WHERE name = #{name} AND password = #{passwordHash}")
    Profile getProfile(@Param("name") String name, @Param("passwordHash") String passwordHash) throws PersistenceException;

    @Select("SELECT * FROM profiles WHERE id = #{id}")
    Profile selectProfile(@Param("id") int id) throws PersistenceException;

    @Select("SELECT * FROM profiles")
    List<Profile> selectAll() throws PersistenceException;

    @Insert("INSERT INTO profiles (name, email, password) VALUES(#{name}, #{email}, #{passwordHash})")
    int insertProfile(@Param("name") String name, @Param("email") String email, @Param("passwordHash") String passwordHash) throws PersistenceException;
}
