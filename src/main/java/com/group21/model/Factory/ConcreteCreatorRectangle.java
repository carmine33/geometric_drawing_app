/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Factory;

import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeRectangle;
import javafx.scene.paint.Color;

/**
 *
 * @author matte
 */
public class ConcreteCreatorRectangle extends Creator {

    @Override
    public ShapeBase createShape(double x, double y, Color strokeColor, Color fillColor) {
        return new ShapeRectangle(x, y, 0, 0, fillColor, strokeColor, 1.0);
    }

    @Override
    public ShapeBase createShape(double x, double y, double width, double height,
                                 Color strokeColor, Color fillColor) {
        ShapeRectangle rect = new ShapeRectangle(x, y, width, height, fillColor, strokeColor, 1.0);
        return rect;
    }
}
