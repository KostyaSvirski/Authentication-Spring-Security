package com.epam.esm.impl;

import com.epam.esm.TagDao;
import com.epam.esm.exception.DBCPDataSourceException;
import com.epam.esm.exception.DaoException;
import com.epam.esm.pool.DBCPDataSource;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String SQL_CREATE_TAG = "insert into tag_for_certificates (name) values (?)";
    private static final String SQL_DELETE_JUNCTIONS = "delete from junction_gift_cerficates_and_tags where id_tag = ?";
    private static final String SQL_DELETE_TAG = "delete from tag_for_certificates where id_tag = ?";
    private static final String SQL_FIND_ALL_TAGS = "select tag_for_certificates.id_tag, tag_for_certificates.name " +
            " from tag_for_certificates";
    private static final String SQL_FIND_SPECIFIC_TAGS = "select tag_for_certificates.id_tag, tag_for_certificates.name  " +
            " from tag_for_certificates" +
            " where tag_for_certificates.name = ?";
    private static final String SQL_FIND_LAST_ID = "select max(id_tag) from tag_for_certificates";

    @Autowired
    private DBCPDataSource dataSource;

    @Override
    public int create(Tag tag) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_TAG);
                ps.setString(1, tag.getName());
                ps.executeUpdate();
                ps = connection.prepareStatement(SQL_FIND_LAST_ID);
                ResultSet rs = ps.executeQuery();
                int id = 0;
                while (rs.next()) {
                    id = rs.getInt(1);
                }
                connection.commit();
                return id;
            } catch (SQLException throwables) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new DaoException("error due to rollback");
                }
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void delete(long id) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(SQL_DELETE_JUNCTIONS);
                ps.setInt(1, (int) id);
                ps.executeUpdate();
                ps = connection.prepareStatement(SQL_DELETE_TAG);
                ps.setInt(1, (int) id);
                ps.executeUpdate();
                connection.commit();
            } catch (SQLException throwables) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new DaoException("error occurs due to rollback", throwables);
                }
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Tag> find(String name) throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_SPECIFIC_TAGS);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                List<Tag> listOfEntities = createListOfEntities(rs);
                return listOfEntities;
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Tag> findAll() throws DaoException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_TAGS);
                ResultSet rs = ps.executeQuery();
                List<Tag> listOfEntities = createListOfEntities(rs);
                return listOfEntities;
            } catch (SQLException throwables) {
                throw new DaoException("error occurs while executing request", throwables);
            } finally {
                dataSource.closeConnection(connection);
            }
        } catch (DBCPDataSourceException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private List<Tag> createListOfEntities(ResultSet rs) throws SQLException {
        List<Tag> resultList = new ArrayList<>();
        while (rs.next()) {
            Tag newTag = new Tag.TagBuilder()
                    .buildId(Long.parseLong(Integer.toString(rs.getInt(1))))
                    .buildName(rs.getString(2))
                    .finishBuilding();
            resultList.add(newTag);
        }
        return resultList;
    }
}
