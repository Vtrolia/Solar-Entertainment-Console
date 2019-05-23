import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class appTile extends JPanel {
	
	/* the colors used to surroung the tile */
	public static final Color OUTLINE_COLOR = Color.GRAY;
	public static final Color ACTIVE_COLOR = Color.CYAN;
	
	/* params for the size of the tile */
	private int x;
	private int y;
	private int width = 300;
	private int height = 300;
	
	/* icon to be drawn */
	private BufferedImage icon;
	
	/* name to be displayed when active */
	private String name;
	
	
	/**
	 * Constructor, bulilds the tile and adds a picture of the app's logo and
	 * displays the name of the application to the user.
	 * @param x: x position of tile
	 * @param y: y position of tile
	 * @param width: width
	 * @param height: height
	 * @param iconFile: the location of the application's icon that will be displayed across the console
	 * @param name: the name of the application to display to the user
	 */
	public appTile(int x, int y, int width, int height, String iconFile, String name) {
		// set the params
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		
		// set the icon in the middle of the tile
		if (iconFile.isEmpty()) {
			iconFile = "no_icon.png";
		}
		try {
			icon = ImageIO.read(new File(iconFile));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Draw the rectangle and image within
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		// set the outline to draw a nice looking, rounded gray rectangle border
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		g2.setColor(OUTLINE_COLOR);
		
		// draw the outer rectangle, the icon and the name
		g2.drawRoundRect(this.x, this.y, width, height, 10, 10);
		g2.drawImage(icon, x + width / 2 - 24, y + height / 2 - 24, this);
		
		// draw spot where name will appear
		g2.setColor(new Color(0, 0, 0));
		g2.fillRoundRect(x + 1, y + 1, width - 2, height / 8, 10, 10);
	}

	
	/* getters and setters */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
