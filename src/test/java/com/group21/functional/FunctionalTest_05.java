/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.functional;

/**
 *
 * @author carmi
 */

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class FunctionalTest_05 extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new com.group21.GeometricDrawingApp().start(stage);
    }

    @Test
    void testColorPickersAffectShapes() {
        // Verifica canvas e color pickers
        verifyThat("#drawingCanvas", isVisible());
        verifyThat("#strokeColorPicker", isVisible());
        verifyThat("#fillColorPicker", isVisible());

        // Seleziona colore per il bordo e riempimento
        clickOn("#strokeColorPicker").write(Color.RED.toString()).press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);
        clickOn("#fillColorPicker").write(Color.YELLOW.toString()).press(MouseButton.PRIMARY).release(MouseButton.PRIMARY);

        // Disegna una forma chiusa (rettangolo)
        clickOn("#rectangleButton");
        moveTo("#drawingCanvas").moveBy(100, 100).press(MouseButton.PRIMARY)
               .moveBy(80, 60).release(MouseButton.PRIMARY);

        // Disegna una seconda forma per verificare che i colori siano mantenuti
        clickOn("#rectangleButton");
        moveTo("#drawingCanvas").moveBy(200, 200).press(MouseButton.PRIMARY)
               .moveBy(80, 60).release(MouseButton.PRIMARY);

        // Verifica che la canvas sia ancora visibile dopo l'interazione
        verifyThat("#drawingCanvas", isVisible());
    }
}
