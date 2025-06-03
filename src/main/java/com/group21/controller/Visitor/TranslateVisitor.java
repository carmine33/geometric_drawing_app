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
public class TranslateVisitor implements ShapeVisitor{
    private final double dx, dy;
    
    public TranslateVisitor(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    @Override
    public void visit(ShapeLine line){
        line.setX(line.getX()+dx);
        line.setY(line.getY()+dy);
        line.setEndX(line.getEndX()+dx);
        line.setEndY(line.getEndY()+dy);
    }
    
    @Override
    public void visit(ShapeRectangle rectangle){
        rectangle.setX(rectangle.getX()+dx);
        rectangle.setY(rectangle.getY()+dy);
    }
    
    @Override
    public void visit(ShapeEllipse ellipse){
        ellipse.setX(ellipse.getX()+dx);
        ellipse.setY(ellipse.getY()+dy);
    }
    
    @Override
    public void visit(ShapeTextBox textbox){
        textbox.setX(textbox.getX()+dx);
        textbox.setY(textbox.getY()+dy);
    }
    
    @Override
    public void visit(ShapePolygon polygon){
        List<Point2D> movedVertices = new ArrayList<>();
        for (Point2D pt : polygon.getVertices()) {
            movedVertices.add(new Point2D(pt.getX() + dx, pt.getY() + dy));
        }
        polygon.setVertices(movedVertices);
    }
}