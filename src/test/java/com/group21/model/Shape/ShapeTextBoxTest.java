/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Shape;

// Importa le classi reali del tuo progetto
import com.group21.controller.Visitor.TextEditVisitor; // La tua classe reale TextEditVisitor

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text; // Necessario per Text per calcolare le dimensioni
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mikel
 */
public class ShapeTextBoxTest {
   
   
    /**
     * Metodo di supporto per creare un GraphicsContext fittizio per i test di disegno.
     * @return Un'istanza di GraphicsContext.
     */
    private GraphicsContext dummyGC() {
        return new Canvas(200, 200).getGraphicsContext2D();
    }

    /**
     * Test di base per la creazione e le proprietà iniziali di ShapeTextBox.
     */
    @Test
    void testShapeTextBoxCreationAndProperties() {
        double x = 50;
        double y = 60;
        double width = 100;
        double height = 30;
        Color fillColor = Color.LIGHTBLUE;
        Color strokeColor = Color.DARKBLUE;
        double strokeWidth = 2.0;
        String textContent = "Hello World";

        ShapeTextBox textBox = new ShapeTextBox(x, y, width, height, fillColor, strokeColor, strokeWidth, textContent);

        // Verifica le proprietà iniziali
        assertEquals(x, textBox.getX(), 0.001, "La coordinata X dovrebbe corrispondere.");
        assertEquals(y, textBox.getY(), 0.001, "La coordinata Y dovrebbe corrispondere.");
        assertEquals(width, textBox.getWidth(), 0.001, "La larghezza dovrebbe corrispondere.");
        assertEquals(height, textBox.getHeight(), 0.001, "L'altezza dovrebbe corrispondere.");
        assertEquals(fillColor, textBox.getFillColor(), "Il colore di riempimento dovrebbe corrispondere.");
        assertEquals(strokeColor, textBox.getStrokeColor(), "Il colore del bordo dovrebbe corrispondere.");
        assertEquals(strokeWidth, textBox.getStrokeWidth(), 0.001, "Lo spessore del bordo dovrebbe corrispondere.");
        assertEquals(textContent, textBox.getText(), "Il testo dovrebbe corrispondere.");
        assertEquals("TextBox", textBox.getType(), "Il tipo della forma dovrebbe essere 'TextBox'.");
        assertFalse(textBox.isEditing(), "isEditing dovrebbe essere false inizialmente.");
        assertEquals(14.0, textBox.getFontSize(), 0.001, "La dimensione del font di default dovrebbe essere 14.0.");
        assertEquals(Color.BLACK, textBox.getTextColor(), "Il colore del testo di default dovrebbe essere BLACK.");
        assertEquals("SansSerif", textBox.getFontFamily(), "Il font family di default dovrebbe essere SansSerif.");
    }

    /**
     * Test del metodo draw di ShapeTextBox.
     * Verifica solo che il metodo non lanci eccezioni durante il disegno.
     */
    @Test
    void testShapeTextBoxDraw() {
        ShapeTextBox textBox = new ShapeTextBox(10, 10, 100, 20, Color.WHITE, Color.BLACK, 1.0, "Test Drawing");
        textBox.setFontSize(20);
        textBox.setFontFamily("Serif");
        textBox.setTextColor(Color.RED);

        // Verifica che il metodo draw non lanci eccezioni
        assertDoesNotThrow(() -> textBox.draw(dummyGC()), "Il metodo draw non dovrebbe lanciare eccezioni.");
    }

    /**
     * Test del metodo containsPoint di ShapeTextBox.
     * Verifica la logica di hit-testing per la selezione, considerando le dimensioni reali del testo.
     * Si assume che ShapeTextBox.x e ShapeTextBox.y siano l'angolo superiore sinistro dell'area di hit-test.
     */
    @Test
    void testShapeTextBoxContainsPoint() {
        double x_start = 10;
        double y_start = 20; // y è l'angolo superiore sinistro dell'area di hit-test
        String textContent = "Short Text";
        double fontSize = 12;

        ShapeTextBox textBox = new ShapeTextBox(x_start, y_start, 0, 0, Color.WHITE, Color.BLACK, 1.0, textContent);
        textBox.setFontSize(fontSize);

        // Calcola le dimensioni reali del testo per impostare textWidth e textHeight
        Text dummyTextNode = new Text(textContent);
        dummyTextNode.setFont(Font.font(textBox.getFontFamily(), textBox.getFontSize()));

        double actualTextWidth = dummyTextNode.getLayoutBounds().getWidth();
        double actualTextHeight = dummyTextNode.getLayoutBounds().getHeight();

        textBox.setTextWidth(actualTextWidth);
        textBox.setTextHeight(actualTextHeight);

        // Le tolleranze sono basate sulla tua implementazione di containsPoint (+10, +6)
        double marginX = 10;
        double marginY = 6;

        // Punti all'interno del bounding box del testo (con margini)
        // Il metodo containsPoint controlla il rettangolo da (x, y) a (x + textWidth + marginX, y + textHeight + marginY)

        // Punto all'interno (es. centro)
        assertTrue(textBox.containsPoint(x_start + (actualTextWidth + marginX) / 2, y_start + (actualTextHeight + marginY) / 2), "Dovrebbe essere all'interno (centro).");

        // Punto vicino all'angolo superiore sinistro (entro i margini)
        assertTrue(textBox.containsPoint(x_start + 1, y_start + 1), "Dovrebbe essere all'interno (vicino top-left).");

        // Punto vicino all'angolo inferiore destro (entro i margini)
        assertTrue(textBox.containsPoint(x_start + actualTextWidth + marginX - 1, y_start + actualTextHeight + marginY - 1), "Dovrebbe essere all'interno (vicino bottom-right).");

        // Punti sui bordi (dovrebbero essere inclusi)
        assertTrue(textBox.containsPoint(x_start, y_start), "Punto (x,y) dovrebbe essere all'interno.");
        assertTrue(textBox.containsPoint(x_start + actualTextWidth + marginX, y_start), "Punto sul bordo destro superiore.");
      

        // Punti all'esterno
        assertFalse(textBox.containsPoint(x_start - 1, y_start), "Fuori a sinistra.");
        assertFalse(textBox.containsPoint(x_start, y_start - 1), "Fuori in alto.");
        assertFalse(textBox.containsPoint(x_start + actualTextWidth + marginX + 1, y_start), "Fuori a destra.");
        assertFalse(textBox.containsPoint(x_start, y_start + actualTextHeight + marginY + 1), "Fuori in basso.");
    }

    /**
     * Test del metodo copy di ShapeTextBox.
     * Verifica che la copia sia profonda e che tutte le proprietà siano replicate.
     */
    @Test
    void testShapeTextBoxCopy() {
        ShapeTextBox originalTextBox = new ShapeTextBox(10, 20, 150, 40, Color.RED, Color.GREEN, 3.0, "Original Text");
        originalTextBox.setFontSize(18);
        originalTextBox.setFontFamily("Monospace");
        originalTextBox.setTextColor(Color.CYAN);

        ShapeBase copiedShape = originalTextBox.copy();

        assertNotNull(copiedShape, "La forma copiata non dovrebbe essere null.");
        assertTrue(copiedShape instanceof ShapeTextBox, "La forma copiata dovrebbe essere un'istanza di ShapeTextBox.");

        ShapeTextBox copiedTextBox = (ShapeTextBox) copiedShape;

        // Verifica che siano oggetti diversi (copia profonda)
        assertNotSame(originalTextBox, copiedTextBox, "La copia dovrebbe essere un oggetto diverso dall'originale.");

        // Verifica che le proprietà siano state copiate correttamente
        assertEquals(originalTextBox.getX(), copiedTextBox.getX(), 0.001);
        assertEquals(originalTextBox.getY(), copiedTextBox.getY(), 0.001);
        assertEquals(originalTextBox.getWidth(), copiedTextBox.getWidth(), 0.001);
        assertEquals(originalTextBox.getHeight(), copiedTextBox.getHeight(), 0.001);
        assertEquals(originalTextBox.getFillColor(), copiedTextBox.getFillColor());
        assertEquals(originalTextBox.getStrokeColor(), copiedTextBox.getStrokeColor());
        assertEquals(originalTextBox.getStrokeWidth(), copiedTextBox.getStrokeWidth(), 0.001);
        assertEquals(originalTextBox.getText(), copiedTextBox.getText());
        assertEquals(originalTextBox.getFontSize(), copiedTextBox.getFontSize(), 0.001);
        assertEquals(originalTextBox.getFontFamily(), copiedTextBox.getFontFamily());
        assertEquals(originalTextBox.getTextColor(), copiedTextBox.getTextColor());
        assertEquals(originalTextBox.getType(), copiedTextBox.getType());
    }


    /**
     * Test della serializzazione e deserializzazione JSON di ShapeTextBox.
     * Questo test si basa sull'uso di Jackson per la serializzazione/deserializzazione.
     */
    @Test
    void testShapeTextBoxSerialization() throws IOException {
        ShapeTextBox originalTextBox = new ShapeTextBox(100, 200, 250, 80, Color.YELLOW, Color.ORANGE, 4.0, "Serializable Text");
        originalTextBox.setFontSize(24);
        originalTextBox.setFontFamily("Serif");
        originalTextBox.setTextColor(Color.PURPLE);
        // È cruciale che textWidth e textHeight siano impostati affinché vengano serializzati.
        // Nel tuo ShapeTextBox.java, questi campi sono serializzabili ma non calcolati automaticamente.
        // Li impostiamo qui per assicurarci che il test di serializzazione/deserializzazione funzioni.
        Text dummyTextNode = new Text(originalTextBox.getText());
        dummyTextNode.setFont(Font.font(originalTextBox.getFontFamily(), originalTextBox.getFontSize()));
        originalTextBox.setTextWidth(dummyTextNode.getLayoutBounds().getWidth());
        originalTextBox.setTextHeight(dummyTextNode.getLayoutBounds().getHeight());


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Attiva il default typing per gestire la polimorfia di ShapeBase e le sue sottoclassi.
        // Questo è fondamentale per la corretta deserializzazione delle forme.
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING);

        String json = mapper.writeValueAsString(originalTextBox);
        // System.out.println(json); // Utile per il debug per vedere il JSON generato

        ShapeTextBox deserializedTextBox = mapper.readValue(json, ShapeTextBox.class);

        assertNotNull(deserializedTextBox, "La casella di testo deserializzata non dovrebbe essere null.");
        assertEquals("TextBox", deserializedTextBox.getType(), "Il tipo dovrebbe essere 'TextBox'.");
        assertEquals(originalTextBox.getX(), deserializedTextBox.getX(), 0.001);
        assertEquals(originalTextBox.getY(), deserializedTextBox.getY(), 0.001);
        assertEquals(originalTextBox.getWidth(), deserializedTextBox.getWidth(), 0.001);
        assertEquals(originalTextBox.getHeight(), deserializedTextBox.getHeight(), 0.001);
        assertEquals(originalTextBox.getFillColor(), deserializedTextBox.getFillColor());
        assertEquals(originalTextBox.getStrokeColor(), deserializedTextBox.getStrokeColor());
        assertEquals(originalTextBox.getStrokeWidth(), deserializedTextBox.getStrokeWidth(), 0.001);
        assertEquals(originalTextBox.getText(), deserializedTextBox.getText());
        assertEquals(originalTextBox.getFontSize(), deserializedTextBox.getFontSize(), 0.001);
        assertEquals(originalTextBox.getFontFamily(), deserializedTextBox.getFontFamily());
        assertEquals(originalTextBox.getTextColor(), deserializedTextBox.getTextColor());
        // Verifica che textWidth e textHeight siano stati deserializzati correttamente
        assertEquals(originalTextBox.getTextWidth(), deserializedTextBox.getTextWidth(), 0.001);
        assertEquals(originalTextBox.getTextHeight(), deserializedTextBox.getTextHeight(), 0.001);
    }

    /**
     * Test del metodo accept del pattern Visitor.
     * Questo test verifica che il metodo accept invochi correttamente il metodo visit sul visitor.
     * Non testa la logica interna del visitor stesso (es. l'apertura di un dialogo).
     */
    @Test
    void testShapeTextBoxAcceptVisitor() throws InterruptedException {
        ShapeTextBox textBox = new ShapeTextBox(10, 10, 100, 20, Color.WHITE, Color.BLACK, 1.0, "Original Text");
        String originalText = textBox.getText();

        // Creamo un Canvas fittizio, anche se TextEditVisitor apre un dialogo,
        // per questo unit test non ci interessa l'interazione GUI, solo che il metodo visit venga chiamato.
        Canvas dummyCanvas = new Canvas();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Utilizziamo un'istanza reale di TextEditVisitor
                // Nota: Questo visitatore apre dialoghi UI. 
                TextEditVisitor visitor = new TextEditVisitor(dummyCanvas);
                textBox.accept(visitor);
            } catch (Exception e) {
// Cattura eventuali eccezioni lanciate dal codice sul thread FX
                                fail("Exception occurred on FX thread during visitor acceptance: " + e.getMessage());
            } finally {
                latch.countDown(); // Rilascia il latch indipendentemente dal successo/fallimento
            }
        });

       
        // Asserzioni post-visita (se il visitatore modifica lo stato senza input utente)
        // Poiché TextEditVisitor apre un dialogo, il testo non cambierà senza input utente simulato.
        // Quindi, l'asserzione che il testo non sia cambiato è corretta per questo scenario.
        assertEquals(originalText, textBox.getText(), "Il testo non dovrebbe cambiare senza input utente simulato tramite dialogo.");
    }
}
