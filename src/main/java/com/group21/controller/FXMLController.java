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
import com.group21.model.Shape.ShapeTextBox;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.util.Arrays;
import java.util.Iterator;

//import java.util.Iterator;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
// Import for Polygon shape manipulation
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon; 


/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author claco
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
    @FXML private Button btnTextBox;
    @FXML private Button btnZoomIn;
    @FXML private Button btnZoomOut;
    
    private Invoker invoker;
    private ShapeSelector selectShape;
    ContextMenu contextMenu = new ContextMenu();
    MenuItem deleteMenu = new MenuItem("Delete");
    MenuItem copyShape = new MenuItem("Copy");
    MenuItem pasteShape = new MenuItem("Paste");
    MenuItem setStrokeWidth = new MenuItem("Set border thickness");
    MenuItem setStrokeColor = new MenuItem("Set border color");
    MenuItem setFillColor = new MenuItem("Set fill color");
    MenuItem modifyTextBox = new MenuItem("Modify text");
    MenuItem toFront = new MenuItem("ToFront");
    MenuItem toBack = new MenuItem("ToBack");


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
    private ShapeBase previewShape = null;
    
    private final double[] zoomLevels = {0.25,0.5, 1.0, 1.5, 1.75};
    private int currentZoomIndex = 2; // corrisponde a 1.0
    private double zoomFactor = zoomLevels[currentZoomIndex];
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(1000, 500);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        selectShape = new ShapeSelector(shapes, null,fillColorPicker, strokeColorPicker);
        strokeColorPicker.setValue(Color.web("#000000"));
        invoker = new Invoker();
        
        btnRectangle.setOnAction(e -> currentMouseCommand = "Rectangle");
        btnEllipse.setOnAction(e -> currentMouseCommand = "Ellipse");
        btnLine.setOnAction(e -> currentMouseCommand = "Line");
        btnPolygon.setOnAction(e -> currentMouseCommand = "Polygon");
        btnSelect.setOnAction(e-> currentMouseCommand = "Select");
        btnTextBox.setOnAction(e -> currentMouseCommand = "TextBox");
        
         btnZoomIn.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon) return;
            if (currentZoomIndex < zoomLevels.length - 1) {
                currentZoomIndex++;
                zoomFactor = zoomLevels[currentZoomIndex];
                redraw(baseCanvas.getGc());
            }
        });

        btnZoomOut.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon) return;
            if (currentZoomIndex > 0) {
                currentZoomIndex--;
                zoomFactor = zoomLevels[currentZoomIndex];
                redraw(baseCanvas.getGc());
            }
        });

        baseCanvas.getCanvas().setOnMousePressed(e -> {
            if(contextMenu.isShowing()){
                contextMenu.hide();
            }
            if(e.isPrimaryButtonDown() && currentMouseCommand != null &&
              !currentMouseCommand.isEmpty() && !"Select".equals(currentMouseCommand)){
                zoomFactor = 1.0;
                currentZoomIndex = 2; 
                redraw(baseCanvas.getGc());
                lineStartX = e.getX()/zoomFactor;
                lineStartY = e.getY()/zoomFactor;
                
                previewStartX = lineStartX;
                previewStartY = lineStartY;
                previewCurrentX = lineStartX;
                previewCurrentY = lineStartY;
                
                if(!isSelected && selectShape.getSelectedShape()!= null){
                    selectShape.setSelectedShape(null);
                }
                
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
                if(!isDrawingPolygon && !contextMenu.isShowing()){
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
                // Reset dello zoom al valore predefinito
                zoomFactor = 1.0;
                currentZoomIndex = 2; // corrisponde a 1.0
                redraw(baseCanvas.getGc());
            } else if (e.isPrimaryButtonDown() && "Select".equals(currentMouseCommand)){
                isSelected = true;
                // Stores OG vertices if we're selecting a polygon
                ShapeBase selected = selectShape.getSelectedShape();
                if(selected instanceof ShapePolygon){
                    ShapePolygon polygon = (ShapePolygon) selected;
                    polygon.storeOriginalVertices();
                    polygon.setResizeAnchor(polygon.getBoundingBoxTopLeft());
                }
            }
        });

        baseCanvas.getCanvas().setOnMouseReleased(e -> {
            double endX = e.getX()/zoomFactor;
            double endY = e.getY()/zoomFactor;
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
            }else if ("TextBox".equals(currentMouseCommand)) {
                // 1. Inserisci il testo
                TextInputDialog textDialog = new TextInputDialog("Testo");
                textDialog.setTitle("Nuova TextBox");
                textDialog.setHeaderText("Inserisci il contenuto:");
                textDialog.setContentText("Testo:");
                Optional<String> resultText = textDialog.showAndWait();
                if (resultText.isEmpty()) return;

                // 2. Inserisci la dimensione
                TextInputDialog sizeDialog = new TextInputDialog("14");
                sizeDialog.setTitle("Dimensione font");
                sizeDialog.setHeaderText("Inserisci la dimensione del testo:");
                sizeDialog.setContentText("Dimensione:");
                Optional<String> resultSize = sizeDialog.showAndWait();
                double fontSize = 14;
                if (resultSize.isPresent()) {
                    try {
                        fontSize = Double.parseDouble(resultSize.get());
                    } catch (NumberFormatException ignored) {}
                }

                // 3. Scegli il tipo di font
                ChoiceDialog<String> fontDialog = new ChoiceDialog<>("Sans", "Sans", "Serif", "Monospace");
                fontDialog.setTitle("Tipo di font");
                fontDialog.setHeaderText("Scegli il font:");
                fontDialog.setContentText("Font:");
                Optional<String> resultFont = fontDialog.showAndWait();
                String fontFamily = "SansSerif";  // default Java name
                if (resultFont.isPresent()) {
                    switch (resultFont.get()) {
                        case "Serif": fontFamily = "Serif"; break;
                        case "Monospace": fontFamily = "Monospaced"; break;
                        default: fontFamily = "SansSerif"; break;
                    }
                }

                // Crea la nuova TextBox con tutto
                ShapeTextBox newTextBox = new ShapeTextBox(
                    endX, endY, 0, 0,
                    fillColorPicker.getValue(),
                    strokeColorPicker.getValue(),
                    1.0,
                    resultText.get()
                );
                newTextBox.setFontSize(fontSize);
                newTextBox.setFontFamily(fontFamily);

                shapes.add(newTextBox);
                selectShape.setSelectedShape(newTextBox);
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
                previewCurrentX = e.getX()/zoomFactor;
                previewCurrentY = e.getY()/zoomFactor;

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
                    Point2D anchor = polygon.getResizeAnchor();
                    double anchorX = anchor.getX();
                    double anchorY = anchor.getY();

                    double initialWidth = polygon.getOriginalBoundingBoxWidth();
                    double initialHeight = polygon.getOriginalBoundingBoxHeight();
                    if (initialWidth == 0 || initialHeight == 0) return;

                    double newWidth = Math.max(10, e.getX() - anchorX);
                    double newHeight = Math.max(10, e.getY() - anchorY);

                    double scaleX = Math.max(0.1, newWidth / initialWidth);
                    double scaleY = Math.max(0.1, newHeight / initialHeight);

                    List<Point2D> scaled = new ArrayList<>();
                    for (Point2D pt : polygon.getOriginalVertices()) {
                        double dx = pt.getX() - anchorX;
                        double dy = pt.getY() - anchorY;
                        scaled.add(new Point2D(anchorX + dx * scaleX, anchorY + dy * scaleY));
                    }

                    polygon.setVertices(scaled);
                }else if (selected instanceof ShapeTextBox) {
                    ShapeTextBox text = (ShapeTextBox) selected;

                    // Calcolo approssimativo: altezza del testo ≈ dimensione font
                    double newFontSize = Math.max(8, mouseY - text.getY());

                    text.setFontSize(newFontSize);
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
        gc.save();
        if (!isDrawingRectangle && !isDrawingEllipse && !isDrawingLine && !isDrawingPolygon) {
            gc.scale(zoomFactor, zoomFactor); // Applica lo zoom solo in modalità visualizzazione
        }

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
                gc.setLineWidth(2.0);
                gc.strokeRect(rect.getX() - 5, rect.getY() - 5,
                              rect.getWidth() + 10, rect.getHeight() + 10);

            } else if (selected instanceof ShapeEllipse) {
                ShapeEllipse ellipse = (ShapeEllipse) selected;
                double x = ellipse.getX();
                double y = ellipse.getY();
                double width = ellipse.getWidth();
                double height = ellipse.getHeight();
                gc.setLineWidth(2.0);
                gc.strokeOval(x - 5, y - 5, width + 10, height + 10);

            } else if (selected instanceof ShapeLine) {
                ShapeLine line = (ShapeLine) selected;
                gc.setLineWidth(2.0); // Thicker edge for visibility
                gc.strokeLine(line.getX(), line.getY(), line.getEndX(), line.getEndY());
            } else if (selected instanceof ShapePolygon){
                ShapePolygon polygon = (ShapePolygon) selected;
                List<Point2D> points = polygon.getVertices();
                Coordinate[] coords = points.stream()
                    .map(p -> new Coordinate(p.getX(), p.getY()))
                    .toArray(Coordinate[]::new);
                coords = Arrays.copyOf(coords, coords.length + 1);
                coords[coords.length - 1] = coords[0];

                GeometryFactory gf = new GeometryFactory();
                LinearRing ring = gf.createLinearRing(coords);
                Polygon jtsPoly = gf.createPolygon(ring);

                // Create buffer (offset)
                Geometry outline = jtsPoly.buffer(5.0); // 5 px outward

                // Extract and draw the offset polygon
                Coordinate[] offsetCoords = outline.getCoordinates();
                gc.beginPath();
                gc.setStroke(Color.RED);
                gc.setLineWidth(2.0);
                gc.moveTo(offsetCoords[0].x, offsetCoords[0].y);
                for (int i = 1; i < offsetCoords.length; i++) {
                    gc.lineTo(offsetCoords[i].x, offsetCoords[i].y);
                }
                gc.stroke();
                
            }else if (selected instanceof ShapeTextBox) {
                ShapeTextBox box = (ShapeTextBox) selected;

                double w = box.getTextWidth() + 10;   // margine orizzontale
                double h = box.getTextHeight() + 6;   // margine verticale
                double x = box.getX();
                double y = box.getY();

                gc.strokeRect(x, y, w, h);
            }
        }
        gc.restore();
        if(previewShape!=null){
            previewShape.draw(gc);
        }
    }
    
    // Select method for selecting shapes
    private void select(MouseEvent event) {
        double clickX = event.getX()/zoomFactor;
        double clickY = event.getY()/zoomFactor;

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
        contextMenu.getItems().addAll(deleteMenu, setStrokeColor, setStrokeWidth, setFillColor, copyShape, pasteShape,toFront,toBack);

        deleteMenu.setOnAction(e -> menuDeleteHandler());
        
        toFront.setOnAction(e -> toFront(shapes.indexOf(selectShape.getSelectedShape()), shapes.size()));
        toBack.setOnAction(e -> toBack(shapes.indexOf(selectShape.getSelectedShape())));

        setStrokeColor.setOnAction(e ->menuModifyColorStroke());

        setFillColor.setOnAction(e -> menuModifyColorFill());

        setStrokeWidth.setOnAction(e ->menuModifyWidthStroke());

        copyShape.setOnAction(e ->menuCopyHandler());

        pasteShape.setOnAction(e ->menuPasteHandler());
        

        baseCanvas.getCanvas().setOnContextMenuRequested(e -> contextMenu.show(baseCanvas.getCanvas(), e.getScreenX(), e.getScreenY()));
    }
    
     
    public void menuModifyColorStroke() {
        
        command = new ModColorCommand(selectShape,"stroke");
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
    } 
     
    public void menuModifyColorFill() {
        
        command = new ModColorCommand(selectShape,"fill");
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
        }

     
    
    public void menuModifyWidthStroke(){
        command = new ModStrWidthCommand(selectShape);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());
    }  
    
    public void menuCopyHandler (){
        
        command = new CopyCommand(selectShape);
        command.execute();
        
    } 
     
    public void menuPasteHandler(){     
        command = new PasteCommand(selectShape);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());  
    }
     
     
     public void menuDeleteHandler() {
        command = new DeleteCommand(selectShape);
        invoker.setCommand(command);
        invoker.startCommand();
        Iterator<ShapeBase> it =shapes.iterator();
        baseCanvas.getGc().clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        while (it.hasNext()) {
            ShapeBase elem = it.next();
            elem.draw(baseCanvas.getGc());
        }
    }
     
     //il command toFront è creato passando come argomenti la posizione della forma selezionata nella lista e la dimensione della lista stessa;
     //infine è invocato il command
      public void toFront(double index, double size) {
        command = new ToFrontCommand(selectShape, index, size);
        invoker.setCommand(command);
        invoker.startCommand();
        Iterator<ShapeBase> it =shapes.iterator();
        baseCanvas.getGc().clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        while (it.hasNext()) {
            ShapeBase elem = it.next();
            elem.draw(baseCanvas.getGc());
        }
    }
      
     //il command toBack è creato passando come argomento la posizione della forma selezionata nella lista;
     //infine è invocato il command
       public void toBack(double index) {
        command = new ToBackCommand(selectShape, index);
        invoker.setCommand(command);
        invoker.startCommand();
        Iterator<ShapeBase> it =shapes.iterator();
        baseCanvas.getGc().clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        while (it.hasNext()) {
            ShapeBase elem = it.next();
            elem.draw(baseCanvas.getGc());
        }
    }
       
       public void performUndo() {
        invoker.startUndo();
        baseCanvas.getGc().clearRect(0, 0, baseCanvas.getCanvas().getWidth(), baseCanvas.getCanvas().getHeight());
        for (ShapeBase elem : shapes) {
            elem.draw(baseCanvas.getGc());
        } 
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
                selectShape.setSelectedShape(null);
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
        selectShape.setSelectedShape(null);
        this.redraw(baseCanvas.getGc());
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit clicked");
        System.exit(0);
    }
    
     @FXML
    private void undoCommand(ActionEvent e) {
        performUndo();
    }
}
