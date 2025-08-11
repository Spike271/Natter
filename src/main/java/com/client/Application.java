package com.client;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Application
{
    public static SignIn signIn;
    public static SignUp signUp;
    public static Natter natter;
    public static SettingPanel settingPanel;
    public static String jarFilePath;
    private static PasswordWindow ps;

    static
    {
        try
        {
            var temp = new Application.initStrings();
            jarFilePath = temp.mainPath;

            if(jarFilePath.contains(".jar"))
            {
                jarFilePath = new File(jarFilePath).getParent();
                jarFilePath += "/";
            }
        }
        catch (URISyntaxException _) {}
    }

    public static void Run() throws InvocationTargetException, InterruptedException
    {
        FlatLaf.registerCustomDefaultsSource("res.com.themes");
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));

        if (Theme.isDarkModeOn) FlatMacDarkLaf.setup();
        else FlatMacLightLaf.setup();

        if (ResourceHandler.alreadyAUser())
        {
            initMainUi();
            if (!ResourceHandler.readPropertiesFile("password").map(String::isBlank).orElseThrow())
            {
                if (ResourceHandler.decode(ResourceHandler.readPropertiesFile("password")
                                    .orElseThrow()).startsWith("true"))
                    ps.setVisible(true);

                else
                    natter.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Something went wrong\nPlease Reinstall the application.");
            }
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
