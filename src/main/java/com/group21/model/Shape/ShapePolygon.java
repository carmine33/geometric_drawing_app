/*
 
Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license,
Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template*/
package com.group21.model.Shape;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author claco   
*/

public class ShapePolygon extends ShapeBase{

     private List<Point2D> vertices = new ArrayList<>();

    // For serialization/deserialization purposes
    public ShapePolygon(){
        this.type = "Polygon";
    }

    public ShapePolygon(List<Point2D> points, Color fillColor, Color strokeColor, double strokeWidth) {
        this.vertices = points;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.type = "Polygon";
    }

    public List<Point2D> getVertices() {
        return vertices;
    }

    public void setVertices(List<Point2D> vertices) {
        this.vertices = vertices;
    }

    public void addVertex(Point2D p) {
        vertices.add(p);
    }

    //TODO
    @Override
    public void draw(GraphicsContext gc) {
        int count = vertices.size();
        if (count < 2) return;

        gc.setLineWidth(strokeWidth);
        if (strokeColor != null) gc.setStroke(strokeColor);
        if (fillColor != null) gc.setFill(fillColor);

                double[] xPoints = new double[count];
        double[] yPoints = new double[count];
        for (int i = 0; i < count; i++) {
            xPoints[i] = vertices.get(i).getX();
            yPoints[i] = vertices.get(i).getY();
        }

        if (count == 2) {
            gc.strokeLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
        } else {
            if (fillColor != null) gc.fillPolygon(xPoints, yPoints, count);
            gc.strokePolygon(xPoints, yPoints, count);
        }
    }

     @Override
    public ShapeBase copy() {
    ShapePolygon copy = new ShapePolygon(vertices, fillColor, strokeColor,strokeWidth);
    copy.setStrokeWidth(this.strokeWidth); // <-- copia strokeWidth
    copy.setFillColor(this.getFillColor());
    copy.setStrokeColor(this.getStrokeColor());    
    return copy;
    } 

    @JsonProperty("vertices")
    public double[][] getVerticesAsArray() {
        double[][] arr = new double[vertices.size()][2];
        for (int i = 0; i < vertices.size(); i++) {
            arr[i][0] = vertices.get(i).getX();
            arr[i][1] = vertices.get(i).getY();
        }
        return arr;
    }

    @JsonProperty("vertices")
    public void setVerticesFromArray(double[][] arr) {
        vertices = new ArrayList<>();
        for (double[] pair : arr) {
            if (pair.length == 2) {
                vertices.add(new Point2D(pair[0], pair[1]));
            }
        }
    }

    // TODO
    @Override
    public boolean containsPoint(double x, double y) {
        return false;
    }
}