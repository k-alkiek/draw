package models.interfaces;

/**
 * Created by khaledabdelfattah on 11/11/17.
 */
public interface IShapesFactory {

    /**
     * To create an instance of shape
     * @param type type of shape
     * @return instance of specific shape
     */
    public Shape createShape(String type);
}
