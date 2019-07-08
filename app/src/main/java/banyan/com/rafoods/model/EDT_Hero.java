package banyan.com.rafoods.model;

/**
 * Created by Jo on 12/28/2017.
 */

public class EDT_Hero {

    String id, name, qty, price;

    public EDT_Hero(String id, String name, String qty, String price) {

        this.id = id;
        this.price = price;
        this.name = name;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }
    public String getPrice() {
        return price;
    }
    public String getName() {
        return name;
    }
    public String getQty() {
        return qty;
    }

}
