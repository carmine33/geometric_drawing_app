/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Command;

import com.group21.model.Decorator.BaseCanvas;
import com.group21.model.Decorator.GridDecorator;
import com.group21.model.Shape.ShapeBase;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;
import java.util.List;
/**
 *
 * @author carmi
 */


public class CanvasManager {
    private final BaseCanvas baseCanvas;
    private final List<ShapeBase> shapes;
    private final ScrollPane scrollPane;
    private final GridDecorator gridDecorator;

    private final double[] zoomLevels = {0.25, 0.5, 1.0, 1.5, 1.75};
    private int currentZoomIndex = 2;
    private boolean gridVisible = false;

    public CanvasManager(BaseCanvas baseCanvas, List<ShapeBase> shapes, ScrollPane scrollPane) {
        this.baseCanvas = baseCanvas;
        this.shapes = shapes;
        this.scrollPane = scrollPane;
        this.gridDecorator = new GridDecorator(baseCanvas);
    }

    public void zoomIn() {
        if (currentZoomIndex < zoomLevels.length - 1) {
            currentZoomIndex++;
            redraw();
        }
    }

    public void zoomOut() {
        if (currentZoomIndex > 0) {
            currentZoomIndex--;
            redraw();
        }
    }

    public void toggleGrid() {
        gridVisible = !gridVisible;
        redraw();
    }

    public double getZoomFactor() {
        return zoomLevels[currentZoomIndex];
    }

    public void redraw() {
        GraphicsContext gc = baseCanvas.getGc();
        gc.clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        gc.save();

        double scrollXOffset = scrollPane.getHvalue() * (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth());
        double scrollYOffset = scrollPane.getVvalue() * (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight());

        gc.translate(-scrollXOffset * getZoomFactor(), -scrollYOffset * getZoomFactor());
        gc.scale(getZoomFactor(), getZoomFactor());

        if (gridVisible) {
            gridDecorator.drawGrid(gc, scrollXOffset, scrollYOffset, getZoomFactor());
        }

        for (ShapeBase shape : shapes) {
            shape.draw(gc);
        }

        gc.restore();
    }
} 

