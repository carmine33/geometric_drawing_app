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
import com.group21.model.Factory.ConcreteCreatorIrregularPolygon;
import com.group21.model.Shape.ShapePolygon;
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeLine;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PolygonTool implements DrawingToolStrategy {
    private final List<ShapeBase> shapes;
    private final List<Point2D> polygonPoints = new ArrayList<>();
    private final Color strokeColor, fillColor;
    private final Runnable redrawCallback;

    private double currentMouseX = -1;
    private double currentMouseY = -1;
    private double offsetX = 0, offsetY = 0, zoomFactor = 1.0;
    private ShapeSelector selectShape;

    public PolygonTool(List<ShapeBase> shapes, Color strokeColor, Color fillColor,ShapeSelector selectShape, Runnable redrawCallback) {
        this.shapes = shapes;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.redrawCallback = redrawCallback;
        this.selectShape = selectShape;
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        double x = (e.getX() / zoomFactor) + offsetX;
        double y = (e.getY() / zoomFactor) + offsetY;
        if (e.isPrimaryButtonDown()) {
            polygonPoints.add(new Point2D(x, y));
        } else if (e.isSecondaryButtonDown()) {
            if (polygonPoints.size() == 1) {   
            } else if (polygonPoints.size() == 2) {
                Point2D p1 = polygonPoints.get(0);
                Point2D p2 = polygonPoints.get(1);
                ShapeLine line = new ShapeLine(p1.getX(), p1.getY(), 0, 0,
                        p2.getX(), p2.getY(), strokeColor, 1);
                             
                        selectShape.addShape(line);
            } else if (polygonPoints.size() >= 3) {
                ShapePolygon polygon = new ShapePolygon(new ArrayList<>(polygonPoints), fillColor, strokeColor, 1);
                selectShape.saveState();       
                selectShape.addShape(polygon);
            }
            polygonPoints.clear();
        }

        if (redrawCallback != null) redrawCallback.run();
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        // Optional: puoi usare anche questo per l’anteprima mentre trascini
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        // niente da fare qui
    }

    @Override
    public ShapeBase getPreviewShape() {
        if (polygonPoints.isEmpty() || currentMouseX < 0 || currentMouseY < 0) return null;

        List<Point2D> temp = new ArrayList<>(polygonPoints);
        temp.add(new Point2D(currentMouseX, currentMouseY));
        return new ShapePolygon(temp, fillColor, strokeColor, 1);
    }
    
       @Override
    public void onMouseMoved(MouseEvent e) {
        /*if (polygonPoints.size() >= 1) {
            double scrollXOffset = scrollPane.getHvalue() *
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() *
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            // Salva la posizione del mouse in coordinate del canvas
            currentMouseX = (e.getX() / zoomFactor) + scrollXOffset;
            currentMouseY = (e.getY() / zoomFactor) + scrollYOffset;

            // Redraw mostrerà anche il segmento corrente
            redraw(baseCanvas.getGc());
        }*/
           currentMouseX = (e.getX() / zoomFactor) + offsetX;
           currentMouseY = (e.getY() / zoomFactor) + offsetY;
           if (redrawCallback != null) redrawCallback.run();  // forza il redraw con anteprima
    }

    @Override
    public void setOffset(double offsetX, double offsetY, double zoomFactor) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.zoomFactor = zoomFactor;
    }
}


