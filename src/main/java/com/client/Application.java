package com.client;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import global.ResourceHandler;
import global.Theme;
import global.UpdateTheme;
import global.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class Application
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static SignIn signIn;
    public static SignUp signUp;
    public static Natter natter;
    public static Theme currentTheme;
    public static SettingPanel settingPanel;
    public static String jarFilePath;
    public static User user;
    private static PasswordWindow ps;

    static
    {
        try
        {
            Application.initStrings temp = new Application.initStrings();
            jarFilePath = temp.mainPath;

            if(jarFilePath.contains(".jar"))
            {
                jarFilePath = new File(jarFilePath).getParent();
                jarFilePath += "/";
            }

            currentTheme = ResourceHandler.getSettings("Global", "Theme").map(Theme::valueOf).orElse(Theme.LIGHT_MODE);
        }
        catch (URISyntaxException _) {}
    }

    public static void Run() throws InvocationTargetException, InterruptedException
    {
        FlatLaf.registerCustomDefaultsSource("res.com.themes");
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));

        if (Application.currentTheme == Theme.DARK_MODE) FlatMacDarkLaf.setup();
        else FlatMacLightLaf.setup();

        user = ResourceHandler.getLocalData();
        ComparePfpTime();

        if (user != null)
        {
            initMainUi();
            if (user.isPasswordEnabled()) ps.setVisible(true);
            else natter.setVisible(true);
        }
        else
        {
            init();
            initMainUi();
        }
    }

    private static void ComparePfpTime()
    {
        Path dir = new File(Application.jarFilePath + "profile/").toPath();
        try (Stream<Path> stream = Files.list(dir))
        {
           String fileName = stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().startsWith(Application.user.username()))
                    .map(p -> p.getFileName().toString())
                    .findFirst().orElse("");

           if (!fileName.isEmpty())
           {
               LocalDateTime localPfpDateTime = parseFileNameToDateTime(fileName);
               LocalDateTime serverPfpDateTime;

               final String selectQuery = "SELECT last_updated FROM pfp WHERE Username = ?";

               try (final Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
                    PreparedStatement stmt = conn.prepareStatement(selectQuery))
               {
                   stmt.setString(1, Application.user.username());
                   try (final ResultSet rs = stmt.executeQuery())
                   {
                       while (rs.next())
                       {
                           serverPfpDateTime = (LocalDateTime) rs.getObject("last_updated");
                           if (localPfpDateTime.isBefore(serverPfpDateTime))
                           {
                               if (Files.deleteIfExists(dir.resolve(fileName)))
                                ResourceHandler.downloadPfp(Application.user.username(), new StringBuilder(Application.jarFilePath + "profile/"));
                           }
                       }
                   }
               }
               catch (SQLException e) {
                   log.error("Error while comparing pfp time\n {}", e.toString());
               }
           }
           else
           {
                ResourceHandler.downloadPfp(Application.user.username(), new StringBuilder(Application.jarFilePath + "profile/"));
           }
        }
        catch (IOException _) {}
    }

    private static LocalDateTime parseFileNameToDateTime(String fileName)
    {
        String dateTimeInAndroidFormat = fileName.split("\\$")[1].split("\\.")[0];
        String date = dateTimeInAndroidFormat.split("_")[0];
        date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);

        String time = dateTimeInAndroidFormat.split("_")[1];
        time = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);

        return LocalDateTime.parse(date + "T" + time);
    }

    private static void init()
    {
        SwingUtilities.invokeLater(() -> {

            signIn = new SignIn();
            signUp = new SignUp();

            signIn.setVisible(true);
            signUp.setVisible(false);
        });
    }

    private static void initMainUi() throws InvocationTargetException, InterruptedException
    {
        SwingUtilities.invokeAndWait(() -> {

            ps = new PasswordWindow();
            natter = new Natter();
            CreateSystemTray.createTrayIcon();
        });
    }

    public static void changeThemes()
    {
        if (currentTheme == Theme.LIGHT_MODE)
        {
            EventQueue.invokeLater(() -> {
                FlatMacDarkLaf.setup();
                FlatLaf.updateUI();
                currentTheme = Theme.DARK_MODE;
                UpdateTheme.update();
                ResourceHandler.changeSettings("Global", "Theme", "DARK_MODE");
            });
        }
        else
        {
            EventQueue.invokeLater(() -> {
                FlatMacLightLaf.setup();
                FlatLaf.updateUI();
                currentTheme = Theme.LIGHT_MODE;
                UpdateTheme.update();
                ResourceHandler.changeSettings("Global", "Theme", "LIGHT_MODE");
            });
        }
    }

    private static class initStrings
    {
        public String mainPath;

        public initStrings() throws URISyntaxException
        {
            mainPath = ResourceHandler.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath().replaceAll("%20", " ");
        }
    }
}
