/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.functional;

/**
 *
 * @author carmi
 */
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class FunctionalTest_04 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testSaveAndLoadDrawing() {
        // Verifica che la canvas sia visibile
        verifyThat("#drawingCanvas", isVisible());

        // Disegna una forma (rettangolo)
        clickOn("#rectangleButton");
        moveTo("#drawingCanvas").moveBy(100, 100).press(MouseButton.PRIMARY)
               .moveBy(80, 60).release(MouseButton.PRIMARY);

        // Salva il disegno
        clickOn("#fileMenu");
        clickOn("#saveMenuItem");

        // Simula il caricamento (idealmente da stesso percorso se supportato)
        clickOn("#fileMenu");
        clickOn("#loadMenuItem");

        // Verifica che la canvas sia ancora visibile
        verifyThat("#drawingCanvas", isVisible());

        // Nota: per verificare la presenza esatta della forma dovremmo accedere alla lista delle forme
    }
}
