package me.theyinspire.projects.grizzle.importer.input.reader.impl;

import me.theyinspire.projects.grizzle.importer.input.reader.InputReader;

import java.sql.*;
import java.util.Iterator;

public abstract class AbstractSqliteInputReader<E> implements InputReader<E> {

    private final Connection connection;
    private final long total;
    private final ResultSet resultSet;
    private long current;

    public AbstractSqliteInputReader(final String file) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        this.total = getTotal();
        this.current = -1;
        final PreparedStatement statement = connection.prepareStatement(selectQuery());
        resultSet = statement.executeQuery();
    }

    protected Connection getConnection() {
        return connection;
    }

    private long getTotal() throws SQLException {
        final PreparedStatement count = connection.prepareStatement(countQuery());
        final ResultSet resultSet = count.executeQuery();
        resultSet.next();
        long total = resultSet.getLong(1);
        resultSet.close();
        return total;
    }

    @Override
    protected void finalize() throws Throwable {
        resultSet.close();
        connection.close();
    }

    @Override
    public long total() {
        return total;
    }

    @Override
    public long current() {
        return current;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                try {
                    return resultSet.next();
                } catch (SQLException e) {
                    return false;
                }
            }

            @Override
            public E next() {
                try {
                    final E result = readObject(resultSet);
                    current++;
                    return result;
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to read data", e);
                }
            }
        };
    }

    protected abstract E readObject(ResultSet resultSet) throws SQLException;

    protected abstract String countQuery();

    protected abstract String selectQuery();

}
