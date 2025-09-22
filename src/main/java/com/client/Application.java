package com.client;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import global.ResourceHandler;
import global.Theme;
import global.UpdateTheme;
import global.UserDetails;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public static UserDetails userDetails;
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

        userDetails = ResourceHandler.getLocalData();
        ComparePfpTime();

        if (userDetails != null)
        {
            initMainUi();
            if (userDetails.isPasswordEnabled()) ps.setVisible(true);
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
                    .filter(p -> p.getFileName().toString().startsWith(Application.userDetails.username()))
                    .map(p -> p.getFileName().toString())
                    .findFirst().orElse("");

           if (!fileName.isEmpty())
           {
               String[] temp = fileName.split("\\$");
               String[] date = temp[1].split("T");
               String[] time = date[1].replaceAll("-", ":").split("\\.");

               LocalDateTime localPfpDateTime = LocalDateTime.parse(date[0] + "T" + time[0]);
               LocalDateTime serverPfpDateTime = null;
               System.out.println(localPfpDateTime);

               try (final Connection conn = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
                    ResultSet rs = conn.createStatement().executeQuery("select last_updated from pfp where Username = '" + Application.userDetails.username() + "'"))
               {
                   while (rs.next())
                   {
                      serverPfpDateTime  = (LocalDateTime) rs.getObject("last_updated");
                      System.out.println(serverPfpDateTime);
                      if (localPfpDateTime.isBefore(serverPfpDateTime))
                      {
                          System.out.println("Pfp is outdated");
                      }
                      else
                      {
                          System.out.println("Pfp is up to date");
                      }
                   }
               }
               catch (SQLException e)
               {
                   log.error("Error while comparing pfp time", e);
               }
           }
           else
           {
                ResourceHandler.downloadPfp(Application.userDetails.username(), Application.jarFilePath + "profile/");
           }
        }
        catch (IOException _) {}
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
