# 계산기
## 1. 프로젝트의 목적
* GUI 환경에서 작동하는 프로그램의 이해력을 높인다.
* 윈도우 프로그램의 구현 방법을 터득한다.
* 스택 자료구조를 이용하여 우선순위 연산을 구현한다.

-----
## 2. 프로젝트의 내용
* 기본적으로 사칙연산 수행이 가능한 계산기를 설계한다.
* 계산기의 추가적인 기능을 구현한다.

-----
## 3. 프로그램의 기능
* 기본적인 사칙연산(+, -, *, /)의 기능
* 추가적인 연산(1/X, ^2, √, %)의 기능
* 우선순위 사칙연산의 기능 
* ‘(’, ‘)’를 이용한 우선순위 연산의 기능
* ‘←’를 이용한 수식 지우기 기능
* ‘.’를 이용한 수식의 실수형 표현 기능

-----
## 4. 윈도우의 설계
**1) Frame의 구현**
```
JFrame frame = new JFrame("★Calculator★"); 
frame.setPreferredSize(new Dimension(450, 500)); 
frame.setLocation(250, 250); 
Container contentPane = frame.getContentPane();
```
ㆍ TitleBar에 들어갈 문구 및 Frame의 사이즈를 설정하였다.   
ㆍ Frame이 시작될 위치를 설정하고 종료버튼을 만들었다.   

**2) TextField의 구현**
```
String result = "0";
JTextField text = new JTextField();
text.setFont(new Font("Helvetica", Font.ITALIC, 30));
text.setHorizontalAlignment(SwingConstants.RIGHT);
text.setFocusable(false);
text.setText(result);
text.setOpaque(true);
text.setBackground(Color.WHITE);
```
ㆍ TextField에 나타나는 폰트, 폰트의 크기 및 배경색상을 설정하였다.   
ㆍ 계산기의 특징을 참고하여 TextField의 프롬프트를 오른쪽으로 설정하였다.     
ㆍ TextField는 계산의 수식을 입력받도록 사용된다.   

**3) Label의 구현**
```
JLabel label = new JLabel(" ");
label.setPreferredSize(new Dimension(600, 80));
label.setFont(new Font("Helvetica", Font.ITALIC, 30));
label.setHorizontalAlignment(SwingConstants.RIGHT);
label.setOpaque(true);
label.setBackground(Color.WHITE);
```
ㆍ수식의 과정이 출력될 Label을 빈 Label으로 생성한다.   
ㆍTextField와 마찬가지로 폰트, 폰트의 크기 및 배경 색상을 설정 하였다.   

**4) Panel의 구현**
```
JPanel panel = new JPanel();
panel.setBackground(Color.WHITE);
panel.setPreferredSize(new Dimension(600, 300));
panel.setLayout(new GridLayout(6, 4, 2, 2));
panel.setFocusable(true);
```
ㆍ Panel은 버튼 구성의 기초가 된다.   
ㆍ 레이아웃은 버튼의 수를 고려하여 행이 6, 열이 4인 Panel을 구성한다.   
ㆍ 다른 컴포넌트와 마찬가지로 폰트, 폰트의 크기, 배경색상을 설정하였다.   

**5) Button의 구현**
```
String operations[] = { "AC", "(", ")", "%", "1/x", "^2", "√", "/", "7", "8", "9", "*", "4", "5", "6", "-", "1",
		"2", "3", "+", ".", "0", "←", "=" };
String number[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
JButton buttonArray[] = new JButton[operations.length];
ActionListener listener = new ButtonActionListener(buttonArray, text, label, "0", number);
for (int i = 0; i < operations.length; i++) {

	buttonArray[i] = new JButton(operations[i]);
	buttonArray[i].setFont(new Font("Helvetica", Font.ITALIC, 30));
	buttonArray[i].addActionListener(listener);
	buttonArray[i].setPreferredSize(new Dimension(100, 30));
	buttonArray[i].setFocusable(false);
	if (i == 0)
		buttonArray[i].setForeground(Color.RED);
	else if (i == 1 | i == 2 | i == 3 | i == 4 | i == 5 | i == 6 | i == 7 | i == 11 | i == 15 | i == 19)
		buttonArray[i].setForeground(Color.BLUE);

	buttonArray[i].setOpaque(true);
	buttonArray[i].setBackground(Color.WHITE);
	panel.add(buttonArray[i]);
}
```
ㆍ String 타입의 배열로 버튼의 동작을 구체화 하였다.   
ㆍ 각각의 버튼의 폰트, 폰트의 사이즈를 설정하였다.   
ㆍ for문을 이용하여 각각의 버튼이 동작하게 될 ActionListener에 추가 하였다.    
ㆍ 버튼의 기능의 구별을 위해 각각의 기능 별로 색상을 달리 구현하였다.   

**6) Component의 위치 설정**
```
contentPane.add(text, BorderLayout.CENTER);
contentPane.add(label, BorderLayout.NORTH);
contentPane.add(panel, BorderLayout.SOUTH);
```
ㆍ Frame에 미리 선언된 ContentPane을 이용하여 각각의 Component의 위치를 설정한다.   

-----
## 5. 우선순위 연산의 구현
**1) infix 수식을 postfix 수식으로 변환**
```
int isp[] = { 0, 19, 12, 12, 13, 13, 13, 0 };
int icp[] = { 20, 19, 12, 12, 13, 13, 13, 0 };

enum precedence {
	lparen, rparen, plus, minus, times, divide, mod, eos, operand
};
```
ㆍ 괄호 연산자를 처리하기 위해 int형 배열인 isp 와 icp를 선언하였다.   
ㆍ isp와 icp배열의 인덱스는 연산자 우선순위 값으로 미리 설정해 두었다.   
```
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
```
ㆍ Stack을 이용하여 infix 수식을 postfix로 변화 하였다.   
ㆍ 우선순위(top) < 우선순위(incoming) : 입력 연산자를 스택에 저장   
ㆍ 우선순위(top) > 우선순위(incoming) : 스택 top에 있는 연산자를 출력   
ㆍ 우선순위(top) = 우선순위(incoming) : 결합성에 따라 처리   

**2) postfix 수식의 계산**
```
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
```
ㆍ 버튼에 따른 우선순위를 설정한다.
```
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
```
ㆍ 바뀐 postfix수식을 이용하여 계산을 한다.   
ㆍ 피연산자는 스택에 저장되며, 연산자는 스택에서 피연산자를 pop하며 결과를 push한다.    

-----
## 6. ButtonActionListener의 구현
**1) ‘AC’ 버튼의 구현**
```
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
```
ㆍ 현재 계산기의 상태를 초기화 하는 역할을 한다.   
ㆍ TextField에 초기 설정인‘0’이 출력되도록 하며, Label또한 초기 상태로 되돌린다.   
ㆍ 오류 처리를 위한 변수(dot, operand, function, rparen) 또한 초기 값인 false로 설정한다.   

**2) ‘(’ 버튼의 구현**
```
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
```
ㆍ 사용자의 재량에 따라 우선순위 연산을 수행하는 연산자이다.   
ㆍ TextField에 출력된 수식에 ‘(’을 추가하여 Label창에 올려진다.   
ㆍ rparen( ‘)’ )과의 수를 맞추기 위해 설정한 변수인 lparenCount 변수의 값을 1 증가시킨다.   

**3) ‘)’ 버튼의 구현**
```
if (e.getSource().equals(buttonArray[2])) {
	if (lparenCount > 0) {
		if (rparen == false) {
			labelString += text.getText() + ")";
		} else {
			labelString += ")";
		}
		lparenCount--;a
		label.setText(labelString);
		operand = false;
		rparen = true;
	}
}
```
ㆍ 우선순위를 설정할 때 ‘(’연산자와 같이 사용되는 연산자이다.   
ㆍ ‘(’와의 수를 맞추기 위해 앞서 언급한 lparenCount 변수 값을 1 감소시킨다.   

**4) 연산 버튼 (+, -, /, \*, %)의 구현**
```
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
```
ㆍ 연산을 수행 하는 버튼들 의 구현 방식은 똑같기 때문에 한 가지 예로 ‘+’를 들겠다.   
ㆍ ‘+’버튼은 TextField의 수식과 연산자 기호를 label에 추가한다.   
ㆍ 연속적인 연산자 입력을 방지하기 위해 미리 설정해둔 변수인 ‘operand’값을 true로 설정하였다. ‘operand’변수 값이 true이면 그 전에 입력한 연산자가 새로 입력한 연산자로 대체 되는 기능을 하도록 신호를 주는 역할을 한다.   

**5) 연산 버튼(1/X, ^2, √)의 구현**
```
if (e.getSource().equals(buttonArray[5])) {
	double result = Math.pow(Double.parseDouble(text.getText()), 2);
	if (result == (long) result)
		inputString = "" + (long) result;
	else
		inputString = "" + result;
	text.setText(inputString);
	function = true;
}
```
ㆍ function 버튼(1/X, ^2, √) 중 ‘^2’을 한 가지 예시로 설명을 하겠다.   
ㆍ TextField의 수식을 wrapper class의 parseDouble 메소드를 이용하여 double형으로 변환을 한 후 해당 연산을 수행한다.   
ㆍ 그 결과 값을 TextField에 출력을 하며, 이 때 정수로 값을 표현 할 수 있으면 long형으로 형 변환을 한 후 출력을 시킨다.   

**6) ‘.’ 버튼의 구현**
```
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
```
ㆍ TextField 수식에 ‘.’ 문자를 추가하여 출력한다.   
ㆍ 소수점 수를 조정하기 위해 미리 설정한 변수인 ‘dot’ 변수의 값을 true로 설정한다.   

**7) ‘←’ 버튼의 구현**
```
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
```
ㆍ ‘←’버튼이 실행되었을 때 TextField에 출력 되어있는 수식을 오른쪽부터 하나씩 없애는 기능이다.    
ㆍ 이때 ‘←’버튼이 실행하게 될 여러 가지 조건을 설정하였다. 첫 번째로 TextField에 출력된 문자가 1개 이상이어야 하고, 두 번째로 앞서 설명한 operand, function변수의 값이 모두 false이어야 하며, 마지막으로 TextField의 초기 값인 ‘0 ’이 아니어야 한다. 이로써, ‘←’버튼이 가질 수 있는 오류들을 처리하였다.   
ㆍ 또한 TextField에 출력되는 수식이 단 하나만 있을 경우 ‘←’버튼을 눌렀을 때 초기값인 ‘0’으로 설정 해놓는 코드 또한 작성을 하였다.   

**8) ‘=’버튼의 구현**
```
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
```
ㆍ ‘=’버튼을 눌렀을 때 라벨의 출력된 수식을 초기화하고 TextField에 연산의 결과를 출력하는 기능을 구현하였다.   
ㆍ ‘=’버튼을 눌렀을 때‘)’연산자의 수를 ‘(’연산자의 수와 맞춰 주기위해 조건문을 추가하였다.   
ㆍ 이때‘=’버튼을 눌렀을 때 연산 과정에서 설정된 모든 변수의 값을 초기화 하였다.   

**9) 숫자 버튼(0~9)의 구현**
```
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
```
ㆍ 계산기의 숫자 버튼(0~9)을 누르면 TextField의 출력이 되도록 설정한 코드이다.   
ㆍ 이때 숫자는 수식의 연산을 String으로 처리하기 때문에 toString()를 이용하여 String으로 형 변환을 한 후 TextField에 올려 진다.   

-----
## 7. InputKeyListener의 구현
```
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
```
ㆍ InputKeyListener를 통하여 키보드와 계산기 프로그램의 버튼의 호환성을 지원하였다.   
ㆍ 예를 들어서 키보드 자판의 0을 누르면 buttonArray의 21번째의 인덱스가 작동 하도록 설정하였다.   

-----
## 8. 최종 테스트 및 동작 확인

<img src="https://user-images.githubusercontent.com/61148914/85944841-bad3a980-b974-11ea-9f2c-9afb14dc2a77.JPG" width="40%">

-----
## 9. 프로젝트의 수행 중 어려웠던 점
* 윈도우의 component 각각의 역할을 이해하는 점이 힘들었다.
* 계산기의 피연산자의 자릿수가 한자리이니 아니면 두 자리 이상인지 구별하는 방법을 구현하는데 어려움이 있었다.
* 스택 자료구조를 사용하여 두자릿수 이상의 우선순위 연산을 구현하는데 어려움이 있었다.

-----
## 10. 프로젝트를 마치고 느낀 점
* 자바 GUI에 대해 전반적으로 이해력을 높일 수 있었다.
* 윈도우 프로그램을 설계하는 능력이 향상되었음 을 느낀다.
* 우선순위 연산에 쓰이는 자료구조의 동작방식을 정확히 이해할 수 있었다.
