package org.example;

import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.System.Logger;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Singleton;

@DataSourceDefinition(
        name = DataSourceConfig.DS_NAME,
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
@Singleton
public class DataSourceConfig {

    public static final String DS_NAME = "java:app/App/MainDs";
    private static final Logger log = System.getLogger(DataSourceConfig.class.getName());

    @Resource(lookup = DS_NAME)
    private DataSource dataSource;

    public void startup(@Observes Startup startup) {
        log.log(Logger.Level.INFO, "#### Data source connection check...");
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.setQueryTimeout(10);
            var isValid = stmt.execute("SELECT '1'");
            if (!isValid) {
                throw new RuntimeException("#### Connection validation failed.");
            }
            log.log(Logger.Level.INFO, "#### Connection is valid");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
