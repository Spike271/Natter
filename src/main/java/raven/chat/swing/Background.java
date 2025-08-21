package raven.chat.swing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.client.Application;
import global.Theme;

public class Background extends JPanel
{
	private BufferedImage backgroundImage;
	
	public Background(File file)
	{
        switchTheme(Application.currentTheme);

		try
		{
			backgroundImage = ImageIO.read(file);
			backgroundImage = applyGaussianBlur(backgroundImage);
		}
		catch (IOException _) {}
	}

	public Background()
	{
        switchTheme(Application.currentTheme);
		setOpaque(false);
	}
	
	private BufferedImage applyGaussianBlur(BufferedImage src)
	{
		int paddedWidth = src.getWidth() + 2;
		int paddedHeight = src.getHeight() + 2;
		
		// Create a new image with padding
		BufferedImage paddedImage = new BufferedImage(paddedWidth, paddedHeight, src.getType());
		
		// Draw the original image onto the padded image
		Graphics2D g2d = paddedImage.createGraphics();
		g2d.drawImage(src, 1, 1, null); // Offset the original image by 1 px
		g2d.dispose();
		
		// Create a Gaussian kernel
		int size = (int) Math.ceil((float) 4 * 3) * 2 + 1; // Kernel size based on sigma
		float[] kernel = new float[size * size];
		float sum = 0f;
		
		int halfSize = size / 2;
		for (int x = -halfSize; x <= halfSize; x++)
		{
			for (int y = -halfSize; y <= halfSize; y++)
			{
				float value = (1f / (2f * (float) Math.PI * (float) 4 * (float) 4))
						* (float) Math.exp(-(x * x + y * y) / (2 * (float) 4 * (float) 4));
				kernel[(x + halfSize) + (y + halfSize) * size] = value;
				sum += value; // normalize
			}
		}
		
		// Normalize the kernel
		for (int i = 0; i < kernel.length; i++)
		{
			kernel[i] /= sum;
		}
		
		// Apply the kernel to the padded image
		ConvolveOp convolveOp = new ConvolveOp(new Kernel(size, size, kernel));
		BufferedImage blurredImage = convolveOp.filter(paddedImage, null);
		
		// Return the cropped blurred image, removing the padding
		return blurredImage.getSubimage(1, 1, src.getWidth(), src.getHeight());
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// Draw the image
		if (backgroundImage != null)
		{
			// Get the dimensions of the panel
			int panelWidth = getWidth();
			int panelHeight = getHeight();
			
			// Get the dimensions of the image
			int imageWidth = backgroundImage.getWidth(this);
			int imageHeight = backgroundImage.getHeight(this);
			
			// Calculate the aspect ratios
			double panelAspect = (double) panelWidth / panelHeight;
			double imageAspect = (double) imageWidth / imageHeight;
			
			int drawWidth;
			int drawHeight;
			
			// Determine the appropriate width and height to maintain the aspect ratio
			if (panelAspect > imageAspect)
			{
				// Panel is wider than the image aspect ratio
				drawWidth = panelWidth;
				drawHeight = (int) (panelWidth / imageAspect); // Calculate height based on width
			}
			else
			{
				// Panel is taller than the image aspect ratio
				drawHeight = panelHeight;
				drawWidth = (int) (panelHeight * imageAspect); // Calculate width based on height
			}
			
			// Calculate the x and y position to center the image
			int x = (panelWidth - drawWidth) / 2;
			int y = (panelHeight - drawHeight) / 2;
			
			// Draw the image
			g.drawImage(backgroundImage, x, y, drawWidth, drawHeight, this);
		}
		else
		{
			Graphics2D g2 = (Graphics2D) g.create();
			int width = getWidth();
			int height = getHeight();

			g2.setPaint(new GradientPaint(0, 0, UIManager.getColor("Component.colorOne"), width, 0, UIManager.getColor("Component.colorTwo")));

			g2.fillRect(0, 0, width, height);
			g2.dispose();
		}
	}

    interface CustomLightColorScheme1 {
        Color PLACE_HOLDER_TEXT_COLOR = Color.BLACK;
        Color COLOR_1 = Color.decode("#74B4E0");
        Color COLOR_2 = Color.decode("#74B4E0");
    }

    interface CustomDarkColorScheme1 {
        Color PLACE_HOLDER_TEXT_COLOR = Color.WHITE;
        Color COLOR_1 = Color.decode("#18191D");
        Color COLOR_2 = Color.decode("#18191D");
    }

    private static void applyColorScheme(Class<?> colorSchemeClass)
    {
        try {
            UIManager.put("Component.placeHolderTextColor", colorSchemeClass.getField("PLACE_HOLDER_TEXT_COLOR").get(null));
            UIManager.put("Component.colorOne", colorSchemeClass.getField("COLOR_1").get(null));
            UIManager.put("Component.colorTwo", colorSchemeClass.getField("COLOR_2").get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void switchTheme(Theme Currenttheme)
    {
        if (Currenttheme == Theme.LIGHT_MODE)
            applyColorScheme(CustomLightColorScheme1.class);
        else
            applyColorScheme(CustomDarkColorScheme1.class);
    }
}
