/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author carmi
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeTextBox extends ShapeBase {

// Testo contenuto nella casella
    private String text;
    // Flag che indica se la casella è in modalità modifica (focus)
    private boolean isEditing;
    
    // Costruttore con parametri di base più testo
    public ShapeTextBox(double x, double y, double width, double height, Color fillColor, Color strokeColor, double strokeWidth, String text) {
        super(x, y, width, height, fillColor, strokeColor, strokeWidth);
        this.text = text;
        this.type = "TextBox";
        this.isEditing = false;
    }

  // Getter del testo
    public String getText() {
        return text;
    }

    // Setter del testo
    public void setText(String text) {
        this.text = text;
    }

    // Verifica se la TextBox è in modalità modifica
    public boolean isEditing() {
        return isEditing;
    }

    // Imposta lo stato di modifica (focus attivo/disattivo)
    public void setEditing(boolean editing) {
        this.isEditing = editing;
    }

    // Metodo per disegnare la casella di testo
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText(text != null ? text : "Text", x + 5, y + this.getHeight() / 2);
    }


     // Verifica se un punto è all'interno della casella
    @Override
    public boolean containsPoint(double px, double py) {
        return px >= x && px <= x + getTextWidth() + 10 && py >= y && py <= y + getTextHeight() + 6;
    }


    // Restituisce una copia dell'oggetto con gli stessi attributi
    @Override
    public ShapeBase copy() {
        return new ShapeTextBox(x, y, this.getWidth(), this.getHeight(), fillColor, strokeColor, strokeWidth, text);
    }

    // Clona l'oggetto corrente (equivalente a copy)
    @Override
    public ShapeBase clone() {
        return copy();
    }
    
    //Calcola la larghezza del testo
        public double getTextWidth() {
        javafx.scene.text.Text t = new javafx.scene.text.Text(text != null ? text : "Text");
        t.setFont(javafx.scene.text.Font.getDefault());
        return t.getLayoutBounds().getWidth();
    }
    
    //Calcola l'altezza del testo
    public double getTextHeight() {
        javafx.scene.text.Text t = new javafx.scene.text.Text(text != null ? text : "Text");
        t.setFont(javafx.scene.text.Font.getDefault());
        return t.getLayoutBounds().getHeight();
    }

}

