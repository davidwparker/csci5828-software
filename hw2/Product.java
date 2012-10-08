public class Product {

    private static int id = 0;
    private String name;
    
    public Product() {
	this.name = "Product<" + id + ">";
	id++;
    }

    public String toString() {
	return name;
    }

}
