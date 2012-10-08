package com.abelsky.complitex.tasklist.db;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andy
 */
class ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    private static final SqlSessionFactory sessionFactory;

    static {
        try {
            final Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            logger.error("Cannot initialize MyBatis", e);
            throw new RuntimeException(e);
        }
    }

    public static SqlSessionFactory getSession() {
        return sessionFactory;
    }
}
