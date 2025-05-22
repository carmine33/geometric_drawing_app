/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import com.group21.model.Shape.*;
import com.group21.model.Memory.*;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author matte
 */
public class ShapeSelector {

    private List<ShapeBase> list;
    private ShapeBase selectedShape;
    private Memory memory;
    @FXML private ColorPicker fillColorPicker;
    @FXML private ColorPicker strokeColorPicker;

    public ShapeSelector(List<ShapeBase> listShape, ShapeBase selectedShape, ColorPicker fillColorPicker, ColorPicker strokeColorPicker) {
        this.list = listShape;
        this.selectedShape = selectedShape;
        this.fillColorPicker = fillColorPicker;
        this.strokeColorPicker = strokeColorPicker;
        this.memory = new Memory();

    }

     public ShapeBase getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(ShapeBase selectedShape) {
        this.selectedShape = selectedShape;
    }

    public void deleteShape() {

        if (this.selectedShape == null) {
            return;
        }

        this.list.remove(this.selectedShape);
        this.memory.addStackShape(this.selectedShape);
    }
    
    public ShapeBase PasteShape() {
    ShapeBase toPaste = this.memory.getCopiedShape();
    if (toPaste != null) {
        ShapeBase newShape = (ShapeBase) toPaste.copy();
        newShape.translate(10, 10); // spostamento per evitare sovrapposizione
        this.list.add(newShape);
        return newShape;
    }
    return null;
}
    
    
    public void CopyShape() {
    if (this.selectedShape != null) {
        ShapeBase copy = (ShapeBase) this.selectedShape.copy();
        this.memory.setCopiedShape(copy); 
        }
    }

    void ModStrWidthShape() {
        if (this.selectedShape != null) {
            TextInputDialog dialog = new TextInputDialog("1.0");
            dialog.setTitle("Imposta spessore bordo");
            dialog.setHeaderText("Inserisci lo spessore del bordo (es. 2.0):");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    double width = Double.parseDouble(value);
                    this.selectedShape.setStrokeWidth(width);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Valore non valido");
                    alert.setContentText("Inserisci un numero valido per lo spessore del bordo.");
                    alert.showAndWait();
                }
            });
        }
    }

    public void ModFillColorShape() {
    if (this.selectedShape != null) {
        ColorPicker picker = new ColorPicker();
        picker.setValue(this.selectedShape.getFillColor());

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Modifica colore di riempimento");
        alert.getDialogPane().setContent(picker);
        alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.selectedShape.setFillColor(picker.getValue());
        }
      } 
    }

    void ModStrColorShape() {
        if (this.selectedShape != null) {
         ColorPicker picker = new ColorPicker();
         picker.setValue(this.selectedShape.getFillColor());

         Alert alert = new Alert(Alert.AlertType.NONE);
         alert.setTitle("Modifica colore bordi");
         alert.getDialogPane().setContent(picker);
         alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.selectedShape.setStrokeColor(picker.getValue());
        }
       }
    }
}


