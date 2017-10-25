package models.commands.commandsClasses;

import models.commands.ICommand;

/**
 * Created by khaledabdelfattah on 10/24/17.
 */
public class SaveCommand implements ICommand {
    private String path;
    public SaveCommand (String path) {
        this.path = path;
    }
    @Override
    public void execute() {

    }
}
