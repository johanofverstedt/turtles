public class TurtleArt {
  public static void main(String[] args) throws InterruptedException {
    final int WIDTH = 600;
    final int HEIGHT = 600;
    final int FRAMES_PER_SECOND = 60;
    final int SLEEP_TIME = (int)(1000.0 / FRAMES_PER_SECOND);
    final int SIMULATION_TIME = 60; //(s)
    final int ITERATION_COUNT = FRAMES_PER_SECOND * SIMULATION_TIME;
    
    World w = new World(WIDTH, HEIGHT);
    Turtle[] ts = new Turtle[256];
    int tCount = 0;
    
    for(int i = 0; i < ITERATION_COUNT; ++i) {
      if(tCount < 255) {
        int newX = (int)(Math.random() * WIDTH);
        int newY = (int)(Math.random() * HEIGHT);
        Turtle newT = new Turtle(w, newX, newY);
        ts[tCount++] = newT;
        newX = (int)(Math.random() * WIDTH);
        newY = (int)(Math.random() * HEIGHT);
        newT = new Turtle(w, newX, newY);
        ts[tCount++] = newT;
      }
      
      if(tCount > 0) {
        int removeIndex = (int)(Math.random() * tCount);
        
        --tCount;
        
        Turtle removedTurtle = ts[removeIndex];
        ts[removeIndex] = ts[tCount];
        ts[tCount] = null;
        
        w.remove(removedTurtle);
      }
      
      for(int k = 0; k < tCount; ++k) {
        ts[k].turn((int)(Math.random() * 60)-30);
        ts[k].move((int)(Math.random() * 10));
      }
      
      Thread.sleep(SLEEP_TIME);
    }
    
    // Remove all turtles
    for(int k = 0; k < tCount; ++k) {
      w.remove(ts[k]);
      ts[k] = null;
    }
    
    tCount = 0;
  }
}
