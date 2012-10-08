package com.abelsky.complitex.tasklist.db;

import com.abelsky.complitex.tasklist.model.Profile;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Accesses profiles.
 * <p>
 * Note: passwords are stored and retrieved in hashed form.
 * </p>
 *
 * @author andy
 */
public class ProfileDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProfileDAO.class);

    public static List<Profile> getAllProfiles() {
        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProfileMapper profileMapper = session.getMapper(ProfileMapper.class);
            return profileMapper.selectAll();

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("getAllProfiles() failed", e);
            }
            throw e;

        } finally {
            session.close();
        }
    }

    /**
     * @return {@code true} iff profile has been successfully created.
     */
    public static boolean addProfile(String username, String email, String password) {
        assert username != null && email != null && password != null;

        final SqlSession session = ConnectionFactory.getSession().openSession(false);

        try {
            final ProfileMapper profileMapper = session.getMapper(ProfileMapper.class);
            profileMapper.insertProfile(username, email, DigestUtils.md5Hex(password));

            session.commit();
            return true;

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Registration error", e);
            }

            if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                // XXX: dirty hack for MySQL
                return false;
            } else {
                throw e;
            }

        } finally {
            session.close();
        }
    }

    public static Profile getProfile(String name, String password) {
        final String passwordHash = DigestUtils.md5Hex(password);

        final SqlSession session = ConnectionFactory.getSession().openSession();
        try {
            final ProfileMapper profileMapper = session.getMapper(ProfileMapper.class);
            return profileMapper.getProfile(name, passwordHash);

        } catch (PersistenceException e) {
            if (logger.isErrorEnabled()) {
                logger.error("getProfile() failed for name=" + name + ", passwordHash=" + passwordHash, e);
            }
            throw e;

        } finally {
            session.close();
        }
    }
}
