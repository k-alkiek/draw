package models.commands.commandsClasses;

import models.commands.ICommand;
import models.interfaces.Shape;

import java.util.List;
import java.util.Stack;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class UpdateShapeCommand implements ICommand {
    private List<Shape> shapes;
    private Stack<Shape> currentShapes;
    private Shape oldShape, newShape;
    public UpdateShapeCommand (List<Shape> shapes,
                               Stack<Shape> currentShapes,
                               Shape oldShape,
                               Shape newShape) {
        this.shapes = shapes;
        this.currentShapes = currentShapes;
        this.oldShape = oldShape;
        this.newShape = newShape;
    }
    @Override
    public void execute() {
        int index = shapes.indexOf(oldShape);
        currentShapes.push(oldShape);
        shapes.set(index, newShape);
    }
}
