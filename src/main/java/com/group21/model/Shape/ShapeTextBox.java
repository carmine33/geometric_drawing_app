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

    // Testo da visualizzare nella casella
    private String text;

    // Flag che indica se la casella è in modalità modifica attiva (non usato nel disegno)
    private boolean isEditing;

    // Dimensione del font del testo (default 14 pt)
    private double fontSize = 14;

    // Colore del testo
    private Color textColor = Color.BLACK;
    
    private String fontFamily = "SansSerif";

    /**
     * Costruttore che inizializza tutti i parametri necessari, ereditando da ShapeBase
     */
    public ShapeTextBox(double x, double y, double width, double height,
                        Color fillColor, Color strokeColor, double strokeWidth,
                        String text) {
        super(x, y, width, height, fillColor, strokeColor, strokeWidth);
        this.text = text;
        this.type = "TextBox";
        this.isEditing = false;
    }

    // Getter del contenuto testuale
    public String getText() {
        return text;
    }

    // Setter del contenuto testuale
    public void setText(String text) {
        this.text = text;
    }

    // Verifica se la casella è in modalità editing
    public boolean isEditing() {
        return isEditing;
    }

    // Imposta lo stato di editing (non influenza il disegno attuale)
    public void setEditing(boolean editing) {
        this.isEditing = editing;
    }

    //Imposta il font selezionato per il testo
        public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    //Restituisce il font corrente del testo 
    public String getFontFamily() {
        return fontFamily;
    }

    // Restituisce la dimensione corrente del font
    public double getFontSize() {
        return fontSize;
    }

    // Imposta una nuova dimensione del font
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    // Restituisce il colore del testo
    public Color getTextColor() {
        return textColor;
    }

    // Imposta il colore del testo
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * Calcola la larghezza stimata del testo in pixel
     * usando un nodo JavaFX invisibile (Text)
     */
    public double getTextWidth() {
        Text t = new Text(text != null ? text : "Text");
        t.setFont(Font.font(fontSize));
        return t.getLayoutBounds().getWidth();
    }

    /**
     * Calcola l'altezza stimata del testo in pixel
     */
    public double getTextHeight() {
        Text t = new Text(text != null ? text : "Text");
        t.setFont(Font.font(fontSize));
        return t.getLayoutBounds().getHeight();
    }

    /**
     * Metodo di disegno della TextBox sulla canvas.
     * Disegna solo il testo con la dimensione, font e colore correnti.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFont(Font.font(fontFamily, fontSize));
        gc.setFill(textColor);
        gc.fillText(text != null ? text : "Text", x + 5, y + fontSize);
    }

    /**
     * Verifica se un punto (px, py) si trova all'interno
     * del rettangolo virtuale che contiene il testo.
     * Serve per la selezione.
     */
    @Override
    public boolean containsPoint(double px, double py) {
        return px >= x && px <= x + getTextWidth() + 10 &&
               py >= y && py <= y + getTextHeight() + 6;
    }

    /**
     * Crea una copia della ShapeTextBox con stessi parametri e colore testo.
     */
    @Override
    public ShapeBase copy() {
        ShapeTextBox copy = new ShapeTextBox(
            x, y, this.getWidth(), this.getHeight(), fillColor, strokeColor, strokeWidth, text
        );
        copy.setFontSize(fontSize);
        copy.setTextColor(textColor);
        return copy;
    }

    /**
     * Alias di copy(), per compatibilità con clone().
     */
    @Override
    public ShapeBase clone() {
        return copy();
    }
}

