package com.captainnigeria;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Calculator extends JFrame {

    public BiFunction<Double, Double, Double> add = Double::sum;
    public BiFunction<Double, Double, Double> subtract = (a, b) -> a - b;
    public BiFunction<Double, Double, Double> multiply = (a, b) -> a * b;
    public BiFunction<Double, Double, Double> divide = (a, b) -> a / b;
    public BiFunction<Double, Double, Double> square = Math::pow;
    public Function<Double, Double> squareRoot = Math::sqrt;


    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);

        CalculatorScreen resultLabel = new CalculatorScreen("ResultLabel", "0", 50F);;
        resultLabel.setForeground(Color.lightGray.brighter());
        resultLabel.setBounds(0, 7, 380, 100);

        CalculatorScreen equationLabel = new CalculatorScreen("EquationLabel", "", 16F);
        equationLabel.setBounds(0, 80, 380, 100);
        equationLabel.setToOriginalColor();

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY.darker());
        mainPanel.setLayout(null);

        CalculatorButton.addNumberButtons(equationLabel, this, mainPanel);
        CalculatorButton.addOperatorButtons(equationLabel, this, mainPanel);

        JButton equalsButton = new CalculatorButton("Equals", "=", Color.WHITE, Color.red);
        equalsButton.addActionListener(e -> ButtonOperationManager.equationOperation(equationLabel, resultLabel, this));
        equalsButton.setBounds(295, 430, 85, 43);

        JButton clearButton = new CalculatorButton("Clear", "C", Color.WHITE, Color.red);
        clearButton.addActionListener((e) -> ButtonOperationManager.clearOperation(equationLabel, resultLabel));
        clearButton.setBounds(205, 180, 85, 43);

        JButton deleteButton = new CalculatorButton("Delete", "DEL", Color.white, Color.red);
        deleteButton.addActionListener((e) -> ButtonOperationManager.deleteOperation(equationLabel));
        deleteButton.setBounds(295, 180, 85, 43);


        mainPanel.add(resultLabel);
        mainPanel.add(equationLabel);
        mainPanel.add(equalsButton);
        mainPanel.add(clearButton);
        mainPanel.add(deleteButton);
        add(mainPanel);
        setResizable(false);
        setVisible(true);
    }

    public String getFormattedText(String fieldText) {

        String[] individualTextValues = getIndividualTextValues(fieldText);

        return Arrays.stream(individualTextValues).map((s) -> {
            if (s.startsWith(".")) {
                return "0" + s;
            }
            if (s.endsWith(".")) {
                return s + "0";
            }
            return s;
        }).collect(Collectors.joining());
    }

    public String getResult(String fieldText) {

        String[] individualValues = getIndividualTextValues(fieldText);

        List<String> postfix = convertToPostFix(individualValues);
        double result = evaluatePostFix(postfix);
        DecimalFormat formatter = new DecimalFormat("0.#####E0");


        if (result % 1 == 0) {
            if (result > 10000000){
                return formatter.format(result);
            }
            return String.valueOf((int) result);
        }

        double truncatedDouble;
        try {
            truncatedDouble = BigDecimal.valueOf(result)
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue();
        } catch (NumberFormatException e) {
            return "Invalid Expression";
        }

        String doubleResult = String.valueOf(truncatedDouble);
        int resultCount = (int) Arrays.stream(doubleResult.split("")).count();

        if (resultCount > 7){
            System.out.println("Hey");
            doubleResult = formatter.format(truncatedDouble);
        }

        return doubleResult;
    }

    public String[] getIndividualTextValues(String fieldText) {

        char[] operators = {'√', '^', '÷', '×', '+', '-', '(', ')'};

        if (fieldText != null && fieldText.strip().length() > 0) {
            String operatorPresent = getOperatorsPresent(fieldText);

            if (operatorPresent.equals("")) {
                return new String[]{fieldText};
            }

            String numbers;
            numbers = Arrays.stream(fieldText.split(""))
                    .map(s -> {
                        if (Arrays.toString(operators).contains(s)) {
                            return " " + s + " ";
                        }
                        return s;
                    }).collect(Collectors.joining());

            return Arrays.stream(numbers.split(" ")).filter(s -> !s.equals("")).toArray(String[]::new);
        }
        return new String[]{};
    }

    public List<String> convertToPostFix(String[] textValues) {

        char[] operators = {'√', '^', '÷', '×', '+', '(', ')', '-'};
        List<String> postFix = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        System.out.println("Text Values -" + Arrays.toString(textValues));

        for (String input : textValues) {
            if (!Arrays.toString(operators).contains(input)) {
                postFix.add(input);
            } else if (input.equals("(")) {
                stack.push(input);
            } else if (input.equals(")")) {
                while (!stack.isEmpty() && !Objects.equals(stack.peek(), "(")) {
                    postFix.add(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.empty() && (getPrecedence(input) <= getPrecedence(stack.peek()))) {
                    postFix.add(stack.pop());
                }
                stack.push(input);
            }
        }

        while (!stack.isEmpty()) {
            postFix.add(stack.pop());
        }
        System.out.println(("Post fix " + postFix));
        return postFix;
    }

    public Double evaluatePostFix(List<String> postfix) {

        Deque<Double> stack = new ArrayDeque<>();
        char[] operators = {'√', '^', '÷', '×', '+', '-'};

        for (String value : postfix) {
            if (!Arrays.toString(operators).contains(value)) {
                stack.push(Double.parseDouble(value));
            } else {
                if (value.equals("√")) {
                    stack.push(doCalculation(value, stack.pop()));
                } else {
                    Double num2 = stack.pop();
                    Double num1 = 0.00;
                    if (!stack.isEmpty()) {
                        num1 = stack.pop();
                    }
                    stack.push(doCalculation(value, num1, num2));
                }
            }
        }
        return stack.pop();
    }

    public Double doCalculation(String operator, double... num) {

        switch (operator) {
            case "+":
                return add.apply(num[0], num[1]);
            case "-":
                return subtract.apply(num[0], num[1]);
            case "÷":
                return divide.apply(num[0], num[1]);
            case "√":
                return squareRoot.apply(num[0]);
            case "^":
                return square.apply(num[0], num[1]);
            case "×":
                return multiply.apply(num[0], num[1]);
        }
        return 0.0;
    }

    public int getPrecedence(String operator) {

        return switch (operator) {
            case "+", "-" -> 1;
            case "×", "÷" -> 2;
            case "^", "√" -> 3;
            default -> -1;
        };
    }

    public String getOperatorsPresent(String fieldText) {

        char[] operators = {'÷', '×', '+', '-', '(', ')'};
        boolean hasOperator = Arrays.stream(fieldText.split("")).anyMatch(s -> Arrays.toString(operators).contains(s));
        if (hasOperator) {
            return Arrays.stream(fieldText.split("")).filter(s -> Arrays.toString(operators).contains(s)).collect(Collectors.joining());
        }
        return "";
    }


}





