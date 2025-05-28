package com.group21.model.Decorator;

/**
 *
 * @author Mikel
 */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridDecorator {
    private BaseCanvas canvas; // Riferimento al canvas sottostante
    private double gridSize = 50; // Dimensione della cella della griglia in pixel
    private Color gridColor = Color.LIGHTGRAY;
    private double gridLineWidth = 0.5;

    public GridDecorator(BaseCanvas canvas) {
        this.canvas = canvas;
    }

    public void drawGrid(GraphicsContext gc, double scrollXOffset, double scrollYOffset, double zoomFactor) {
        gc.setStroke(gridColor);
        gc.setLineWidth(gridLineWidth / zoomFactor); // Mantieni spessore costante con lo zoom

        double canvasWidth = canvas.getCanvas().getWidth() / zoomFactor;
        double canvasHeight = canvas.getCanvas().getHeight() / zoomFactor;


        double scaledGridSize = gridSize * zoomFactor;

    // Allineamento con lo scroll
    double startX = -scrollXOffset % scaledGridSize;
    double startY = -scrollYOffset % scaledGridSize;

    for (double x = startX; x < canvasWidth; x += scaledGridSize) {
        gc.strokeLine(x, 0, x, canvasHeight);
    }

    for (double y = startY; y < canvasHeight; y += scaledGridSize) {
        gc.strokeLine(0, y, canvasWidth, y);
    }
}




    // Metodi per configurare la griglia 
    public void setGridSize(double gridSize) {
        if (gridSize > 0) {
            this.gridSize = gridSize;
        }
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public void setGridLineWidth(double gridLineWidth) {
        if (gridLineWidth > 0) {
            this.gridLineWidth = gridLineWidth;
        }
    }
}
