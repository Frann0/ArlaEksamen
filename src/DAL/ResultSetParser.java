package DAL;

import BE.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetParser {

    /**
     * Creates and returns a ScreenBit object from a ResultSet row.
     *
     * @param rs
     * @return ScreenBit object
     * @throws SQLException
     */
    public ScreenBit getScreenBit(ResultSet rs) throws SQLException {

        // Get ScreenBit info from ResultSet
        int screenId = rs.getInt("ScreenId");
        String name = rs.getString("ScreenName");
        String screenInfo = rs.getString("ScreenInfo");

        ScreenBit newScreenBit = new ScreenBit(screenId, name, screenInfo);

        return newScreenBit;
    }

    /**
     * Creates and returns a User object from a ResultSet row.
     *
     * @param rs
     * @return User object
     * @throws SQLException
     */
    public User getUser(ResultSet rs) throws SQLException {

        // Get assigned User info from ResultSet

        int userId = rs.getInt("Id");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String userName = rs.getString("UserName");
        String email = rs.getString("Email");
        int password = rs.getInt("Password");
        int userRole = rs.getInt("UserRole");
        int phoneNumber = rs.getInt("Phone");
        Enum gender = rs.getBoolean("Gender") ? Gender.Male : Gender.Female;
        String photoPath = rs.getString("PhotoPath");
        String title = rs.getString("Title");

        return new User(userId, firstName, lastName, userName, email, password, userRole, phoneNumber, gender, photoPath, title);
    }

    /**
     * Creates and returns a User object from a ResultSet row.
     *
     * @param rs
     * @return User object
     * @throws SQLException
     */
    public Bug getBug(ResultSet rs) throws SQLException {
        int bugId = rs.getInt("Id");
        String description = rs.getString("Description");
        String fixMessage = rs.getString("FixMessage");
        String dateRegistered = String.valueOf(rs.getTimestamp("DateRegistered"));
        Boolean bugResolved = rs.getInt("ResolvedStatus") == 1;
        int adminId = rs.getInt("AssignedAdmin");
        String screenName = rs.getString("ScreenName");
        String referencedUser = rs.getString("UserName");

        return new Bug(bugId, description, fixMessage, dateRegistered, bugResolved, adminId, screenName, referencedUser);
    }

    /**
     * Returns a setting.
     * @param rs
     * @return
     * @throws SQLException
     */
    public Settings getSetting(ResultSet rs) throws SQLException {
        int settingsId = rs.getInt("Id");
        SettingsType settingsType = SettingsType.valueOf(rs.getInt("Type"));
        String attribute = rs.getString("Attribute");
        return new Settings(settingsId, settingsType, attribute);
    }

    /**
     * Returns the generated key, retrieved by a executeUpdate().
     * @param pSql
     * @return
     * @throws SQLException
     */
    public int getGeneratedKey(PreparedStatement pSql) throws SQLException {
        ResultSet generatedKeys = pSql.getGeneratedKeys();
        generatedKeys.next();

        return generatedKeys.getInt(1);
    }
}
