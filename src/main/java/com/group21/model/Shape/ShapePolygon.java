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
     private List<Point2D> originalVertices = new ArrayList<>();
     private Point2D resizeAnchor;

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

    @Override
    public boolean containsPoint(double x, double y) {
        int crossings = 0;
        int count = vertices.size();

        for (int i = 0; i < count; i++) {
            Point2D a = vertices.get(i);
            Point2D b = vertices.get((i + 1) % count);

            // Check if the point is between the y-coordinates of the edge
            if ((a.getY() > y) != (b.getY() > y)) {
                double atX = (b.getX() - a.getX()) * (y - a.getY()) / (b.getY() - a.getY()) + a.getX();
                if (x < atX) {
                    crossings++;
                }
            }
        }

        // Odd number of crossings means point is inside
        return (crossings % 2 == 1);
    }
    
    
    public void storeOriginalVertices() {
        originalVertices.clear();
        for (Point2D pt : vertices) {
            originalVertices.add(new Point2D(pt.getX(), pt.getY()));
        }
    }

    public List<Point2D> getOriginalVertices() {
        return originalVertices;
    }

    public void setOriginalVertices(List<Point2D> originalVertices) {
        this.originalVertices = originalVertices;
    }

    public void setResizeAnchor(Point2D anchor) {
        this.resizeAnchor = anchor;
    }

    public Point2D getResizeAnchor() {
        return this.resizeAnchor;
    }

    public Point2D getBoundingBoxTopLeft() {
        double minX = vertices.stream().mapToDouble(Point2D::getX).min().orElse(0);
        double minY = vertices.stream().mapToDouble(Point2D::getY).min().orElse(0);
        return new Point2D(minX, minY);
    }

    public double getOriginalBoundingBoxWidth() {
        double minX = originalVertices.stream().mapToDouble(Point2D::getX).min().orElse(0);
        double maxX = originalVertices.stream().mapToDouble(Point2D::getX).max().orElse(0);
        return maxX - minX;
    }

    public double getOriginalBoundingBoxHeight() {
        double minY = originalVertices.stream().mapToDouble(Point2D::getY).min().orElse(0);
        double maxY = originalVertices.stream().mapToDouble(Point2D::getY).max().orElse(0);
        return maxY - minY;
    }

}