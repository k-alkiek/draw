package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 10/25/17.
 */
public class Rectangle extends AbstractShape implements Serializable {

    public Rectangle () {
        super();
    }
    public Rectangle(double length) {
        height = width = length;
    }

    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setFill(fillColor);
        canvas.fillRect(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                width,
                height);
        canvas.setStroke(perimeterColor);
        canvas.strokeRect(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                width,
                height);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newRectangle = new Rectangle();
        cloning(newRectangle, this);
        return newRectangle;
    }
}
