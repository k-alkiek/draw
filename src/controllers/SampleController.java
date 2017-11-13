package controllers;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStrokePreview();
        toolsComboBox.getItems().add(new Label("Circle"));
        toolsComboBox.getItems().add(new Label("Rectangle"));
        toolsComboBox.getItems().add(new Label("Triangle"));

        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Platform.runLater(() -> {


        });
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
