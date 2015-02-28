
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
import javax.swing.*;

public class Turtle {
  //
  //  Turtle attributes
  //

  private int x;
  private int y;
  private int direction;
  
  private Color color;
  private Color limbColor;
  
  private boolean visible;
  private boolean drawPathFlag;
  
  private World world;
  
  //
  //  Turtle constructors
  //

  /**
   *  Constructs a turtle and places it at the center of the world.
   *
   *  @param w The world which the turtle will reside in.
   */
  public Turtle(World w) {
    this(w, w.getWidth() / 2, w.getHeight() / 2);
  }
  
  /**
   *  Constructs a turtle and places it at the specified position
   *  in the world.
   *
   *  @param w The world which the turtle will reside in.
   *  @param x X-coordinate of the point where the turtle will start at.
   *  @param y Y-coordinate of the point where the turtle will start at.
   */
  public Turtle(World w, int x, int y) {
    if(w == null)
      throw new RuntimeException("The provided world is null. Turtles can not live without a world.");
    
    this.world = w;
    
    if(x < 0)
      x = 0;
    if(y < 0)
      y = 0;
    if(x >= w.getWidth())
      x = w.getWidth() - 1;
    if(y >= w.getHeight())
      y = w.getHeight() - 1;
    
    this.x = x;
    this.y = y;
    
    //Randomize turtle color
    this.color = Color.getHSBColor((float)Math.random(), 0.25f + 0.65f * (float)Math.random(), 0.5f);
    this.limbColor = this.color.brighter();
    
    this.visible = true;
    this.drawPathFlag = true;
    
    w.add(this);
    
    //Redraw the world to immediately display the new turtle!
    this.updateWorld();
  }
  
  //
  //  Getters (and some setters) for the turtle attributes
  //
  
  /**
   *  Gets the World-object associated with this turtle.
   *
   *  @return The world.
   */
  public World getWorld() {
    return this.world;
  }
  
  public int getXPos() {
    return this.x;
  }
  
  public int getYPos() {
    return this.y;
  }
  
  /**
   *  Gets the direction of the turtle in degrees.
   *  The value is guaranteed to be in domain [0-359].
   *
   *  @return The direction in degrees.
   */
  public int getDirection() {
    return this.direction;
  }
  
  public Color getColor() {
    return this.color;
  }
  
  public Color getLimbColor() {
    return this.limbColor;
  }
  
  /**
   *  Sets the color of the turtle to the specified RGB color.
   *
   *  @param red The red component of the color. [0-255]
   *  @param green The green component of the color. [0-255]
   *  @param blue The blue component of the color. [0-255]
   */
  public void setColor(int red, int green, int blue) {
    this.color = new Color(red, green, blue);
    this.limbColor = this.color.brighter();
  }
  
  public boolean isVisible() {
    return this.visible;
  }
  
  /**
   *  Sets the visibility status of the turtle.
   *
   *  @param visible Visibility status. (false hides the turtle and true shows it.)
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
  
  public boolean isPathEnabled() {
    return this.drawPathFlag;
  }
  
  public void enablePath() {
    this.drawPathFlag = true;
  }
  
  public void disablePath() {
    this.drawPathFlag = false;
  }
  
  /**
   *  Calculates the Euclidean distance between
   *  a turtle and a given other point.
   *
   *  @param x The x-coordinate of the other point.
   *  @param y The y-coordinate of the other point.
   *
   *  @return The Euclidean distance as a double.
   */
public double distanceTo(int x, int y) {
  double xDelta = this.x - x;
  double yDelta = this.y - y;

  return Math.sqrt(xDelta*xDelta + yDelta*yDelta);
}

  /**
   *  Generates a compact string representation of the turtle.
   *
   *  @return The string representation.
   */
  public String toString() {
    String result = "Turtle(" + 
      "x: " + this.x +
      ", y: " + this.y +
      ", direction: " + this.direction +
      ", red: " + this.color.getRed() +
      ", green: " + this.color.getGreen() +
      ", blue: " + this.color.getBlue() +
      ")";
    
    return result;
  }
  
  //
  //  Core movement and update methods
  //
  
  /**
   *  Repositions the turtle to the given x- and y-coordinates.
   *  moveTo is the basic movement method which all other movement methods
   *  should call. Handles drawing of the turtle path, keeping the turtle
   *  inside the world and world redrawing.
   *
   *  @param x X-coordinate the turtle will go to.
   *  @param y Y-coordinate the turtle will go to.
   */
  
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
  
  /**
   *  Turns the turtle clockwise by the given integer number of degrees.
   *  All int-values are valid and the value will wrap around correctly.
   *
   *  @param degrees The number of degrees to turn.
   *  (Clockwise if positive and counter-clockwise if negative.)
   */
  public void turn(int degrees) {
    //Add the angle to the current direction
    //and restrict the stored direction to
    //a domain of 0-359
    degrees = (degrees % 360);
    this.direction = (this.direction + degrees) % 360;
    if(this.direction < 0)
      this.direction += 360;
    
    updateWorld();
  }
  
  /**
   *  Turns the turtle to face the point specified by the x- and y-coordinate.
   *
   *  @param x X-coordinate of the point which to turn towards.
   *  @param y Y-coordinate of the point which to turn towards.
   */
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
  
  //
  //  Prebuilt movement methods
  //
  
  /**
   *  Moves the turtle along its current direction by
   *  an integer step-size.
   *
   *  @param step The delta to move the turtle by.
   *  (Moves forward if positive and reverse if negative.)
   */
  public void move(int step) {
    double dirRads = Math.PI * (this.direction / 180.0);
    int xStep = (int)Math.round(Math.cos(dirRads) * step);
    int yStep = (int)Math.round(Math.sin(dirRads) * step);
    
    moveTo(x + xStep, y + yStep);
  }
  
  //
  //  Write your own methods hereafter!
  //
  
  
}
