/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Command;

import com.group21.model.Command.Command;
import com.group21.model.Command.ShapeSelector;

/**
 *
 * @author mikel
 */
public class ModFillColorCommand implements Command {
private ShapeSelector shape;
    
    public ModFillColorCommand(ShapeSelector shape) {
        this.shape = shape;
    }    
    @Override
    public void execute() {
         shape.ModFillColorShape();
    }
}
