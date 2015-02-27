
/**
 * Copyright (c) 2015, Johan Öfverstedt <johan.ofverstedt@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN
 * AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

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
		if(!t.isVisible())
			return;

		int xPos = t.getXPos();
		int yPos = t.getYPos();
		double dirRads = (Math.PI/180.0) * t.getDirection();

		int size = t.getSize();
		int halfSize = size / 2;
		
		int headSize = (int)(t.getSize() / 1.5);
		int halfHeadSize = headSize / 2;

		int legSize = (int)(t.getSize() / 2);
		int halfLegSize = legSize / 2;

		Color color = t.getColor();

		//Draw head
		int headXPos = xPos + (int)(Math.cos(dirRads) * 0.95 * (halfSize + halfHeadSize));
		int headYPos = yPos + (int)(Math.sin(dirRads) * 0.95 * (halfSize + halfHeadSize));
		g.setColor(color);
		g.fillOval(headXPos - halfHeadSize, headYPos - halfHeadSize, headSize, headSize);

		//Draw legs
		for(int i = 0; i < 4; ++i) {
			int legXPos = xPos + (int)(Math.cos(dirRads + 2.0 * Math.PI * ((i+1)/5.0)) * 0.95 * (halfSize + halfLegSize));
			int legYPos = yPos + (int)(Math.sin(dirRads + 2.0 * Math.PI * ((i+1)/5.0)) * 0.95 * (halfSize + halfLegSize));
			g.setColor(color);
			g.fillOval(legXPos - halfLegSize, legYPos - halfLegSize, legSize, legSize);
		}

		//Draw body
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
