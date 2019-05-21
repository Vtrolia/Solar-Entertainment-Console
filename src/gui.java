import java.awt.*;
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

@SuppressWarnings("serial")
public class gui extends JFrame {
	
	/* for multiple methods to get the screen size */
	private Dimension screenSize;
	
	/* used to paint and change what's drawn on screen */
	public JPanel innerPanel;
	
	
	/**
	 * Constructor, creates a full-screen JFrame and the panel, and adds the background art.
	 */
	public gui() {
		// create window and set it to the full size of the screen
		super("Solar Entertainment Console");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		getContentPane().setPreferredSize(screenSize);
		
		// makes sure it takes up the entire screen with no title bar
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setUndecorated(true);
	    
		pack();
		setResizable(false);
		
		// border layout, set the inner panel to the same size as the frame
		innerPanel = new JPanel();
		innerPanel.setSize(screenSize);
		innerPanel.setLayout(new BorderLayout());
		add(innerPanel);
		
		// draw the intro then reset the panel
		drawIntro();
		innerPanel.removeAll();
		
		/* The first thing that should be drawn on the screen after the splash screen will be the background
		 * image. This will be drawn using a JLabel applied directly on the frame and then
		 * adding the content panel afterwards.
		 */
		JLabel background = new JLabel();
		background.setLocation(0, 0);
		drawBackground(background, "ec_bg.png");
		innerPanel.add(background);
		innerPanel.paintAll(getGraphics());
	}
	
	
	/**
	 * Draws and plays the sound associated with the introductory splash screen.
	 */
	public void drawIntro() {
		// add the logo
		ImageIcon logo = new ImageIcon(this.getClass().getResource("logo.gif"));
		JLabel icon = new JLabel(logo);
		innerPanel.add(icon);
		setVisible(true);
		
		// use audio stream to play intro song
		AudioInputStream bloodyStream = null;
		try {
			// use DataLine interface to know when audio track ends
			bloodyStream = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream("intro.wav"));
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
	
	public static void main(String[] args) {
		gui GUI = new gui();
		
	}
}
