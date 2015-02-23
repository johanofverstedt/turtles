
import java.awt.*;
import javax.swing.*;

public class Turtle {
	private int x;
	private int y;
	private int direction;

	private int size;
	private Color color;

	private boolean drawPathFlag;

	private World world;

	public Turtle(World w) {
		this(w, w.getWidth() / 2, w.getHeight() / 2);
	}

	public Turtle(World w, int x, int y) {
		this.world = w;
		this.x = x;
		this.y = y;

		this.size = 20;
		this.color = new Color((int)(Math.random() * 256),
			(int)(Math.random() * 256),
			(int)(Math.random() * 256));
		//new Color(0, 178, 0);

		this.drawPathFlag = true;

		w.add(this);
	}

	public World getWorld() {
		return this.world;
	}

	public int getXPos() {
		return this.x;
	}

	public int getYPos() {
		return this.y;
	}

	public int getSize() {
		return this.size;
	}

	public Color getColor() {
		return this.color;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setColor(int red, int green, int blue) {
		this.color = new Color(red, green, blue);
	}

	public void enablePath() {
		this.drawPathFlag = true;
	}

	public void disablePath() {
		this.drawPathFlag = false;
	}

	public void turn(int angle) {
		this.direction += angle;

		updateWorld();
	}

	public void move(int step) {
		double dirRads = Math.PI * (this.direction / 180.0);
		int xStep = (int)Math.round(Math.cos(dirRads) * step);
		int yStep = (int)Math.round(Math.sin(dirRads) * step);

		moveTo(x + xStep, y + yStep);
	}

	public void moveTo(int x, int y) {
		int xOld = this.x;
		int yOld = this.y;
		int xNew = x;
		int yNew = y;

		if(xNew < 0)
			xNew = 0;
		if(yNew < 0)
			yNew = 0;
		if(xNew >= this.world.getWidth())
			xNew = this.world.getWidth() - 1;
		if(yNew >= this.world.getHeight())
			yNew = this.world.getHeight() - 1;

		if(this.drawPathFlag)
			this.world.drawPath(this, xOld, yOld, xNew, yNew);

		this.x = xNew;
		this.y = yNew;

		updateWorld();
	}

	public void turnTo(int x, int y) {
		if(this.x == x && this.y == y)
			return;

		double dirRads = Math.atan2(y - this.y, x - this.x);
		this.direction = (int)Math.round((180.0 / Math.PI) * dirRads);

		updateWorld();
	}

	private void updateWorld() {
		this.world.repaint();		
	}
}
