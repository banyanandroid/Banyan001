package banyan.com.rafoods.model;

/**
 * Created by Jo on 12/28/2017.
 */

public class Hero {

    String id, group_id, act_price, price, gst, name, qty, discount ,discount_type, live_stock, transit;

    public Hero(String id, String group_id, String act_price, String price, String gst, String name, String qty,
                String discount, String discount_type, String live_stock, String transit) {
        this.id = id;
        this.group_id = group_id;
        this.act_price = act_price;
        this.price = price;
        this.gst = gst;
        this.name = name;
        this.qty = qty;
        this.discount = discount;
        this.discount_type = discount_type;
        this.live_stock = live_stock;
        this.transit = transit;
    }

    public String getId() {
        return id;
    }
    public String getgroupId() {
        return group_id;
    }
    public String getAct_price() {
        return act_price;
    }
    public String getPrice() {
        return price;
    }
    public String getGst() {
        return gst;
    }
    public String getName() {
        return name;
    }
    public String getQty() {
        return qty;
    }
    public String getDiscount() {
        return discount;
    }
    public String getDiscount_type() {
        return discount_type;
    }
    public String getLive_stock() {
        return live_stock;
    }
    public String getTransit() {
        return transit;
    }

}
