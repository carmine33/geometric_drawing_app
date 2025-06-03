/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.controller.Visitor;

import com.group21.model.Shape.*;
import java.util.Optional;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author Loren
 */
public class TextEditVisitor implements ShapeVisitor{
    private final Canvas canvas;
    
    public TextEditVisitor(Canvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public void visit(ShapeTextBox textBox) {
        TextInputDialog dialog = new TextInputDialog(textBox.getText());
        dialog.setTitle("Modify Text");
        dialog.setHeaderText("Enter new text for the TextBox:");
        dialog.setContentText("Text:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
        dialogPane.getStyleClass().add("root");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newText -> {
            textBox.setText(newText);
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            // Call your redraw method here (not included)
        });
    }
    
    @Override public void visit(ShapeRectangle rectangle) {}
    @Override public void visit(ShapeEllipse ellipse) {}
    @Override public void visit(ShapeLine line) {}
    @Override public void visit(ShapePolygon polygon) {}
}
