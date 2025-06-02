/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author Loren
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.geom.Ellipse2D;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeEllipse extends ShapeBase{
        
    private Ellipse2D ellipse;
    
    // For serialization/deserialization purposes
    public ShapeEllipse() {
        this.type = "Ellipse";
    }
    
    public ShapeEllipse(double x, double y, double width, double height, Color fillColor, Color strokeColor, double strokeWidth){
        super(x, y, width, height, fillColor, strokeColor,strokeWidth);
        this.type = "Ellipse";
        this.ellipse = new Ellipse2D.Double();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(fillColor);
        gc.fillOval(x, y, this.getWidth(), this.getHeight());
        gc.setLineWidth(strokeWidth);
        gc.setStroke(strokeColor);
        gc.strokeOval(x, y, this.getWidth(), this.getHeight());
    }
    
    @Override
    public boolean containsPoint(double x, double y) {
        return x >= this.x && x <= this.x + this.getWidth() &&
        y >= this.y && y <= this.y + this.getHeight();
    }
    
    @Override
    public ShapeBase copy() {
        ShapeEllipse copy = new ShapeEllipse(x, y, getWidth(), getHeight(), fillColor, strokeColor,strokeWidth);
        copy.setStrokeWidth(this.strokeWidth);
        copy.setFillColor(this.getFillColor());
        copy.setStrokeColor(this.getStrokeColor());
        return copy;
    }

    @Override
    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }
    
    @JsonIgnore
    @Override
public List<String> getSupportedActions() {
    return List.of("delete", "copy","paste", "toFront", "toBack", "fillColor", "strokeColor", "strokeWidth");
}



}