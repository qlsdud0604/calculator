import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

class InputKeyListener implements KeyListener {

	JButton buttonArray[];

	InputKeyListener(JButton buttonArray[]) {
		this.buttonArray = buttonArray;
	}

	public void keyPressed(KeyEvent e) {

		for (int i = 0; i < 10; i++) {
			if (e.getKeyChar() == Integer.toString(i).charAt(0)) {
				if (i == 0)
					buttonArray[21].doClick();
				if (i == 1)
					buttonArray[16].doClick();
				if (i == 2)
					buttonArray[17].doClick();
				if (i == 3)
					buttonArray[18].doClick();
				if (i == 4)
					buttonArray[12].doClick();
				if (i == 5)
					buttonArray[13].doClick();
				if (i == 6)
					buttonArray[14].doClick();
				if (i == 7)
					buttonArray[8].doClick();
				if (i == 8)
					buttonArray[9].doClick();
				if (i == 9)
					buttonArray[10].doClick();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			buttonArray[0].doClick();

		if (e.getKeyChar() == '(')
			buttonArray[1].doClick();

		if (e.getKeyChar() == ')')
			buttonArray[2].doClick();

		if (e.getKeyChar() == '%')
			buttonArray[3].doClick();

		if (e.getKeyChar() == '/')
			buttonArray[7].doClick();

		if (e.getKeyChar() == '*')
			buttonArray[11].doClick();

		if (e.getKeyChar() == '-')
			buttonArray[15].doClick();

		if (e.getKeyChar() == '+')
			buttonArray[19].doClick();

		if (e.getKeyChar() == '.')
			buttonArray[20].doClick();

		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			buttonArray[22].doClick();

		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			buttonArray[23].doClick();
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

}