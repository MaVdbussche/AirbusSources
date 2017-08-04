package vandenbussche.airbussources.core;


import android.content.Context;


public class Product implements Comparable, Namable {

    private String name;
    private boolean isOnCFT;

    public Product(Context context, String name){
        this.name =name;
        this.isOnCFT = false;
    }

    public Product(Context context, String name, boolean isOnCFT){
        this.name =name;
        this.isOnCFT = isOnCFT;
    }

    public String getIdentifier(){return this.getName();}

    public String getName(){return this.name;}
    public boolean getCftState(){return this.isOnCFT;}

    public int compareTo(Object other) throws ClassCastException{
        if( ! (other instanceof Product)){
            throw new ClassCastException("Could not compare those, as one of them is not a Product");
        }
        return this.name.compareTo(((Product) other).getName());
    }

}
