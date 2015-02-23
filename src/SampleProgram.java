
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