/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.model.Shape.*;
import java.util.ArrayList;

/**
 *
 * @author matte
 */
public class ToFrontCommand implements Command {

    private final ShapeSelector shape;
    private final double index;
    private final double size;

    public ToFrontCommand(ShapeSelector shape, double index, double size) {
        this.shape = shape;
        this.index = index;
        this.size = size;
    }

    @Override
    public void execute() {
        shape.getMemory().saveState(new ArrayList<>(shape.getShape())); // Salva lo stato PRIMA della modifica
        shape.toFront(size);
    }

    @Override
    public void undo() {
        if (shape.getMemory().canUndo()) {
            shape.getShape().clear();
            shape.getShape().addAll(shape.getMemory().restoreLastState());
        }
    }
}