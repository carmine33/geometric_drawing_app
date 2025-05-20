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

// Added for additional tools
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

// Shapes
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeEllipse;
import com.group21.model.Shape.ShapeLine;
import com.group21.model.Shape.ShapeRectangle;

import com.group21.model.Command.*;

// Import for save/load 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group21.model.Decorator.PaneInterface;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author carmi
 */
public class FXMLController implements Initializable {
    
    @FXML
    private MenuItem menuNew, menuOpen, menuSave, menuExit;
    
    @FXML private Pane canvasPlaceholder;
    private BaseCanvas baseCanvas;
    @FXML private ColorPicker fillColorPicker;
    @FXML private ColorPicker strokeColorPicker;
    @FXML private Button btnRectangle;
    @FXML private Button btnEllipse;
    @FXML private Button btnLine;
    @FXML private Button btnSelect;
    
    private ShapeSelector selectShape;
    ContextMenu contextMenu = new ContextMenu();
    MenuItem deleteMenu = new MenuItem("Delete");
    MenuItem cutMenu = new MenuItem("Cut");
    MenuItem copyMenu = new MenuItem("Copy");
    MenuItem pasteMenu = new MenuItem("Paste");
    private String currentMouseCommand = null;
    private List<ShapeBase> shapes = new ArrayList<>();
    private double lineStartX, lineStartY;
    private boolean isDrawingLine = false, isDrawingRectangle = false, isDrawingEllipse = false;
    private Command command = null;
    private String mod = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(1000, 500);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        
        btnRectangle.setOnAction(e -> currentMouseCommand = "Rectangle");
        btnEllipse.setOnAction(e -> currentMouseCommand = "Ellipse");
        btnLine.setOnAction(e -> currentMouseCommand = "Line");
        btnSelect.setOnAction(e-> currentMouseCommand = "Select");
        
        baseCanvas.getCanvas().setOnMousePressed(e -> {
            if(e.isPrimaryButtonDown() && !(currentMouseCommand.equals("Select")) && !(currentMouseCommand.isEmpty()) ){
            lineStartX = e.getX();
            lineStartY = e.getY();
                if (currentMouseCommand.equals("Line")) {
                    isDrawingLine = true;
                } else if(currentMouseCommand.equals("Rectangle")){
                    isDrawingRectangle = true;
                } else if(currentMouseCommand.equals("Ellipse")){
                    isDrawingEllipse = true;
            }} else if(e.isSecondaryButtonDown()){ //se invece clicco tasto destro...
                select(e);
                initContextMenu();
               }else if (e.isPrimaryButtonDown() && currentMouseCommand.equals("Select") ){
                    select(e);
               }
        });

        baseCanvas.getCanvas().setOnMouseReleased(e -> {
            double endX = e.getX();
            double endY = e.getY();
            if (currentMouseCommand.equals("Line") && isDrawingLine) {
                isDrawingLine = false;
                ShapeBase line = new ShapeLine(lineStartX, lineStartY, 0, 0,
                                                 e.getX(), e.getY(), 
                                            strokeColorPicker.getValue()
                                              );
                shapes.add(line);
                redraw(baseCanvas.getGc());
            }else if(currentMouseCommand.equals("Rectangle")  && isDrawingRectangle){
                double x = Math.min(lineStartX, endX);
                double y = Math.min(lineStartY, endY);
                double width = Math.abs(endX - lineStartX);
                double height = Math.abs(endY - lineStartY);
                if(width == 0 && height == 0){
                    width = 100;
                    height = 40;
                }
                
                isDrawingRectangle = false;
                ShapeBase rectangle = new ShapeRectangle(x, y, width, height, fillColorPicker.getValue(), strokeColorPicker.getValue());
                shapes.add(rectangle);
                redraw(baseCanvas.getGc());
                
            } else if(currentMouseCommand.equals("Ellipse")  && isDrawingEllipse){
                double x = Math.min(lineStartX, endX);
                double y = Math.min(lineStartY, endY);
                double width = Math.abs(endX - lineStartX);
                double height = Math.abs(endY - lineStartY);
                double ellipseX = (lineStartX + endX) / 2;
                double ellipseY = (lineStartY + endY) / 2;
                isDrawingEllipse = false;
                if(width == 0 && height == 0){
                    width = 100;
                    height = 40;
                }
                
                ShapeBase ellipse = new ShapeEllipse(ellipseX, ellipseY, width, height, fillColorPicker.getValue(),
                                           strokeColorPicker.getValue());
                shapes.add(ellipse);
                redraw(baseCanvas.getGc());
            }
        });
    }
       
    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void redraw(GraphicsContext gc) {
        gc.clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        for (ShapeBase shape : shapes) {
            shape.draw(gc);
        }
    }
    
    //funzione per gestire la selezione delle figure 
    private void select(MouseEvent event) {
        Iterator<ShapeBase> it = shapes.iterator();
        while (it.hasNext()) {
            ShapeBase elem = it.next();
            if (elem.containsPoint(event.getX(), event.getY())) {
                selectShape.setSelectedShape(elem);

                if (selectShape.getSelectedShape() == null) {
                    selectShape.setSelectedShape(null);
                } 
            }
        }
    }
    
    //funzione per il menu a tendina dopo aver cliccato tasto destro
     private void initContextMenu() {
        contextMenu.getItems().addAll(deleteMenu);
        baseCanvas.getCanvas().setOnContextMenuRequested(e -> contextMenu.show(baseCanvas.getCanvas(), e.getScreenX(), e.getScreenY()));
     

        deleteMenu.setOnAction(new EventHandler<ActionEvent>() { //set the action of the deleteMenu item
            public void handle(ActionEvent event) {
                delete();
                mod = "Delete";
            }
        });
    }
     
     public void delete() {
        command = new DeleteCommand(selectShape);
        command.execute();
        redraw(baseCanvas.getGc());
    }
     
     // Dropdown menù handlers
    public void handleSelect() {
        
    }
        
    // Dropdown menù handlers
    public void handleSave() {
        System.out.println("File saved");
    }
    
    

    // Called when Load menu item is clicked
    public void handleLoad() {
        System.out.println("File loaded");
    }
    
    @FXML
    private void handleNew() {
        baseCanvas.clear();
        System.out.println("Canvas cleared");
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit clicked");
        System.exit(0);
    }
}
