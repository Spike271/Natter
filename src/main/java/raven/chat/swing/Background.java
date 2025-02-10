package raven.chat.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import raven.color.theme.Theme;
import raven.resource.swing.GetAndSetColor;

public class Background extends JPanel implements Theme
{
	private static final long serialVersionUID = 1L;
	private BufferedImage backgroundImage;
	String mode = Theme.isDarkModeOn ? "dark_mode" : "light_mode";
	
	public Background(File file)
	{
		try
		{
			backgroundImage = ImageIO.read(file);
			
			backgroundImage = applyGaussianBlur(backgroundImage, 4);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Background()
	{
		setOpaque(false);
	}
	
	private BufferedImage applyGaussianBlur(BufferedImage src, float sigma)
	{
		int paddedWidth = src.getWidth() + 2;
		int paddedHeight = src.getHeight() + 2;
		
		// Create a new image with padding
		BufferedImage paddedImage = new BufferedImage(paddedWidth, paddedHeight, src.getType());
		
		// Draw the original image onto the padded image
		Graphics2D g2d = paddedImage.createGraphics();
		g2d.drawImage(src, 1, 1, null); // Offset the original image by 1px
		g2d.dispose();
		
		// Create a Gaussian kernel
		int size = (int) Math.ceil(sigma * 3) * 2 + 1; // Kernel size based on sigma
		float[] kernel = new float[size * size];
		float sum = 0f;
		
		int halfSize = size / 2;
		for (int x = -halfSize; x <= halfSize; x++)
		{
			for (int y = -halfSize; y <= halfSize; y++)
			{
				float value = (1f / (2f * (float) Math.PI * sigma * sigma))
						* (float) Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
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
			
			g2.setPaint(new GradientPaint(0, 0, Color.decode(GetAndSetColor.getSettings(mode, "Color1")), width, 0,
					Color.decode(GetAndSetColor.getSettings(mode, "Color2"))));
			
			g2.fillRect(0, 0, width, height);
			g2.dispose();
			super.paintComponent(g);
		}
	}
}
