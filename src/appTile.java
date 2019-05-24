import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * This class is the implementation of each tile displayed on the screen. It holds an
 * Icon and the name of the App, and controls how the differece between an active and 
 * inactive title are displayed. It also implements the mouse listening events.
 * 
 * @author Vincent Trolia
 *
 */
@SuppressWarnings("serial")
public class appTile extends JPanel implements MouseListener {
	
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
	private boolean active;
	
	/* app shortcut folder location */
	public static final String appFolder = "\\Apps\\";
	
	
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
		this.active = false;
		
		// set the icon in the middle of the tile
		if (iconFile.isEmpty()) {
			iconFile = "resources\\no_icon.png";
		}
		else {
			iconFile = "icons\\" + iconFile;
		}
		try {
			icon = ImageIO.read(new File(iconFile));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		addMouseListener(this);
	}
	
	
	/**
	 * Draw the rectangle and image within
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		// draw active_color if the tile is currently active, if not,
		// draw the outline_color
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(4));
		if (active) {
			g2.setColor(ACTIVE_COLOR);
		}
		else {
			g2.setColor(OUTLINE_COLOR);
		}
		
		// draw the outer rectangle, the icon and the name
		g2.drawRect(this.x, this.y, width, height);
		g2.drawImage(icon, x + width / 2 - 24, y + height / 2 - 24, this);
		
		// draw spot where name will appear
		g2.setColor(new Color(0, 0, 0));
		g2.fillRoundRect(x + 1, y + 1, width - 2, height / 8, 10, 10);
		
		// if the tile is active, display its name to the user
		if (active) {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Rockwell", Font.PLAIN, width / 10));
			g2.drawString(name, 10, (int)(width / 8.5));
		}
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
	
	public boolean getActive() {
		return this.active;
	}
	
	/* public interfaces for when key presses and remote button entries are implemented */
	public void setActive() {
		this.active = true;
		repaint();
	}
	
	public void removeActive() {
		this.active = false;
		repaint();
	}
	
	
	/**
	 * When enter key is pressed or tile is clicked, it opens the associated media app
	 */
	public void openApp() {
		Runtime app = Runtime.getRuntime();
		
		// don't do anything if there is no app
		if (this.name.equals("Empty")) {
			return;
		}
		
		// use the windows command line to run the applications
		try {
			app.exec("cmd /c START " + System.getProperty("user.dir") + appFolder + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/* mouse methods */
	@Override
	public void mouseClicked(MouseEvent e) {
		openApp();
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	
	/**
	 * When the mouse hovers over the tile, it displays the name and changes the
	 * display color to the active color 
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// it is the active element
		active = true;
		repaint();
		
	}

	/**
	 * When the mouse leaves a tile, remove name and gray it back out
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// it is not the active element
		active = false;
		repaint();
		
	}
	
}
