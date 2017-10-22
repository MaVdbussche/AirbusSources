package vandenbussche.airbussources.core;

public class Product implements Nameable, Comparable<Product>  {

    private String name;
    private boolean isOnCFT;

    public Product(String name){
        this.name =name;
        this.isOnCFT = false;
    }

    public Product(String name, boolean isOnCFT){
        this.name =name;
        this.isOnCFT = isOnCFT;
    }

    public String getIdentifier(){return this.getName();}

    public String getName(){return this.name;}
    public boolean getIsOnCFT(){return this.isOnCFT;}

    public void setName(String s){this.name = s;}
    public void setCFT(boolean b){this.isOnCFT = b;}

    public int compareTo(Product other) throws ClassCastException{
        if( other != null){
            throw new ClassCastException("Could not compare those, as one of them is not a Product and/or is null");
        }
        return this.name.compareTo(((Product) other).getName());
    }

    public String toString(){
        return this.name;
    }

}
