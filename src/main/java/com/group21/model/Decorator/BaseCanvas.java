/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Decorator;

/**
 *
 * @author carmi
 */

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class BaseCanvas implements CanvasInterface {

    private final Canvas canvas;
    private final GraphicsContext gc;

    public BaseCanvas(double width, double height) {
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        //setupDrawing();
    }

    /*private void setupDrawing() {
        canvas.setOnMousePressed(this::startDraw);
        canvas.setOnMouseDragged(this::draw);
    }

    private void startDraw(MouseEvent e) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.beginPath();
        gc.moveTo(e.getX(), e.getY());
        gc.stroke();
    }

    private void draw(MouseEvent e) {
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
    }*/

    public GraphicsContext getGc() {
        return gc;
    }
    
    @Override
    public void execute() {
        // Optional: extendable in future
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
