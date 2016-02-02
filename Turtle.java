
/**
 * Copyright (c) 2015, Johan Ofverstedt <johan.ofverstedt@gmail.com>
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

import java.awt.Color;

/**
 *  Turtle is a class representing a fun-loving reptile residing in
 *  a World. It contains attributes for x and y position, direction, color, etc.
 *
 *  The turtle is dynamic and can be moved, turned, made (in)visible,
 *  re-colored, etc.
 *
 *  Responsibility for drawing the turtle according to its
 *  attributes belong to the World-class.
 *
 *  The idea is to allow expansion of its capabilities by writing
 *  additional methods for more complex behavior and operations.
 */
public class Turtle {
  //
  //  Turtle attributes
  //
  public static final double RADIUS = 10.0;

  private int x;
  private int y;
  private int direction;
  
  private float size = 1.0f;

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
    updateWorld();
  }
  
  //
  //  Getters (and some setters) for the turtle attributes
  //
  
  /**
   *  Returns the {@link World} associated with this turtle.
   *
   *  @return The world.
   */
  public World getWorld() {
    return this.world;
  }
  
  /**
   *  Returns the x-coordinate of the turtle.
   *
   *  @return X-coordinate as an integer.
   */
  public int getXPos() {
    return this.x;
  }
  
  /**
   *  Returns the y-coordinate of the turtle.
   *
   *  @return Y-coordinate as an integer.
   */
  public int getYPos() {
    return this.y;
  }
  
  /**
   *  Returns the direction of the turtle in degrees.
   *  The value is guaranteed to be in domain <code>[0-359]</code>.
   *
   *  @return The direction in degrees.
   */
  public int getDirection() {
    return this.direction;
  }

  /**
   *  Sets the direction of the tutle in degrees.
   *  The value is converted to the [0-359] range.
   *
   *  @param direction The direction in degrees.
   */
  public void setDirection(int direction) {
    direction = (direction % 360);
    if(direction < 0)
      direction += 360;

    this.direction = direction;

    updateWorld();
  }
  
  /**
   *  Returns the scaling factor for the turtle where 1.0
   *  is the default size and 2.0 is double the default size
   *  and 0.5 is half the default size.
   *
   *  @return Scaling factor of the turtle.
   */
  public double getSize() {
    return (double)this.size;
  }

  /**
   *  Sets the scaling factor for the turtle where 1.0
   *  is the default size and 2.0 is double the default size
   *  and 0.5 is half the default size.
   *
   *  @param size Scaling factor of the turtle.
   */
  public void setSize(double size) {
    if(size < 0.0)
      size = 0.0;
    this.size = (float)size;

    updateWorld();
  }

  /**
   *  Returns the radius corresponding to the current scaling factor
   *  of the turtle.
   *
   *  @return The radius in pixels as a double.
   */
  public double getRadius() {
    return this.size * RADIUS;
  }

  /**
   *  Set the scaling factor corresponding to the specified radius.
   *
   *  @param radius The new radius to resize the turtle to.
   */
  public void setRadius(double radius) {
    setSize(radius / RADIUS);
  }

  /**
   *  Returns the main {@link Color} of the turtle (used to draw its body).
   *
   *  @return The color of the body.
   */
  public Color getColor() {
    return this.color;
  }
  
  /**
   *  Returns the {@link Color} of the turtle's limbs (head and legs).
   *
   *  @return The color of the limbs.
   */
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

    updateWorld();
  }
  
  /**
   *  Returns <code>true</code> if the turtle is visible and <code>false</code> if it's invisible.
   *
   *  @return Visibility flag as a boolean.
   */
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
 
    updateWorld();
 }
  
  /**
   *  Returns <code>true</code> if path drawing is enabled and <code>false</code> if it's disabled.
   *
   *  @return Path drawing flag as a boolean.
   */
  public boolean isPathEnabled() {
    return this.drawPathFlag;
  }
  
  /**
   *  Enables drawing of the path when the turtle moves.
   */
  public void enablePath() {
    this.drawPathFlag = true;
  }
  
  /**
   *  Disables drawing of the path when the turtle moves.
   */
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
   *  Generates a compact string representation of some of the turtle's
   *  attributes.
   *  <p>
   *  Example:
   *  <code>Turtle(x: 57, y: 173, direction: 180, size: 2.00000,red: 128, green: 57, blue: 34)</code>.
   *  </p>
   *
   *  @return The string representation.
   */
  public String toString() {
    String result = String.format("Turtle {x: %d, y: %d, direction: %d, size: %.5f, red: %d, green: %d, blue: %d}",
      this.x, this.y, this.direction, this.size, this.color.getRed(), this.color.getGreen(), this.color.getBlue());
    
    return result;
  }
  
  //
  //  Core movement and update methods
  //
  
  /**
   *  Repositions the turtle to the given x- and y-coordinates.
   *  moveTo is the basic movement method which all other movement methods
   *  should call.
   *  <p>
   *  When moveTo is called, the following actions are performed:
   *  </p>
   *  <ul>
   *  <li>
   *    Clamp the position so that the turtle remains inside the world.
   *  </li>
   *  <li>
   *    Draw the turtle's path from the old to new position (if paths are enabled).
   *  </li>
   *  <li>
   *    Redraw the world to display the changes immediately.
   *  </li>
   *  </ul>
   *
   *  @param x X-coordinate the turtle will move to.
   *  @param y Y-coordinate the turtle will move to.
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
    int newDirection = (this.direction + degrees) % 360;
    
    if(newDirection < 0)
      newDirection += 360;

    this.direction = newDirection;
    
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

  /**
   *  Turns the turtle to face the position of another turtle.
   *
   *  @param t The turtle to turn towards.
   */
  public void turnTo(Turtle t) {
    turnTo(t.getXPos(), t.getYPos());
  }
  
  private void updateWorld() {
    this.world.turtleUpdate();  
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
