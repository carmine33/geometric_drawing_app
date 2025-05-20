/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Loren
 */
public class ShapeLine extends ShapeBase{    
    private double endX;
    private double endY;
    
    // For serialization/deserialization purposes
    public ShapeLine() {
        this.type = "Line";
    }
    
    public ShapeLine(double startX, double startY, double endX, double endY, Color strokeColor) {
        super(startX, startY, null, strokeColor); // fillColor is unused (null)
        this.endX = endX;
        this.endY = endY;
        this.type = "Line";
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(strokeColor);
        gc.strokeLine(x, y, endX, endY);
    }
    
}