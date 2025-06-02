/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Strategy;

/**
 *
 * @author carmi
 */

import com.group21.model.Command.ShapeSelector;
import com.group21.model.Factory.ConcreteCreatorEllipse;
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeEllipse;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class EllipseTool implements DrawingToolStrategy {
    private final List<ShapeBase> shapes;
    private final Color strokeColor, fillColor;
    private final Runnable redrawCallback;
    private double startX, startY;
    private ShapeBase previewShape;
    private double offsetX = 0, offsetY = 0, zoomFactor = 1.0;
    private ShapeSelector selectShape;

    public EllipseTool(List<ShapeBase> shapes, Color strokeColor, Color fillColor,ShapeSelector selectShape,  Runnable redrawCallback) {
        this.shapes = shapes;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.redrawCallback = redrawCallback;
        this.selectShape = selectShape;
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
        double w = Math.abs(endX - startX);
        double h = Math.abs(endY - startY);

        previewShape = new ConcreteCreatorEllipse().createShape(x, y, w, h, strokeColor, fillColor);

        if (redrawCallback != null) redrawCallback.run();
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        double endX = (e.getX() / zoomFactor) + offsetX;
        double endY = (e.getY() / zoomFactor) + offsetY;

        double x = Math.min(startX, endX);
        double y = Math.min(startY, endY);
        double w = Math.abs(endX - startX);
        double h = Math.abs(endY - startY);

        if (w == 0) w = 100;
        if (h == 0) h = 40;

        ShapeBase ellipse = new ConcreteCreatorEllipse().createShape(x, y, w, h, strokeColor, fillColor);
        selectShape.getMemory().saveState(new ArrayList<>(shapes));
        shapes.add(ellipse);

        previewShape = null;
        if (redrawCallback != null) redrawCallback.run();
    }

    @Override
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
