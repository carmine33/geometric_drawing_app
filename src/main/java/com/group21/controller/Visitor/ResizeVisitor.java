/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Visitor;

import com.group21.model.Shape.*;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 *
 * @author Loren
 */
public class ResizeVisitor implements ShapeVisitor{
    private final double mouseX, mouseY;

    public ResizeVisitor(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
      
    @Override
    public void visit(ShapeLine line){
        line.setEndX(mouseX);
        line.setEndY(mouseY);
    }
    
    @Override
    public void visit(ShapeRectangle rectangle) { 
        double newWidth = Math.max(10, mouseX - rectangle.getX());
        double newHeight = Math.max(10, mouseY - rectangle.getY());
        rectangle.setWidth(newWidth);
        rectangle.setHeight(newHeight);
    }
    
    @Override
    public void visit(ShapeEllipse ellipse) {
        double newWidth = Math.max(10, mouseX - ellipse.getX());
        double newHeight = Math.max(10, mouseY - ellipse.getY());
        ellipse.setWidth(newWidth);
        ellipse.setHeight(newHeight);
    }
    
    @Override
    public void visit(ShapeTextBox textbox) {
        double newFontSize = Math.max(8, mouseY - textbox.getY());
        textbox.setFontSize(newFontSize);
    }
    
    @Override
    public void visit(ShapePolygon polygon){
        polygon.storeOriginalVertices();
        polygon.setResizeAnchor(polygon.getBoundingBoxTopLeft());
        Point2D anchor = polygon.getResizeAnchor();
        double anchorX = anchor.getX();
        double anchorY = anchor.getY();
        double initialWidth = polygon.getOriginalBoundingBoxWidth();
        double initialHeight = polygon.getOriginalBoundingBoxHeight();
        if (initialWidth == 0 || initialHeight == 0) return;
        double newWidth = Math.max(10, mouseX - anchorX);
        double newHeight = Math.max(10, mouseY - anchorY);
        double scaleX = Math.max(0.1, newWidth / initialWidth);
        double scaleY = Math.max(0.1, newHeight / initialHeight);
        List<Point2D> scaled = new ArrayList<>();
        for (Point2D pt : polygon.getOriginalVertices()) {
            double dx = pt.getX() - anchorX;
            double dy = pt.getY() - anchorY;
            scaled.add(new Point2D(anchorX + dx * scaleX, anchorY + dy * scaleY));
        }
        polygon.setVertices(scaled);
    }
}
