package controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.interfaces.Shape;
import models.shapes.Line;
import models.shapes.ShapesFactory;
import models.shapes.Triangle;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.net.URL;
import java.util.*;
import java.util.List;

public class SampleController implements Initializable{
    private String filePath;
    private Painter painter;
    private ShapesFactory factory = new ShapesFactory();
    private BiMap<String, Shape> shapesMap;
    List<Point2D> clickHistory = new ArrayList<Point2D>();

    @FXML private Canvas canvas;

    @FXML private Menu shapesMenu;

    @FXML private JFXComboBox<Label> toolsComboBox;
    @FXML private JFXColorPicker fillColorPicker;
    @FXML private JFXColorPicker strokeColorPicker;
    @FXML private JFXSlider strokeSlider;

    @FXML private Canvas strokePreviewCanvas;
    @FXML private JFXBadge undoBadge;
    @FXML private JFXBadge redoBadge;
    @FXML private JFXBadge saveBadge;
    @FXML private JFXBadge deselectBadge;
    @FXML private JFXBadge cloneBadge;
    @FXML private JFXBadge deleteBadge;

    @FXML private VBox selectedShapeLayout;
    @FXML private Label selectedShapeLabel;
    @FXML private JFXColorPicker selectedShapeFillColorPicker;
    @FXML private JFXColorPicker selectedShapeStrokeColorPicker;

    @FXML private JFXListView<Label> shapesListView;

    @FXML private Label xLabel;
    @FXML private Label yLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        painter = Painter.getInstanceOfPainter();
        shapesMap = HashBiMap.create();
        filePath = null;
        initializeStrokePreview();
        initializeTools();
        initializeBadges();
        initializeShapesMenuItem();

        shapesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        clear();
    }

    @FXML
    void showCoordinates(MouseEvent event) {
        xLabel.setText("x: " + (int) event.getX());
        yLabel.setText("y: " + (int) event.getY());
    }

    @FXML
    void hideCoordinates() {
        xLabel.setText("");
        yLabel.setText("");
    }

    @FXML
    void onShapeSelect() {
        refresh();
        if (selectedShapes().size() == 1) {
            selectedShapeLayout.setDisable(false);
            Shape shape = selectedShapes().get(0);
            selectedShapeLabel.setText(shapesMap.inverse().get(shape));
            selectedShapeFillColorPicker.setValue(shape.getFillColor());
            selectedShapeStrokeColorPicker.setValue(shape.getColor());
            drawBoundingBox(selectedShapes());
        }
        else if (selectedShapes().size() > 1) {
            selectedShapeLayout.setDisable(false);
            selectedShapeLabel.setText("Shapes");
            selectedShapeFillColorPicker.setValue(Color.BLACK);
            selectedShapeStrokeColorPicker.setValue(Color.WHITE);
            drawBoundingBox(selectedShapes());
        }
        else {
            deselectShapes();
        }
    }

    @FXML
    void deselectShapes() {
        selectedShapeLayout.setDisable(true);
        shapesListView.getSelectionModel().select(-1);
        selectedShapeLabel.setText("Selected Shape");
        canvas.setOnMouseDragged(null);
        removeBoundingBox();
    }

    private void drawBoundingBox(List<Shape> shapes) {
        double minX = canvas.getWidth();
        double minY = canvas.getHeight();
        double maxX = 0;
        double maxY = 0;

        for (Shape shape : selectedShapes()) {
            double x1 = shape.getProperties().get("upperPointX");
            double y1 = shape.getProperties().get("upperPointY");
            if (x1 < minX) minX = x1;
            if (y1 < minY) minY = y1;

            double x2 = shape.getProperties().get("bottomPointX");
            double y2 = shape.getProperties().get("bottomPointY");
            if (x2 > maxX) maxX = x2;
            if (y2 > maxY) maxY = y2;
        }
        drawBoundingBoxLines(minX, minY, maxX, maxY);
        addEditHandlersToCanvas(minX, minY, maxX, maxY);
        resetGraphicsContext();
    }

    private void removeBoundingBox() {
        refresh();
    }

    private void drawBoundingBoxLines(double minX, double minY, double maxX, double maxY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.setLineDashes(7);
        gc.setLineWidth(1);
        gc.strokeRect(minX - 5, minY - 5, maxX - minX + 10,maxY - minY + 10);

        gc.setFill(Color.WHITE);
        gc.setLineDashes(0);

        gc.fillRect(minX - 10, minY - 10, 10, 10);
        gc.strokeRect(minX - 10, minY - 10, 10, 10);

        gc.fillRect(minX - 10, maxY, 10, 10);
        gc.strokeRect(minX - 10, maxY, 10, 10);

        gc.fillRect(maxX, minY - 10, 10, 10);
        gc.strokeRect(maxX, minY - 10, 10, 10);

        gc.fillRect(maxX, maxY, 10, 10);
        gc.strokeRect(maxX, maxY, 10, 10);

        gc.fillRect((maxX + minX)/2, minY - 10, 10, 10);
        gc.strokeRect((maxX + minX)/2, minY - 10, 10, 10);

        gc.fillRect((maxX + minX)/2, maxY, 10, 10);
        gc.strokeRect((maxX + minX)/2, maxY, 10, 10);

        gc.fillRect(minX - 10, (maxY + minY)/2, 10, 10);
        gc.strokeRect(minX - 10, (maxY + minY)/2, 10, 10);

        gc.fillRect(maxX, (maxY + minY)/2, 10, 10);
        gc.strokeRect(maxX, (maxY + minY)/2, 10, 10);
    }

    private void addEditHandlersToCanvas(double minX, double minY, double maxX, double maxY) {
        canvas.setOnMousePressed(click -> {
            List<Shape> newShapes = new ArrayList<>();
            List<Shape> oldShapes = selectedShapes();
            if (click.getX() > minX && click.getX() < maxX && click.getY() > minY && click.getY() < maxY) {
                System.out.println("inside!");
                double originX = click.getX();
                double originY = click.getY();
                canvas.setOnMouseDragged(event -> {
                    System.out.println("Dragging");
                    double deltaX = event.getX() - originX;
                    double deltaY = event.getY() - originY;
                    try {
                        for (Shape shape : oldShapes) {
                            Shape newShape = (Shape) shape.clone();
                            Map<String, Double> properties = new HashMap<>(shape.getProperties());
                            for(String key : properties.keySet()) {
                                if (key.charAt(0) == 'x') {
                                    newShape.getProperties().replace(key, properties.get(key) + deltaX);
                                }
                                else if (key.charAt(0) == 'y') {
                                    newShape.getProperties().replace(key, properties.get(key) + deltaY);
                                }
                            }
                            newShapes.add(newShape);
                        }
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    for (Shape shapePreview : newShapes) painter.addShapePreview(shapePreview);
                    refresh();
                    for (Shape shapePreview : newShapes) painter.removeShapePreview(shapePreview);
                    drawBoundingBoxLines(minX + deltaX, minY + deltaY, maxX + deltaX, maxY + deltaY);
                });
            }
            canvas.setOnMouseReleased(event -> {
                for (int i = 0; i < oldShapes.size(); i++) {
                    painter.updateShape(oldShapes.get(i), newShapes.get(i));
                }
                canvas.setOnMouseDragged(null);
            });
        });
    }

    @FXML
    void updateSelectedShapes() {
        for (Shape shape: selectedShapes()) {
            Shape newShape;
            try {
                newShape = shape.getClass().newInstance();
            } catch (Exception e) {
                return;
            }
            newShape.setProperties(shape.getProperties());
            newShape.setFillColor(selectedShapeFillColorPicker.getValue());
            newShape.setColor(selectedShapeStrokeColorPicker.getValue());
            painter.updateShape(shape, newShape);

            String shapeName = generateUniqueName(newShape);
            shapesMap.put(shapeName, newShape);

            refreshShapeList();
            refresh();
        }
    }

    @FXML
    void deleteSelectedShapes() {
        for (Shape shape: selectedShapes()) {
            painter.removeShape(shape);
        }
        deselectShapes();
        refresh();
        refreshShapeList();
    }

    @FXML
    void cloneSelectedShapes() {
        for (Shape shape: selectedShapes()) {
            try {
                addShape((Shape)shape.clone());
            } catch (CloneNotSupportedException e) {
                continue;
            }
        }
        deselectShapes();
        refresh();
        refreshShapeList();
    }

    @FXML
    void undo() {
        painter.undo();
        refresh();
        refreshShapeList();
    }

    @FXML
    void redo() {
        painter.redo();
        refresh();
        refreshShapeList();
    }

    @FXML
    void save() {
        if (filePath == null) {
            saveAs();
        }
        else {
            painter.save(filePath);
        }
    }

    @FXML
    void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing XML");

        try {
            StringBuilder path = new StringBuilder(fileChooser.showSaveDialog(new Stage()).getPath());
            if (!path.substring(path.length() - 4).equals(".xml")) {
                path.append(".xml");
            }
            filePath = path.toString();
            painter.save(filePath);
        } catch (Exception e) {

        }
    }

    @FXML
    void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Drawing XML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));
        try {
            filePath = fileChooser.showOpenDialog(new Stage()).getPath();
            painter.load(filePath);
        } catch (Exception e) {

        }
        shapesMap.clear();
        for (Shape shape : painter.getShapes()) {
            String shapeName = generateUniqueName(shape);
            shapesMap.put(shapeName, shape);
        }
        refreshShapeList();
        refresh();
    }

    @FXML
    void exit() {
        Platform.exit();
    }



    @FXML
    void onCanvasClick(MouseEvent click) {
        Shape shape = null;
        String toolSelected = toolsComboBox.getValue().getText();

        if (toolSelected == "Edit") {
            return;
        }
        try {
            shape = factory.createShape(toolSelected);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if (shape instanceof Triangle) {
            if (clickHistory.size() < 2) {
                clickHistory.add(new Point2D(click.getX(), click.getY()));
            } else {
                clickHistory.add(new Point2D(click.getX(), click.getY()));
                Map<String, Double> properties = new HashMap<>();
                properties.put("x1", clickHistory.get(0).getX());
                properties.put("y1", clickHistory.get(0).getY());
                properties.put("x2", clickHistory.get(1).getX());
                properties.put("y2", clickHistory.get(1).getY());
                properties.put("x3", clickHistory.get(2).getX());
                properties.put("y3", clickHistory.get(2).getY());
                properties.put("borderWidth", strokeSlider.getValue());

                shape.setProperties(properties);
                setShapeColors(shape);
                addShape(shape);
                painter.refresh(canvas.getGraphicsContext2D());
                clickHistory.clear();
            }
        }
        else {
            clickHistory.clear();
            Point2D originPoint = new Point2D(click.getX(), click.getY());
            canvasDrag(shape, originPoint);
        }
    }

    private void canvasDrag(Shape shape, Point2D originPoint) {
        Map<String, Double> properties = new HashMap<String, Double>();


        properties.put("borderWidth", strokeSlider.getValue());

        setShapeColors(shape);
//        EventHandler<? super MouseEvent> drag =  canvas.getOnMouseDragged();
        canvas.setOnMouseDragged(mouseEvent->{
            if ( shape instanceof Line || (mouseEvent.getX() >= originPoint.getX() && mouseEvent.getY() >= originPoint.getY())) {
                properties.put("x1", originPoint.getX());
                properties.put("y1", originPoint.getY());
                properties.put("x2", mouseEvent.getX());
                properties.put("y2", mouseEvent.getY());
            }
            else if (mouseEvent.getX() >= originPoint.getX() && mouseEvent.getY() <= originPoint.getY()) {
                properties.put("x1", originPoint.getX());
                properties.put("y1", mouseEvent.getY());
                properties.put("x2", mouseEvent.getX());
                properties.put("y2", originPoint.getY());
            }
            else if (mouseEvent.getX() <= originPoint.getX() && mouseEvent.getY() <= originPoint.getY()) {
                properties.put("x1", mouseEvent.getX());
                properties.put("y1", mouseEvent.getY());
                properties.put("x2", originPoint.getX());
                properties.put("y2", originPoint.getY());
            }
            else if (mouseEvent.getX() <= originPoint.getX() && mouseEvent.getY() >= originPoint.getY()) {
                properties.put("x1", mouseEvent.getX());
                properties.put("y1", originPoint.getY());
                properties.put("x2", originPoint.getX());
                properties.put("y2", mouseEvent.getY());
            }

            shape.setProperties(properties);
            painter.addShapePreview(shape);
            refresh();
            painter.removeShapePreview(shape);
        });
        addShape(shape);
        canvas.setOnMouseReleased(event -> {
            canvas.setOnMouseDragged(null);
        });
    }

    private void initializeBadges() {
        Map<JFXBadge, String> map = new HashMap<JFXBadge, String>();
        map.put(undoBadge, "UNDO");
        map.put(redoBadge, "REPEAT");
        map.put(saveBadge, "SAVE");
        map.put(deselectBadge, "TIMES");
        map.put(cloneBadge, "CLONE");
        map.put(deleteBadge, "TRASH");

        for (JFXBadge badge : map.keySet()) {
            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setGlyphName(map.get(badge));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(icon);
            badge.getChildren().add(stackPane);
        }
    }

    private void initializeShapesMenuItem() {
        List<Class<? extends Shape>> shapeClasses = painter.getSupportedShapes();

        for (Class<? extends Shape> shapeClass : shapeClasses) {
            String shapeName = shapeClass.getSimpleName();
            MenuItem shapeMenuItem = setupShapeMenuItem(shapeClass);


            shapesMenu.getItems().add(shapeMenuItem);
        }
    }

    private MenuItem setupShapeMenuItem(Class<? extends Shape> shapeClass) {
        MenuItem shapeMenuItem = new MenuItem(shapeClass.getSimpleName());
        shapeMenuItem.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/new_shape.fxml"));
                Stage stage = new Stage();
                stage.setTitle("New " + shapeClass.getSimpleName());
                stage.setScene(new Scene(root));
                stage.show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        return shapeMenuItem;
    }

    private void initializeTools() {
        List<Class<? extends Shape>> shapeClasses = painter.getSupportedShapes();
        toolsComboBox.getItems().add(new Label("Edit"));
        toolsComboBox.getSelectionModel().select(0);
        for (Class<? extends Shape> shapeClass : shapeClasses) {
            toolsComboBox.getItems().add(new Label(shapeClass.getSimpleName()));
        }
    }

    @FXML
    void onToolSelected() {
        clickHistory.clear();
    }

    private void initializeStrokePreview() {
        drawStrokePreview(strokeColorPicker.getValue(), strokeSlider.getValue());

        strokeColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            drawStrokePreview(newValue, strokeSlider.getValue());
        });
        strokeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            drawStrokePreview(strokeColorPicker.getValue(), newValue.doubleValue());
        });
    }

    private void drawStrokePreview(Color color, double value) {
        GraphicsContext gc = strokePreviewCanvas.getGraphicsContext2D();

        double centerX = strokePreviewCanvas.getWidth() / 2;
        double centerY = strokePreviewCanvas.getHeight() / 2;
        double radius = strokeSlider.getValue() / 2;

        gc.clearRect(0, 0, strokePreviewCanvas.getWidth(), strokePreviewCanvas.getHeight());
        gc.setFill(color);
        gc.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    private void clear() {
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().setFill(fillColorPicker.getValue());
    }

    private void setShapeColors(Shape shape) {
        if (shape instanceof Line) {
            shape.setFillColor(strokeColorPicker.getValue());
            shape.setColor(Color.WHITE);
        }
        else {
            shape.setColor(strokeColorPicker.getValue());
            shape.setFillColor(fillColorPicker.getValue());
        }
    }

    private void addShape(Shape shape) {
        painter.addShape(shape);
        String shapeName = generateUniqueName(shape);
        shapesMap.put(shapeName, shape);
        refreshShapeList();
    }

    private void refresh() {
        clear();
        painter.refresh(canvas.getGraphicsContext2D());
    }

    private void resetGraphicsContext() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineDashes(0);
        gc.setFill(fillColorPicker.getValue());
        gc.setStroke(strokeColorPicker.getValue());
    }

    private void refreshShapeList() {
        shapesListView.getItems().clear();
        for (Shape shape : painter.getShapes()) {
            shapesListView.getItems().add(0, new Label(shapesMap.inverse().get(shape)));
        }
    }

    private List<Shape> selectedShapes() {
        List<Shape> list = new ArrayList<Shape>();
        for (Label label : shapesListView.getSelectionModel().getSelectedItems()) {
            list.add(shapesMap.get(label.getText()));
        }
        return list;
    }

    private String generateUniqueName(Shape shape) {
        String name = shape.getClass().getSimpleName();
        String currentName = new String(name);

        int count = 1;
        while ( shapesMap.keySet().contains(currentName)) {
            currentName = name + " " + count;
            count ++;
        }
        return currentName;
    }
}
