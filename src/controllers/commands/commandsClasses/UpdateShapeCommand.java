package controllers.commands.commandsClasses;

import controllers.commands.ICommand;
import models.interfaces.Shape;

import java.util.ArrayList;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class UpdateShapeCommand implements ICommand {
    private ArrayList<Shape> shapes;
    private Shape oldShape, newShape;
    public UpdateShapeCommand (Shape oldShape,
                               Shape newShape,
                               ArrayList<Shape> shapes) {
        this.shapes = shapes;
        this.oldShape = oldShape;
        this.newShape = newShape;
    }
    @Override
    public void execute() {
        shapes.set(shapes.indexOf(oldShape), newShape);
    }

    @Override
    public void unexecute() {
        shapes.set(shapes.indexOf(newShape), oldShape);
    }
}
