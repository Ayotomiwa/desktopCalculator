package com.captainnigeria;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;



public class ButtonOperationManager {

    public static void equationOperation(CalculatorScreen equationLabel, CalculatorScreen resultLabel, Calculator main){
        String[] invalidOperators = {"(", "÷", "×", "+", "-", "."};
        String screenText = equationLabel.getText();

        boolean lastDigitIsInvalid = Arrays.stream(invalidOperators).anyMatch((s) -> equationLabel.getText().endsWith(s));

        int leftBracketCount = (int) Arrays.stream(screenText.split(""))
                .filter(s -> s.equals("(")).count();

        int rightBracketCount = (int) Arrays.stream(screenText.split(""))
                .filter(s -> s.equals(")")).count();

        boolean bracketOpen = leftBracketCount > rightBracketCount;

        if (lastDigitIsInvalid || bracketOpen) {
            equationLabel.setForeground(Color.red.brighter());
            return;
        }
        if (equationLabel.getText().length() == 0) {
            return;
        }

        String result = main.getResult(equationLabel.getText());

        if (result.equals("Invalid Expression")) {
            equationLabel.setForeground(Color.red.darker());
            return;
        }
        equationLabel.setForeground(Color.GREEN.darker());
        System.out.println("Formatted- " + main.getFormattedText(equationLabel.getText()));
        equationLabel.setText(main.getFormattedText(equationLabel.getText()));
        resultLabel.setText(result);
    }

    public static void clearOperation(JLabel equationLabel, JLabel resultLabel) {
        resultLabel.setText("0");
        equationLabel.setText("");
    }

    public static void deleteOperation(JLabel equationLabel) {
        if (!equationLabel.getText().equals("")) {
            if (equationLabel.getText().endsWith("√(") || equationLabel.getText().endsWith("^(")) {
                equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 2));
                return;
            }
            equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 1));
        }
    }

    public static void numberOperation(CalculatorScreen label, Calculator main, String buttonText){
        if (!label.getForeground().equals(CalculatorScreen.getColor())) {
            label.setToOriginalColor();
        }

        if (buttonText.equals(".")) {
            label.setText(SignTextManager.getDotText(label, buttonText));
            return;
        }
        if (buttonText.equals("±")) {
            boolean negation = (!(label.getText().startsWith("(-") && label.getText().endsWith((")")) ||
                    label.getText().endsWith("(-")
                    || label.getText().startsWith("(-") && main.getOperatorsPresent(label.getText()).equals("(-")));
            label.setText(SignTextManager.getNegationText(label.getText(), negation, main));
            return;
        }
        label.setText(label.getText() + buttonText);
    }


    public static void signOperation(JLabel textField, String finalOperator, Calculator main) {

        String currentText = textField.getText();
        String[] allowedFirstOperators = {"()", "√"};

        if (currentText.length() == 0 && !Arrays.toString(allowedFirstOperators).contains(finalOperator)) {
            System.out.println(false);
            textField.setText(currentText);
            return;
        }

        String formattedText = currentText;
        boolean numberOfDots = Arrays.asList(textField.getText().split("")).contains(".");

        if (numberOfDots) {
            formattedText = main.getFormattedText(currentText);
        }

        textField.setText(getSignText(finalOperator, formattedText));
    }



    public static String getSignText(String operator, String formattedText) {

        switch (operator) {
            case "()":
                return SignTextManager.getBracketOperationText(formattedText);
            case "√":
                return SignTextManager.getSquareRootText(formattedText);
            case "^":
                return SignTextManager.getPowerText(formattedText);
            case "x²":
                return SignTextManager.getSquaredText(formattedText);
            default:
                return SignTextManager.getBasicOperatorText(formattedText, operator);
        }
    }

}
