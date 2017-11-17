package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 10/23/17.
 */
public class Circle extends AbstractShape implements Serializable {

    public Circle () {
        super();
    }

    @Override
    public void draw(GraphicsContext canvas) {
        setAttributes();
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillOval(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                width,
                height);
        canvas.setStroke(perimeterColor);
        canvas.strokeOval(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                width,
                height);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newCircle = new Circle();
        cloning(newCircle, this);
        return newCircle;
    }
}
