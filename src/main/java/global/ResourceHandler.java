package global;

import com.client.Application;
import com.client.DB;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Optional;

public class ResourceHandler
{
    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);
    private static final String jarFilePath = Application.jarFilePath;
    private static final String settingFile = "res/Settings/settings.ini";
    private static final String SQLITE_CONNECTION_URL = "jdbc:sqlite:file:mydatabase.db?cipher=chacha20&key=S:3,FAv.vE#pRL>";

    public static Optional<String> getSettings(String section, String key)
    {
        try
        {
            Wini ini = new Wini();
            ini.load(new FileReader(jarFilePath + settingFile));
            String result = ini.get(section, key);

            if (result != null) return Optional.of(result);
        }
        catch (IOException e) {
            log.error("cannot find the config file\ncalled from getSettings()\n{}" + settingFile, jarFilePath);
        }
        return Optional.empty();
    }

    public static void changeSettings(String section, String key, String value)
    {
        try
        {
            String settingFilePath = jarFilePath + settingFile;
            File iniFile = new File(settingFilePath);
            Wini ini = new Wini(iniFile);

            ini.put(section, key, value);
            ini.store();
        }
        catch (IOException e) {
            log.error("cannot find the config file\ncalled from changeSettings()");
        }
    }

    public static Font getFont(String name, float size)
    {
        File FontFile;
        Font font = null;
        try
        {
            FontFile = getFontFile(jarFilePath + "res/Fonts/" + name);
            font = Font.createFont(Font.TRUETYPE_FONT, FontFile).deriveFont(size);
        }
        catch (Exception e) {
            log.error("could not find the font\nCalled from getFont");
        }
        return font;
    }

    private static File getFontFile(String relFilePath)
    {
        if (relFilePath.contains("%20"))
        {
            relFilePath = relFilePath.replaceAll("%20", " ");
        }
        return new File(relFilePath);
    }

    public static void createLocalDB()
    {
        try (Connection conn = DriverManager.getConnection(SQLITE_CONNECTION_URL))
        {
            String createTable = "CREATE TABLE IF NOT EXISTS localdata" +
                    "(Username VARCHAR(40) PRIMARY KEY, isEnabled BOOLEAN, Password VARCHAR(30), last_directory text)";

            try (Statement stmt = conn.createStatement())
            {
                if (stmt.execute(createTable))
                {
                    log.info("Table created");
                }
            }
        }
        catch (SQLException e) {
            log.error("{}", e.toString());
        }
    }

    public static void insertDataInLocalDB(String username, String password)
    {
        try (Connection conn = DriverManager.getConnection(SQLITE_CONNECTION_URL))
        {
            String insertQuery = "INSERT INTO localdata(Username, Password) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery))
            {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            log.error("{}", e.toString());
        }
    }

    public static void updateIsEnabledInLocalDB(boolean boolValue)
    {
        try (Connection conn = DriverManager.getConnection(SQLITE_CONNECTION_URL))
        {
            String updateQuery = "Update localdata SET isEnabled = ? WHERE Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery))
            {
                pstmt.setBoolean(1, boolValue);
                pstmt.setString(2, Application.user.username());
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            log.error("{}", e.toString());
        }
    }

    public static void updatePathInLocalDB(String last_directory)
    {
        try (Connection conn = DriverManager.getConnection(SQLITE_CONNECTION_URL))
        {
            String updateQuery = "Update localdata SET last_directory = ? WHERE Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery))
            {
                pstmt.setString(1, last_directory);
                pstmt.setString(2, Application.user.username());
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            log.error("{}: {}", e.getClass().getName(), e.getMessage());
        }
    }

    public static void updatePassword(String username, String password)
    {
        try (Connection conn = DriverManager.getConnection(SQLITE_CONNECTION_URL))
        {
            String updateQuery = "Update localdata SET Password = ? WHERE Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery))
            {
                pstmt.setString(1, password);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            log.error("{}: {}", e.getClass().getName(), e.getMessage());
        }
    }

    public static User getLocalData()
    {
        try (Connection conn = DriverManager.getConnection(SQLITE_CONNECTION_URL))
        {
            String fetchQuery = "SELECT Username, isEnabled, Password, last_directory FROM localdata";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(fetchQuery))
            {
                if (rs.next())
                {
                    return new User(rs.getString("Username"), rs.getBoolean("isEnabled"),
                            rs.getString("Password"), rs.getString("last_directory"));
                }
            }
        } catch (SQLException e)
        {
            log.error("{}: {}", e.getClass().getName(), e.getMessage());
        }
        return null;
    }

    public static void deleteLocalDB()
    {
        try
        {
            Path db = Paths.get("mydatabase.db");
            Path wal = Paths.get("mydatabase.db-wal");
            Path shm = Paths.get("mydatabase.db-shm");

            Files.deleteIfExists(db);
            Files.deleteIfExists(wal);
            Files.deleteIfExists(shm);
        }
        catch (IOException e) {
            log.error("{}", e.toString());
        }
    }

    public static void downloadPfp(String username, StringBuilder pathToSave)
    {
        final String fetchQuery = "SELECT Profile_Picture, Image_extension, last_updated FROM pfp WHERE Username = ?";

        try (final Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
             final PreparedStatement getPfp = conn.prepareStatement(fetchQuery))
        {
            getPfp.setString(1, username);
            ResultSet rs = getPfp.executeQuery();

            if (rs.next())
            {
                byte[] imageData = rs.getBytes("Profile_picture");

                if (imageData != null)
                {
                    String extension = rs.getString("Image_extension");
                    String lastUpdatedTimestamp = String.valueOf(rs.getObject("last_updated"))
                                                    .replaceAll(":", "")
                                                    .replaceAll("-", "")
                                                    .replaceAll("T", "_");

                    pathToSave.append(username).append("$").append(lastUpdatedTimestamp).append(extension);

                    try (InputStream in = new java.io.ByteArrayInputStream(imageData);
                         OutputStream out = new FileOutputStream(String.valueOf(pathToSave)))
                    {
                        byte[] buffer = new byte[8192];
                        int read;
                        while ((read = in.read(buffer)) != -1)
                        {
                            out.write(buffer, 0, read);
                        }
                    }
                    log.info("Image retrieved and saved successfully.");
                }
            }
        }
        catch (SQLException | IOException e) {
            log.error("{}", e.toString());
        }
    }

    public static String getSettings(String imgPath)
    {
        return Application.jarFilePath + "res/background/" + imgPath;
    }
}
