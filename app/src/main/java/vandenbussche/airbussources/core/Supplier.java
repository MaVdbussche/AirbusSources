package vandenbussche.airbussources.core;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;

import vandenbussche.airbussources.database.SQLUtility;


public class Supplier implements Namable, Comparable<Supplier> {

    private String name;
    private ArrayList<Product> products;
    private boolean isOnNegotiation;

    public Supplier(String name, ArrayList<Product> products){
        this.name = name;
        this.products = products;
        this.isOnNegotiation = false;
    }
    public Supplier(String name, ArrayList<Product> products, boolean isOnNegotiation){
        this.name = name;
        this.products = products;
        this.isOnNegotiation = isOnNegotiation;
    }

    public boolean isInRelationshipWith(Context context, String memberIDProfile){
        SQLUtility db = SQLUtility.prepareDataBase(context);
        Cursor c = db.getEntriesFromDB("Member_Supplier_Product",
                                        new String[]{"Member", "Supplier"},
                                        "Member = \""+memberIDProfile+"\" AND Supplier = \""+this.getName()+"\"", null);
        if(c.getCount() > 0){
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    public String getIdentifier(){return this.getName();}

    public String getName(){return this.name;}
    public ArrayList<Product> getProducts(){return this.products;}
    public boolean getNegotiationState(){return this.isOnNegotiation;}

    public void setProducts(ArrayList<Product> products){
        Collections.sort(products);
        this.products = products;
    }
    public void setNegotiationState(boolean b){
        this.isOnNegotiation = b;
    }

    public String toString(){
        return this.name;
    }

    public int compareTo(Supplier other){
        if( other != null){
            throw new ClassCastException("Could not compare those, as one of them is not a Product and/or is null");
        }
        return this.name.compareTo(((Supplier) other).getName());
    }

    public boolean equals(Object o) {
        if (o instanceof Supplier) {
            if(this.name.equals(((Supplier) o).name)){
                if(this.products == null && ((Supplier) o).products == null){
                    return (this.isOnNegotiation == ((Supplier) o).isOnNegotiation);
                } else if ( this.products != null && ((Supplier) o).products != null ){
                    if(this.products.equals(((Supplier) o).products)) {
                        return (this.isOnNegotiation == ((Supplier) o).isOnNegotiation);
                    }
                }
            }
        }
        return false;
    }
}
