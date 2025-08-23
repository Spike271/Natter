package com.client;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import global.ResourceHandler;
import global.Theme;
import net.miginfocom.swing.MigLayout;
import raven.chatModal.ChatUI;
import raven.chatModal.ChatUI.ChatBoxList;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Natter extends JFrame
{
	private JPanel usersPanel, appDesc;
	private ChatUI chatComponent;
	private static JPanel selectedPanel;
	private final ArrayList<String> usersList = new ArrayList<>();
	private final HashMap<String, ChatUI> usersMap = new HashMap<>();
	
	public Natter()
	{
		super("Natter");
		
		this.setIconImage(new ImageIcon((Application.jarFilePath + "res/icons/logo32_32.png")).getImage());
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(1000, 800));
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				MessagesSendAndReceive.stopMessageListening();
			}
		});
		this.setLocationRelativeTo(null);
		this.setFocusable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		addGuiComponents();
        this.requestFocus();

        MessagesSendAndReceive.startMessageListening();
	}
	
	private JPanel userComponentPanel(String receiver)
	{
		JPanel chatItemPanel = new JPanel();
		chatItemPanel.setLayout(new MigLayout("", "[][]", "[]"));
        chatItemPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000; " +
                "[dark]background: #00000000");         // outer layer color of userlist's (transparent)
		
		Icon profileImage = createProfilePic(receiver);
		ProfilePicture avatar = new ProfilePicture();
		avatar.setBorderSize(1);
		avatar.setBorderSpace(1);
		avatar.setImage(profileImage);
		chatItemPanel.add(avatar, "al left, w 60, h 60");
		
		// Chat details
        JPanel detailsPanel = new JPanel(new MigLayout("al left, wrap, gapy 10", "[][]", "[][]"));
        detailsPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000;" +
                "[dark]background: #00000000"); // inner layer color of userlist's (transparent)

        JLabel nameLabel = new JLabel(receiver);
		nameLabel.setFont(ResourceHandler.getFont("ClearSans-Medium.ttf", 16f));
        nameLabel.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000;" +
                "[dark]foreground: #FFFFFF");
		detailsPanel.add(nameLabel, "pushx, growx, w 150!");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String currentTime = LocalDateTime.now().format(formatter);
		Thread.startVirtualThread(() -> UserInfo.addNewUser(receiver, currentTime));
		
		JLabel timestampLabel = new JLabel(currentTime);
		timestampLabel.setFont(ResourceHandler.getFont("ClearSans-Bold.ttf", 14f));
        timestampLabel.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000;" + "[dark]foreground: #FFFFFF");
		detailsPanel.add(timestampLabel);
		
		chatItemPanel.add(detailsPanel, "al center, w 230!");
		
		chatItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chatItemPanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent evt)
			{
				if (SwingUtilities.isLeftMouseButton(evt))
				{
					if (!ChatUI.userName.equals(receiver))
					{
                        if (selectedPanel != null) {
                            selectedPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000; " +
                                    "[dark]background: #00000000");
                        }

                        selectedPanel = chatItemPanel;
                        selectedPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #D2D2D2;" + "[dark]background: #515151");

						if (usersMap.containsKey(receiver))
						{
							if (chatComponent != null) remove(chatComponent);

							chatComponent = usersMap.get(receiver);
							add(chatComponent);
							remove(appDesc);
						}
						else
						{
							String userString = Application.userDetails.username();
							showChatUI(userString, receiver, createProfilePic(userString), profileImage);
						}
					}
				}
				
				if (SwingUtilities.isRightMouseButton(evt) && chatComponent != null)
				{
					if (ChatUI.userName.equals(receiver))
					{
                        JPopupMenu popupMenu = buildPopupMenu(receiver, chatItemPanel);
						popupMenu.show(chatItemPanel, evt.getX(), evt.getY());
					}
				}
				repaint();
				revalidate();
			}
		});
		
		return chatItemPanel;
	}

    private JPopupMenu buildPopupMenu(String receiver, JPanel chatItemPanel)
    {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem close = new JMenuItem("Close Chat",
                new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/close icon.svg"))
                        .derive(13, 13)
                        .setColorFilter(Application.currentTheme == Theme.DARK_MODE ? new ColorFilter(_ -> Color.WHITE) : null));

        close.addActionListener(_ -> {
            this.remove(chatComponent);
            usersMap.put(receiver, chatComponent);
            chatComponent = null;
            this.add(appDesc, BorderLayout.CENTER);
            ChatUI.userName = "";
            chatItemPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000;" + "[dark]background: #00000000");
            this.repaint();
            this.revalidate();
        });

        popupMenu.add(close);

        SwingUtilities.updateComponentTreeUI(popupMenu);
        return popupMenu;
    }
	
	private JPanel appDesc()
	{
		JPanel panel = new JPanel(new MigLayout("al center center", "[][]", "[]"));
		panel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000;" +
                "[dark]background: #00000000");
		
		JLabel img = new JLabel(new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/logo.svg"))
								.derive(100, 100));
		panel.add(img, "center, wrap");
		
		JLabel label1 = new JLabel("<html>" + "<center><b>Chatting app for all PC's</b></center>" + "<br/>"
				+ "No conversations selected" + "</html>");
		label1.setFont(ResourceHandler.getFont("Roboto-Bold.ttf", 18f));
        label1.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000;" + "[dark]foreground: #FFFFFF");
		panel.add(label1, "gapx 8, wrap, sg 1");
		return panel;
	}
	
	private void showChatUI(String sender, String receiver, Icon senderIcon, Icon receiverIcon)
	{
		this.remove(appDesc);
		
		if (!Objects.equals(ChatUI.userName, receiver))
		{
			if (chatComponent != null) remove(chatComponent);
			
			if (usersMap.containsKey(receiver))
            {
                add(usersMap.get(ChatUI.userName));
            }
			else
			{
				chatComponent = new ChatUI();
				
				ChatUI.userName = receiver;
				usersMap.put(ChatUI.userName, chatComponent);
				add(chatComponent.createChatUI(sender, receiver, senderIcon, receiverIcon), BorderLayout.CENTER);
				
				var temp = ChatUI.chatBoxLists;
				for (ChatBoxList chatBoxList : temp)
				{
					chatComponent.chatArea.addChatBox(chatBoxList.getModal(), chatBoxList.getType());
				}
				ChatUI.chatBoxLists.clear();
			}
			repaint();
			revalidate();
		}
	}
	
	private JScrollPane createScroll()
	{
		JScrollPane scroll = new JScrollPane();
		scroll.setBorder(null);
		scroll.setViewportBorder(null);
		return scroll;
	}
	
	private void addGuiComponents()
	{
		usersPanel = new JPanel();
		usersPanel.setLayout(new MigLayout("wrap, insets 10, gapy 4", "[290:310:320]", ""));
        usersPanel.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #FCFCFC; " +
                "[dark]foreground: #2C2C2C;" +
                "border: 1, 0, 0, 1, #515151");

        // chats heading
		JLabel userPanelHeading = new JLabel("Chats");
		userPanelHeading.setFont(ResourceHandler.getFont("Roboto-Black.ttf", 24f));
        userPanelHeading.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000; " +
                "[dark]foreground: #FFFFFF");
		usersPanel.add(userPanelHeading, "gapx 20, w 40, h 40, split");
		
		// setting icon
		JLabel setting = new JLabel(new FlatSVGIcon(new File(Application.jarFilePath + "res/icons/setting.svg"))
                                                            .derive(25, 25));
		setting.setCursor(new Cursor(Cursor.HAND_CURSOR));
		setting.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e)	{}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e)
            {
				if (Application.settingPanel == null)
					Application.settingPanel = new SettingPanel(Natter.this);
            }

			@Override
			public void mouseClicked(MouseEvent e)
			{
                Application.settingPanel.setLocationRelativeTo(Natter.this);
				Application.settingPanel.setVisible(true);
			}
		});
		
		usersPanel.add(setting, "gapx 5, split");

		RoundedButton roundedButton = new RoundedButton("+");
		roundedButton.addActionListener(_ -> {
            String username = JOptionPane.showInputDialog(Natter.this, "Enter the Username: ", "");
            if (username != null)
            {
                if (!username.isBlank() && !containsIgnoreCase(usersList, username))
                {
                    if (userExist(username))
                    {
                        usersList.add(username);
                        usersPanel.add(userComponentPanel(username), "pushx, growx, span1");
                        repaint();
                        revalidate();
                    }
                    else
                    {
                        Notifications.getInstance().setJFrame(this);
                        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.BOTTOM_RIGHT, "User doesn't exist!");
                    }
                }
            }
        });

		roundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		usersPanel.add(roundedButton, "gapx 130, w 20, h 20, wrap");
		usersPanel.add(new JSeparator(), "gapy 5");
		
		JScrollPane scrollBody = createScroll();
		scrollBody.setViewportView(usersPanel);
		scrollBody.setVerticalScrollBar(new CustomScrollBar());
		scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBody.getViewport().setOpaque(false);
		
		this.add(scrollBody, BorderLayout.WEST);
		
		appDesc = appDesc();
		this.add(appDesc, BorderLayout.CENTER);
		
		var temp = UserInfo.readExistingUsers();
        for (var users : temp)
		{
            usersPanel.add(initUserComponentPanel(users.name(), users.time()), "pushx, growx, span1");
            usersList.add(users.name().toLowerCase());
        }

        repaint();
	}

	private boolean userExist(String user)
	{
		try (Connection con = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
             PreparedStatement ps = con
						.prepareStatement("Select * from account_info where BINARY Username = ?"))
		{
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			
			return rs.next();
		}
		catch (Exception _) {}
		return false;
	}
	
	private ImageIcon createProfilePic(String user)
	{
		String targetDirectoryPath = Application.jarFilePath;
		File imagePath = loadImageWithoutExtension(targetDirectoryPath, "profile/" + user);
		
		if (imagePath == null)
		{
			try (Connection con = DriverManager.getConnection(DB.dbUrl, DB.username, DB.password);
				 PreparedStatement ps = con.prepareStatement("Select Profile_Picture, Image_extension from pfp where username = ?"))
			{
				ps.setString(1, user);
				ResultSet rs = ps.executeQuery();
				String fileExtension;

				if (rs.next())
				{
					byte[] imageData = rs.getBytes("Profile_Picture");
					if (imageData != null)
					{
						fileExtension = rs.getString("Image_extension");
						OutputStream outputStream = new FileOutputStream(targetDirectoryPath + "profile/" + user + fileExtension);
						outputStream.write(imageData);
						outputStream.close();
						
						imagePath = new File((targetDirectoryPath + "profile/" + user + fileExtension));
						
						if (imagePath.exists())
							return new ImageIcon(imagePath.getPath());
					}
				}
				
			}
			catch (Exception _) {}
			return new ImageIcon((Application.jarFilePath + "profile/null.png"));
		}
		
		else
			return new ImageIcon(imagePath.getPath());
	}
	
	private File loadImageWithoutExtension(String dir, String baseName)
	{
		try
		{
			String[] extensions = { "jpg", "jpeg", "png" };
			
			for (String ext : extensions)
			{
				File file;
				file = new File(dir + baseName + "." + ext);
				
				if (file.exists()) return file;
			}
		}
		catch (Exception _) {}
		
		return null;
	}
	
	public boolean containsIgnoreCase(ArrayList<String> list, String target)
	{
        if (list != null)
        {
            for (String s : list)
            {
                if (s.equalsIgnoreCase(target)) return true;
            }
        }
		return false;
	}
	
	private JPanel initUserComponentPanel(String receiver, String time)
	{
		JPanel chatItemPanel = new JPanel();
		chatItemPanel.setLayout(new MigLayout("", "[][]", "[]"));
        chatItemPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000; " +
                "[dark]background: #00000000");
		
		Icon profileImage = createProfilePic(receiver);
		ProfilePicture avatar = new ProfilePicture();
		avatar.setBorderSize(1);
		avatar.setBorderSpace(1);
		avatar.setImage(profileImage);
		chatItemPanel.add(avatar, "al left, w 60, h 60");
		
		// Chat details
		JPanel detailsPanel = new JPanel(new MigLayout("al left, wrap, gapy 10", "[][]", "[][]"));
		detailsPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000;" +
                "[dark]background: #00000000");
		
		JLabel nameLabel = new JLabel(receiver);
		nameLabel.setFont(ResourceHandler.getFont("ClearSans-Medium.ttf", 16f));
        nameLabel.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000;" +
                "[dark]foreground: #FFFFFF");
		detailsPanel.add(nameLabel, "pushx, growx, w 150!");
		
		JLabel timestampLabel = new JLabel(time);
		timestampLabel.setFont(ResourceHandler.getFont("ClearSans-Bold.ttf", 14f));
        timestampLabel.putClientProperty(FlatClientProperties.STYLE, "[light]foreground: #000000;" +
                "[dark]foreground: #FFFFFF");
		detailsPanel.add(timestampLabel);
		
		chatItemPanel.add(detailsPanel, "al center, w 230!");
		
		chatItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chatItemPanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent evt)
			{
				if (SwingUtilities.isLeftMouseButton(evt))
				{
					if (!ChatUI.userName.equals(receiver))
					{
						if (selectedPanel != null) {
                            selectedPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #00000000; " +
                                    "[dark]background: #00000000");
                        }
						
						selectedPanel = chatItemPanel;
                        selectedPanel.putClientProperty(FlatClientProperties.STYLE, "[light]background: #D2D2D2; " +
                                "[dark]background: #515151");

						if (usersMap.containsKey(receiver))
						{
							if (chatComponent != null) remove(chatComponent);

							chatComponent = usersMap.get(receiver);
							add(chatComponent);
							remove(appDesc);
							ChatUI.userName = receiver;
						}
						else
						{
							String userString = Application.userDetails.username();
							showChatUI(userString, receiver, createProfilePic(userString), profileImage);
						}
					}
				}
				
				if (SwingUtilities.isRightMouseButton(evt) && chatComponent != null)
				{
					if (ChatUI.userName.equals(receiver))
					{
                        JPopupMenu popupMenu = buildPopupMenu(receiver, chatItemPanel);
						popupMenu.show(chatItemPanel, evt.getX(), evt.getY());
					}
				}
				repaint();
				revalidate();
			}
		});
		
		return chatItemPanel;
	}

    public void updateTheme()
    {
        SwingUtilities.invokeLater(() -> {

            SwingUtilities.updateComponentTreeUI(this);

            for (ChatUI panel : usersMap.values()) {
                SwingUtilities.updateComponentTreeUI(panel);
                panel.repaint();
                panel.revalidate();
            }

            this.invalidate();
            this.validate();
            this.repaint();
        });
    }
	
	static class RoundedButton extends JButton
	{
		public RoundedButton(String text)
		{
			super(text);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setFont(ResourceHandler.getFont("ARIALBD_1.TTF", 24f));
            putClientProperty(FlatClientProperties.STYLE, "foreground: #FFFFFF;" + "background: #48B3CC;");
		}
		
		@Override
		protected void paintComponent(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setColor(getBackground());
			g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
			
			super.paintComponent(g);
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			return new Dimension(100, 100);
		}
	}
}