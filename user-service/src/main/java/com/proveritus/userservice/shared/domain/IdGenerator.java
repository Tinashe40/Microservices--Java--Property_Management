package com.proveritus.userservice.shared.domain;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String prefix = "";
        if (object.getClass().getSimpleName().equals("User")) {
            prefix = "USR-";
        } else if (object.getClass().getSimpleName().equals("UserGroup")) {
            prefix = "RLE-";
        } else if (object.getClass().getSimpleName().equals("Permission")) {
            prefix = "PRM-";
        } else if (object.getClass().getSimpleName().equals("UserToken")) {
            prefix = "UT-";
        } else if (object.getClass().getSimpleName().equals("PasswordHistory")) {
            prefix = "PH-";
        } else if (object.getClass().getSimpleName().equals("PasswordPolicy")) {
            prefix = "PP-";
        }

        Connection connection = session.getJdbcConnectionAccess().obtainConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select count(id) as id from " + object.getClass().getSimpleName());
            if (rs.next()) {
                int id = rs.getInt(1) + 1;
                return prefix + String.format("%04d", id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
