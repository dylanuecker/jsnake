import javax.swing.JFrame;

public class Driver {
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initGUI();
			}
		});
	}

	private static void initGUI() {
		JFrame frame = new JFrame("Snake");
		frame.setContentPane(new Model(30, 40));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}

