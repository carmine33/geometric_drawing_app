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
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class FunctionalTest_01 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testMainWindowAndCanvasPresence() {
        // Verifica che la finestra principale sia visibile
        assertThat(window("Geometric Drawing App - UNISA Group21")).isShowing();

        // Verifica che la canvas esista e sia visibile
        Canvas canvas = lookup("#drawingCanvas").queryAs(Canvas.class);
        assertThat(canvas).isNotNull();
        assertThat(canvas.isVisible()).isTrue();
    }
}
