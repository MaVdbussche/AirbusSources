package vandenbussche.airbussources.core;


public class Product implements Comparable {

    private String name;

    public Product(String name){
        this.name =name;
    }



    public String getName(){return this.name;}

    public int compareTo(Object other) throws ClassCastException{
        if( ! (other instanceof Product)){
            throw new ClassCastException("Could not compare those, as one of them is not a Product");
        }
        return this.name.compareTo(((Product) other).getName());
    }

}
