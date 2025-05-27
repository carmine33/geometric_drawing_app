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
public class ToBackCommand implements Command {

    private final ShapeSelector shape;
    private final double index;

    public ToBackCommand(ShapeSelector shape, double index) {
        this.shape = shape;
        this.index = index;
    }

    @Override
    public void execute() {
        shape.getMemory().saveState(new ArrayList<>(shape.getShape())); // Salva lo stato PRIMA della modifica
        shape.toBack(index);
    }

}