package vandenbussche.airbussources.core;

import java.util.ArrayList;

public class Supplier {

    private String name;
    private ArrayList<Product> products;

    public Supplier(String name, ArrayList<Product> products){
        this.name = name;
        this.products = products;

    }

    public static ArrayList<Supplier> getAllSuppliers(){
        return new ArrayList<Supplier>();
    }
    public String getName(){
        return this.name;
    }
}
