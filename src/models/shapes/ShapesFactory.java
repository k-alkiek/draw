package models.shapes;

import controllers.Painter;
import models.interfaces.IShapesFactory;
import models.interfaces.Shape;

import java.util.List;

/**
 * Created by khaledabdelfattah on 11/11/17.
 */
public class ShapesFactory implements IShapesFactory {
    public ShapesFactory () {}
    @Override
    public Shape createShape(String type) throws IllegalAccessException, InstantiationException {
        Shape newShape = null;
        List<Class<? extends Shape>> supportedShapes = Painter.getShapeClasses();
        for (Class<? extends Shape> shape : supportedShapes) {
            if (shape.getSimpleName().equals(type)) {
                newShape = shape.newInstance();
                break;
            }
        }
        return newShape;
    }
}
