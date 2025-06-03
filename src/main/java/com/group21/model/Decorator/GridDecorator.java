package com.group21.model.Decorator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridDecorator implements CanvasInterface {
    private final CanvasInterface decoratedCanvas;

    private double gridSize = 50;
    private Color gridColor = Color.LIGHTGRAY;
    private double gridLineWidth = 0.5;

    // Scroll e zoom da aggiornare prima di eseguire
    private double scrollXOffset = 0;
    private double scrollYOffset = 0;
    private double zoomFactor = 1.0;

    public GridDecorator(CanvasInterface decoratedCanvas) {
        this.decoratedCanvas = decoratedCanvas;
    }

    public void setOffset(double scrollXOffset, double scrollYOffset, double zoomFactor) {
        this.scrollXOffset = scrollXOffset;
        this.scrollYOffset = scrollYOffset;
        this.zoomFactor = zoomFactor;
    }

    @Override
    public void execute() {
        decoratedCanvas.execute();  // disegna forme

        GraphicsContext gc = decoratedCanvas.getCanvas().getGraphicsContext2D();
        gc.setStroke(gridColor);
        gc.setLineWidth(gridLineWidth / zoomFactor);

        double canvasWidth = decoratedCanvas.getCanvas().getWidth() / zoomFactor;
        double canvasHeight = decoratedCanvas.getCanvas().getHeight() / zoomFactor;
        double scaledGridSize = gridSize * zoomFactor;

        double startX = -scrollXOffset % scaledGridSize;
        double startY = -scrollYOffset % scaledGridSize;

        for (double x = startX; x < canvasWidth; x += scaledGridSize)
            gc.strokeLine(x, 0, x, canvasHeight);

        for (double y = startY; y < canvasHeight; y += scaledGridSize)
            gc.strokeLine(0, y, canvasWidth, y);
    }

    @Override
    public Canvas getCanvas() {
        return decoratedCanvas.getCanvas();
    }

    @Override
    public void clear() {
        decoratedCanvas.clear();
    }

    // Metodi di configurazione opzionale
    public void setGridSize(double gridSize) {
        if (gridSize > 0) this.gridSize = gridSize;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public void setGridLineWidth(double gridLineWidth) {
        if (gridLineWidth > 0) this.gridLineWidth = gridLineWidth;
    }
}
