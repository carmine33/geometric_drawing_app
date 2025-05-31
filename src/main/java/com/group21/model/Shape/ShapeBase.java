/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author Loren
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ShapeRectangle.class, name = "Rectangle"),
  @JsonSubTypes.Type(value = ShapeEllipse.class, name = "Ellipse"),
  @JsonSubTypes.Type(value = ShapeLine.class, name = "Line"),
  @JsonSubTypes.Type(value = ShapePolygon.class, name = "Polygon"),
  @JsonSubTypes.Type(value = ShapeTextBox.class, name = "TextBox")
})
public abstract class ShapeBase implements Shape {
    protected double x, y;
    private double width;
    private double height;
    
    @JsonIgnore
    protected Color fillColor;

    protected double strokeWidth = 1.0;
    
    @JsonIgnore
    protected Color strokeColor;

    protected String type;

    public ShapeBase() {}

    public ShapeBase(double x, double y, double width, double height, Color fillColor, Color strokeColor, double strokeWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth= strokeWidth;
    }

    public double getX() { 
        return x; 
    }
    public double getY() { 
        return y; 
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

    public void setX(double x) { 
        this.x = x; 
    }
    public void setY(double y) { 
        this.y = y; 
    }

    public Color getFillColor() { 
        return fillColor; 
    }
    public Color getStrokeColor() { 
        return strokeColor; 
    }

    public void setFillColor(Color fillColor) { 
        this.fillColor = fillColor; 
    }
    
    public void setStrokeColor(Color strokeColor) { 
        this.strokeColor = strokeColor; 
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public abstract void draw(GraphicsContext gc);
    
    // contains serve per capire se un punto (click del mouse) è all'interno della figura.
    // public abstract boolean contains(double x, double y);   
    
     
    public abstract void translate(double dx, double dy);
    
    public abstract ShapeBase copy();
    
    public abstract List<String> getSupportedActions();
        
    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
    
    // -------------------------------
    // JSON Serialization/Deserialization
    // -------------------------------
    
    // Color serialization as RGBA double arrays for save/load implementation
    @JsonProperty("fillColor")
    public double[] getFillColorArray() {
        if (fillColor == null) return null;
        return new double[]{
            fillColor.getRed(),
            fillColor.getGreen(),
            fillColor.getBlue(),
            fillColor.getOpacity()
        };
    }

    @JsonProperty("fillColor")
    public void setFillColorArray(double[] rgba) {
        if (rgba != null && rgba.length == 4) {
            this.fillColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
    }

    @JsonProperty("strokeColor")
    public double[] getStrokeColorArray() {
        if (strokeColor == null) return null;
        return new double[]{
            strokeColor.getRed(),
            strokeColor.getGreen(),
            strokeColor.getBlue(),
            strokeColor.getOpacity()
        };
    }
    
    @JsonProperty("strokeColor")
    public void setStrokeColorArray(double[] rgba) {
        if (rgba != null && rgba.length == 4) {
            this.strokeColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
    }
}
