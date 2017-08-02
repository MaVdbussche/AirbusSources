package vandenbussche.airbussources.core;


import android.content.Context;


public class Product implements Comparable {

    private String name;

    public Product(Context context, String name){
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
