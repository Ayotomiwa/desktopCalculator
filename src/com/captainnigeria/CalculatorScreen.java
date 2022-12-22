package com.captainnigeria;

import javax.swing.*;
import java.awt.*;

public class CalculatorScreen extends JLabel {

    private final static int alignment = SwingConstants.RIGHT;
    private final static Color color = Color.green.darker();


    public CalculatorScreen(String buttonName, String buttonText, float fontSize){
        super(buttonText, alignment);
        this.setFont(this.getFont().deriveFont(fontSize));
        setName(buttonName);
    }

    public static Color getColor() {
        return color;
    }

    public void setToOriginalColor(){
        setForeground(color);
    }
}
