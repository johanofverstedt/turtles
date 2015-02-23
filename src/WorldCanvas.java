
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class WorldCanvas extends JPanel {
	private BufferedImage img;
	private Color bgrColor;
	private ArrayList<Turtle> turtles;

	WorldCanvas(int width, int height, ArrayList<Turtle> turtles) {
		this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.bgrColor = new Color(255, 255, 255);
		this.turtles = turtles;
		
		this.setPreferredSize(new Dimension(width, height));

		clear();
		drawLine(0, 0, width, height, new Color(128, 128, 128));
		drawLine(0, height, width, 0, new Color(255, 0, 0));
	}

	//
	//  Persistent drawing methods
	//

	public void clear() {
		Graphics2D g = img.createGraphics();
		g.setColor(this.bgrColor);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.dispose();
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color color) {
		Graphics2D g = img.createGraphics();
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
		g.dispose();
	}

	//
	//  Methods for redrawing to the screen
	//

	private void paintTurtle(Graphics g, Turtle t) {
		int size = t.getSize();
		int halfSize = size / 2;
		Color color = t.getColor();

		g.setColor(color);
		g.fillOval(t.getXPos() - halfSize, t.getYPos() - halfSize, size, size);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
    	g2.drawRenderedImage(this.img, null);

    	for(Turtle t : this.turtles) {
    		paintTurtle(g, t);
    	}
	}
}
