/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Factory;

import com.group21.model.Shape.ShapeTextBox;
import javafx.scene.paint.Color;

/**
 *
 * @author matte
 */
public class ConcreteCreatorText extends Creator {

    @Override
    public ShapeTextBox createShape(double x, double y, Color strokeColor, Color fillColor) {
        return new ShapeTextBox(x, y, 0, 0, fillColor, strokeColor, 1.0, "Text");
    }

    // Metodo extra con testo e fontSize
    public ShapeTextBox createShape(double x, double y, Color strokeColor, Color fillColor, String text, double fontSize) {
        ShapeTextBox box = new ShapeTextBox(x, y, 0, 0, fillColor, strokeColor, 1.0, text);
        box.setFontSize(fontSize);
        return box;
    }
}
