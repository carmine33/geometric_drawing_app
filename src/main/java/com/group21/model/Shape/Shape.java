/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author Loren
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface Shape {
    double getX();
    double getY();
    void setX(double x);
    void setY(double y);

    Color getFillColor();
    void setFillColor(Color fillColor);

    Color getStrokeColor();
    void setStrokeColor(Color strokeColor);

    String getType();
    void setType(String type);

    void draw(GraphicsContext gc);
}
