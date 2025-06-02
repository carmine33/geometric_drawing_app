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
        shape.deleteShape();
    }

}
