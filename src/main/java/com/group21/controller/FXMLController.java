package com.group21.controller;

import com.group21.model.Decorator.BaseCanvas;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

// Added for additional tools
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;

// Shapes
import com.group21.model.Shape.ShapeBase;
import com.group21.model.Shape.ShapeEllipse;
import com.group21.model.Shape.ShapeLine;
import com.group21.model.Shape.ShapeRectangle;

import com.group21.model.Command.*;

// Import for save/load 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
    private String currentMouseCommand = null;
    private List<ShapeBase> shapes = new ArrayList<>();
    private double lineStartX, lineStartY;
    private boolean isDrawingLine = false, isDrawingRectangle = false, isDrawingEllipse = false, isSelected = false;
    private Command command = null;
    private String mod = null;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(1000, 500);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        selectShape = new ShapeSelector(shapes, null);
        
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
                }
            } else if(e.isSecondaryButtonDown()){ //se invece clicco tasto destro...
                select(e);
                initContextMenu();
            }else if (e.isPrimaryButtonDown() && currentMouseCommand.equals("Select") ){
                isSelected = true;
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
            }else if(currentMouseCommand.equals("Select") && isSelected){
                select(e);
                isSelected = false; 
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

    // Disegna bordo porpora attorno alla forma selezionata
    ShapeBase selected = selectShape.getSelectedShape();
    if (selected != null) {
        gc.setStroke(Color.MEDIUMPURPLE);
        gc.setLineWidth(3.0);

        if (selected instanceof ShapeRectangle) {
            ShapeRectangle rect = (ShapeRectangle) selected;
            gc.strokeRect(rect.getX() - 5, rect.getY() - 5,
                          rect.getWidth() + 10, rect.getHeight() + 10);

        } else if (selected instanceof ShapeEllipse) {
            ShapeEllipse ellipse = (ShapeEllipse) selected;
            double x = ellipse.getX();
            double y = ellipse.getY();
            double width = ellipse.getWidth();
            double height = ellipse.getHeight();

            gc.strokeOval(x - 5, y - 5, width + 10, height + 10);


        } else if (selected instanceof ShapeLine) {
            ShapeLine line = (ShapeLine) selected;
            gc.setLineWidth(5.0); // bordo più spesso per visibilità
            gc.strokeLine(line.getX(), line.getY(), line.getEndX(), line.getEndY());
        }
    }
}

    
// Funzione per gestire la selezione delle figure
private void select(MouseEvent event) {
    double clickX = event.getX();
    double clickY = event.getY();

    // Scorri le forme dalla più recente alla più vecchia
    for (int i = shapes.size() - 1; i >= 0; i--) {
        ShapeBase shape = shapes.get(i);

        // Verifica se il punto cliccato è dentro la figura
        if (shape.containsPoint(clickX, clickY)) {
            // Se sì, imposta la figura come selezionata
            selectShape.setSelectedShape(shape);

            // Ridisegna il canvas con la nuova selezione evidenziata
            redraw(baseCanvas.getGc());
            return; // Selezionata una forma, possiamo uscire
        }
    }

    // Nessuna figura trovata sotto il click → deseleziona tutto
    selectShape.setSelectedShape(null);
    redraw(baseCanvas.getGc());
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
        
   // Save/Load logic implementation
    public void saveShapes(List<ShapeBase> shapes, File file){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(file, shapes);
            System.out.println("Saved " + shapes.size() + " shapes to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<ShapeBase> loadShapes(File file) {
        ObjectMapper mapper = new ObjectMapper();
        List<ShapeBase> shapes = null;

        try {
            shapes = mapper.readValue(file, 
                    mapper.getTypeFactory().constructCollectionType(List.class, ShapeBase.class));
            System.out.println("Loaded " + shapes.size() + " shapes from " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shapes;
    }

 // Dropdown menù handlers
    public void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shapes");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        Window window = baseCanvas.getCanvas().getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            saveShapes(shapes, file);
            showInfo("Save successful", "Saved " + shapes.size() + " shapes.");
        }
    }

    public void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Shapes");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        Window window = baseCanvas.getCanvas().getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            List<ShapeBase> loadedShapes = loadShapes(file);
            if (loadedShapes != null) {
                shapes.clear();
                shapes.addAll(loadedShapes);
                GraphicsContext gc = baseCanvas.getCanvas().getGraphicsContext2D();
                redraw(gc);
                showInfo("Load successful", "Loaded " + shapes.size() + " shapes.");
            }
        }
    }
    
    @FXML
    private void handleNew() {
        baseCanvas.clear();
        shapes.clear();
        this.redraw(baseCanvas.getGc());
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit clicked");
        System.exit(0);
    }
}
