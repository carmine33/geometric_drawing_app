/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Memento;

/**
 *
 * @author carmi
 */

import com.group21.model.Shape.ShapeBase;
import java.util.ArrayList;
import java.util.List;

public class Memento {
    private final List<ShapeBase> savedShapes;

    public Memento(List<ShapeBase> shapes) {
        this.savedShapes = new ArrayList<>();
        for (ShapeBase shape : shapes) {
            this.savedShapes.add(shape.copy()); // Assicura una copia profonda
        }
    }

    public List<ShapeBase> getSavedShapes() {
        return new ArrayList<>(savedShapes); // Restituisce una copia difensiva
    }
}

