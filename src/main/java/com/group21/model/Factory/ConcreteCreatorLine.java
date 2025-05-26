/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Factory;

import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeLine;
import javafx.scene.paint.Color;

/**
 *
 * @author matte
 */
public class ConcreteCreatorLine extends Creator {

    @Override
    public ShapeBase createShape(double x, double y, Color strokeColor, Color fillColor) {
        return new ShapeLine(x, y, 0, 0, x, y, strokeColor, 1.0);
    }

    public ShapeBase createShape(double startX, double startY, double endX, double endY, Color strokeColor) {
        return new ShapeLine(startX, startY, 0, 0, endX, endY, strokeColor, 1.0);
    }
}