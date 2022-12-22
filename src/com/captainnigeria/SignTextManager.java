package com.captainnigeria;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class SignTextManager {


    public static String getSquareRootText(String formattedText){
        return formattedText + "√(";
    }

    public static String getPowerText(String formattedText){
        return formattedText + "^(";
    }

    public static String getSquaredText(String formattedText){
        return formattedText + "^(2)";
    }

    public static String getBasicOperatorText(String formattedText, String operator) {

        String[] operators = {"(", ")", "x²", "^", "√", "÷", "×", "+", "-"};

        if (Arrays.stream(operators).anyMatch(formattedText::endsWith) || formattedText.endsWith(".")) {
            if (formattedText.endsWith("(")) {
                return formattedText;
            }
            if (formattedText.endsWith(")")) {
                return formattedText + operator;
            }
            return formattedText.substring(0, formattedText.length() - 1) + operator;
        }
        return formattedText + operator;
    }

    public static String getNegationText(String text, boolean negation, Calculator main) {
        String[] operators = {"(", "x²", "xʏ", "√", "÷", "×", "+", "-"};

        if (negation) {
            if (text.length() == 0 || Arrays.stream(operators).anyMatch(text::endsWith)) {
                return text + "(-";
            }
            if (main.getOperatorsPresent(text).equals("")) {
                return "(-" + text;
            }
            return "(-(" + text + "))";
        } else {
            if (main.getOperatorsPresent(text).equals("(-")) {
                System.out.println(main.getOperatorsPresent(text).equals("(-"));
                return text.substring(2);
            }
            if (!text.endsWith(")")) {
                return text.substring(0, text.length() - 2);
            }
            return text.substring(3, text.length() - 2);
        }
    }

    public static String getBracketOperationText(String formattedText) {

        String[] mainOperators = {"x²", "xʏ", "√", "÷", "×", "+", "-"};

        int leftBracketCount = (int) Arrays.stream(formattedText.split(""))
                .filter(s -> s.equals("(")).count();

        int rightBracketCount = (int) Arrays.stream(formattedText.split(""))
                .filter(s -> s.equals(")")).count();

        if (formattedText.endsWith(")") && rightBracketCount == leftBracketCount) {
            return formattedText;
        }

        if ((Arrays.stream(mainOperators).anyMatch(formattedText::endsWith) || formattedText.endsWith("("))) {
            return formattedText + "(";
        } else {
            if (formattedText.length() == 0 && leftBracketCount == rightBracketCount) {
                return formattedText + "(";
            }

            if (leftBracketCount > rightBracketCount && !formattedText.endsWith("(")) {
                return formattedText + ")";
            }
        }
        return formattedText;
    }


    public static String getDotText(JLabel label, String buttonText) {

        String[] operators = {"÷", "×", "+", "-"};
        boolean lastCharacterIsADot = label.getText().endsWith(".");

        boolean lastCharacterIsADigit = Arrays.stream(NumberConstants.values()).map(NumberConstants::getValue)
                .anyMatch((t) -> label.getText().endsWith(t));

        int numberOfDots = (int) Stream.of(label.getText().split("")).
                filter(s -> Objects.equals(s, ".")).count();

        int operatorCount = (int) Stream.of(label.getText().split("")).
                filter(s -> Arrays.toString(operators).contains(s)).count();

        boolean enoughDots = (numberOfDots - operatorCount) < 1;

        if (label.getText().length() == 0) {
            return buttonText;
        } else if (!lastCharacterIsADigit && enoughDots && operatorCount > 0) {
            return label.getText() + buttonText;
        } else if (!lastCharacterIsADot && (enoughDots)) {
            return label.getText() + buttonText;
        }
        return label.getText();
    }

}
