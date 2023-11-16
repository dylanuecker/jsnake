import java.awt.Point;
import java.util.LinkedList;

public class Snake {

	private int direction, size, speed, amountToGrow;
	private LinkedList<Point> snake;

	public Snake(int headX, int headY, int initialDirection, int initialSize, int speed) {
		this.direction = initialDirection;
		this.size = initialSize;
		this.speed = speed;
		amountToGrow = 0;

		snake = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			snake.add(new Point(headX, headY + i));
		}
	}

	public int getSize() {
		return size;
	}

	public int getHeadX() {
		return (int) snake.getFirst().getX(); 
	}

	public int getHeadY() {
		return (int) snake.getFirst().getY(); 
	}

	public int getTailX() {
		return (int) snake.getLast().getX(); 
	}

	public int getTailY() {
		return (int) snake.getLast().getY(); 
	}

	public void setDirection(int direction) {
		if (direction == Model.LEFT && this.direction != Model.RIGHT ||
				direction == Model.RIGHT && this.direction != Model.LEFT ||
				direction == Model.UP && this.direction != Model.DOWN ||
				direction == Model.DOWN && this.direction != Model.UP) {
			this.direction = direction;
		}
	}

	public void updateHead() {
		if (direction == Model.LEFT) {
			snake.addFirst(new Point(getHeadX() - speed, getHeadY()));
		} else if (direction == Model.RIGHT) {
			snake.addFirst(new Point(getHeadX() + speed, getHeadY()));
		} else if (direction == Model.UP) {
			snake.addFirst(new Point(getHeadX(), getHeadY() - speed));
		} else if (direction == Model.DOWN) {
			snake.addFirst(new Point(getHeadX(), getHeadY() + speed));
		}
	}

	public void updateTail() {
		if (amountToGrow == 0) {
			snake.removeLast();
		} else {
			amountToGrow--;
		}
		
	}
	
	public void grow(int amount) {
		amountToGrow += amount;
	}
	
	public boolean growing() {
		return amountToGrow != 0;
	}
	
	public void pause() {
		speed = 0;
	}
	
	public void unpause(int speed) {
		this.speed = speed;
	}
}
