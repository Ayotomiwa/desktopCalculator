package com.captainnigeria;

import javax.swing.*;
import java.awt.*;

enum NumberConstants {
    SEVEN("7"), EIGHT("8"), NINE("9"), FOUR("4"), FIVE("5"), SIX("6"), ONE("1"), TWO("2"),
    THREE("3"), PLUSMINUS("±"), Zero("0"), DOT(".");

    private final String value;

    NumberConstants(String i) {
        this.value = i;
    }

    public String getValue() {
        return value;
    }
}

public class CalculatorButton extends JButton {

    public static float fontSize = 20f;

    public CalculatorButton(String name, String text, Color backgroundColor, Color foregroundColor){
        super(text);
        setForeground(foregroundColor);
        setBackground(backgroundColor);
        setName(name);
        setFont(this.getFont().deriveFont(fontSize));
    }


    public static void addOperatorButtons(JLabel textField, Calculator main, JPanel mainPanel){

        JButton[] operatorButtons = new JButton[9];
        String[] operators = {"(", ")", "x²", "^", "√", "÷", "×", "+", "-"};
        String[] operatorNames = {"null", "Parentheses", "PowerTwo", "PowerY", "SquareRoot",
                "Divide", "Multiply", "Add", "Subtract"};

        int x = 25;
        int y = 180;

        for (int i = 1; i < operatorButtons.length; i++) {
            String operator = operators[i];

            if (i == 1) {
                operator = "()";
            }
            operatorButtons[i] = new CalculatorButton(operatorNames[i],operator, Color.black, Color.white);
            String finalOperator = operator;
            operatorButtons[i].addActionListener(e-> ButtonOperationManager.signOperation(textField, finalOperator, main));
            operatorButtons[i].setBounds(x, y, 85, 43);
            mainPanel.add(operatorButtons[i]);
            if (i > 1 && i <= 4) {
                x += 90;
            }
            if (i == 1 || i > 4) {
                y += 50;
            }
        }

    }

    public static void addNumberButtons(CalculatorScreen label, Calculator main, JPanel mainPanel){
        JButton[] numberButtons = new JButton[12];
        int i = 0;
        int x = 25;
        int y = 280;
        int j = 0;

        for (NumberConstants num : NumberConstants.values()) {
            String name = String.valueOf(num).charAt(0) + String.valueOf(num).substring(1).toLowerCase();
            if (num.getValue().equals("±")) {
                name = "PlusMinus";
            }
            numberButtons[i] = new CalculatorButton(name, num.getValue(), Color.WHITE, Color.black);
            numberButtons[i].addActionListener(e -> ButtonOperationManager.numberOperation(label, main, num.getValue()));
            numberButtons[i].setBounds(x, y, 85, 43);
            mainPanel.add(numberButtons[i]);
            x += 90;
            if (i == 2 || i == 5 || i == 8) {
                x = 25;
                y = 330 + j;
                j += 50;
            }
            i++;
        }
    }
}
