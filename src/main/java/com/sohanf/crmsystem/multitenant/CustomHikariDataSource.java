package com.sohanf.crmsystem.multitenant;

import com.zaxxer.hikari.HikariDataSource;

import javax.swing.text.Utilities;
import java.sql.Connection;
import java.sql.SQLException;

public class CustomHikariDataSource extends HikariDataSource {
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection =  super.getConnection();
        connection.setSchema(TenantContext.getCurrentTenant());
        return connection;
    }
}
