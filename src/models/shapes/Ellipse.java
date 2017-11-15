package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 11/11/17.
 */
public class Ellipse extends AbstractShape implements Serializable {

    public Ellipse () {
        super();
    }
    @Override
    public void draw(GraphicsContext canvas) {
        setAtrributes();
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
        Shape newEllipse = new Ellipse();
        cloning(newEllipse, this);
        return newEllipse;
    }
}
