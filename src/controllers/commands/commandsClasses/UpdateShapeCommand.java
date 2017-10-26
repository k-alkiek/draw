package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.List;
import java.util.Stack;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class UpdateShapeCommand implements ICommand {
    private List<Shape> shapes;
    private Shape oldShape, newShape;
    private int index;
    public UpdateShapeCommand (List<Shape> shapes,
                               Shape oldShape,
                               Shape newShape) {
        this.shapes = shapes;
        this.oldShape = oldShape;
        this.newShape = newShape;
        index = shapes.indexOf(oldShape);
    }
    @Override
    public void execute() {
        shapes.set(index, newShape);
    }

    @Override
    public void unexecute() {
        shapes.set(index, oldShape);
    }
}
