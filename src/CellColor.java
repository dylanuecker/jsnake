import java.awt.Color;

public enum CellColor {

	WALL(Color.BLACK), BACKGROUND(Color.BLUE), APPLE(Color.RED), SNAKE(Color.GREEN);
	
	private Color color;
	
	private CellColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
