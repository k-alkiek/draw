package controllers.commands;

/**
 * Created by khaledabdelfattah on 10/25/17.
 */
public interface ICommand {

    void execute();

    void unexecute();
}
