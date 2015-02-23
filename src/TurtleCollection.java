
public class TurtleCollection {
	private Turtle[] turtles;
	private int turtleCount;

	public TurtleCollection(int max) {
		if(max < 1)
			max = 1;

		turtles = new Turtle[max];
	}

	public void add(Turtle t) {
		if(turtleCount == turtles.length) {
			Turtle[] newArray = new Turtle[1 + (turtles.length * 3) / 2];
			
			for(int i = 0; i < turtleCount; ++i) {
				newArray[i] = turtles[i];
			}

			turtles = newArray;
		}

		turtles[turtleCount++] = t;
	}

	public void remove(Turtle t) {
		for(int i = 0; i < turtleCount; ++i) {
			if(turtles[i] == t) {
				remove(i);
				return;
			}
		}		
	}

	private void remove(int index) {
		if(index < 0 || index >= turtleCount)
			throw new RuntimeException("TurtleCollection method 'remove' called with invalid index: " + index + ".");

		Turtle t = turtles[index];
		for(int i = index; i < turtleCount; ++i) {
			turtles[i] = turtles[i + 1];
		}

		--turtleCount;
		turtles[turtleCount] = null;

		return t;
	}
}
