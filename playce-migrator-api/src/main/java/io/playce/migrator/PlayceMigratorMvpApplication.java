package io.playce.migrator;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.playce.migrator.util.ObjectChecker.requireTrue;

@SpringBootApplication(scanBasePackages = {"io.playce.migrator", "io.playce.common.subscription"})
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class PlayceMigratorMvpApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        initDatabase();
        SpringApplication.run(PlayceMigratorMvpApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        initDatabase();
        super.onStartup(servletContext);
    }

    private static void initDatabase() {
        String jdbcUrl = System.getProperty("spring.datasource.url", "jdbc:mariadb://localhost:3306/migratordb");
        String username = System.getProperty("spring.datasource.username", "playce");
        String password = System.getProperty("spring.datasource.password", "playce");

        requireTrue(jdbcUrl.contains("mysql") || jdbcUrl.contains("maria"), new PlayceMigratorException(ErrorCode.PM402, jdbcUrl + " is unsupported DB or JDBC driver not exist."));
        // DB 로깅을 비활성화한다.
        String url = jdbcUrl.replaceAll("log4jdbc:", "");
        String driverClass = jdbcUrl.contains("mariadb") ? "org.mariadb.jdbc.Driver" : "com.mysql.jdbc.Driver";

        try {
            Class.forName(driverClass).getDeclaredConstructor().newInstance();
            List<String> tableNames = getTableNames(username, password, url);
            if (tableNames.isEmpty()) {
                System.out.println(new Date() + " : Migrator database NOT initialized. Starting auto configuration for Migrator database.");
                System.setProperty("spring.sql.init.mode", "always");
            } else {
                System.out.println(new Date() + " : Migrator database already initialized.");
            }
        } catch (PlayceMigratorException e) {
            System.err.println("\n" + e.getMessage());
            System.err.println("\nMigrator will be terminated. Please use MariaDB over 10.6.0.");
            System.err.println("[Connection URL] : " + url);
            System.exit(-1);
        } catch (SQLNonTransientConnectionException | SQLInvalidAuthorizationSpecException | SQLSyntaxErrorException e) {
            System.err.println("\n" + e.getMessage());
            System.err.println("\nMigrator will be terminated. Please check the DB connection information is valid.");
            System.err.println("[Connection URL] : " + url);
            System.err.println("[Username] : " + username);
            System.err.println("[Password] : " + password);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getTableNames(String username, String password, String url) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement stmt = connection.createStatement();
             ResultSet results = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '" + connection.getCatalog() + "';")
        ) {
            while (results.next()) {
                tableNames.add(results.getString("table_name").toUpperCase());
            }
        }
        return tableNames;
    }

}
