package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import models.interfaces.Shape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by khaledabdelfattah on 11/19/17.
 */
public class newShapeController {

    public static Map<String, Double> setProps(Shape type, boolean isSupported) throws IOException {
        GridPane grid = new GridPane();

        grid.setPrefHeight(200);
        grid.setPrefWidth(400);
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.setTitle("New " + type.getClass().getSimpleName());

        JFXButton drawButton = new JFXButton("Draw");

        Map<String, Double> props = new HashMap<>();

        if (isSupported) {
            if (type.getClass().getSimpleName().equals("Circle")) {
                Label label = new Label("Radius");
                label.setFont(Font.font(20));
                JFXTextField radius = new JFXTextField();

                grid.getChildren().addAll(label, radius, drawButton);
                GridPane.setConstraints(label, 0, 0);
                GridPane.setConstraints(radius, 2, 0);

                GridPane.setConstraints(drawButton, 1, 2);
                drawButton.setTranslateX(30);

                stage.setScene(new Scene(grid));
                stage.show();

                drawButton.setOnAction(e -> {
                    props.put("height", Double.parseDouble(radius.getText()) * 2);
                    props.put("width", Double.parseDouble(radius.getText()) * 2);
                    stage.close();
                });
            } else {
                Label heightLabel = new Label("Height");
                JFXTextField height = new JFXTextField();
                Label widthLabel = new Label("Width");
                JFXTextField width = new JFXTextField();

                grid.getChildren().addAll(heightLabel, widthLabel, height, width, drawButton);

                GridPane.setConstraints(heightLabel, 0, 0);
                GridPane.setConstraints(height, 2, 0);

                GridPane.setConstraints(drawButton, 1, 2);
                drawButton.setTranslateX(30);

                GridPane.setConstraints(widthLabel, 0, 1);
                GridPane.setConstraints(width, 2, 1);

                stage.setScene(new Scene(grid));
                stage.show();

                drawButton.setOnAction(e -> {
                    props.put("height", Double.parseDouble(height.getText()));
                    props.put("width", Double.parseDouble(width.getText()));
                    stage.close();
                });
            }
        } else {
            Label keyName;
            JFXTextField value;
            ArrayList<JFXTextField> inputs = new ArrayList<>();
            int i = 0;
            for (String key : type.getProperties().keySet()) {
                keyName = new Label(key);
                value = new JFXTextField();
                grid.getChildren().addAll(keyName, value);
                GridPane.setConstraints(keyName, 0, i);
                GridPane.setConstraints(value, 2, i);
                inputs.add(value);
                i ++;
            }

            GridPane.setConstraints(drawButton, 1, i);
            drawButton.setTranslateX(30);
            grid.getChildren().add(drawButton);

            stage.setScene(new Scene(grid));
            stage.show();

            drawButton.setOnAction(e -> {
                int j = 0;
                for (String key : type.getProperties().keySet()) {
                    props.put(key, Double.parseDouble(inputs.get(j).getText()));
                    j++;
                }
                stage.close();
            });
        }
        return props;
    }
}
