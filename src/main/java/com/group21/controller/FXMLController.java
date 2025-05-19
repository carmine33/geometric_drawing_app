/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.group21.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author carmi
 */
public class FXMLController implements Initializable {


    @FXML
    private MenuItem menuNew, menuOpen, menuSave, menuExit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("FXMLController initialized");
    }

    @FXML
    private void handleNew() {
        System.out.println("New menu clicked");
    }

    @FXML
    private void handleOpen() {
        System.out.println("Open menu clicked");
    }

    @FXML
    private void handleSave() {
        System.out.println("Save menu clicked");
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit menu clicked");
        System.exit(0);
    }
}
