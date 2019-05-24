import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * This class implements the main Graphical User Interface of the Solar Entertainment Console
 * It takes in shortcuts and icon file paths to generate tiles to display on the screen.
 * It loads the splash animation screen and holds all of the information about the tiles.
 * Also, this class handles the keyboard listening for the active tile
 * @author Vincent Trolia
 *
 */
@SuppressWarnings("serial")
public class gui extends JFrame implements KeyListener {
	
	/* for multiple methods to get the screen size */
	private Dimension screenSize;
	
	/* used to paint and change what's drawn on screen */
	public JLayeredPane innerPanel;
	public JLabel background;
	
	private appTile[][] apps;

	
	/**
	 * Constructor, creates a full-screen JFrame and the panel, and adds the background art.
	 */
	public gui(int numOfApps, String[] names, String[] iconFiles) {
		// create window and set it to the full size of the screen
		super("Solar Entertainment Console");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getContentPane().setPreferredSize(screenSize);
		try {
			this.setIconImage(ImageIO.read(new File("resources\\logo.png")));
		}
		catch(Exception e) {}
		
		// makes sure it takes up the entire screen with no title bar
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setUndecorated(true);
	    addKeyListener(this);
	    
		pack();
		setResizable(false);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		innerPanel = new JLayeredPane();
		contentPane.add(innerPanel, BorderLayout.CENTER);
		
		// draw the intro then reset the panel
		drawIntro();
		innerPanel.removeAll();
		
		/* The first thing that should be drawn on the screen after the splash screen will be the background
		 * image. This will be drawn using a JLabel applied directly on the frame and then
		 * adding the content panel afterwards.
		 */
		background = new JLabel();
		background.setBounds(0, 0, screenSize.width, screenSize.height);
		drawBackground(background, "resources\\ec_bg.png");
		innerPanel.add(background);
		
		// create appTile array and draw them on the screen
		apps = new appTile[numOfApps][4];
		createApps(names, iconFiles);
		drawApps();
	}
	
	
	/**
	 * Draws and plays the sound associated with the introductory splash screen.
	 */
	public void drawIntro() {
		// add the logo
		ImageIcon logo = new ImageIcon(this.getClass().getResource("logo.gif"));
		JLabel icon = new JLabel(logo);
		icon.setBounds(0, screenSize.height / 2, screenSize.width, screenSize.height / 2);
		innerPanel.add(icon);
		setVisible(true);
		
		// use audio stream to play intro song
		AudioInputStream bloodyStream = null;
		try {
			// use DataLine interface to know when audio track ends
			bloodyStream = AudioSystem.getAudioInputStream(new File("resources\\intro.wav"));
			AudioFormat format = bloodyStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			SourceDataLine bloodyStone = (SourceDataLine) AudioSystem.getLine(info);
			bloodyStone.open(format);
			bloodyStone.start();
			
			byte[] bloodyStorm = new byte[4096];
			int read = -1;
			while((read = bloodyStream.read(bloodyStorm)) != -1)
				bloodyStone.write(bloodyStorm, 0, read);
			
			bloodyStone.drain();
			bloodyStone.close();
			bloodyStream.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	/**
	 * For the GUI and it's background to be properly scaled for different display types,
	 * the image used for the background must be correctly scaled. This method will take the
	 * base image and scale it to the full screen's size
	 * @param b: the pointer to the BufferedImage that hold the original data of the bg image
	 * @return result: the correctly scaled version of the bg image
	 */
	private BufferedImage scaleBackgroundImage(BufferedImage b) {
		// create the place holders for the raw image and the eventual result
		Image resized = b.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
		BufferedImage result = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
		
		// use Graphics2D API to now draw the scaled image into result
		Graphics2D drawing = result.createGraphics();
		drawing.drawImage(resized, 0, 0, null);
		drawing.dispose();
		
		return result;
	}
	
	
	/**
	 * This method encapsulates the setting of the image within the JLabel containing the background
	 * image. If the image has been moved or deleted, then the image is not set 
	 * @param container: the JLabel that will hold the background image.
	 */
	private void drawBackground(JLabel container, String name) {
		
		BufferedImage raw = null;
		
		// if the file is not found, the background will remain un-set
		try {
			raw = ImageIO.read(new File(name));
		} 
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// scale the image to the screen then attach it to the JLabel
		raw = scaleBackgroundImage(raw);
		ImageIcon bgEC = new ImageIcon(raw);
		container.setIcon(bgEC);
		return;
	}
	
	
	/**
	 * Creates and sets positions for all of the tiles for apps to be displayed on screen. Ads a gap
	 * between each tile and each row of tiles and stores them in the apps variable.
	 * 
	 * * names and iconPaths must be of equal lengths, pass in empty string for apps with no icon.**
	 * 
	 * @param names: the string of names to be associated with the app.
	 * @param iconPaths: the path to the icon file to be displayed in the tile. 
	 */
	public void createApps(String[] names, String[] iconPaths) {
		// throw an exception if both arguments are the same length
		if (names.length != iconPaths.length) {
			throw new IllegalArgumentException("There must be as many iconPaths as names!(can pass blank String)");
		}
		
		// keep track of where to draw and where to insert blank tiles
		int nameNum = 0;
		int x = 0;
		
		// keep track of vertical height and the gaps between tiles
		int widthGap = (int) (screenSize.getWidth() / 10) / 2;
		int heightGap = (int) (screenSize.getHeight() / 10) / 2;
		int y = heightGap * 2;
		
		// calculate width and height the scaled tiles should be
		final int width = (int) ((screenSize.getWidth() / 4.5)) - widthGap;
		final int height = (int) ((screenSize.getHeight() / 2.5)) - heightGap;
		
		// for each row
		for (int i = 0; i < apps.length; i++)
		{
			x = widthGap;
			for (int j = 0; j < apps[i].length; j++) {
				// display a blank tile if there aren't enough per row
				if (nameNum < names.length) {
					apps[i][j] = new appTile(2, 2, width, height, iconPaths[nameNum], names[nameNum]);
					apps[i][j].setBounds(x, y, width + 4, height + 4);
				}
				else {
					apps[i][j] = new appTile(2, 2, width, height, "", "Empty");
					apps[i][j].setBounds(x, y, width + 4, height + 4);
				}
				// move each tile according to the gaps needed
				x += width + widthGap;
				nameNum++;
			}
			y += height + heightGap;
		}
		
		apps[0][0].setActive();
		
	}
	
	
	/**
	 * Draws each appTile on the main screen
	 */
	public void drawApps() {
		for (int i = 0; i < apps.length; i++) {
			for (int j = 0; j < apps[i].length; j++) {
				innerPanel.add(apps[i][j]);
			}
		}
	}
	
	
	/**
	 * When an arrow key is pressed and the active tile needs to be moved, The active
	 * tile needs to be changed. This function finds the active tile and creates
	 * a new Dimension where the width is the row, height is the tile. 
	 * @return: a Dimension with coordinates to the active tile in the apps array
	 */
	public Dimension getActiveTile() {
		for (int i = 0; i < apps.length; i++) {
			for (int j = 0; j < apps[i].length; j++) {
				if (apps[i][j].getActive())
				{
					return new Dimension(i, j);
				}
			}
		}
		return new Dimension(0, 0);
	}

	
	/* unused keyboard events */
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

	
	/**
	 * When an arrow key is pressed, moves active tile in the direction
	 * of the arrow pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		Dimension active = getActiveTile();
		switch(keyCode) {
		case KeyEvent.VK_UP:
			apps[active.width][active.height].removeActive();
			apps[Math.abs(active.width -1) % apps.length][active.height].setActive();
			break;
		case KeyEvent.VK_DOWN:
			apps[active.width][active.height].removeActive();
			apps[(active.width + 1) % apps.length][active.height].setActive();
			break;
		case KeyEvent.VK_LEFT:
			apps[active.width][active.height].removeActive();
			apps[active.width][Math.abs(active.height - 1) % 4].setActive();
			break;
		case KeyEvent.VK_RIGHT:
			apps[active.width][active.height].removeActive();
			apps[active.width][(active.height + 1) % 4].setActive();
			break;
		case KeyEvent.VK_ENTER:
			apps[active.width][active.height].openApp();
		}
	}


	
}
