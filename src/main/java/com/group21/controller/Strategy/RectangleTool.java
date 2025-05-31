/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Strategy;

/**
 *
 * @author carmi
 */

import com.group21.model.Factory.ConcreteCreatorRectangle;
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeRectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class RectangleTool implements DrawingToolStrategy {
    private List<ShapeBase> shapes;
    private Color strokeColor, fillColor;
    private double startX, startY;
    private Runnable redrawCallback;
    private ShapeBase previewShape;
    private double offsetX = 0, offsetY = 0, zoomFactor = 1.0;


    public RectangleTool(List<ShapeBase> shapes, Color strokeColor, Color fillColor, Runnable redrawCallback) {
        this.shapes = shapes;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.redrawCallback = redrawCallback;
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        startX = (e.getX() / zoomFactor) + offsetX;
        startY = (e.getY() / zoomFactor) + offsetY;
        previewShape = null;
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        double endX = (e.getX() / zoomFactor) + offsetX;
        double endY = (e.getY() / zoomFactor) + offsetY;
        double x = Math.min(startX, endX);
        double y = Math.min(startY, endY);
        double width = Math.abs(endX - startX);
        double height = Math.abs(endY - startY);

        previewShape = new ConcreteCreatorRectangle().createShape(x, y, width, height, strokeColor, fillColor);
        if (redrawCallback != null) redrawCallback.run();
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        double endX = (e.getX() / zoomFactor) + offsetX;
        double endY = (e.getY() / zoomFactor) + offsetY;
        double x = Math.min(startX, endX);
        double y = Math.min(startY, endY);
        double width = Math.abs(endX - startX);
        double height = Math.abs(endY - startY);

        if (width == 0) width = 100;
        if (height == 0) height = 40;

        ShapeBase finalShape = new ConcreteCreatorRectangle().createShape(x, y, width, height, strokeColor, fillColor);
        shapes.add(finalShape);

        previewShape = null;
        if (redrawCallback != null) redrawCallback.run();
    }

    public ShapeBase getPreviewShape() {
        return previewShape;
    }
    
       @Override
    public void onMouseMoved(MouseEvent e) {
        
    }
    
    @Override
public void setOffset(double offsetX, double offsetY, double zoomFactor) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.zoomFactor = zoomFactor;
}

}



