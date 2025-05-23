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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ShapeTextBox extends ShapeBase {

    // Testo visualizzato nella casella
    private String text;

    // Indica se la casella è in modalità "editing" (focus)
    private boolean isEditing;

    // Dimensione del font (in punti)
    private double fontSize = 14;

    /**
     * Costruttore con parametri per posizione, dimensione, colori, spessore e contenuto.
     */
    public ShapeTextBox(double x, double y, double width, double height,
                        Color fillColor, Color strokeColor, double strokeWidth,
                        String text) {
        super(x, y, width, height, fillColor, strokeColor, strokeWidth);
        this.text = text;
        this.type = "TextBox";
        this.isEditing = false;
    }

    /** Restituisce il testo contenuto nella casella. */
    public String getText() {
        return text;
    }

    /** Imposta il testo contenuto nella casella. */
    public void setText(String text) {
        this.text = text;
    }

    /** Restituisce true se la casella è in modalità editing. */
    public boolean isEditing() {
        return isEditing;
    }

    /** Imposta la modalità editing (focus attivo o disattivo). */
    public void setEditing(boolean editing) {
        this.isEditing = editing;
    }

    /** Restituisce la dimensione del font usato per il testo. */
    public double getFontSize() {
        return fontSize;
    }

    /** Imposta la dimensione del font. */
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Calcola la larghezza del testo corrente usando un nodo JavaFX invisibile.
     */
    public double getTextWidth() {
        Text t = new Text(text != null ? text : "Text");
        t.setFont(Font.font(fontSize));
        return t.getLayoutBounds().getWidth();
    }

    /**
     * Calcola l’altezza del testo corrente usando un nodo JavaFX invisibile.
     */
    public double getTextHeight() {
        Text t = new Text(text != null ? text : "Text");
        t.setFont(Font.font(fontSize));
        return t.getLayoutBounds().getHeight();
    }

    /**
     * Disegna il testo sulla canvas.
     * Non disegna alcun rettangolo visivo, solo il testo centrato verticalmente.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFont(Font.font(fontSize));
        gc.setFill(Color.BLACK);
        gc.fillText(text != null ? text : "Text", x + 5, y + fontSize);
    }

    /**
     * Restituisce true se il punto cliccato si trova all’interno del rettangolo
     * virtuale che circonda il testo (usato per selezione).
     */
    @Override
    public boolean containsPoint(double px, double py) {
        return px >= x && px <= x + getTextWidth() + 10 &&
               py >= y && py <= y + getTextHeight() + 6;
    }

    /**
     * Crea una copia profonda della ShapeTextBox, inclusa la dimensione del font.
     */
    @Override
    public ShapeBase copy() {
        ShapeTextBox copy = new ShapeTextBox(
            x, y, this.getWidth(), this.getHeight(),
            fillColor, strokeColor, strokeWidth,
            text
        );
        copy.setFontSize(fontSize);
        return copy;
    }

    /** Alias di copy() per compatibilità con clone. */
    @Override
    public ShapeBase clone() {
        return copy();
    }
}

