/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group21.model.Decorator;


/**
 *
 * @author Loren
 */

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class Pane implements PaneInterface{
   
    @FXML private BorderPane Pane;

    public Pane(BorderPane Pane) {
        this.Pane = Pane;
    }
    
    @Override
    public BorderPane getPane(){
        return Pane;
    }
}
