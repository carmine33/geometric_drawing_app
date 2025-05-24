/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.model.Shape.ShapeBase;

/**
 *
 * @author matte
 */
public class DeleteCommand implements Command {

    private ShapeSelector shape;
    private double pos;

    public DeleteCommand(ShapeSelector shape) {
        this.shape = shape;
        this.pos = pos;
    }

    @Override
    public void execute() {
        shape.deleteShape();
    }
    
     @Override
    public void undo() {
        shape.getShape().add((int) pos, (ShapeBase) this.shape.getMemory().popStackShape());
    }

}
