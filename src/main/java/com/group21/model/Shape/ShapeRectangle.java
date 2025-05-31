/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

import java.awt.geom.Rectangle2D;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Loren
 */
    public class ShapeRectangle extends ShapeBase{    

    private Rectangle2D rectangle = null;
    
    // For serialization/deserialization purposes
    public ShapeRectangle() {
        this.type = "Rectangle";
    }
    
    public ShapeRectangle(double x, double y, double width, double height, Color fillColor, Color strokeColor, double strokeWidth)
    {
        super(x, y, width, height, fillColor, strokeColor,strokeWidth);
        this.type = "Rectangle";
        this.rectangle = new Rectangle2D.Double();
    }    
       
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(fillColor);
        gc.fillRect(x, y, this.getWidth(), this.getHeight());
        gc.setLineWidth(strokeWidth); 
        gc.setStroke(strokeColor);
        gc.strokeRect(x, y, this.getWidth(), this.getHeight());
    }
    
    @Override
    public boolean containsPoint(double x, double y) {
        return x >= this.x && x <= this.x + this.getWidth() &&
        y >= this.y && y <= this.y + this.getHeight();
    }
    
    
    @Override
    public ShapeBase copy() {
    ShapeRectangle copy = new ShapeRectangle(x, y, this.getWidth(), this.getHeight(), fillColor, strokeColor,strokeWidth);
    copy.setStrokeWidth(this.strokeWidth); // <-- copia strokeWidth
    copy.setFillColor(this.getFillColor());
    copy.setStrokeColor(this.getStrokeColor());    
    return copy;
    } 

    @Override
    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
public List<String> getSupportedActions() {
    return List.of("delete", "copy", "paste", "toFront", "toBack", "fillColor", "strokeColor", "strokeWidth");
}


}
