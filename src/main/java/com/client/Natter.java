package com.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import raven.test.ChatUI;

public class Natter extends JFrame implements Theme
{
	private static final long serialVersionUID = 1L;
	private final String mode = toggle ? "dark_mode" : "light_mode";
	private JPanel usersPanel, appDesc;
	private Color transpentColor = new Color(0, 0, 0, 0);
	private static ChatUI chatComponent;
	
	public Natter()
	{
		super("Natter");
		
		this.setIconImage(ResourceHandler.loadImageIcon(ResourceHandler.getSettings(mode, "iconPath")).getImage());
		this.setLayout(new BorderLayout());
		this.setSize(1100, 850);
		this.setMinimumSize(new Dimension(1000, 800));
		this.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setLocationRelativeTo(null);
		this.setFocusable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		addGuiComponents();
	}
	
	private JPanel userComponentPanel(String user)
	{
		JPanel chatItemPanel = new JPanel();
		chatItemPanel.setLayout(new MigLayout("", "[][]", "[]"));
		chatItemPanel.setBackground(transpentColor);
		
		Icon profileImage = createProfilePic(user);
		ProfilePicture avatar = new ProfilePicture();
		avatar.setBorderSize(1);
		avatar.setBorderSpace(1);
		avatar.setImage(profileImage);
		chatItemPanel.add(avatar, "al left, w 60, h 60");
		
		// Chat details
		JPanel detailsPanel = new JPanel(new MigLayout("al left, wrap, gapy 10", "[][]", "[][]"));
		detailsPanel.setBackground(transpentColor);
		
		JLabel nameLabel = new JLabel(user);
		nameLabel.setFont(ResourceHandler.getFont("ClearSans-Medium.ttf", 16f));
		nameLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		detailsPanel.add(nameLabel, "pushx, growx, w 150!");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		JLabel timestampLabel = new JLabel(LocalDateTime.now().format(formatter));
		timestampLabel.setFont(ResourceHandler.getFont("ClearSans-Bold.ttf", 14f));
		timestampLabel.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		detailsPanel.add(timestampLabel);
		
		chatItemPanel.add(detailsPanel, "al center, w 230!");
		
		chatItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chatItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt)
			{
				showChatUI(user, createProfilePic(ResourceHandler.readPropertiesFile("username")), profileImage);
			}
		});
		
		return chatItemPanel;
	}
	
	private JPanel appDesc()
	{
		JPanel panel = new JPanel(new MigLayout("al center center", "[][]", "[]"));
		panel.setBackground(transpentColor);
		
		try
		{
			BufferedImage originalImage = ImageIO
					.read(new File(getClass().getResource(ResourceHandler.getSettings(mode, "iconPath")).getPath()));
			Image scaledDownImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			JLabel img = new JLabel(new ImageIcon(scaledDownImage));
			panel.add(img, "center, wrap");
		}
		catch (IOException e)
		{
			System.err.println("cannot find the logo");
		}
		
		JLabel label1 = new JLabel("<html>" + "<center><b>Chatting app for all PC's</b></center>" + "<br/>"
				+ "No conversations selected" + "</html>");
		label1.setFont(ResourceHandler.getFont("Roboto-Bold.ttf", 18f));
		label1.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		panel.add(label1, "gapx 8, wrap, sg 1");
		return panel;
	}
	
	private void showChatUI(String title, Icon senderIcon, Icon receiverIcon)
	{
		this.remove(appDesc);
		if (chatComponent != null)
			this.remove(chatComponent);
		
		repaint();
		chatComponent = new ChatUI();
		this.add(chatComponent.createChatUI(title, senderIcon, receiverIcon), BorderLayout.CENTER);
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
		usersPanel.setBackground(Color.decode(ResourceHandler.getSettings(mode, "userPanel")));
		usersPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.gray)); // Top and right border only
		
		// chats heading
		JLabel userPanelHeading = new JLabel("Chats");
		userPanelHeading.setFont(ResourceHandler.getFont("Roboto-Black.ttf", 24f));
		userPanelHeading.setForeground(Color.decode(ResourceHandler.getSettings(mode, "fontColor")));
		usersPanel.add(userPanelHeading, "gapx 20, w 40, h 40, split");
		
		// setting icon
		JLabel setting = new JLabel(new ImageIcon(
				ResourceHandler.loadImageIcon(ResourceHandler.getSettings(mode, "settingIcon")).getImage()));
		setting.setCursor(new Cursor(Cursor.HAND_CURSOR));
		setting.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (NatterMain.settingPanel == null)
					NatterMain.settingPanel = new SettingPanel();
				
				NatterMain.settingPanel.setVisible(true);
			}
		});
		
		usersPanel.add(setting, "gapx 5, split");
		
		// Create and add + button
		RoundedButton roundedButton = new RoundedButton("+");
		roundedButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String username = JOptionPane.showInputDialog(Natter.this, "Enter the Username: ", "");
				if (!username.isBlank() && username != null)
				{
					if (userExist(username))
					{
						usersPanel.add(userComponentPanel(username), "pushx, growx, span1");
						repaint();
						revalidate();
					}
					else
					{
						JOptionPane.showMessageDialog(Natter.this, "User doesn't exist");
					}
				}
			}
		});
		
		roundedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		usersPanel.add(roundedButton, "gapx 130, w 20, h 20, wrap");
		
		JScrollPane scrollBody = createScroll();
		scrollBody.setViewportView(usersPanel);
		scrollBody.setVerticalScrollBar(new CustomScrollBar());
		scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBody.getViewport().setOpaque(false);
		
		this.add(scrollBody, BorderLayout.WEST);
		
		appDesc = appDesc();
		this.add(appDesc, BorderLayout.CENTER);
	}
	
	private boolean userExist(String user)
	{
		try (Connection con = DriverManager.getConnection(DB.dbUrl, DB.username,
				DB.password); PreparedStatement ps = con
						.prepareStatement("Select * from account_info where username = ?"))
		{
			ps.setString(1, user);
			ResultSet rs = ps.executeQuery();
			
			return rs.next();
		}
		catch (Exception e)
		{}
		return false;
	}
	
	private ImageIcon createProfilePic(String user)
	{
		String targetDirectoryPath = getClass().getResource("Natter.class").getPath();
		targetDirectoryPath = targetDirectoryPath.substring(0, targetDirectoryPath.lastIndexOf("/") + 1);
		
		File imagePath = loadImageWithoutExtension(targetDirectoryPath, "profile/" + user);
		
		if (imagePath == null)
		{
			try (Connection con = DriverManager.getConnection(DB.dbUrl, DB.username,
					DB.password); PreparedStatement ps = con
							.prepareStatement("Select Profile_Picture, Image_extension from pfp where username = ?"))
			{
				ps.setString(1, user);
				ResultSet rs = ps.executeQuery();
				String fileExtension = null;
				
				if (rs.next())
				{
					byte[] imageData = rs.getBytes("Profile_Picture");
					if (imageData != null)
					{
						fileExtension = rs.getString("Image_extension");
						OutputStream outputStream = new FileOutputStream(
								targetDirectoryPath + "profile/" + user + fileExtension);
						outputStream.write(imageData);
						outputStream.close();
						
						imagePath = new File(getClass().getResource("profile/" + user + fileExtension).getPath());
						
						if (imagePath.exists())
							return new ImageIcon(imagePath.getPath());
					}
				}
				
			}
			catch (Exception e)
			{}
			return new ImageIcon(getClass().getResource("profile/null.png"));
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
				File file = null;
				file = new File(dir + baseName + "." + ext);
				
				if (file.exists())
					return file;
			}
		}
		catch (Exception e)
		{}
		
		return null;
	}
	
	class RoundedButton extends JButton
	{
		private static final long serialVersionUID = 1L;
		
		public RoundedButton(String text)
		{
			super(text);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setForeground(Color.decode("#FFFFFF"));
			setFont(ResourceHandler.getFont("ARIALBD_1.TTF", 24f));
			setBackground(new Color(0, 200, 250));
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