/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.controller.Visitor.ShapeVisitor;
import com.group21.controller.Visitor.TranslateVisitor;
import com.group21.model.Memento.Memory;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import com.group21.model.Shape.*;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.control.DialogPane;


/**
 *
 * @author matte
 */
public class ShapeSelector {

    
    private ShapeBase selectedShape;
    private Memory memory;
    @FXML private ColorPicker fillColorPicker;
    @FXML private ColorPicker strokeColorPicker;
    private List<ShapeBase> list = new ArrayList<>();

    public ShapeSelector(ColorPicker fillColorPicker, ColorPicker strokeColorPicker) {
        this.list = new ArrayList<>();
        this.selectedShape = selectedShape;
        this.fillColorPicker = fillColorPicker;
        this.strokeColorPicker = strokeColorPicker;
        this.memory = new Memory();

    }
    
    public List<ShapeBase> getShape() {
        return list;
    }
    
    public Memory getMemory() {
        return memory;
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
        this.memory.saveState(new ArrayList<>(list));
        this.list.remove(this.selectedShape);
        this.setSelectedShape(null);
    }
    
    public void toFront(double ignoredIndex) {
     if (selectedShape != null) {
        int index = list.indexOf(selectedShape);
        if (index < list.size() - 1) {
            list.remove(index);
            list.add(index + 1, selectedShape);
        }
     }
    }

    
    public void toBack(double ignoredIndex) {
     if (selectedShape != null) {
         int index = list.indexOf(selectedShape);
         if (index > 0) {
             list.remove(index);
             list.add(index - 1, selectedShape);
         }
     }
    }

    public ShapeBase pasteShape() {
        ShapeBase toPaste = this.memory.getCopiedShape();
        if (toPaste != null) {
            this.memory.saveState(new ArrayList<>(list));
            ShapeBase newShape = (ShapeBase) toPaste.copy();

            // Gestione specifica per ShapePolygon se serve
            if (newShape instanceof ShapePolygon) {
                ShapePolygon poly = (ShapePolygon) newShape;
                List<Point2D> shifted = new ArrayList<>();
                for (Point2D p : poly.getVertices()) {
                    shifted.add(new Point2D(p.getX() + 10, p.getY() + 10));
                }
                poly.setVertices(shifted);
            } else {
                ShapeVisitor translateVisitor = new TranslateVisitor(10, 10);
                newShape.accept(translateVisitor);
            }

            this.list.add(newShape);
            return newShape;
        }
        return null;
    }

    
    public void copyShape() {
    if (this.selectedShape != null) {
        ShapeBase copy = (ShapeBase) this.selectedShape.copy();
        this.memory.setCopiedShape(copy); 
        }
    }

void modStrWidthShape() {
    if (this.selectedShape != null) {
        memory.saveState(new ArrayList<>(list));
        TextInputDialog dialog = new TextInputDialog("1.0");
        dialog.setTitle("Imposta spessore bordo");
        dialog.setHeaderText("Inserisci lo spessore del bordo (es. 2.0):");

        // Applica il CSS personalizzato alla dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
        dialogPane.getStyleClass().add("root");  // o la classe che vuoi definire nel css

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

                // Applica il CSS personalizzato all'alert
                DialogPane alertPane = alert.getDialogPane();
                alertPane.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
                alertPane.getStyleClass().add("root");

                alert.showAndWait();
            }
        });
    }
}


public void modColorShape(String tipo) {
    if (this.selectedShape == null) return;
    memory.saveState(new ArrayList<>(list));
    Color initialColor;
    String titolo;

    if ("fill".equalsIgnoreCase(tipo)) {
        if (selectedShape instanceof ShapeTextBox) {
            initialColor = ((ShapeTextBox) selectedShape).getTextColor();
            titolo = "Modifica colore del testo";
        } else {
            initialColor = selectedShape.getFillColor();
            titolo = "Modifica colore di riempimento";
        }
    } else if ("stroke".equalsIgnoreCase(tipo)) {
        initialColor = selectedShape.getStrokeColor();
        titolo = "Modifica colore del bordo";
    } else {
        return; // tipo non supportato
    }

    ColorPicker picker = new ColorPicker(initialColor);

    Alert alert = new Alert(Alert.AlertType.NONE);
    alert.setTitle(titolo);
    alert.getDialogPane().setContent(picker);
    alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    // Applica il CSS personalizzato al dialog
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
    dialogPane.getStyleClass().add("root");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        Color nuovoColore = picker.getValue();
        if ("fill".equalsIgnoreCase(tipo)) {
            if (selectedShape instanceof ShapeTextBox) {
                ((ShapeTextBox) selectedShape).setTextColor(nuovoColore);
            } else {
                selectedShape.setFillColor(nuovoColore);
            }
        } else {
            selectedShape.setStrokeColor(nuovoColore);
        }
    }
}

    void cutShape() {
        
        this.copyShape();
        this.deleteShape();
    }
    
    public void addShape(ShapeBase shape) {
    list.add(shape);
}

public void removeShape(ShapeBase shape) {
    list.remove(shape);
}

public void clearShapes() {
    list.clear();
}

public List<ShapeBase> getShapes() {
    return new ArrayList<>(list);
}

public void saveState() {
    memory.saveState(new ArrayList<>(list));
}

public void restoreLastState() {
    list.clear();
    list.addAll(memory.restoreLastState());
}


}


