/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

import java.awt.geom.Line2D;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Loren
 */
public class ShapeLine extends ShapeBase{    
    private double endX;
    private double endY;
    private Line2D line;
    
    // For serialization/deserialization purposes
    public ShapeLine() {
        this.type = "Line";
    }
    
    public ShapeLine(double startX, double startY, double width, double height, double endX, double endY, Color strokeColor, double strokeWidth) {
        super(startX, startY, width, height, null, strokeColor, strokeWidth); // fillColor is unused (null)
        this.endX = endX;
        this.endY = endY;
        this.type = "Line";
        this.line = new Line2D.Double();
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
        gc.setLineWidth(strokeWidth);
        gc.strokeLine(x, y, endX, endY);
    }
    
    // Check if the point (x,y) intersects a small area around the line (8x8 pixels)
    @Override
    public boolean containsPoint(double x, double y) {
        if (line == null) {
            line = new Line2D.Double();
        }
        line.setLine(this.x, this.y, this.endX, this.endY);
        return line.intersects(x - 4, y - 4, 8.0, 8.0);
    }
    
    @Override
    public ShapeBase copy() {
        ShapeLine copy = new ShapeLine(x, y, getWidth(), getHeight(),endX,endY,strokeColor,strokeWidth);
        copy.setStrokeWidth(this.strokeWidth);
        copy.setFillColor(this.getFillColor());
        return copy;
    }

    @Override
    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        this.endX += dx;
        this.endY += dy;
    }
    
}