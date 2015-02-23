
public class SampleProgram {

	public static void main(String[] args) throws InterruptedException {
		World w = new World(400, 400);

		Turtle t1 = new Turtle(w, 200, 200);
		Turtle t2 = new Turtle(w, 100, 120);

		t1.move(20);
		t1.turn(45);
		t1.move(20);
		t1.turn(45);
		t1.move(20);
		t1.turn(45);
		t1.move(20);

		Thread.sleep(1000);

		t1.turn(45);
		t1.move(20);
		t2.move(40);

		Thread.sleep(1000);

		t1.turnTo(t2.getXPos(), t2.getYPos());
		t1.move(120);

		Thread.sleep(1000);

		t1.move(200);

		System.out.println("This is a sample program!");
	}
}