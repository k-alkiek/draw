package models.shapes;

import models.interfaces.IShapesFactory;
import models.interfaces.Shape;

/**
 * Created by khaledabdelfattah on 11/11/17.
 */
public class ShapesFactory implements IShapesFactory {
    public ShapesFactory () {}
    @Override
    public Shape createShape(String type) {
        Shape newShape = null;
        if (type.equals("models.shapes.Circle")) {
            newShape = new Circle();
        } else if (type.equals("models.shapes.Line")) {
            newShape = new Line();
        } else if (type.equals("models.shapes.Rectangle")) {
            newShape = new Rectangle();
        } else if (type.equals("models.shapes.Ellipse")) {
            newShape = new Ellipse();
        } else if (type.equals("models.shapes.RoundRectangle")) {
            newShape = new RoundRectangle();
        }
        return newShape;
    }
}
