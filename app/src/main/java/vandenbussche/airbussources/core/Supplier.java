package vandenbussche.airbussources.core;

import java.util.ArrayList;


public class Supplier implements Namable {

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

    public String getIdentifier(){return this.getName();}

    public String getName(){return this.name;}
    public ArrayList<Product> getProducts(){return this.products;}
    public boolean getNegotiationState(){return this.isOnNegotiation;}

    public void setProducts(ArrayList<Product> products){
        this.products = products;
    }
}
