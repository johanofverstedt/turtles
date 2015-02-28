
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

public class World {
  private int width;
  private int height;
  
  private ArrayList<Turtle> turtles;
  
  private JFrame frame;
  private WorldCanvas canvas;
  
  private static int worldCount = 0;
  
  /**
   *  Constructs a world with default size 400 by 400.
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
    this.turtles.remove(t);
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
    String s = "{World(" + width + ", " + height + ")";
    if(this.turtles.isEmpty())
      return s + "}";
    s += turtles.get(0);
    for(int i = 0; i < turtles.size(); ++i) {
      s += ", " + turtles.get(i);
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
    
    this.canvas = new WorldCanvas(this.width, this.height, this.turtles);
    
    frame.add(this.canvas);
    frame.pack();
    frame.setVisible(true);
    
    frame.repaint();
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
      Graphics2D g = img.createGraphics();
      
      g.setColor(this.bgrColor);
      g.fillRect(0, 0, img.getWidth(), img.getHeight());
      
      g.dispose();
    }
    
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
      Graphics2D g = img.createGraphics();
      
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
    
    private int circularXOffset(double angle, double radius) {
      return (int)Math.round(Math.cos(angle) * radius);
    }
    private int circularYOffset(double angle, double radius) {
      return (int)Math.round(Math.sin(angle) * radius);
    }
    
    private void fillCenteredCircle(Graphics g, int x, int y, int size) {
      int halfSize = size / 2;
      g.fillOval(x - halfSize, y - halfSize, size, size);
    }
    
    private void paintTurtle(Graphics g, Turtle t) {
      final int SIZE = 16;
      final int HALF_SIZE = SIZE / 2;
      final int HEAD_SIZE = 10;
      final int LEG_SIZE = 6;
      
      if(!t.isVisible())
        return;
      
      int xPos = t.getXPos();
      int yPos = t.getYPos();
      double dirRads = (Math.PI/180.0) * t.getDirection();
      
      Color color = t.getColor();
      Color limbColor = t.getLimbColor();
      
      g.setColor(limbColor);
      
      //Draw head
      int headXPos = xPos + circularXOffset(dirRads, HALF_SIZE+2);
      int headYPos = yPos + circularYOffset(dirRads, HALF_SIZE+2);
      fillCenteredCircle(g, headXPos, headYPos, HEAD_SIZE);
      
      //Draw legs
      for(int i = 0; i < 4; ++i) {
        double legAngle = dirRads + 2.0 * Math.PI * ((i+1)/5.0);
        int legXPos = xPos + circularXOffset(legAngle, HALF_SIZE+1);
        int legYPos = yPos + circularYOffset(legAngle, HALF_SIZE+1);
        fillCenteredCircle(g, legXPos, legYPos, LEG_SIZE);
      }
      
      //Draw body
      g.setColor(color);
      fillCenteredCircle(g, xPos, yPos, SIZE);
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
