package pe.com.centro.configuration;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

import static java.lang.System.getenv;

/**
 * Manages Database connections. You can use the following static methods:
 * <ul>
 *     <li><i>{@code getConnection()}</i></li>
 *     <li><i>{@code close()}</i></li>
 * </ul>
 * This class doesn't have a class to close main connection because AWS
 * Lambda does it for us (when deletes the instance) and reuse it if the container is not deleted.
 */
@Slf4j
public final class BaseDAO {

    private static Connection connection;

    private static final String DB_URL =
            String.format("jdbc:postgresql://%s/%s", getenv("AWS_RDS_DB_SERVER"), getenv("AWS_RDS_DB_SCHEMA"));
    private static final String DB_USER = getenv("AWS_RDS_USER");
    private static final String DB_PASSWORD = getenv("AWS_RDS_PASSWORD");

    /**
     * Gives you a new connection using database credentials
     * <br/>
     * Check the following environment variables in {@code template.yml}
     * <ul>
     *     <li>{@code AWS_RDS_DB_SERVER}</li>
     *     <li>{@code AWS_RDS_DB_SCHEMA}</li>
     *     <li>{@code AWS_RDS_DB_USER}</li>
     *     <li>{@code AWS_RDS_DB_PASSWORD}</li>
     * </ul>
     *
     * @return a {@link java.sql.Connection} instance.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                log.info("Database", DB_URL);
            } catch (SQLException e) {
                log.error("Error during database connection", e);
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    /**
     * Closes a {@link java.sql.PreparedStatement} provided
     *
     * @param statement the {@link java.sql.PreparedStatement} to be closed
     */
    public static void close(PreparedStatement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            log.error("Error during statement closing", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes a {@link java.sql.ResultSet} provided
     *
     * @param resultSet the {@link java.sql.ResultSet} to be closed
     */
    public static void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            log.error("Error during resultSet closing", e);
            throw new RuntimeException(e);
        }
    }
}
