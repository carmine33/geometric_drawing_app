/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.model.Shape.ShapeBase;

/**
 *
 * @author mikel
 */
public class PasteCommand implements Command {
    
    private ShapeSelector shape;
    
    public PasteCommand(ShapeSelector shape) {
        this.shape = shape;
    }
    
    @Override
    public void execute() {
         shape.PasteShape();
    }
    
}
