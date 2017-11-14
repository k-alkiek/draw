package controllers;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SampleController implements Initializable{

    @FXML Canvas canvas;

    @FXML JFXComboBox<Label> toolsComboBox;
    @FXML JFXColorPicker fillColorPicker;
    @FXML JFXColorPicker strokeColorPicker;
    @FXML JFXSlider strokeSlider;
    @FXML Canvas strokePreviewCanvas;

    @FXML Group selectedShapeGroup;
    @FXML JFXColorPicker selectedShapeFillColorPicker;
    @FXML JFXColorPicker selectedShapeStrokeColorPicker;
    @FXML JFXComboBox<?> shapesComboBox;

    @FXML JFXBadge undoBadge;
    @FXML JFXBadge redoBadge;
    @FXML JFXBadge saveBadge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStrokePreview();
        initializeTools();
        initializeBadges();

        canvas.setOnDragDetected(event -> {
            Point2D startPoint = new Point2D(event.getX(), event.getY());
            canvas.setOnMouseDragged(m->{
                double x = m.getX();
                double y = m.getY();
                System.out.println("X coord: " + x);
                System.out.println("Y coord: " + y);
                canvas.getGraphicsContext2D().setFill(strokeColorPicker.getValue());
                canvas.getGraphicsContext2D().fillRect(x, y, strokeSlider.getValue(), strokeSlider.getValue());

            });
        });

    }

    private void initializeBadges() {
        FontAwesomeIconView undoIcon = new FontAwesomeIconView();
        undoIcon.setGlyphName("UNDO");
        StackPane s1 = new StackPane();
        s1.getChildren().add(undoIcon);
        undoBadge.getChildren().add(s1);

        FontAwesomeIconView redoIcon = new FontAwesomeIconView();
        redoIcon.setGlyphName("REPEAT");
        StackPane s2 = new StackPane();
        s2.getChildren().add(redoIcon);
        redoBadge.getChildren().add(s2);

        FontAwesomeIconView saveIcon = new FontAwesomeIconView();
        saveIcon.setGlyphName("SAVE");
        StackPane s3 = new StackPane();
        s3.getChildren().add(saveIcon);
        saveBadge.getChildren().add(s3);
    }

    @FXML
    void redo() {

    }

    @FXML
    void save() {

    }

    @FXML
    void undo() {

    }

    private void initializeTools() {
        toolsComboBox.getItems().add(new Label("Circle"));
        toolsComboBox.getItems().add(new Label("Rectangle"));
        toolsComboBox.getItems().add(new Label("Triangle"));

        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        System.out.println(canvas.getBoundsInLocal().getMinX());
        System.out.println(canvas.getBoundsInLocal().getMinY());



        Platform.runLater(() -> {

        });
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
}
