package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 10/26/17.
 */
public class Line extends AbstractShape implements Serializable {

    public Line () {
        super();
    }
    @Override
    public void draw(GraphicsContext canvas) {
        canvas.setLineWidth(shapeProperties.get("borderWidth"));
        canvas.setStroke(fillColor);
        canvas.strokeLine(shapeProperties.get("x1"),
                shapeProperties.get("y1"),
                shapeProperties.get("x2"),
                shapeProperties.get("y2"));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newLine = new Line();
        cloning(newLine, this);
        return newLine;
    }
}
