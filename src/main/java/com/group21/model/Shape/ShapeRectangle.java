/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

import java.awt.geom.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Loren
 */
public class ShapeRectangle extends ShapeBase{    
    private double width = 100;
    private double height = 60;
    private Rectangle2D rectangle = null;
    
    // For serialization/deserialization purposes
    public ShapeRectangle() {
        this.type = "Rectangle";
    }
    
    public ShapeRectangle(double x, double y, double width, double height, Color fillColor, Color strokeColor)
    {
        super(x, y, fillColor, strokeColor);
        this.height = height;
        this.width = width;
        this.type = "Rectangle";
        this.rectangle = new Rectangle2D.Double();
    }    

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(fillColor);
        gc.fillRect(x, y, width, height);

        gc.setStroke(strokeColor);
        gc.strokeRect(x, y, width, height);
    }
    
    @Override
    public boolean containsPoint(double x, double y) {
        return rectangle.contains(x, y);
    }
}
