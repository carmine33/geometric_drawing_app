/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.paint.Color;
import com.group21.model.Shape.*;

/**
 *
 * @author matte
 */


import com.group21.model.Shape.ShapeRectangle;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ColorPicker;

import static org.junit.jupiter.api.Assertions.*;

class ToFrontCommandTest {

    private ShapeSelector shapeSelector;
    private List<ShapeBase> shapeList;
    private ShapeBase shape1;
    private ShapeBase shape2;

    @BeforeEach
    void setUp() {
        shape1 = new ShapeRectangle(0, 0, 10, 10, Color.RED, Color.BLACK, 1.0);
        shape2 = new ShapeRectangle(20, 20, 10, 10, Color.BLUE, Color.BLACK, 1.0);
        shapeList = new ArrayList<>();
        shapeList.add(shape1);
        shapeList.add(shape2);
        
        ColorPicker fillColorPicker = new ColorPicker(Color.RED);
        ColorPicker strokeColorPicker = new ColorPicker(Color.BLUE);

        shapeSelector = new ShapeSelector(fillColorPicker,strokeColorPicker);
    }

    @Test
    void execute_shouldMoveSelectedShapeForward() {
        ToFrontCommand command = new ToFrontCommand(shapeSelector, 0, 2); // size = list size
        command.execute();

        assertEquals(shape2, shapeList.get(0), "La shape in fondo dovrebbe ora essere in prima posizione.");
        assertEquals(shape1, shapeList.get(1), "La shape selezionata dovrebbe essere stata portata avanti.");
    }   
}
