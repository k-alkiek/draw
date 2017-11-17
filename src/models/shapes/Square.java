package models.shapes;

import javafx.scene.canvas.GraphicsContext;
import models.interfaces.Shape;

import java.util.HashMap;

/**
 * Created by khaledabdelfattah on 11/14/17.
 */
public class Square extends AbstractShape {
    private Shape rec = new Rectangle();
    public Square () {
        super();
    }

    @Override
    public void draw(GraphicsContext canvas) {
        setAtrributes();
        rec = new Rectangle(width);
        rec.setColor(perimeterColor);
        rec.setFillColor(fillColor);
        rec.setProperties(shapeProperties);
        rec.draw(canvas);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newSquare = new Square();
        cloning(newSquare, this);
        return newSquare;
    }
}
