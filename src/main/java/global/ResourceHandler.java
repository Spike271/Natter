package global;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Optional;

import com.client.Application;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class ResourceHandler
{
	private static final String jarFilePath = Application.jarFilePath;
	private static final String settingFile = "res/Settings/settings.ini";
	private static final String colorFile = "libres/Settings/Color.ini";
    private static final String url = "jdbc:sqlite:mydatabase.db";

    public static Optional<String> getSettings(String section, String key)
    {
        try
        {
            INIConfiguration iniConfig = new INIConfiguration();
            FileHandler fileHandler = new FileHandler(iniConfig);

            fileHandler.load(new File(jarFilePath + settingFile));
            return Optional.of(iniConfig.getString(section + "." + key));
        }
        catch (ConfigurationException e)
        {
            System.err.println("cannot find the config file\ncalled from getSettings()\n"+ jarFilePath + settingFile);
        }
        return Optional.empty();
    }

	public static void changeSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);

			String settingFilePath = jarFilePath + settingFile;
			fileHandler.load(new File(settingFilePath));
			
			iniConfig.setProperty(property, value);
			iniConfig.write(new FileWriter(settingFilePath));
		}
		catch (ConfigurationException | IOException e)
		{
			System.err.println("cannot find the config file\ncalled from changeSettings()");
		}
	}
	
	public static void changeColorFileSettings(String property, String value)
	{
		try
		{
			INIConfiguration iniConfig = new INIConfiguration();
			FileHandler fileHandler = new FileHandler(iniConfig);

			String colorFilePath = jarFilePath + colorFile;
			fileHandler.load(new File(colorFilePath));
			
			iniConfig.setProperty(property, value);
			iniConfig.write(new FileWriter(colorFilePath));
		}
		catch (ConfigurationException | IOException _)
		{
			System.err.println("cannot find the config file\ncalled from changeColorFileSettings()");
		}
	}
	
	public static Font getFont(String name, float size)
	{
		File FontFile;
		Font font = null;
		try
		{
			FontFile = getFontFile( jarFilePath + "res/Fonts/" + name);
			font = Font.createFont(Font.TRUETYPE_FONT, FontFile).deriveFont(size);
		}
		catch (Exception e)
		{
			System.err.println("could not find the font\nCalled from getFont");
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
        try (Connection conn = DriverManager.getConnection(url))
        {
            String createTable = "CREATE TABLE IF NOT EXISTS localdata" +
                    "(Username VARCHAR(40) PRIMARY KEY, isEnabled BOOLEAN, Password VARCHAR(30), last_directory text)";

            try (Statement stmt = conn.createStatement())
            {
                if (stmt.execute(createTable))
                    System.out.println("Table created");
            }
        }
        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void insertDataInLocalDB(String username, String password)
    {
        try (Connection conn = DriverManager.getConnection(url))
        {
            String insert = "INSERT INTO localdata(Username, Password) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insert))
            {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void updateIsEnabledInLocalDB(boolean boolValue)
    {
        try (Connection conn = DriverManager.getConnection(url))
        {
            String insert = "Update localdata SET isEnabled = ? where Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(insert))
            {
                pstmt.setBoolean(1, boolValue);
                pstmt.setString(2, Application.userDetails.username());
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void updatePathInLocalDB(String last_directory)
    {
        try (Connection conn = DriverManager.getConnection(url))
        {
            String insert = "Update localdata SET last_directory = ? where Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(insert))
            {
                pstmt.setString(1, last_directory);
                pstmt.setString(2, Application.userDetails.username());
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void updatePassword(String username, String password)
    {
        try (Connection conn = DriverManager.getConnection(url))
        {
            String insert = "Update localdata SET Password = ? where Username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(insert))
            {
                pstmt.setString(1, password);
                pstmt.setString(2, username);
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static UserDetails getLocalData()
    {
        try (Connection conn = DriverManager.getConnection(url))
        {
            String query = "SELECT Username, isEnabled, Password, last_directory FROM localdata";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query))
            {
                if (rs.next())
                {
                    return new UserDetails(rs.getString("Username"), rs.getBoolean("isEnabled"),
                            rs.getString("Password"), rs.getString("last_directory"));
                }
            }
        }
        catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public static void deleteLocalDB()
    {
        try {
            Path db = Paths.get("mydatabase.db");
            Path wal = Paths.get("mydatabase.db-wal");
            Path shm = Paths.get("mydatabase.db-shm");

            Files.deleteIfExists(db);
            Files.deleteIfExists(wal);
            Files.deleteIfExists(shm);
        }
        catch (IOException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static String getSettings(String imgPath)
    {
        return Application.jarFilePath + "res/background/" + imgPath;
    }
}
