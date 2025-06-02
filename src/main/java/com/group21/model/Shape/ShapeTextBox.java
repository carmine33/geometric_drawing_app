/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

/**
 *
 * @author carmi
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javafx.geometry.Bounds;

public class ShapeTextBox extends ShapeBase {

    private String text;
    private boolean isEditing;
    private double fontSize = 14;
    private Color textColor = Color.BLACK;
    private String fontFamily = "SansSerif";
    private double textHeight;
    private double textWidth;
    
    public ShapeTextBox() {
        // No-args constructor for Jackson serialization
    }
    
    public ShapeTextBox(double x, double y, double width, double height,
                        Color fillColor, Color strokeColor, double strokeWidth,
                        String text) {
        super(x, y, width, height, fillColor, strokeColor, strokeWidth);
        this.text = text;
        this.type = "TextBox";
        this.isEditing = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        this.isEditing = editing;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * Calcola la larghezza stimata del testo in pixel
     * usando un nodo JavaFX invisibile (Text)
     */
    @JsonProperty("textWidth")
    public double getTextWidth() {
        Text t = new Text(text != null ? text : "Text");
        t.setFont(Font.font(fontFamily, fontSize));
        return t.getLayoutBounds().getWidth();
    }

    /**
     * Calcola l'altezza stimata del testo in pixel
     */
    @JsonProperty("textHeight")
    public double getTextHeight() {
        Text t = new Text(text != null ? text : "Text");
        t.setFont(Font.font(fontFamily, fontSize));
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
        double textX = x + 5;
        double textY = y + getTextHeight();
        gc.fillText(text != null ? text : "Text", textX, textY);
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
     * @return 
     */
    @Override
    public ShapeBase copy() {
        ShapeTextBox copy = new ShapeTextBox(getX(), getY(), getWidth(), getHeight(),
            getFillColor(), getStrokeColor(), getStrokeWidth(), getText());

        copy.setFontSize(getFontSize());
        copy.setFontFamily(getFontFamily());
        copy.setTextColor(getTextColor());
        return copy;
    }
    
     // -------------------------------
    // JSON Serialization/Deserialization
    // -------------------------------
    
    // Getter for serialization
    @JsonProperty("textColor")
    public double[] getTextColorArray() {
        if (textColor == null) return null;
        return new double[]{
            textColor.getRed(),
            textColor.getGreen(),
            textColor.getBlue(),
            textColor.getOpacity()
        };
    }

    // Setter for deserialization
    @JsonProperty("textColor")
    public void setTextColorArray(double[] rgba) {
        if (rgba == null || rgba.length != 4) {
            this.textColor = null;
        } else {
            this.textColor = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
        }
    }
    
    @JsonProperty("textWidth")
    public void setTextWidth(double textWidth) {
        this.textWidth = textWidth;
    }
    @JsonProperty("textHeight")
    public void setTextHeight(double textHeight) {
        this.textHeight = textHeight;
    }

    @Override
    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @JsonIgnore
    @Override
public List<String> getSupportedActions() {
    return List.of("delete", "copy", "paste", "toFront", "toBack", "fillColor", "modifyText");
}

}

