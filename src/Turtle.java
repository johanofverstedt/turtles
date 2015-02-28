
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

package turtles;

import java.awt.*;
import javax.swing.*;

public class Turtle {
  private int x;
  private int y;
  private int direction;
  
  private Color color;
  private Color limbColor;
  
  private boolean visible;
  private boolean drawPathFlag;
  
  private World world;
  
  public Turtle(World w) {
    this(w, w.getWidth() / 2, w.getHeight() / 2);
  }
  
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
  
  public World getWorld() {
    return this.world;
  }
  
  public int getXPos() {
    return this.x;
  }
  
  public int getYPos() {
    return this.y;
  }
  
  public int getDirection() {
    return this.direction;
  }
  
  public Color getColor() {
    return this.color;
  }
  
  public Color getLimbColor() {
    return this.limbColor;
  }
  
  public void setColor(int red, int green, int blue) {
    this.color = new Color(red, green, blue);
  }
  
  public void setLimbColor(int red, int green, int blue) {
    this.limbColor = new Color(red, green, blue);
  }
  
  public boolean isVisible() {
    return this.visible;
  }
  
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
   * moveTo is the basic movement method which all other movement methods
   *  should call. Handles drawing of the turtle path, keeping the turtle
   *  inside the world and world redrawing.
   *
   * @param x X-coordinate the turtle will go to.
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
  
  public void turn(int degrees) {
    //Add the angle to the current direction
    //and restrict the stored direction to
    //a domain of 0-359
    this.direction = (this.direction + degrees) % 360;
    if(this.direction < 0)
      this.direction += 360;
    
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
  
  //
  //  Prebuilt movement methods
  //
  
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
