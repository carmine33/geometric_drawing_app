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

public class ModColorCommand implements Command {
    private ShapeSelector selector;
    private String tipo; // "fill" oppure "stroke"

    public ModColorCommand(ShapeSelector selector, String tipo) {
        this.selector = selector;
        this.tipo = tipo.toLowerCase(); // sicurezza
    }

    @Override
    public void execute() {
        selector.modColorShape(tipo);
    }

}

