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
import com.group21.model.Factory.ConcreteCreatorLine;
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeLine;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class LineTool implements DrawingToolStrategy {
    private List<ShapeBase> shapes;
    private double startX, startY;
    private Color strokeColor;
    private Runnable onUpdateCallback;
    private ShapeBase previewShape;
    private double offsetX, offsetY, zoomFactor = 1.0;
    private ShapeSelector selectShape;


    public LineTool(List<ShapeBase> shapes, Color strokeColor, ShapeSelector selectShape, Runnable onUpdateCallback) {
        this.shapes = shapes;
        this.strokeColor = strokeColor;
        this.onUpdateCallback = onUpdateCallback;
        this.selectShape = selectShape;
    }

@Override
public void onMousePressed(MouseEvent e) {
    startX = (e.getX() / zoomFactor) + offsetX;
    startY = (e.getY() / zoomFactor) + offsetY;
}


    @Override
    public void onMouseDragged(MouseEvent e) {
       double endX = (e.getX() / zoomFactor) + offsetX;
       double endY = (e.getY() / zoomFactor) + offsetY;
        previewShape = new ConcreteCreatorLine().createShape(startX, startY, endX, endY, strokeColor);
        onUpdateCallback.run();  // Chiama redraw() nel controller
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        double endX = (e.getX() / zoomFactor) + offsetX;
        double endY = (e.getY() / zoomFactor) + offsetY;
        ShapeBase finalShape = new ConcreteCreatorLine().createShape(startX, startY, endX, endY, strokeColor);
        selectShape.getMemory().saveState(new ArrayList<>(shapes));
        shapes.add(finalShape);
        previewShape = null;
        onUpdateCallback.run();
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



