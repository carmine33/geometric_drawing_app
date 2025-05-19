/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group21.controller;

import com.group21.model.Decorator.BaseCanvas;
import com.group21.model.Decorator.CanvasInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author carmi
 */
public class FXMLController implements Initializable {
    
    @FXML
    private MenuItem menuNew, menuOpen, menuSave, menuExit;
    
    @FXML
    private BorderPane rootPane;
    
    private CanvasInterface canvas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvas = new BaseCanvas(1000, 500);
        rootPane.setCenter(canvas.getCanvas());
        System.out.println("FXMLController initialized");
    }

    @FXML
    private void handleNew() {
        canvas.clear();
        System.out.println("Canvas cleared");
    }

    @FXML
    private void handleOpen() {
        System.out.println("Open clicked");
    }

    @FXML
    private void handleSave() {
        System.out.println("Save clicked");
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit clicked");
        System.exit(0);
    }
}
