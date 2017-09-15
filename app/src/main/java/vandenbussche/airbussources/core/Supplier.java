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
    public void setNegotiationState(boolean b){
        this.isOnNegotiation = b;
    }

    public String toString(){
        return this.name;
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
