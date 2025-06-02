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
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

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

        // Dialogo per il testo
        TextInputDialog textDialog = new TextInputDialog("Testo");
        applyThemeToDialog(textDialog);
        Optional<String> text = textDialog.showAndWait();
        if (text.isEmpty()) return;

        // Dialogo per la dimensione
        TextInputDialog sizeDialog = new TextInputDialog("14");
        applyThemeToDialog(sizeDialog);
        Optional<String> size = sizeDialog.showAndWait();
        double fontSize = size.map(s -> {
            try { return Double.parseDouble(s); } catch (NumberFormatException e1) { return 14.0; }
        }).orElse(14.0);

        // Dialogo per il font
        ChoiceDialog<String> fontDialog = new ChoiceDialog<>("Sans", "Sans", "Serif", "Monospace");
        applyThemeToDialog(fontDialog);
        Optional<String> font = fontDialog.showAndWait();
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
    
    private void applyThemeToDialog(Dialog<?> dialog) {
    DialogPane dialogPane = dialog.getDialogPane();
    dialogPane.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
    dialogPane.getStyleClass().add("root"); // opzionale, aggiunge la classe root
}
}

