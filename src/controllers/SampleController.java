package controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import models.interfaces.Shape;
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

    @FXML private JFXComboBox<Label> toolsComboBox;
    @FXML private JFXColorPicker fillColorPicker;
    @FXML private JFXColorPicker strokeColorPicker;
    @FXML private JFXSlider strokeSlider;

    @FXML private Canvas strokePreviewCanvas;
    @FXML private JFXBadge undoBadge;
    @FXML private JFXBadge redoBadge;
    @FXML private JFXBadge saveBadge;
    @FXML private JFXBadge deleteBadge;

    @FXML private VBox selectedShapeLayout;
    @FXML private Label selectedShapeLabel;
    @FXML private JFXColorPicker selectedShapeFillColorPicker;
    @FXML private JFXColorPicker selectedShapeStrokeColorPicker;

    @FXML private JFXListView<Label> shapesListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        painter = new Painter();
        shapesMap = HashBiMap.create();
        filePath = null;
        initializeStrokePreview();
        initializeTools();
        initializeBadges();

        shapesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        clear();
    }

    @FXML
    void onShapeSelect() {
        if (selectedShapes().size() == 1) {
            selectedShapeLayout.setDisable(false);
            Shape shape = selectedShapes().get(0);
            selectedShapeLabel.setText(shapesMap.inverse().get(shape));
            selectedShapeFillColorPicker.setValue(shape.getFillColor());
            selectedShapeStrokeColorPicker.setValue(shape.getColor());
            drawBoundingBox();
        }
        else if (selectedShapes().size() > 1) {
            selectedShapeLayout.setDisable(false);
            selectedShapeLabel.setText("Shapes");
            selectedShapeFillColorPicker.setValue(Color.BLACK);
            selectedShapeStrokeColorPicker.setValue(Color.WHITE);
            drawBoundingBox();
        }
        else {
            deselect();
        }
    }

    @FXML
    void deselect() {
        selectedShapeLayout.setDisable(true);
        shapesListView.getSelectionModel().select(-1);
        selectedShapeLabel.setText("Selected Shape");
        removeBoundingBox();
    }

    private void drawBoundingBox() {
        double minX, minY;
        for (Shape shape : selectedShapes()) {
        }
    }

    private void removeBoundingBox() {
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

            String shapeName = shapesMap.inverse().get(shape);
            shapesMap.remove(shapeName);
            shapesMap.put(shapeName, newShape);

            refresh();
        }
    }

    @FXML
    void deleteSelectedShapes() {
        for (Shape shape: selectedShapes()) {
            painter.removeShape(shape);
        }
        deselect();
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
    void undo() {
        painter.undo();
        refresh();
        refreshShapeList();
    }



    @FXML
    void onCanvasClick(MouseEvent click) {
        Shape shape = factory.createShape(toolsComboBox.getValue().getText());

        if (shape instanceof Triangle) {
            if (clickHistory.size() < 2) {
                clickHistory.add(new Point2D(click.getX(), click.getY()));
            } else {
                clickHistory.add(new Point2D(click.getX(), click.getY()));
                Map<String, Double> properties = new HashMap<String, Double>();
                properties.put("x1", clickHistory.get(0).getX());
                properties.put("y1", clickHistory.get(0).getY());
                properties.put("x2", clickHistory.get(1).getX());
                properties.put("y2", clickHistory.get(1).getY());
                properties.put("x3", clickHistory.get(2).getX());
                properties.put("y3", clickHistory.get(2).getY());
                properties.put("borderWidth", strokeSlider.getValue());

                shape.setProperties(properties);
                shape.setFillColor(fillColorPicker.getValue());
                shape.setColor(strokeColorPicker.getValue());
                addShape(shape);
                painter.refresh(canvas.getGraphicsContext2D());
                clickHistory.clear();
            }
        }
        else {
            clickHistory.clear();
            canvasDrag(shape, click);
        }
    }

    private void canvasDrag(Shape shape, MouseEvent click) {
        Map<String, Double> properties = new HashMap<String, Double>();
        Point2D originPoint = new Point2D(click.getX(), click.getY());

        properties.put("x1", click.getX());
        properties.put("y1", click.getY());
        properties.put("borderWidth", strokeSlider.getValue());

        setShapeColors(shape);

        canvas.setOnMouseDragged(mouseEvent->{
            properties.put("x2", mouseEvent.getX());
            properties.put("y2", mouseEvent.getY());
            shape.setProperties(properties);
            painter.addShapePreview(shape);
            refresh();
            painter.removeShapePreview(shape);
            System.out.println(shape);
            System.out.println("X coord: " + mouseEvent.getX());
            System.out.println("Y coord: " + mouseEvent.getY());
        });
        addShape(shape);
    }

    private void initializeBadges() {
        Map<JFXBadge, String> map = new HashMap<JFXBadge, String>();
        map.put(undoBadge, "UNDO");
        map.put(redoBadge, "REPEAT");
        map.put(saveBadge, "SAVE");
        map.put(deleteBadge, "TRASH");

        for (JFXBadge badge : map.keySet()) {
            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setGlyphName(map.get(badge));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(icon);
            badge.getChildren().add(stackPane);
        }
    }

    private void initializeTools() {
        List<Class<? extends Shape>> shapeClasses = painter.getSupportedShapes();

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
        shape.setFillColor(fillColorPicker.getValue());
        shape.setColor(strokeColorPicker.getValue());
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
