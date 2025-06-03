/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Visitor;

import com.group21.model.Shape.*;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

/**
 *
 * @author Loren
 */
public class SelectionOutlineVisitor implements ShapeVisitor{
    
    private final GraphicsContext gc;

    public SelectionOutlineVisitor(GraphicsContext gc) {
        this.gc = gc;
    }
    
    public void visit(ShapeRectangle rectangle){
        gc.setLineWidth(2.0);
        gc.strokeRect(rectangle.getX() - 5, rectangle.getY() - 5, rectangle.getWidth() + 10, rectangle.getHeight() + 10);
    }
    
    public void visit(ShapeEllipse ellipse){
        gc.setLineWidth(2.0);
        gc.strokeOval(ellipse.getX() - 5, ellipse.getY() - 5, ellipse.getWidth() + 10, ellipse.getHeight() + 10);
    }
    
    public void visit(ShapeLine line){
        gc.setLineWidth(2.0);
        gc.strokeLine(line.getX(), line.getY(), line.getEndX(), line.getEndY());
    }
    
    public void visit(ShapePolygon polygon){
        List<Point2D> points = polygon.getVertices();
        Coordinate[] coords = points.stream()
            .map(p -> new Coordinate(p.getX(), p.getY()))
            .toArray(Coordinate[]::new);
        coords = Arrays.copyOf(coords, coords.length + 1);
        coords[coords.length - 1] = coords[0];
        GeometryFactory gf = new GeometryFactory();
        LinearRing ring = gf.createLinearRing(coords);
        Polygon jtsPoly = gf.createPolygon(ring);
        Geometry outline = jtsPoly.buffer(5.0);
        Coordinate[] offsetCoords = outline.getCoordinates();
        gc.beginPath();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2.0);
        gc.moveTo(offsetCoords[0].x, offsetCoords[0].y);
        for (int i = 1; i < offsetCoords.length; i++) {
            gc.lineTo(offsetCoords[i].x, offsetCoords[i].y);
        }
        gc.stroke();
    }
    
    public void visit(ShapeTextBox textBox){
        double w = textBox.getTextWidth() + 11;
        double h = textBox.getTextHeight() + 6;
        gc.strokeRect(textBox.getX(), textBox.getY(), w, h);
    }
}
