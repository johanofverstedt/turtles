
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

public class World {
	private int width;
	private int height;

	private ArrayList<Turtle> turtles;

	private JFrame frame;
	private WorldCanvas canvas;

	public World() {
		this(400, 400);
	}

	public World(int width, int height) {
		this.width = width;
		this.height = height;

		this.turtles = new ArrayList<Turtle>(4);

		createWindow();
	}

	public void add(Turtle t) {
		this.turtles.add(t);
	}

	public void remove(Turtle t) {
		this.turtles.remove(t);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void repaint() {
		canvas.repaint();
	}

	public String toString() {
		String s = "{World(" + width + ", " + height + ")";
		if(this.turtles.isEmpty())
			return s + "}";
		s += turtles.get(0);
		for(int i = 0; i < turtles.size(); ++i) {
			s += ", " + turtles.get(i);
		}
		return s + "}";
	}

	void drawPath(Turtle t, int xOld, int yOld, int xNew, int yNew) {
		this.canvas.drawLine(xOld, yOld, xNew, yNew, t.getColor());
	}

	private void createWindow() {
		this.frame = new JFrame("World");

		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.canvas = new WorldCanvas(this.width, this.height, this.turtles);

		frame.add(this.canvas);
		frame.pack();

		frame.setVisible(true);

		frame.repaint();
	}
}
