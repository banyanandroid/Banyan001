package banyan.com.rafoods.activity;

/**
 * Created by User on 9/22/2016.
 */
public class AppConfig {

    static String base_url = "http://epictech.in/rafoods_erp/Apicontroller/";

    static String url_login = base_url + "login";




    static String url_dis_login = base_url + "distributor_login";

    static String url_shop_list = base_url + "shop_list";
    static String url_up_shop_visit = base_url + "shopvisit";
    static String url_all_shop_list = base_url + "other_shop_list";
    static String url_state_list = base_url + "state_list";
    static String url_dis_agency_location = base_url + "location_list";
    public static String url_dis_add_shop = base_url + "add_shop";
    static String url_dis_search_shop = base_url + "search_shop";
    static String url_dis_edit_shop = base_url + "update_shop";

    static String url_dis_check_list = base_url + "check_status";
    static String url_dis_prod_near_by_shop = base_url + "nearby_shop";
    static String url_dis_prod_shop = base_url + "shop_list";
    static String url_dis_prod_group = base_url + "productgrouplist";
    static String url_dis_prod_details = base_url + "productlist";
    static String url_dis_offline_prod_details = base_url + "getdistributorprice";
    public static String url_dis_add_product = base_url + "addorder";
    static String url_dis_order_list = base_url + "list_order";
    static String url_dis_seson_offer = base_url + "get_seasonaloffers";
    static String url_ordered_items = base_url + "edit_order";
    static String url_update_order = base_url + "update_order";

    static String url_dis_get_bill = base_url + "pending_amount";
    static String url_dis_update_bill = base_url + "collection_amount";

    public static String url_add_sales_return = base_url + "addsalesreturn";
    static String url_sales_return_list = base_url + "list_salesreturn";

    static String url_target = base_url + "target_list";

    static String url_view_enquiry = base_url + "view_enquiry";
    static String url_update_enquiry = base_url + "update_enquiry";

    static String url_closing_stock = base_url + "getclosingstock";
    static String url_get_price = base_url + "getprice";
    static String url_add_rtgs = base_url + "addrfg";

   //New Process

    static String url_primary_order = base_url + "distributor_primary_shop_list";
}
