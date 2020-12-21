import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

class ButtonActionListener implements ActionListener {

	JButton buttonArray[];
	JTextField text;
	JLabel label;
	String inputString;
	String labelString = "";
	String infix;

	/** variables for error handling */
	boolean dot = false;
	boolean operand = false;
	boolean function = false;
	boolean rparen = false;

	String ppostfix = "";
	int lparenCount = 0;

	/** array for postfix formula conversion */
	int isp[] = { 0, 19, 12, 12, 13, 13, 13, 0 };
	int icp[] = { 20, 19, 12, 12, 13, 13, 13, 0 };
	String number[];

	enum precedence {
		lparen, rparen, plus, minus, times, divide, mod, eos, operand
	};

	Stack<Double> stack = new Stack<>();

	ButtonActionListener(JButton buttonArray[], JTextField text, JLabel label, String textString, String number[]) {
		this.buttonArray = buttonArray;
		this.text = text;
		this.label = label;
		this.inputString = textString;
		this.number = number;
	}

	/** to get precedence sign */
	char get_symbol(precedence token) {
		switch (token) {
		case lparen:
			return '(';
		case rparen:
			return ')';
		case plus:
			return '+';
		case minus:
			return '-';
		case divide:
			return '/';
		case times:
			return '*';
		case mod:
			return '%';
		case eos:
			return ' ';
		default:
			return ' ';
		}
	}

	/** get precedence according to sign */
	precedence get_token(char symbol) {
		switch (symbol) {
		case '(':
			return precedence.lparen;
		case ')':
			return precedence.rparen;
		case '+':
			return precedence.plus;
		case '-':
			return precedence.minus;
		case '/':
			return precedence.divide;
		case '*':
			return precedence.times;
		case '%':
			return precedence.mod;
		case ' ':
			return precedence.eos;
		default:
			return precedence.operand;
		}
	}

	/** calculate postfix */
	double eval(String postfix) {
		double op1, op2;
		double operand;
		String[] ParsedSpace = postfix.split(" ");
		for (int i = 0; i < ParsedSpace.length; i++) {
			try {
				operand = Double.parseDouble(ParsedSpace[i]);
				stack.push(operand);
			} catch (NumberFormatException e) {
				op2 = stack.pop();
				op1 = stack.pop();
				switch (ParsedSpace[i]) {
				case "+":
					stack.push(op1 + op2);
					break;
				case "-":
					stack.push(op1 - op2);
					break;
				case "*":
					stack.push(op1 * op2);
					break;
				case "/":
					stack.push(op1 / op2);
					break;
				case "%":
					stack.push(op1 % op2);
					break;
				}
			}
		}
		return stack.pop();
	}

	/** convert postfix to infix */
	String postfix(String infix) {
		Stack<precedence> stack = new Stack<>();
		boolean operandCheck = false;

		String postfix = "";
		char symbol;
		precedence token;
		stack.push(precedence.eos);
		for (int i = 0; i < infix.length(); i++) {
			symbol = infix.charAt(i);
			token = get_token(symbol);
			if (token == precedence.operand) {
				operandCheck = true;
				postfix += symbol;
			} else if (token == precedence.rparen) {
				if (operandCheck == true) {
					postfix += " ";
					operandCheck = false;
				}
				while (stack.peek() != precedence.lparen) {
					symbol = get_symbol(stack.pop());
					postfix += symbol;
					postfix += " ";
				}
				stack.pop();
			} else {
				if (operandCheck == true) {
					postfix += " ";
					operandCheck = false;
				}
				while (isp[stack.peek().ordinal()] >= icp[token.ordinal()]) {
					symbol = get_symbol(stack.pop());
					postfix += symbol;
					postfix += " ";
				}
				stack.push(token);
			}
		}
		postfix = postfix.trim();
		postfix += " ";
		while (!stack.empty()) {
			symbol = get_symbol(stack.pop());
			postfix += symbol;
			postfix += " ";
		}
		postfix = postfix.trim();
		return postfix;
	}

	/** add the number of inputs to inputString */
	public void setInputString(String inputString) {
		if (this.inputString.equals("0"))
			if (!inputString.equals("."))
				this.inputString = "";
		this.inputString += inputString;
	}

	/** define button actions */
	public void actionPerformed(ActionEvent e) {
		/** 'AC' button */
		if (e.getSource().equals(buttonArray[0])) {
			inputString = "0";
			labelString = "";
			text.setText(inputString);
			label.setText(" ");
			dot = false;
			operand = false;
			function = false;
			rparen = false;
			lparenCount = 0;
		}
		/** '(' button */
		if (e.getSource().equals(buttonArray[1])) {
			if (rparen == false) {
				if (operand == true) {
					operand = false;
					inputString = "0";
					text.setText(inputString);
				}

				labelString = label.getText() + "(";
				label.setText(labelString);
				lparenCount++;
				dot = false;
				rparen = false;
			}
		}
		/** ')' button */
		if (e.getSource().equals(buttonArray[2])) {
			if (lparenCount > 0) {
				if (rparen == false) {
					labelString += text.getText() + ")";
				} else {
					labelString += ")";
				}
				lparenCount--;
				label.setText(labelString);
				operand = false;
				rparen = true;
			}
		}
		/** '%' button */
		if (e.getSource().equals(buttonArray[3])) {
			if (rparen == true) {
				labelString += "%";
				label.setText(labelString);
				inputString = "0";
				text.setText(inputString);
				rparen = false;
				dot = false;
				operand = true;
			} else {
				if (operand == false) {
					labelString += text.getText() + "%";
					operand = true;
					label.setText(labelString);
					inputString = "0";
					dot = false;
					function = false;
				} else {
					labelString = label.getText();
					labelString = labelString.substring(0, labelString.length() - 1);
					labelString += "%";
					label.setText(labelString);
				}
			}

		}
		/** '1/x' button */
		if (e.getSource().equals(buttonArray[4])) {
			double result = 1 / Double.parseDouble(text.getText());
			if (result == (long) result)
				inputString = "" + (long) result;
			else
				inputString = "" + result;
			text.setText(inputString);
			function = true;
		}
		/** '^2' button */
		if (e.getSource().equals(buttonArray[5])) {
			double result = Math.pow(Double.parseDouble(text.getText()), 2);
			if (result == (long) result)
				inputString = "" + (long) result;
			else
				inputString = "" + result;
			text.setText(inputString);
			function = true;
		}
		/** 'ก๎' button */
		if (e.getSource().equals(buttonArray[6])) {
			double result = Math.sqrt(Double.parseDouble(text.getText()));
			if (result == (long) result)
				inputString = "" + (long) result;
			else
				inputString = "" + result;
			text.setText(inputString);
			function = true;
		}
		/** '/' button */
		if (e.getSource().equals(buttonArray[7])) {
			if (rparen == true) {
				labelString += "/";
				label.setText(labelString);
				inputString = "0";
				text.setText(inputString);
				rparen = false;
				dot = false;
				operand = true;
			} else {
				if (operand == false) {
					labelString += text.getText() + "/";
					operand = true;
					label.setText(labelString);
					inputString = "0";
					dot = false;
					function = false;
				} else {
					labelString = label.getText();
					labelString = labelString.substring(0, labelString.length() - 1);
					labelString += "/";
					label.setText(labelString);
				}
			}

		}
		/** '*' button */
		if (e.getSource().equals(buttonArray[11])) {
			if (rparen == true) {
				labelString += "*";
				label.setText(labelString);
				inputString = "0";
				text.setText(inputString);
				rparen = false;
				dot = false;
				operand = true;
			} else {
				if (operand == false) {
					labelString += text.getText() + "*";
					operand = true;
					label.setText(labelString);
					inputString = "0";
					dot = false;
					function = false;
				} else {
					labelString = label.getText();
					labelString = labelString.substring(0, labelString.length() - 1);
					labelString += "*";
					label.setText(labelString);
				}
			}

		}
		/** '-' button */
		if (e.getSource().equals(buttonArray[15])) {
			if (rparen == true) {
				labelString += "-";
				label.setText(labelString);
				inputString = "0";
				text.setText(inputString);
				rparen = false;
				dot = false;
				operand = true;
			} else {
				if (operand == false) {
					labelString += text.getText() + "-";
					operand = true;
					label.setText(labelString);
					inputString = "";
					dot = false;
					function = false;
				} else {
					labelString = label.getText();
					labelString = labelString.substring(0, labelString.length() - 1);
					labelString += "-";
					label.setText(labelString);
				}
			}
		}
		/** '+' button */
		if (e.getSource().equals(buttonArray[19])) {
			if (rparen == true) {
				labelString += "+";
				label.setText(labelString);
				inputString = "0";
				text.setText(inputString);
				rparen = false;
				dot = false;
				operand = true;
			} else {
				if (operand == false) {
					labelString += text.getText() + "+";
					operand = true;
					label.setText(labelString);
					inputString = "0";
					dot = false;
					function = false;
				} else {
					labelString = label.getText();
					labelString = labelString.substring(0, labelString.length() - 1);
					labelString += "+";
					label.setText(labelString);
				}
			}

		}
		/** '.' button */
		if (e.getSource().equals(buttonArray[20])) {
			if (function == true) {
				inputString = "0";
				text.setText(inputString);
				function = false;
			}
			if (dot == false) {
				setInputString(".");
				text.setText(inputString);
				dot = true;
			}
		}
		/** 'ก็' button */
		if (e.getSource().equals(buttonArray[22])) {
			if (inputString.length() > 0 && operand == false && function == false && !inputString.equals("0")) {

				if (inputString.charAt(inputString.length() - 1) == '.')
					dot = false;

				inputString = inputString.substring(0, inputString.length() - 1);

				if (inputString.length() == 0)
					inputString = "0";

				text.setText(inputString);
			}
		}
		/** '=' button */
		if (e.getSource().equals(buttonArray[23])) {
			if (lparenCount > 0)
				for (; lparenCount > 0;) {
					if (rparen == false) {
						labelString += text.getText() + ")";
					} else {
						labelString += ")";
					}
					lparenCount--;
					label.setText(labelString);
					operand = false;
					rparen = true;
				}
			if (rparen == true) {
				labelString += "=";
				label.setText(labelString);
			} else {
				labelString += text.getText() + "=";
				label.setText(labelString);
			}
			infix = labelString.substring(0, labelString.length() - 1).trim();
			ppostfix = postfix(infix);

			double result = eval(ppostfix);
			if (result == (long) result)
				text.setText("" + (long) result);
			else
				text.setText("" + result);
			/** initialize all variable */
			dot = false;
			operand = false;
			function = false;
			rparen = false;
			labelString = "";
			inputString = "0";
		}
		/** number button event */
		for (Integer i = 0; i < 10; i++) {
			if (e.getActionCommand().equals(number[i])) {
				if (function == true) {
					inputString = "0";
					text.setText(inputString);
					function = false;
				}
				operand = false;
				if (text.getText().equals("0"))
					if (i == 0)
						return;
				setInputString(i.toString());
				text.setText(inputString);
			}
		}
	}
}