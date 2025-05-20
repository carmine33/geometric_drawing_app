/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author Loren
 */

import java.awt.geom.Ellipse2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeEllipse extends ShapeBase{
    
    private double width;
    private double height;    
    private Ellipse2D ellipse;
    
    // For serialization/deserialization purposes
    public ShapeEllipse() {
        this.type = "Ellipse";
    }
    
    public ShapeEllipse(double x, double y, double width, double height, Color fillColor, Color strokeColor){
        super(x, y, fillColor, strokeColor);
        this.width = width;
        this.height = height;
        this.type = "Ellipse";
        this.ellipse = new Ellipse2D.Double();
    }

    public double getWidht() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setWidht(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(fillColor);
        gc.fillOval(x, y, width, height);

        gc.setStroke(strokeColor);
        gc.strokeOval(x, y, width, height);
    }
    
    @Override
    public boolean containsPoint(double x, double y) {
        return ellipse.contains(x, y);
    }
}