package ru.pravvich.jdbc.action;

import org.junit.Assert;
import org.junit.Test;
import ru.pravvich.jdbc.properties.PropertiesLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PasswordExistCheckerTest {

    @Test
    public void whenPasswordIsNotExistThenPasswordIsExistReturnFalse()
            throws SQLException {

        final PropertiesLoader properties = mock(PropertiesLoader.class);
        when(properties.get("password_exist")).thenReturn("mock_script");

        final ResultSet set = mock(ResultSet.class);
        when(set.getBoolean(1)).thenReturn(false);
        when(set.next()).thenReturn(true);

        final PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(set);

        final Connection connection = mock(Connection.class);
        when(connection.prepareStatement("mock_script")).thenReturn(statement);

        final PasswordExistChecker checker =
                new PasswordExistChecker(connection, properties);

        final boolean result = checker.passwordIsExist("test");

        Assert.assertFalse(result);

        verify(properties).get("password_exist");
        verify(connection).prepareStatement("mock_script");
        verify(statement).setString(1, "test");
        verify(statement).executeQuery();
        verify(set).next();
        verify(set).getBoolean(1);
    }

    @Test
    public void whenPasswordExistThenPasswordIsExistReturnTrue()
            throws SQLException {

        final PropertiesLoader properties = mock(PropertiesLoader.class);
        when(properties.get("password_exist")).thenReturn("mock_script");

        final ResultSet set = mock(ResultSet.class);
        when(set.getBoolean(1)).thenReturn(true);
        when(set.next()).thenReturn(true);

        final PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(set);

        final Connection connection = mock(Connection.class);
        when(connection.prepareStatement("mock_script")).thenReturn(statement);

        final PasswordExistChecker checker =
                new PasswordExistChecker(connection, properties);

        final boolean result = checker.passwordIsExist("test");

        Assert.assertTrue(result);

        verify(properties).get("password_exist");
        verify(connection).prepareStatement("mock_script");
        verify(statement).setString(1, "test");
        verify(statement).executeQuery();
        verify(set).next();
        verify(set).getBoolean(1);
    }

    @Test
    public void whenResultSetNextReturnFalseThenReturnFalse()
            throws SQLException {

        final PropertiesLoader properties = mock(PropertiesLoader.class);
        when(properties.get("password_exist")).thenReturn("mock_script");

        final ResultSet set = mock(ResultSet.class);
        when(set.getBoolean(1)).thenReturn(true);
        when(set.next()).thenReturn(false);

        final PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(set);

        final Connection connection = mock(Connection.class);
        when(connection.prepareStatement("mock_script")).thenReturn(statement);

        final PasswordExistChecker checker =
                new PasswordExistChecker(connection, properties);

        final boolean result = checker.passwordIsExist("test");

        Assert.assertFalse(result);

        verify(properties).get("password_exist");
        verify(connection).prepareStatement("mock_script");
        verify(statement).setString(1, "test");
        verify(statement).executeQuery();
        verify(set).next();
    }

    @Test
    public void whenTrowSQLException() throws SQLException {


        final PropertiesLoader properties = mock(PropertiesLoader.class);
        when(properties.get("password_exist")).thenReturn("mock_script");

        final Connection connection = mock(Connection.class);
        when(connection.prepareStatement("mock_script"))
                .thenThrow(mock(SQLException.class));

        final PasswordExistChecker checker =
                new PasswordExistChecker(connection, properties);

        final boolean result = checker.passwordIsExist("test");

        Assert.assertFalse(result);

        verify(properties).get("password_exist");

        verify(connection).prepareStatement("mock_script");
    }
}