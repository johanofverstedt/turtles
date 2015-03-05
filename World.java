
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

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;

/**
 *  World is a class representing an environment for Turtles.
 *
 *  The world is visualized as a window, and its size can be
 *  selected upon construction.
 *
 *  As the turtles move around, they are redrawn and they leave
 *  tracks on the background in the form of their linear path.
 */
public class World {
  //
  //  World attributes
  //
  
  private int width;
  private int height;
  
  private ArrayList<Turtle> turtles;
  
  private JFrame frame;
  private WorldCanvas canvas;
  
  private static int worldCount = 0;
  
  /**
   *  Constructs a world with default size of 400 by 400 pixels.
   */
  public World() {
    this(400, 400);
  }
  
  /**
   *  Constructs a world with a specified width and height.
   *
   *  @param width The width of the world in pixels.
   *  @param height The height of the world in pixels.
   */
  public World(int width, int height) {
    if(width < 1)
      throw new RuntimeException("Invalid world width.");
    if(height < 1)
      throw new RuntimeException("Invalid world height.");

    this.width = width;
    this.height = height;
    
    this.turtles = new ArrayList<Turtle>(4);
    
    createWindow();
  }

  /**
   *  Removes a turtle from the world.
   *
   *  @param t A reference to the turtle to remove.
   */
  public void remove(Turtle t) {
    if(this.turtles.remove(t))
      this.repaint();
  }
  
  /**
   *  Returns the width of the world in pixels.
   *
   *  @return The width of the world in pixels.
   */
  public int getWidth() {
    return this.width;
  }
  
  /**
   *  Returns the height of the world in pixels.
   *
   *  @return The height of the world in pixels.
   */
  public int getHeight() {
    return this.height;
  }
  
  /**
   *  Forces a repaint of the world.
   */
  public void repaint() {
    this.canvas.repaint();
  }
  
  /**
   *  Generates a string representation of the world
   *  and all the turtles in it.
   */
  public String toString() {
    String s = "{World(" + this.width + ", " + this.height + ")";
    if(this.turtles.isEmpty())
      return s + "}";
    s += this.turtles.get(0);
    for(int i = 0; i < this.turtles.size(); ++i) {
      s += ", " + this.turtles.get(i);
    }
    return s + "}";
  }
  
  //
  //  Implementation details below here...
  //  Not intended for the faint of heart!
  //

  /**
   *  Package local method which adds
   *  a turtle to this world.
   *
   *  @param t Turtle to be added to the world.
   */
  void add(Turtle t) {
    if(t == null)
      throw new RuntimeException("Can't add a null turtle reference to the world.");

    //Only add the turtle if it doesn't already exist
    //in the world to bulletproof the class against
    //surprising behavior.
    if(!this.turtles.contains(t))
      this.turtles.add(t);
  }

  /**
   *  Package local method which draws a line from
   *  an old position to the new for a given turtle.
   */
  void drawPath(Turtle t, int xOld, int yOld, int xNew, int yNew) {
    this.canvas.drawLine(xOld, yOld, xNew, yNew, t.getColor());
  }
  
  /**
   *  Creates the world window and sets up the canvas
   *  which is used to draw the world and turtles.
   */
  private void createWindow() {
    String worldTitle = "World";
    if(++worldCount > 1)
      worldTitle += (" " + worldCount);
    
    this.frame = new JFrame(worldTitle);
    
    this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.frame.setResizable(false);
    this.frame.setLocation(10, 10);

    this.canvas = new WorldCanvas(this.width, this.height, this.turtles);
    
    this.frame.add(this.canvas);
    this.frame.pack();
    this.frame.setVisible(true);

    this.frame.repaint();
  }
  
  /**
   *  Private class which implements the drawing
   *  facilities of the World class and maintains
   *  the current persistent turtle tracks.
   */
  private class WorldCanvas extends JPanel {
    private BufferedImage img;
    private Color bgrColor;
    private ArrayList<Turtle> turtles;
    
    WorldCanvas(int width, int height, ArrayList<Turtle> turtles) {
      this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      this.bgrColor = new Color(255, 255, 255);
      this.turtles = turtles;
      
      this.setPreferredSize(new Dimension(width, height));
      
      clear();
    }
    
    //
    //  Persistent drawing methods
    //
    
    public void clear() {
      Graphics2D g = this.img.createGraphics();
      
      g.setColor(this.bgrColor);
      g.fillRect(0, 0, this.img.getWidth(), this.img.getHeight());
      
      g.dispose();
    }
    
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
      Graphics2D g = this.img.createGraphics();
      
      //Enable anti-aliasing to make the lines look pretty
      Object previousAntiAliasHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      g.setColor(color);
      g.drawLine(x1, y1, x2, y2);
      
      //Restore previous anti-aliasing mode
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousAntiAliasHint);
      
      g.dispose();
    }
    
    //
    //  Methods for redrawing to the screen
    //
    
    private double circularXOffset(double angle, double radius) {
      return Math.cos(angle) * radius;
    }
    private double circularYOffset(double angle, double radius) {
      return Math.sin(angle) * radius;
    }
    
    private void fillCenteredCircle(Graphics2D g, double x, double y, double radius) {
      double diameter = 2.0 * radius;
      g.fill(new Ellipse2D.Double(x-radius, y-radius, diameter, diameter));
      //g.fillOval(x - radius, y - radius, diameter, diameter);
    }
    
    private void paintTurtle(Graphics g, Turtle t) {
      final double RADIUS = 8.0 * t.getSize();
      final double HEAD_RADIUS = (5.0/8.0) * RADIUS;
      final double LEG_RADIUS = (3.0/8.0) * RADIUS;
      
      if(!t.isVisible())
        return;

      Graphics2D g2 = (Graphics2D)g;
      
      int xPos = t.getXPos();
      int yPos = t.getYPos();
      double dirRads = (Math.PI/180.0) * t.getDirection();
      
      Color color = t.getColor();
      Color limbColor = t.getLimbColor();
      
      g.setColor(limbColor);
      
      //Draw head
      double headXPos = xPos + circularXOffset(dirRads, RADIUS+(2.0/8.0) * RADIUS);
      double headYPos = yPos + circularYOffset(dirRads, RADIUS+(2.0/8.0) * RADIUS);
      fillCenteredCircle(g2, headXPos, headYPos, HEAD_RADIUS);
      
      //Draw legs
      for(int i = 0; i < 4; ++i) {
        double legAngle = dirRads + 2.0 * Math.PI * ((i+1)/5.0);
        double legXPos = xPos + circularXOffset(legAngle, RADIUS+(1.0/8.0) * RADIUS);
        double legYPos = yPos + circularYOffset(legAngle, RADIUS+(1.0/8.0) * RADIUS);
        fillCenteredCircle(g2, legXPos, legYPos, LEG_RADIUS);
      }
      
      //Draw body
      g.setColor(color);
      fillCenteredCircle(g2, xPos, yPos, RADIUS);
    }
    
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      //Need to manually cast the Graphics reference to a Graphics2D reference
      //to make use of better drawing facilities
      Graphics2D g2 = (Graphics2D)g;
      
      //Draw back-buffer
      g2.drawRenderedImage(this.img, null);
      
      //Enable anti-aliasing to make the turtles look pretty
      Object previousAntiAliasHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      for(Turtle t : this.turtles) {
        paintTurtle(g, t);
      }
      
      //Restore previous anti-aliasing mode
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousAntiAliasHint);
    }
  }
}
