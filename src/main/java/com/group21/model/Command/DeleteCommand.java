/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.model.Shape.ShapeBase;
import java.util.ArrayList;

/**
 *
 * @author matte
 */
public class DeleteCommand implements Command {

    private ShapeSelector shape;
    private ShapeBase deletedShape;

    public DeleteCommand(ShapeSelector shape) {
        this.shape = shape;
    }

    @Override
    public void execute() {
        ShapeBase selected = shape.getSelectedShape();
        if (selected != null) {
            shape.getMemory().saveState(new ArrayList<>(shape.getShape())); // salva prima della modifica
            deletedShape = selected;
            shape.getShape().remove(selected);
            shape.setSelectedShape(null);
        }
    }

    @Override
    public void undo() {
        if (shape.getMemory().canUndo()) {
            shape.getShape().clear();
            shape.getShape().addAll(shape.getMemory().restoreLastState());
        }
    }
}
