import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
	
	private final int HEIGHT, WIDTH;
	private final int CELL_HEIGHT = 30, CELL_WIDTH = 30;
	private final int DELAY = 100;
	private final int INITIAL_DIRECTION = UP, INITIAL_SIZE = 1, SPEED = 1;
	private final int APPLE_CHANCE = 12, APPLE_TIME = 0, APPLE_GROW_MAX = 4, APPLE_GROW_MIN = 1;
	private final Color TEXT_COLOR = Color.WHITE;
	private final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3, CENTER = 4;
	
	private JPanel[][] board;
	private JPanel[] borders;
	private Snake snake;
	private boolean paused, applePresent, gameOver;
	private Random random;
	private int score;
	private JLabel scoreDisplay, instructionsDisplay;
	private String scoreText = "Score: ";
	private String instructionsText = "Arrow keys:  move  P: pause  Space: reset";
	
	public Model(int height, int width) {
		this.HEIGHT = height;
		this.WIDTH = width;
		
		board = new JPanel[HEIGHT][WIDTH];
		borders = new JPanel[] {new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel()};
		random = new Random();
		scoreDisplay = new JLabel();
		scoreDisplay.setForeground(TEXT_COLOR);
		instructionsDisplay = new JLabel(instructionsText);
		instructionsDisplay.setForeground(TEXT_COLOR);
		
		setLayout(new BorderLayout());
		
		borders[NORTH].add(scoreDisplay);
		borders[SOUTH].add(instructionsDisplay);
		
		add(borders[NORTH], "North");
		add(borders[SOUTH], "South");
		add(borders[EAST], "East");
		add(borders[WEST], "West");
		
		for (int i = 0; i < borders.length - 1; i++) { // skip the center
			borders[i].setPreferredSize(new Dimension(WIDTH, CELL_HEIGHT));
			borders[i].setBackground(CellColor.WALL.getColor());
		}
		
		borders[CENTER].setLayout(new GridLayout(HEIGHT, WIDTH, 2, 2));
		borders[CENTER].setBackground(CellColor.BACKGROUND.getColor());
		
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				board[row][col] = new JPanel();
				board[row][col].setPreferredSize(new Dimension(CELL_HEIGHT, CELL_WIDTH));
				borders[CENTER].add(board[row][col]);
			}
		}
		
		start();
		add(borders[CENTER], "Center");
		
		Timer timer = new Timer(DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!applePresent && !gameOver && random.nextInt(APPLE_CHANCE) == APPLE_TIME) {
					applePresent = true;
					generateApple();
				}
				
				repaint();
			}
		});
		
		setFocusable(true); // necessary for panel to respond to key events
		addKeyListener(new KeyAdapter() {
			
			private boolean keyReady = true;
			private int heldDownKey;
			
			@Override
			public void keyPressed(KeyEvent ke) {
				if (keyReady) {
					keyReady = false;
					heldDownKey = ke.getKeyCode();
					
					if (heldDownKey == KeyEvent.VK_P) {
						paused = !paused;
						
						if (paused) {
							snake.pause();
						} else {
							snake.unpause(SPEED);
						}
					} else if (heldDownKey == KeyEvent.VK_SPACE) {
						start();
					} else if (!paused) {
						if (heldDownKey == KeyEvent.VK_LEFT) {
							snake.setDirection(LEFT);
						} else if (heldDownKey == KeyEvent.VK_RIGHT) {
							snake.setDirection(RIGHT);
						} else if (heldDownKey == KeyEvent.VK_UP) {
							snake.setDirection(UP);
						} else if (heldDownKey == KeyEvent.VK_DOWN) {
							snake.setDirection(DOWN);
						} else {
							keyReady = true;
						}
					} 
				}
			}
			
			@Override
			public void keyReleased(KeyEvent ke) {
				if (heldDownKey == ke.getKeyCode()) {
					keyReady = true;	
				}
			}
		});
		
		timer.start();
	}
	
	public void start() {
		snake = new Snake(WIDTH / 2, HEIGHT / 2, INITIAL_DIRECTION, INITIAL_SIZE, SPEED);
		paused = false;
		applePresent = false;
		gameOver = false;
		
		score = INITIAL_SIZE;
		
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				if (col == WIDTH / 2 && row >= HEIGHT / 2 && row < HEIGHT / 2 + snake.getSize()) {
					board[row][col].setBackground(CellColor.SNAKE.getColor());
				} else {
					board[row][col].setBackground(CellColor.BACKGROUND.getColor());
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		scoreDisplay.setText(scoreText + score);
		
		if (!paused && !gameOver) {
			snake.updateHead(); 
			
			if (!withinBounds() || board[snake.getHeadY()][snake.getHeadX()].getBackground()
					.equals(CellColor.SNAKE.getColor())) { // ran into wall or itself
				gameOver = true;
				return;
			} else if (board[snake.getHeadY()][snake.getHeadX()].getBackground()
					.equals(CellColor.APPLE.getColor())) {
				int amount = random.nextInt(APPLE_GROW_MAX) + APPLE_GROW_MIN;
				snake.grow(amount);
				applePresent = false;
			}
			
			board[snake.getHeadY()][snake.getHeadX()]
					.setBackground(CellColor.SNAKE.getColor()); // add to head
			
			board[snake.getTailY()][snake.getTailX()]
					.setBackground(CellColor.BACKGROUND.getColor()); // delete from tail
			
			if(snake.growing()) {
				score++;
			}
			
			snake.updateTail();
		} else if (gameOver) {
			
		}
	}
	
	public boolean withinBounds() {
		int y = snake.getHeadY(), x = snake.getHeadX();
		
		return y >= 0 && y < HEIGHT && x >= 0 && x < WIDTH;
	}
	
	public void generateApple() {
		int randY, randX;
		
		do {
			randY = random.nextInt(HEIGHT);
			randX = random.nextInt(WIDTH);
		} while (board[randY][randX].getBackground().equals(CellColor.SNAKE.getColor()));
		
		board[randY][randX].setBackground(CellColor.APPLE.getColor());
	}
}
