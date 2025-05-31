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
import com.group21.model.Decorator.GridDecorator;
import com.group21.model.Shape.ShapeTextBox;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
// Import for Polygon shape manipulation
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon; 
import com.group21.controller.Strategy.*;
import javafx.scene.input.MouseButton;

/**
 * FXML Controller class for the main view.
 * Handles window initialization and file menu actions.
 * 
 * @author claco
 */
public class FXMLController implements Initializable {
    
    //@FXML private MenuItem menuNew, menuOpen, menuSave, menuExit;
    
    @FXML private ScrollPane scrollPane;
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
    @FXML private Button btnPan;
    @FXML private Button btnToggleGrid;
    
    private Invoker invoker;
    private ShapeSelector selectShape;
    private List<ShapeBase> shapes = new ArrayList<>();
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

    private static final int CANVAS_MARGIN = 30; // margine uniforme
    private ShapeBase copiedShape = null;
    ShapePolygon currentPolygon = null;
    private List<Point2D> polygonPoints = new ArrayList<>();
    private String currentMouseCommand = null;
    private double lineStartX, lineStartY;
    private boolean isDrawingLine = false, isDrawingRectangle = false, isDrawingEllipse = false, 
                    isDrawingPolygon = false, isSelected = false, isDrawingTextBox=false, isPanMode=false;
    private Command command = null;
    private String mod = null;
    private double previewStartX, previewStartY;
    private double previewCurrentX, previewCurrentY;
    private ShapeBase previewShape = null;
    
    private final double[] zoomLevels = {0.25,0.5, 1.0, 1.5, 1.75};
    private int currentZoomIndex = 2; // corrisponde a 1.0
    private double zoomFactor = zoomLevels[currentZoomIndex];
    
    private Point2D lastMousePos = null;
    private double lastMouseX;
    private double lastMouseY;
    private GridDecorator gridDecorator;
    private boolean isGridVisible = false;
    private double currentMouseX;
    private double currentMouseY;
    
    private boolean multiSelectMode = false;
    private double selectionStartX, selectionStartY;
    private Rectangle selectionRectangle;
    private final List<ShapeBase> selectedShapes = new ArrayList<>();
    
    private DrawingToolContext toolContext = new DrawingToolContext();
    private boolean hasSavedStateDuringDrag = false;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        baseCanvas = new BaseCanvas(4000,4000);
        baseCanvas.getCanvas().setCache(false);
        canvasPlaceholder.getChildren().add(baseCanvas.getCanvas());
        scrollPane.setPannable(false);//scrollPane inizialmente inattivo
        selectShape = new ShapeSelector(shapes, null,fillColorPicker, strokeColorPicker);
        strokeColorPicker.setValue(Color.web("#000000"));
        invoker = new Invoker();
        gridDecorator = new GridDecorator(baseCanvas);
        
        //Strategy Pattern
        btnSelect.setOnAction(e -> {
            currentMouseCommand = "Select";
        });

        btnLine.setOnAction(e -> {
            currentMouseCommand = "Line";
            final LineTool[] toolHolder = new LineTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new LineTool(shapes, strokeColorPicker.getValue(), callback);
            toolContext.setStrategy(toolHolder[0]);
        });

        btnRectangle.setOnAction(e -> {
            currentMouseCommand = "Rectangle";
            final RectangleTool[] toolHolder = new RectangleTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new RectangleTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), callback);
            toolContext.setStrategy(toolHolder[0]);
        });

        btnEllipse.setOnAction(e -> {
            currentMouseCommand = "Ellipse";
            final EllipseTool[] toolHolder = new EllipseTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new EllipseTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), callback);
            toolContext.setStrategy(toolHolder[0]);
        });

        btnTextBox.setOnAction(e -> {
            currentMouseCommand = "TextBox";
            final TextBoxTool[] toolHolder = new TextBoxTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new TextBoxTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), callback);
            toolContext.setStrategy(toolHolder[0]);
        });

        btnPolygon.setOnAction(e -> {
            currentMouseCommand = "Polygon";
            final PolygonTool[] toolHolder = new PolygonTool[1];
            Runnable callback = () -> {
                setPreviewShape(toolHolder[0].getPreviewShape());
                redraw(baseCanvas.getGc());
            };
            toolHolder[0] = new PolygonTool(shapes, strokeColorPicker.getValue(), fillColorPicker.getValue(), callback);
            toolContext.setStrategy(toolHolder[0]);
        });
        
         btnZoomIn.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon || isDrawingTextBox) return;
            if (currentZoomIndex < zoomLevels.length - 1) {
                currentZoomIndex++;
                zoomFactor = zoomLevels[currentZoomIndex];
                
                updateScrollPaneViewport();
                redraw(baseCanvas.getGc());
            }
        });
        
        //attiva il panning dello scrollpane
        btnPan.setOnAction(e -> {
            boolean newPanState = !scrollPane.isPannable();
            scrollPane.setPannable(newPanState);

            if (newPanState) {
                currentMouseCommand = "Pan";
                toolContext.setStrategy(null); // disattiva qualsiasi tool
                selectShape.setSelectedShape(null); // ❗ rimuove selezione figura
                redraw(baseCanvas.getGc());         // ❗ aggiorna canvas senza selezione
            } else {
                currentMouseCommand = null;
            }

            setActiveToolButton(newPanState ? btnPan : null);
        });

        
        btnZoomOut.setOnAction(e -> {
            if (isDrawingRectangle || isDrawingEllipse || isDrawingLine || isDrawingPolygon || isDrawingTextBox) return;
            if (currentZoomIndex > 0) {
                currentZoomIndex--;
                zoomFactor = zoomLevels[currentZoomIndex];
                
                updateScrollPaneViewport();
                redraw(baseCanvas.getGc());
            }
        });

        btnToggleGrid.setOnAction(e -> {
            isGridVisible = !isGridVisible;
            redraw(baseCanvas.getGc());
        });
        
        baseCanvas.getCanvas().setOnMousePressed(e -> {

        // Nascondi il context menu se visibile
        if (contextMenu.isShowing() && e.isPrimaryButtonDown()) {
            contextMenu.hide();
        }

        // Se tasto destro (secondary), attiva menu o chiusura poligono
        if (e.isSecondaryButtonDown()) {
            if (!"Polygon".equals(currentMouseCommand)) {
                if(!contextMenu.isShowing()){
                select(e);
                initContextMenu();
                }
        }
            // Offset per lo scroll e zoom
            double scrollXOffset = scrollPane.getHvalue() * 
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() * 
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            if (toolContext.getStrategy() != null) {
                toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
            }
            toolContext.handleMousePressed(e);
        }

        // Se selezione attiva
        if ("Select".equals(currentMouseCommand)) {
            isSelected = true;
            select(e);  // <-- essenziale per selezionare forma cliccata

            ShapeBase selected = selectShape.getSelectedShape();
            if (selected != null) {
                selectShape.getMemory().saveState(new ArrayList<>(shapes));

                if (selected instanceof ShapePolygon) {
                    ShapePolygon polygon = (ShapePolygon) selected;
                    polygon.storeOriginalVertices();
                    polygon.setResizeAnchor(polygon.getBoundingBoxTopLeft());
                }
            }
        }

        // Altrimenti: modalità disegno (linea, rettangolo, ecc.)
        else {
            // Offset per lo scroll e zoom
            double scrollXOffset = scrollPane.getHvalue() * 
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() * 
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            if (toolContext.getStrategy() != null) {
                toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
            }
            toolContext.handleMousePressed(e);
        }
    });


    baseCanvas.getCanvas().setOnMouseReleased(e -> {
        // Se non è tasto sinistro, ignora
        if (e.getButton() != MouseButton.PRIMARY) return;
        
        //tiene memoria della posizione di riposizionamento
        selectShape.getMemory().saveState(new ArrayList<>(shapes));
        // Tool di disegno
        if (!"Select".equals(currentMouseCommand)) {
            // Offset per lo scroll e zoom
            double scrollXOffset = scrollPane.getHvalue() * 
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() * 
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            if (toolContext.getStrategy() != null) {
                toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
            }
            toolContext.handleMouseReleased(e);
        }

        // Modalità selezione
        else if (isSelected) {
            select(e);
            isSelected = false;
        }
        lastMousePos = null;
    });


        
baseCanvas.getCanvas().setOnMouseMoved(e -> {
    if (!"Select".equals(currentMouseCommand)) {
        // Offset per lo scroll e zoom
            double scrollXOffset = scrollPane.getHvalue() * 
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() * 
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            if (toolContext.getStrategy() != null) {
                toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
            }
        toolContext.handleMouseMoved(e);
    }
});



        
baseCanvas.getCanvas().setOnMouseDragged(e -> {

    // Disegno anteprima solo con tasto sinistro premuto
    if (!"Select".equals(currentMouseCommand) && e.isPrimaryButtonDown()) {
        // Offset per lo scroll e zoom
            double scrollXOffset = scrollPane.getHvalue() * 
                (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
            double scrollYOffset = scrollPane.getVvalue() * 
                (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

            if (toolContext.getStrategy() != null) {
                toolContext.getStrategy().setOffset(scrollXOffset, scrollYOffset, zoomFactor);
            }
        toolContext.handleMouseDragged(e);
    }

    // Gestione spostamento/ridimensionamento selezione
    else if ("Select".equals(currentMouseCommand)) {
        ShapeBase selected = selectShape.getSelectedShape();
        if (selected == null) return;

        double scrollXOffset = scrollPane.getHvalue() * (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
        double scrollYOffset = scrollPane.getVvalue() * (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;
        double mouseX = (e.getX() / zoomFactor) + scrollXOffset;
        double mouseY = (e.getY() / zoomFactor) + scrollYOffset;

        if (lastMousePos == null) {
            lastMousePos = new Point2D(mouseX, mouseY);
            return;
        }

        double dx = mouseX - lastMousePos.getX();
        double dy = mouseY - lastMousePos.getY();

        if (e.isControlDown()) {
            selected.translate(dx, dy);
        } else {
            this.resizeShape(selected, mouseX, mouseY);
        }

        lastMousePos = new Point2D(mouseX, mouseY);
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

    double scrollXOffset = scrollPane.getHvalue() *
        (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
    double scrollYOffset = scrollPane.getVvalue() *
        (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

    gc.translate(-scrollXOffset * zoomFactor, -scrollYOffset * zoomFactor);
    gc.scale(zoomFactor, zoomFactor);

    if (isGridVisible) {
        gridDecorator.drawGrid(gc, scrollXOffset, scrollYOffset, zoomFactor);
    }

    for (ShapeBase shape : shapes) {
        shape.draw(gc);
    }

    ShapeBase selected = selectShape.getSelectedShape();
    if (selected != null) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3.0);

        if (selected instanceof ShapeRectangle) {
            ShapeRectangle rect = (ShapeRectangle) selected;
            gc.setLineWidth(2.0);
            gc.strokeRect(rect.getX() - 5, rect.getY() - 5, rect.getWidth() + 10, rect.getHeight() + 10);
        } else if (selected instanceof ShapeEllipse) {
            ShapeEllipse ellipse = (ShapeEllipse) selected;
            gc.setLineWidth(2.0);
            gc.strokeOval(ellipse.getX() - 5, ellipse.getY() - 5,
                          ellipse.getWidth() + 10, ellipse.getHeight() + 10);
        } else if (selected instanceof ShapeLine) {
            ShapeLine line = (ShapeLine) selected;
            gc.setLineWidth(2.0);
            gc.strokeLine(line.getX(), line.getY(), line.getEndX(), line.getEndY());
        } else if (selected instanceof ShapePolygon) {
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

            Geometry outline = jtsPoly.buffer(5.0);
            Coordinate[] offsetCoords = outline.getCoordinates();

            gc.beginPath();
            gc.setStroke(Color.RED);
            gc.setLineWidth(2.0);
            gc.moveTo(offsetCoords[0].x, offsetCoords[0].y);
            for (int i = 1; i < offsetCoords.length; i++) {
                gc.lineTo(offsetCoords[i].x, offsetCoords[i].y);
            }
            gc.stroke();
        } else if (selected instanceof ShapeTextBox) {
            ShapeTextBox box = (ShapeTextBox) selected;
            double w = box.getTextWidth() + 11;
            double h = box.getTextHeight() + 6;
            gc.strokeRect(box.getX(), box.getY(), w, h);
        }
    }

    if (isDrawingPolygon && polygonPoints.size() >= 1 && currentMouseX >= 0 && currentMouseY >= 0) {
        gc.setStroke(strokeColorPicker.getValue());
        gc.setLineWidth(1.0);
        for (int i = 0; i < polygonPoints.size() - 1; i++) {
            Point2D p1 = polygonPoints.get(i);
            Point2D p2 = polygonPoints.get(i + 1);
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
        Point2D last = polygonPoints.get(polygonPoints.size() - 1);
        gc.strokeLine(last.getX(), last.getY(), currentMouseX, currentMouseY);
    }

    if (toolContext.getStrategy() != null && toolContext.getStrategy().getPreviewShape() != null) {
        toolContext.getStrategy().getPreviewShape().draw(gc);
    }
    // Anteprima in tempo reale del poligono (aggiunta dinamica)
    ShapeBase preview = toolContext.getStrategy() != null ? toolContext.getStrategy().getPreviewShape() : null;
    if (preview != null) {
        preview.draw(gc);
    }

    gc.restore();
    canvasPlaceholder.requestLayout();
}


    
private void adjustCanvasSizeIfNeeded(double requiredWidth, double requiredHeight) {
    double currentWidth = baseCanvas.getCanvas().getWidth();
    double currentHeight = baseCanvas.getCanvas().getHeight();

    if (requiredWidth > currentWidth || requiredHeight > currentHeight) {
        double newWidth = Math.max(currentWidth, requiredWidth);
        double newHeight = Math.max(currentHeight, requiredHeight);

        baseCanvas.getCanvas().setWidth(newWidth);
        baseCanvas.getCanvas().setHeight(newHeight);

        // Imposta il canvasPlaceholder alle stesse dimensioni del canvas
        canvasPlaceholder.setPrefSize(newWidth, newHeight);
        canvasPlaceholder.setMinSize(newWidth, newHeight);
        canvasPlaceholder.setMaxSize(newWidth, newHeight);
        canvasPlaceholder.resize(newWidth, newHeight);
        canvasPlaceholder.layout();
        scrollPane.layout();  // forza il ricalcolo della viewport della ScrollPane


        // Applica lo zoom come scaling (non va usato *zoomFactor nelle size)
        updateScrollPaneViewport();
        scrollPane.setHvalue(scrollPane.getHvalue()); // forza aggiornamento
        scrollPane.setVvalue(scrollPane.getVvalue());

    }
}

    // Select method for selecting shapes
    private void select(MouseEvent event) {
        double scrollXOffset = scrollPane.getHvalue() *
        (baseCanvas.getCanvas().getWidth() - scrollPane.getViewportBounds().getWidth()) / zoomFactor;
    double scrollYOffset = scrollPane.getVvalue() *
        (baseCanvas.getCanvas().getHeight() - scrollPane.getViewportBounds().getHeight()) / zoomFactor;

    double clickX = (event.getX() / zoomFactor) + scrollXOffset;
    double clickY = (event.getY() / zoomFactor) + scrollYOffset;

        // Scroll through the forms from newest to oldest
        for (int i = shapes.size() - 1; i >= 0; i--) {
            ShapeBase shape = shapes.get(i);

            // Check if the clicked point is inside the figure
            if (shape.containsPoint(clickX, clickY)) {
                // If so, set the figure as selected
                selectShape.setSelectedShape(shape);
                lastMousePos = null;
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
        

        baseCanvas.getCanvas().setOnContextMenuRequested(e -> {
    if (isDrawingPolygon) {
        e.consume();  // Impedisce l'apertura del context menu
    } else {
        contextMenu.show(baseCanvas.getCanvas(), e.getScreenX(), e.getScreenY());
    }
});
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
        redraw(baseCanvas.getGc());
    }
     
     //il command toFront è creato passando come argomenti la posizione della forma selezionata nella lista e la dimensione della lista stessa;
     //infine è invocato il command
      public void toFront(double index, double size) {
        command = new ToFrontCommand(selectShape, index, size);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());

    }
      
     //il command toBack è creato passando come argomento la posizione della forma selezionata nella lista;
     //infine è invocato il command
       public void toBack(double index) {
        command = new ToBackCommand(selectShape, index);
        invoker.setCommand(command);
        invoker.startCommand();
        redraw(baseCanvas.getGc());

    }
       
public void performUndo() {
    if (selectShape.getMemory().canUndo()) {
        ShapeBase oldSelected = selectShape.getSelectedShape();

        shapes.clear();
        List<ShapeBase> restored = selectShape.getMemory().restoreLastState();
        shapes.addAll(restored);

        // Deselect by default
        selectShape.setSelectedShape(null);

        // Facoltativo: ripristina selezione se esiste equivalente
        if (oldSelected != null) {
            for (ShapeBase shape : restored) {
                if (shape.equals(oldSelected)) {  // usa equals definito correttamente
                    selectShape.setSelectedShape(shape);
                    break;
                }
            }
        }
        redraw(baseCanvas.getGc());
    } else {
        showInfo("Undo", "Nessuno stato precedente disponibile.");
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
                selectShape.getMemory().saveState(new ArrayList<>(shapes));
                shapes.clear();
                selectShape.setSelectedShape(null);
                shapes.addAll(loadedShapes);
                GraphicsContext gc = baseCanvas.getCanvas().getGraphicsContext2D();
                redraw(gc);
                showInfo("Load successful", "Loaded " + shapes.size() + " shapes.");
            }
        }
    }
    
private void updateScrollPaneViewport() {
    double canvasWidth = baseCanvas.getCanvas().getWidth();
    double canvasHeight = baseCanvas.getCanvas().getHeight();

    // Calcola le dimensioni scalate per il canvas
    double scaledWidth = canvasWidth * zoomFactor;
    double scaledHeight = canvasHeight * zoomFactor;

    // Imposta le dimensioni effettive del canvasPlaceholder (e quindi dello ScrollPane)
    canvasPlaceholder.setPrefWidth(scaledWidth);
    canvasPlaceholder.setPrefHeight(scaledHeight);
    canvasPlaceholder.setMinWidth(scaledWidth);
    canvasPlaceholder.setMinHeight(scaledHeight);
    canvasPlaceholder.setMaxWidth(scaledWidth);
    canvasPlaceholder.setMaxHeight(scaledHeight);

    scrollPane.setPannable(true);
    scrollPane.layout(); // forza aggiornamento layout
}

    @FXML
    private void handleNew() {
        selectShape.getMemory().saveState(new ArrayList<>(shapes));
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
    
    private void setActiveToolButton(Button activeButton) {
         btnPan.setStyle("");
         btnSelect.setStyle("");
         btnLine.setStyle("");
         btnRectangle.setStyle("");
         btnEllipse.setStyle("");
         btnPolygon.setStyle("");
         btnTextBox.setStyle("");

         if (activeButton != null) {
             activeButton.setStyle("-fx-background-color: lightblue;");
         }
     }

   
     @FXML
    private void onMultiSelectMode() {
         multiSelectMode = true;
    }
    
    @FXML
    private void onGroupSelected() {

    }
    
    private void resizeShape(ShapeBase selected, double mouseX, double mouseY) {
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

        } else if (selected instanceof ShapePolygon) {
            ShapePolygon polygon = (ShapePolygon) selected;
            Point2D anchor = polygon.getResizeAnchor();
            double anchorX = anchor.getX();
            double anchorY = anchor.getY();

            double initialWidth = polygon.getOriginalBoundingBoxWidth();
            double initialHeight = polygon.getOriginalBoundingBoxHeight();
            if (initialWidth == 0 || initialHeight == 0) return;

            double newWidth = Math.max(10, mouseX - anchorX);
            double newHeight = Math.max(10, mouseY - anchorY);

            double scaleX = Math.max(0.1, newWidth / initialWidth);
            double scaleY = Math.max(0.1, newHeight / initialHeight);

            List<Point2D> scaled = new ArrayList<>();
            for (Point2D pt : polygon.getOriginalVertices()) {
                double dx = pt.getX() - anchorX;
                double dy = pt.getY() - anchorY;
                scaled.add(new Point2D(anchorX + dx * scaleX, anchorY + dy * scaleY));
            }

            polygon.setVertices(scaled);

        } else if (selected instanceof ShapeTextBox) {
            ShapeTextBox text = (ShapeTextBox) selected;
            double newFontSize = Math.max(8, mouseY - text.getY());
            text.setFontSize(newFontSize);
        }
    }

    //gestisce l'anteprima con lo Strategy
        public void setPreviewShape(ShapeBase shape) {
        this.previewShape = shape;
    }
    
}
