package com.group21.model.Shape;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author mikel
 */

public class ShapePolygonTest {
    /*
   
    /**
     * Metodo di supporto per creare un GraphicsContext fittizio per i test di disegno.
     * @return Un'istanza di GraphicsContext.
     */
    private GraphicsContext dummyGC() {
        return new Canvas(200, 200).getGraphicsContext2D();
    }

    /**
     * Test di base per la creazione, il disegno, containsPoint e la copia di ShapePolygon.
     */
    @Test
    void testShapePolygonBasicOperations() {
        // 1. Creazione di un poligono (un quadrato per semplicità)
        List<Point2D> vertices = new ArrayList<>();
        vertices.add(new Point2D(10, 10));
        vertices.add(new Point2D(90, 10));
        vertices.add(new Point2D(90, 90));
        vertices.add(new Point2D(10, 90));

        ShapePolygon polygon = new ShapePolygon(vertices, Color.BLUE, Color.GREEN, 5.0);

        // 2. Verifica delle proprietà iniziali
        assertEquals(4, polygon.getVertices().size(), "Il poligono dovrebbe avere 4 vertici.");
        assertEquals(Color.BLUE, polygon.getFillColor(), "Il colore di riempimento dovrebbe essere BLUE.");
        assertEquals(Color.GREEN, polygon.getStrokeColor(), "Il colore del bordo dovrebbe essere GREEN.");
        assertEquals(5.0, polygon.getStrokeWidth(), 0.001, "Lo spessore del bordo dovrebbe essere 5.0.");
        assertEquals("Polygon", polygon.getType(), "Il tipo della forma dovrebbe essere 'Polygon'.");

        // 3. Chiamata al metodo draw (verifica solo che non lanci eccezioni)
        assertDoesNotThrow(() -> polygon.draw(dummyGC()), "Il metodo draw non dovrebbe lanciare eccezioni.");

        // 4. Test del metodo containsPoint
        assertTrue(polygon.containsPoint(50, 50), "Il punto (50,50) dovrebbe essere all'interno del quadrato.");
        assertFalse(polygon.containsPoint(5, 5), "Il punto (5,5) dovrebbe essere all'esterno del quadrato.");
        assertFalse(polygon.containsPoint(100, 100), "Il punto (100,100) dovrebbe essere all'esterno del quadrato.");
        assertTrue(polygon.containsPoint(10, 10), "Il punto (10,10) dovrebbe essere sul bordo (considerato all'interno).");
        assertTrue(polygon.containsPoint(50, 10), "Il punto (50,10) dovrebbe essere sul bordo.");

        
    }

    

    /**
     * Test della serializzazione e deserializzazione JSON di ShapePolygon.
     */
    @Test
    void testShapePolygonSerialization() throws IOException {
        List<Point2D> vertices = new ArrayList<>();
        vertices.add(new Point2D(10, 20));
        vertices.add(new Point2D(30, 40));
        vertices.add(new Point2D(50, 60));

        ShapePolygon originalPolygon = new ShapePolygon(vertices, Color.RED, Color.GREEN, 3.0);
        // Popola originalVertices e resizeAnchor per testare la loro serializzazione
        originalPolygon.storeOriginalVertices();
        originalPolygon.setResizeAnchor(new Point2D(5, 5));

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Attiva il default typing per gestire la deserializzazione delle sottoclassi di ShapeBase
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING);

        // Serializza l'oggetto in JSON
        String json = mapper.writeValueAsString(originalPolygon);
        // System.out.println(json); // Utile per il debug

        // Deserializza l'oggetto dal JSON
        ShapePolygon deserializedPolygon = mapper.readValue(json, ShapePolygon.class);

        assertNotNull(deserializedPolygon, "Il poligono deserializzato non dovrebbe essere null.");
        assertEquals("Polygon", deserializedPolygon.getType(), "Il tipo dovrebbe essere 'Polygon'.");
        assertEquals(originalPolygon.getFillColor(), deserializedPolygon.getFillColor(), "Il colore di riempimento dovrebbe corrispondere.");
        assertEquals(originalPolygon.getStrokeColor(), deserializedPolygon.getStrokeColor(), "Il colore del bordo dovrebbe corrispondere.");
        assertEquals(originalPolygon.getStrokeWidth(), deserializedPolygon.getStrokeWidth(), 0.001, "Lo spessore del bordo dovrebbe corrispondere.");

        // Verifica i vertici
        assertEquals(originalPolygon.getVertices().size(), deserializedPolygon.getVertices().size(), "Il numero di vertici dovrebbe corrispondere.");
        for (int i = 0; i < originalPolygon.getVertices().size(); i++) {
            assertEquals(originalPolygon.getVertices().get(i).getX(), deserializedPolygon.getVertices().get(i).getX(), 0.001);
            assertEquals(originalPolygon.getVertices().get(i).getY(), deserializedPolygon.getVertices().get(i).getY(), 0.001);
        }

        // Verifica originalVertices
        assertEquals(originalPolygon.getOriginalVertices().size(), deserializedPolygon.getOriginalVertices().size(), "Il numero di originalVertices dovrebbe corrispondere.");
        for (int i = 0; i < originalPolygon.getOriginalVertices().size(); i++) {
            assertEquals(originalPolygon.getOriginalVertices().get(i).getX(), deserializedPolygon.getOriginalVertices().get(i).getX(), 0.001);
            assertEquals(originalPolygon.getOriginalVertices().get(i).getY(), deserializedPolygon.getOriginalVertices().get(i).getY(), 0.001);
        }

        // Verifica resizeAnchor
        assertNotNull(deserializedPolygon.getResizeAnchor(), "resizeAnchor deserializzato non dovrebbe essere null.");
        assertEquals(originalPolygon.getResizeAnchor().getX(), deserializedPolygon.getResizeAnchor().getX(), 0.001);
        assertEquals(originalPolygon.getResizeAnchor().getY(), deserializedPolygon.getResizeAnchor().getY(), 0.001);
    }
}