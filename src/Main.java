import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
	public static void main(String[] args) {

		/** frame */
		JFrame frame = new JFrame("¡ÚCalculator¡Ú"); // title bar
		frame.setPreferredSize(new Dimension(450, 500)); // size of frame
		frame.setLocation(250, 250); // start position of frame
		Container contentPane = frame.getContentPane(); // get content pane

		/** text field */
		String result = "0";
		JTextField text = new JTextField();
		text.setFont(new Font("Helvetica", Font.ITALIC, 30)); // font, text size
		text.setHorizontalAlignment(SwingConstants.RIGHT); // set prompt right
		text.setFocusable(false);
		text.setText(result);
		text.setOpaque(true);
		text.setBackground(Color.WHITE); // background color of text field

		/** label */
		JLabel label = new JLabel(" "); // create empty label
		label.setPreferredSize(new Dimension(600, 80)); // size of label
		label.setFont(new Font("Helvetica", Font.ITALIC, 30)); // font, text size
		label.setHorizontalAlignment(SwingConstants.RIGHT); // set prompt right
		label.setOpaque(true);
		label.setBackground(Color.WHITE); // background color of label

		/** panel */
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE); // background color of panel
		panel.setPreferredSize(new Dimension(600, 300)); // size of panel
		panel.setLayout(new GridLayout(6, 4, 2, 2)); // layout of panel
		panel.setFocusable(true);

		panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				panel.requestFocus(true);
			}
		});

		/** button */
		String operations[] = { "AC", "(", ")", "%", "1/x", "^2", "¡î", "/", "7", "8", "9", "*", "4", "5", "6", "-", "1",
				"2", "3", "+", ".", "0", "¡ç", "=" }; // button component
		String number[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		JButton buttonArray[] = new JButton[operations.length];
		ActionListener listener = new ButtonActionListener(buttonArray, text, label, "0", number);
		for (int i = 0; i < operations.length; i++) {

			buttonArray[i] = new JButton(operations[i]);
			buttonArray[i].setFont(new Font("Helvetica", Font.ITALIC, 30)); // font, text size
			buttonArray[i].addActionListener(listener);
			buttonArray[i].setPreferredSize(new Dimension(100, 30)); // size of button
			buttonArray[i].setFocusable(false);
			if (i == 0)
				buttonArray[i].setForeground(Color.RED);
			else if (i == 1 | i == 2 | i == 3 | i == 4 | i == 5 | i == 6 | i == 7 | i == 11 | i == 15 | i == 19)
				buttonArray[i].setForeground(Color.BLUE);

			buttonArray[i].setOpaque(true);
			buttonArray[i].setBackground(Color.WHITE);
			panel.add(buttonArray[i]);
		}
		KeyListener key = new InputKeyListener(buttonArray);
		panel.addKeyListener(key);

		/** Component */
		contentPane.add(text, BorderLayout.CENTER);
		contentPane.add(label, BorderLayout.NORTH);
		contentPane.add(panel, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true); // display frame
	}

}