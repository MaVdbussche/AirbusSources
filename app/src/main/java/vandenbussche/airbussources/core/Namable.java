package vandenbussche.airbussources.core;


/**
 * This interface is only used in order to allow creating ArrayList that can contain Members, Products and Suppliers at the same time
 */
public interface Namable {

    /**
     * Returns the String used to identify this object. Can be basically anything, as long as it identifies the object self-sufficiently in the database
     * @return the identifier of this object
     */
    String getIdentifier();
}
