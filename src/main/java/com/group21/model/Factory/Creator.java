/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Factory;

import com.group21.model.Shape.ShapeBase;
import javafx.scene.paint.Color;

/**
 *
 * @author matte
 */
public abstract class Creator {

    public abstract ShapeBase createShape(double x, double y, Color strokeColor, Color fillColor);

    public ShapeBase createShape(double x, double y, double width, double height,
                                 Color strokeColor, Color fillColor) {
        throw new UnsupportedOperationException("This shape does not support size or rotation");
    }

    public ShapeBase createShape(double x, double y, double width, double height, String text,
                                 Color strokeColor, Color fillColor) {
        throw new UnsupportedOperationException("This shape does not support text");
    }
}

