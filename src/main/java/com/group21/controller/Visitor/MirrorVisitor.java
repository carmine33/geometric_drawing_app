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
public class MirrorVisitor implements ShapeVisitor{
   
    private final boolean mirrorHorizontally;
    private final boolean mirrorVertically;
    private final double centerX;
    private final double centerY;

    public MirrorVisitor(boolean mirrorHorizontally, boolean mirrorVertically, double centerX, double centerY) {
        this.mirrorHorizontally = mirrorHorizontally;
        this.mirrorVertically = mirrorVertically;
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    public void visit(ShapeRectangle rectangle){
        double x = rectangle.getX();
        double y = rectangle.getY();
        double width = rectangle.getWidth();
        double height = rectangle.getHeight();

        if (mirrorHorizontally) {
            x = (x + width);
        }
        if (mirrorVertically) {
            y = (y + height);
        }

        rectangle.setX(x);
        rectangle.setY(y);
    }
    
    public void visit(ShapeEllipse ellipse){
        double x = ellipse.getX();
        double y = ellipse.getY();
        double width = ellipse.getWidth();
        double height = ellipse.getHeight();

        if (mirrorHorizontally) {
            x = x + width;
        }
        if (mirrorVertically) {
            y = y + height;
        }

        ellipse.setX(x);
        ellipse.setY(y);
    }
    
    public void visit(ShapeLine line){
        double x1 = line.getX();
        //double y1 = line.getY();
        double x2 = line.getEndX();
        //double y2 = line.getEndY();

        if (mirrorHorizontally) {
            double originalWidth = x2 - x1;
            x1 = x2;
            x2 = x2 + originalWidth;
        }

        line.setX(x1);
        line.setEndX(x2);
    }
    
    public void visit(ShapePolygon polygon){
        List<Point2D> mirrored = new ArrayList<>();
        for (Point2D pt : polygon.getVertices()) {
            double newX = pt.getX();
            double newY = pt.getY();
            if (mirrorHorizontally) {
                newX = 2 * centerX - pt.getX();
            }
            if (mirrorVertically) {
                newY = 2 * centerY - pt.getY();
            }
            mirrored.add(new Point2D(newX, newY));
        }
        polygon.setVertices(mirrored);
    }
    
    public void visit(ShapeTextBox textBox){
        double x = textBox.getX();
        double y = textBox.getY();
        double width = textBox.getWidth();
        double height = textBox.getHeight();

        if (mirrorHorizontally) {
            x = x + width;
        }
        if (mirrorVertically) {
            y = y + height;
        }

        textBox.setX(x);
        textBox.setY(y);
    }
}
