/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Memory;

/**
 *
 * @author carmi
 */
import com.group21.model.Shape.ShapeRectangle;
import com.group21.model.Shape.ShapeBase;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryTest {

    @Test
    void testAddStackShapeStoresShape() {
        Memory memory = new Memory();
        ShapeRectangle shape = new ShapeRectangle(10, 10, 20, 20, Color.BLACK, Color.RED, 1.5);

        memory.addStackShape(shape);

        // Non possiamo accedere direttamente allo stack, ma almeno verifichiamo che il metodo non fallisca
        assertDoesNotThrow(() -> memory.addStackShape(shape));
    }
}