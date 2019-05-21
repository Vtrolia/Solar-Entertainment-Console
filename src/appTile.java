import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

@SuppressWarnings("serial")
public class appTile extends JComponent {
	
	/* the colors used to surroung the tile */
	public static final Color OUTLINE_COLOR = Color.GRAY;
	public static final Color ACTIVE_COLOR = Color.CYAN;
	
	/* params for the size of the tile */
	private int x;
	private int y;
	
	
	/**
	 * constructor for when default values should be used
	 * @param x: x position
	 * @param y: y position
	 */
	public appTile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(OUTLINE_COLOR);
		g2.drawRoundRect(this.x, this.y, 240, 240, 10, 10);
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
	
	public static void main(String[]args) {
		JFrame fr = new JFrame();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setSize(800, 800);
		fr.getContentPane().setBackground(Color.BLACK);
		fr.getContentPane().add(new appTile(10, 10));
		fr.setVisible(true);
	}
	
	
	
}
