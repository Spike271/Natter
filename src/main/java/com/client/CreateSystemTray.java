package com.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class CreateSystemTray
{
    private static final Logger log = LoggerFactory.getLogger(CreateSystemTray.class);

    public static void createTrayIcon()
    {
        if (!SystemTray.isSupported())
        {
            JOptionPane.showMessageDialog(null, "System tray is not supported.");
            System.exit(1);
        }
        createAndShowGUI();
    }

    private static void createAndShowGUI()
    {
        try
        {
            // Create a system tray
            SystemTray tray = SystemTray.getSystemTray();

            // Create an image for the tray icon
            Image image = new ImageIcon(Application.jarFilePath + "res/icons/logo16_16.png").getImage();

            // Create a popup menu
            PopupMenu popup = getPopupMenu();

            // Create the tray icon and add it to the system tray
            TrayIcon trayIcon = new TrayIcon(image, "Natter", popup);
            tray.add(trayIcon);

        } catch (AWTException ex) {
            log.error("TrayIcon creation error: {}", ex.getMessage());
        }
    }

    private static PopupMenu getPopupMenu()
    {
        PopupMenu popup = new PopupMenu();
        MenuItem showItem = new MenuItem("Show");
        MenuItem exitItem = new MenuItem("Exit");

        showItem.addActionListener(_ -> Application.natter.setVisible(true));

        exitItem.addActionListener(_ -> {
            MessagesSendAndReceive.stopMessageListening();
            System.exit(0);
        });

        popup.add(showItem);
        popup.add(exitItem);
        return popup;
    }
}
