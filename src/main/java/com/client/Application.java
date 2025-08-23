package com.client;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import global.ResourceHandler;
import global.Theme;
import global.UserDetails;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Application
{
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
                raven.chat.swing.TextField.switchTheme(currentTheme);
                raven.chat.component.ChatBox.switchTheme(currentTheme);
                raven.chat.swing.Background.switchTheme(currentTheme);
                natter.updateTheme();
                ResourceHandler.changeSettings("Global", "Theme", "DARK_MODE");
            });
        }
        else
        {
            EventQueue.invokeLater(() -> {
                FlatMacLightLaf.setup();
                FlatLaf.updateUI();
                currentTheme = Theme.LIGHT_MODE;
                raven.chat.swing.TextField.switchTheme(currentTheme);
                raven.chat.component.ChatBox.switchTheme(currentTheme);
                raven.chat.swing.Background.switchTheme(currentTheme);
                natter.updateTheme();
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
