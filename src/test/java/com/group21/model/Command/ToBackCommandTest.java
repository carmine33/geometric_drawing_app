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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToBackCommandTest { 

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

        // selezioniamo shape2 che è all'indice 1
        shapeSelector = new ShapeSelector(shapeList, shape2, null, null);
    }

    @Test
    void execute_shouldMoveSelectedShapeBackwards() {
        ToBackCommand command = new ToBackCommand(shapeSelector, 1);
        command.execute();

        assertEquals(shape2, shapeList.get(0), "La shape selezionata dovrebbe essere spostata indietro nella lista.");
        assertEquals(shape1, shapeList.get(1), "La shape precedentemente prima dovrebbe ora essere dopo.");
    }   
}

