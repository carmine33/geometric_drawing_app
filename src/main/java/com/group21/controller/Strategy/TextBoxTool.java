/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Strategy;

/**
 *
 * @author carmi
 */

import com.group21.model.Factory.ConcreteCreatorText;
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeTextBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Optional;

public class TextBoxTool implements DrawingToolStrategy {
    private List<ShapeBase> shapes;
    private Color strokeColor, fillColor;
    private Runnable redrawCallback;
    private double offsetX = 0, offsetY = 0, zoomFactor = 1.0;

    public TextBoxTool(List<ShapeBase> shapes, Color strokeColor, Color fillColor, Runnable redrawCallback) {
        this.shapes = shapes;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.redrawCallback = redrawCallback;
    }

    @Override
    public void onMousePressed(MouseEvent e) {}

    @Override
    public void onMouseDragged(MouseEvent e) {}

    @Override
    public void onMouseReleased(MouseEvent e) {
        double x = (e.getX() / zoomFactor) + offsetX;
        double y = (e.getY() / zoomFactor) + offsetY;

        Optional<String> text = new TextInputDialog("Testo").showAndWait();
        if (text.isEmpty()) return;

        Optional<String> size = new TextInputDialog("14").showAndWait();
        double fontSize = size.map(s -> {
            try { return Double.parseDouble(s); } catch (NumberFormatException e1) { return 14.0; }
        }).orElse(14.0);

        Optional<String> font = new ChoiceDialog<>("Sans", "Sans", "Serif", "Monospace").showAndWait();
        String fontFamily = "SansSerif";
        if (font.isPresent()) {
            if ("Serif".equals(font.get())) {
                fontFamily = "Serif";
            } else if ("Monospace".equals(font.get())) {
                fontFamily = "Monospaced";
            } else {
                fontFamily = "SansSerif";
            }
        }


        ShapeTextBox box = new ConcreteCreatorText().createShape(x, y, strokeColor, fillColor, text.get(), fontSize);
        box.setFontSize(fontSize);
        box.setFontFamily(fontFamily);
        shapes.add(box);
        if (redrawCallback != null) redrawCallback.run();
    }

    @Override
    public ShapeBase getPreviewShape() {
        return null;
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

