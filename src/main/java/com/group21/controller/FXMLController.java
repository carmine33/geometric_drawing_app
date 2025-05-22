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
import com.group21.model.Shape.ShapePolygon;

import com.group21.model.Command.*;

// Import for save/load 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;

//import java.util.Iterator;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import java.util.Optional;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author carmi
 */
public class FXMLController implements Initializable {
    
    //@FXML private MenuItem menuNew, menuOpen, menuSave, menuExit;
    
    @FXML private Pane canvasPlaceholder;
    private BaseCanvas baseCanvas;
    @FXML private ColorPicker fillColorPicker;
    @FXML private ColorPicker strokeColorPicker;
    @FXML private Button btnRectangle;
    @FXML private Button btnEllipse;
    @FXML private Button btnLine;
    @FXML private Button btnPolygon;
    @FXML private Button btnSelect;
    
    private ShapeSelector selectShape;
    ContextMenu contextMenu = new ContextMenu();
    MenuItem deleteMenu = new MenuItem("Delete");
    MenuItem copyShape = new MenuItem("Copy");
    MenuItem pasteShape = new MenuItem("Paste");
    MenuItem setStrokeWidth = new MenuItem("Set border thickness");
    MenuItem setStrokeColor = new MenuItem("Set border color");
    MenuItem setFillColor = new MenuItem("Set fill color");

    private ShapeBase copiedShape = null;
    ShapePolygon currentPolygon = null;
    private List<Point2D> polygonPoints = new ArrayList<>();
    private String currentMouseCommand = null;
    private List<ShapeBase> shapes = new ArrayList<>();
    private double lineStartX, lineStartY;
    private boolean isDrawingLine = false, isDrawingRectangle = false, isDrawingEllipse = false, 
                    isDrawingPolygon = false, isSelected = false;
    private Command command = null;
    private String mod = null;
    private double previewStartX, previewStartY;
    private double previewCurrentX, previewCurrentY;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(1000, 500);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        selectShape = new ShapeSelector(shapes, null,fillColorPicker, strokeColorPicker);
        strokeColorPicker.setValue(Color.web("#000000"));
        
        btnRectangle.setOnAction(e -> currentMouseCommand = "Rectangle");
        btnEllipse.setOnAction(e -> currentMouseCommand = "Ellipse");
        btnLine.setOnAction(e -> currentMouseCommand = "Line");
        btnPolygon.setOnAction(e -> currentMouseCommand = "Polygon");
        btnSelect.setOnAction(e-> currentMouseCommand = "Select");

        baseCanvas.getCanvas().setOnMousePressed(e -> {
            if(e.isPrimaryButtonDown() && currentMouseCommand != null &&
              !currentMouseCommand.isEmpty() && !"Select".equals(currentMouseCommand)){
                lineStartX = e.getX();
                lineStartY = e.getY();
                
                previewStartX = lineStartX;
                previewStartY = lineStartY;
                previewCurrentX = lineStartX;
                previewCurrentY = lineStartY;
                
                if ("Line".equals(currentMouseCommand)) {
                    isDrawingLine = true;
                } else if("Rectangle".equals(currentMouseCommand)){
                    isDrawingRectangle = true;
                } else if("Ellipse".equals(currentMouseCommand)){
                    isDrawingEllipse = true;
                }else if("Polygon".equals(currentMouseCommand)){
                    isDrawingPolygon = true;
                    polygonPoints.add(new Point2D(lineStartX,lineStartY));
                }
            } else if(e.isSecondaryButtonDown()){
                if(!isDrawingPolygon){
                    select(e);
                    initContextMenu();
                }else{                    
                    // Finalize Polygon based on the number of vertices
                    if (polygonPoints.size() == 1) {
                        // DO NOTHING: only 1 point present in the polygon shape
                    }else if (polygonPoints.size() == 2) {
                        Point2D p1 = polygonPoints.get(0);
                        Point2D p2 = polygonPoints.get(1);
                        ShapeLine line = new ShapeLine(p1.getX(), p1.getY(), 0, 0,
                                           p2.getX(), p2.getY(),
                                           strokeColorPicker.getValue(), 1);
                        shapes.add(line);
                    }else if (polygonPoints.size() >= 3) {
                        ShapePolygon polygon = new ShapePolygon(new ArrayList<>(polygonPoints),
                                                    fillColorPicker.getValue(),
                                                    strokeColorPicker.getValue(), 1);
                        shapes.add(polygon);
                    }
                    polygonPoints.clear();
                    isDrawingPolygon = false;
                    redraw(baseCanvas.getGc());
                }
            } else if (e.isPrimaryButtonDown() && "Select".equals(currentMouseCommand)){
                isSelected = true;
            }
        });

        baseCanvas.getCanvas().setOnMouseReleased(e -> {
            double endX = e.getX();
            double endY = e.getY();
            if ("Line".equals(currentMouseCommand) && isDrawingLine) {
                isDrawingLine = false;
                ShapeBase line = new ShapeLine(lineStartX, lineStartY, 0, 0,
                    e.getX(), e.getY(), 
                    strokeColorPicker.getValue(),1
                );
                shapes.add(line);
                redraw(baseCanvas.getGc());
            } else if("Rectangle".equals(currentMouseCommand) && isDrawingRectangle){
                double x = Math.min(lineStartX, endX);
                double y = Math.min(lineStartY, endY);
                double width = Math.abs(endX - lineStartX);
                double height = Math.abs(endY - lineStartY);
                if(width == 0) {
                    width = 100;
                }
                if(height == 0){
                    height = 40;
                }
                
                isDrawingRectangle = false;
                ShapeBase rectangle = new ShapeRectangle(x, y, width, height, fillColorPicker.getValue(), strokeColorPicker.getValue(),1);
                shapes.add(rectangle);
                redraw(baseCanvas.getGc());
            } else if("Ellipse".equals(currentMouseCommand) && isDrawingEllipse){
                double x = Math.min(lineStartX, endX);
                double y = Math.min(lineStartY, endY);
                double width = Math.abs(endX - lineStartX);
                double height = Math.abs(endY - lineStartY);
                isDrawingEllipse = false;
                if(width == 0) {
                    width = 100;
                }
                if(height == 0){
                    height = 40;
                }
                ShapeBase ellipse = new ShapeEllipse(x, y, width, height, fillColorPicker.getValue(),
                    strokeColorPicker.getValue(),1);
                shapes.add(ellipse);
                redraw(baseCanvas.getGc());
            } else if("Select".equals(currentMouseCommand) && isSelected){
                select(e);
                isSelected = false; 
            }
        });
        
        baseCanvas.getCanvas().setOnMouseMoved( e-> {
            if(isDrawingPolygon && polygonPoints.size() >= 1){
                redraw(baseCanvas.getGc());

                // Draw preview
                baseCanvas.getGc().setStroke(strokeColorPicker.getValue());
                baseCanvas.getGc().setLineWidth(1.0);

                // Draw lines between all current points
                for (int i = 0; i < polygonPoints.size() - 1; i++) {
                    Point2D p1 = polygonPoints.get(i);
                    Point2D p2 = polygonPoints.get(i + 1);
                    baseCanvas.getGc().strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                }

                // Draw line from last point to current mouse position
                Point2D last = polygonPoints.get(polygonPoints.size() - 1);
                baseCanvas.getGc().strokeLine(last.getX(), last.getY(), e.getX(), e.getY());
            }
        });
        
        baseCanvas.getCanvas().setOnMouseDragged(e -> {
            if(!isDrawingPolygon && (isDrawingLine || isDrawingEllipse || isDrawingRectangle)){
                previewCurrentX = e.getX();
                previewCurrentY = e.getY();

                redraw(baseCanvas.getGc());

                baseCanvas.getGc().setStroke(strokeColorPicker.getValue());
                baseCanvas.getGc().setLineWidth(1.0);
                baseCanvas.getGc().setFill(fillColorPicker.getValue());
                
                if(isDrawingLine){
                    baseCanvas.getGc().strokeLine(previewStartX, previewStartY, previewCurrentX, previewCurrentY);
                
                }else if(isDrawingEllipse){
                    double x = Math.min(previewStartX, previewCurrentX);
                    double y = Math.min(previewStartY, previewCurrentY);
                    double w = Math.abs(previewCurrentX - previewStartX);
                    double h = Math.abs(previewCurrentY - previewStartY);
                    baseCanvas.getGc().fillOval(x, y, w, h);
                    baseCanvas.getGc().strokeOval(x, y, w, h);
                
                }else if(isDrawingRectangle){
                    double x = Math.min(previewStartX, previewCurrentX);
                    double y = Math.min(previewStartY, previewCurrentY);
                    double w = Math.abs(previewCurrentX - previewStartX);
                    double h = Math.abs(previewCurrentY - previewStartY);
                    baseCanvas.getGc().fillRect(x, y, w, h);
                    baseCanvas.getGc().strokeRect(x, y, w, h);
                
                }
            }else if(!isDrawingPolygon && !isDrawingLine && !isDrawingEllipse &&
                     !isDrawingRectangle && isSelected){
            
                ShapeBase selected = selectShape.getSelectedShape();
                if (selected == null) return;

                // New mouse coordinates
                double mouseX = e.getX();
                double mouseY = e.getY();

                // Resize the figure according to the new mouse position
                if (selected instanceof ShapeRectangle) {
                    ShapeRectangle rect = (ShapeRectangle) selected;
                    double newWidth = Math.max(10, mouseX - rect.getX());
                    double newHeight = Math.max(10, mouseY - rect.getY());
                    rect.setWidth(newWidth);
                    rect.setHeight(newHeight);

                } else if (selected instanceof ShapeEllipse) {
                    ShapeEllipse ellipse = (ShapeEllipse) selected;
                    double newWidth = Math.max(10, mouseX - ellipse.getX());
                    double newHeight = Math.max(10, mouseY - ellipse.getY());
                    ellipse.setWidth(newWidth);
                    ellipse.setHeight(newHeight);

                } else if (selected instanceof ShapeLine) {
                    ShapeLine line = (ShapeLine) selected;
                    line.setEndX(mouseX);
                    line.setEndY(mouseY);
                }else if (selected instanceof ShapePolygon){
                    ShapePolygon polygon = (ShapePolygon) selected;
                    // TODO: implements select of polygons with rectangle shape
                }

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

        // Draw purple border around selected shape
        ShapeBase selected = selectShape.getSelectedShape();
        if (selected != null) {
            gc.setStroke(Color.RED);
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
                gc.setLineWidth(5.0); // Thicker edge for visibility
                gc.strokeLine(line.getX(), line.getY(), line.getEndX(), line.getEndY());
            }
        }
    }
    
    // Select method for selecting shapes
    private void select(MouseEvent event) {
        double clickX = event.getX();
        double clickY = event.getY();

        // Scroll through the forms from newest to oldest
        for (int i = shapes.size() - 1; i >= 0; i--) {
            ShapeBase shape = shapes.get(i);

            // Check if the clicked point is inside the figure
            if (shape.containsPoint(clickX, clickY)) {
                // If so, set the figure as selected
                selectShape.setSelectedShape(shape);

                // Redraw the canvas with the new selection highlighted
                redraw(baseCanvas.getGc());
                return;
            }
        }

        // No figure found under click → deselect all
        selectShape.setSelectedShape(null);
        redraw(baseCanvas.getGc());
    }

    
    // Drop-down menù after right-click
     private void initContextMenu() {
        contextMenu.getItems().clear();
        contextMenu.getItems().addAll(deleteMenu, setStrokeColor, setStrokeWidth, setFillColor, copyShape, pasteShape);

        deleteMenu.setOnAction(e -> menuDeleteHandler());

        setStrokeColor.setOnAction(e ->menuModifyColorStroke());

        setFillColor.setOnAction(e -> menuModifyColorFill());

        setStrokeWidth.setOnAction(e ->menuModifyWidthStroke());

        copyShape.setOnAction(e ->menuCopyHandler());

        pasteShape.setOnAction(e ->menuPasteHandler());
        

        baseCanvas.getCanvas().setOnContextMenuRequested(e -> contextMenu.show(baseCanvas.getCanvas(), e.getScreenX(), e.getScreenY()));
    }
    
     
    public void menuModifyColorStroke() {
        
        command = new ModStrColorCommand(selectShape);
        command.execute();
        redraw(baseCanvas.getGc());
    } 
     
     public void menuModifyColorFill() {
        
        command = new ModFillColorCommand(selectShape);
        command.execute();
        redraw(baseCanvas.getGc());
  
    }
     
    
    public void menuModifyWidthStroke(){
        command = new ModStrWidthCommand(selectShape);
        command.execute();
        redraw(baseCanvas.getGc());
    }  
    
    public void menuCopyHandler (){
        
        command = new CopyCommand(selectShape);
        command.execute();
        
    } 
     
    public void menuPasteHandler(){     
        command = new PasteCommand(selectShape);
        command.execute();
        redraw(baseCanvas.getGc());  
    }
     
     
     public void menuDeleteHandler() {
        command = new DeleteCommand(selectShape);
        command.execute();
        redraw(baseCanvas.getGc());
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
