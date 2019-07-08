package banyan.com.rafoods.database;

/**
 * Vivekanandhan
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    private Cursor cursor;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /*****************************
     *  ORDER FORM
     * ****************************/
    public long insert_Order_Form(String shop_id, String final_order, String user_id, String user_type, String str_combo_offer) {
        System.out.println("### insert_Order_Form ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.ORDER_FORM_SHOP_ID, shop_id);
        contentValue.put(DatabaseHelper.ORDER_FORM_FINAL_ORDER, final_order);
        contentValue.put(DatabaseHelper.ORDER_FORM_USER_ID, user_id);
        contentValue.put(DatabaseHelper.ORDER_FORM_USER_TYPE, user_type);
        contentValue.put(DatabaseHelper.ORDER_FORM_COMBO_OFFER, str_combo_offer);

        long response = database.insert(DatabaseHelper.TABLE_ORDER_FORM, null, contentValue);
        System.out.println("### insert_Order_Form : response " + response);
        return response;
    }

    public Cursor Fetch_Order_Form() {
        System.out.println("### Fetch_Order_Form");
        String[] columns = new String[]{DatabaseHelper.ORDER_FORM_ID, DatabaseHelper.ORDER_FORM_SHOP_ID, DatabaseHelper.ORDER_FORM_FINAL_ORDER, DatabaseHelper.ORDER_FORM_USER_ID, DatabaseHelper.ORDER_FORM_USER_TYPE, DatabaseHelper.ORDER_FORM_COMBO_OFFER};

        System.out.println("DBBBBBB :: " + database);

        if (database == null) {
            System.out.println("DB IS NULL");
            open();
        }
        try {
            cursor = database.query(DatabaseHelper.TABLE_ORDER_FORM, columns, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLException e) {

            System.out.println("SQL EXCEPTION :: " + e);
        }

        return cursor;
    }


    public int delete_Order_Form(long _id) {
        return database.delete(DatabaseHelper.TABLE_ORDER_FORM, DatabaseHelper.ORDER_FORM_ID + "=" + _id, null);
    }


    /*****************************
     *  SR ORDER FORM
     * ****************************/
    public long insert_SR_Order_Form(String shop_id, String final_order, String user_id, String user_type,
                                     String str_return_type, String str_remarks) {
        System.out.println("### insert_SR_Order_Form ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.ORDER_SR_SHOP_ID, shop_id);
        contentValue.put(DatabaseHelper.ORDER_SR_FINAL_ORDER, final_order);
        contentValue.put(DatabaseHelper.ORDER_SR_USER_ID, user_id);
        contentValue.put(DatabaseHelper.ORDER_SR_USER_TYPE, user_type);
        contentValue.put(DatabaseHelper.ORDER_SR_RETURN_TYPE, str_return_type);
        contentValue.put(DatabaseHelper.ORDER_SR_REMARK, str_remarks);

        long response = database.insert(DatabaseHelper.TABLE_SR_FORM, null, contentValue);
        System.out.println("### insert_Order_Form : response " + response);
        return response;
    }

    public Cursor Fetch_SR_Order_Form() {
        System.out.println("### Fetch_Order_Form");
        String[] columns = new String[]{DatabaseHelper.ORDER_SR_ID, DatabaseHelper.ORDER_SR_SHOP_ID, DatabaseHelper.ORDER_SR_FINAL_ORDER,
                DatabaseHelper.ORDER_SR_USER_ID, DatabaseHelper.ORDER_SR_USER_TYPE, DatabaseHelper.ORDER_SR_RETURN_TYPE
                , DatabaseHelper.ORDER_SR_REMARK};

        System.out.println("DBBBBBB :: " + database);

        if (database == null) {
            System.out.println("DB IS NULL");
            open();
        }
        try {
            cursor = database.query(DatabaseHelper.TABLE_SR_FORM, columns, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLException e) {

            System.out.println("SQL EXCEPTION :: " + e);
        }

        return cursor;
    }


    public int delete_Sr_Order_Form(long _id) {
        return database.delete(DatabaseHelper.TABLE_SR_FORM, DatabaseHelper.ORDER_SR_ID + "=" + _id, null);
    }


    /***********************
     *  ENQUIRY FORM
     * *********************/
    public long insert_Add_Enquiry(String user_id, String user_type, String shop_name, String state, String name, String mobileno,
                                   String landline, String lat, String lon, String area, String shop_previous, String agency_id,
                                   String type, String image, String loc, String remarks, String credit_limit, String branch_id) {
        System.out.println("### insert_Order_Form ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.ENQUIRY_USER_ID, user_id);
        contentValue.put(DatabaseHelper.ENQUIRY_USER_TYPE, user_type);
        contentValue.put(DatabaseHelper.ENQUIRY_SHOP_NAME, shop_name);
        contentValue.put(DatabaseHelper.ENQUIRY_STATE, state);

        contentValue.put(DatabaseHelper.ENQUIRY_NAME, name);
        contentValue.put(DatabaseHelper.ENQUIRY_MOBILE_NO, mobileno);

        contentValue.put(DatabaseHelper.ENQUIRY_LANDLINE, landline);
        contentValue.put(DatabaseHelper.ENQUIRY_LAT, lat);

        contentValue.put(DatabaseHelper.ENQUIRY_LON, lon);
        contentValue.put(DatabaseHelper.ENQUIRY_AREA, area);

        contentValue.put(DatabaseHelper.ENQUIRY_SHOP_PREVIOUS, shop_previous);
        contentValue.put(DatabaseHelper.ENQUIRY_AGENCY_ID, agency_id);

        contentValue.put(DatabaseHelper.ENQUIRY_TYPE, type);
        contentValue.put(DatabaseHelper.ENQUIRY_IMAGE, image);

        contentValue.put(DatabaseHelper.ENQUIRY_LOC, loc);
        contentValue.put(DatabaseHelper.ENQUIRY_REMARKS, remarks);
        contentValue.put(DatabaseHelper.ENQUIRY_CREDIT_LIMIT, credit_limit);

        contentValue.put(DatabaseHelper.ENQUIRY_BRANCH_ID, branch_id);

        long response = database.insert(DatabaseHelper.TABLE_ENQUIRY, null, contentValue);
        System.out.println("### insert_Add_Enquiry : response " + response);
        return response;
    }


    public Cursor Fetch_Enquiry() {
        System.out.println("### Fetch_Enquiry");
        String[] columns = new String[]{DatabaseHelper.ENQUIRY_ID, DatabaseHelper.ENQUIRY_USER_ID, DatabaseHelper.ENQUIRY_USER_TYPE,
                DatabaseHelper.ENQUIRY_SHOP_NAME, DatabaseHelper.ENQUIRY_STATE, DatabaseHelper.ENQUIRY_NAME,
                DatabaseHelper.ENQUIRY_MOBILE_NO, DatabaseHelper.ENQUIRY_LANDLINE, DatabaseHelper.ENQUIRY_LAT, DatabaseHelper.ENQUIRY_LON,
                DatabaseHelper.ENQUIRY_AREA, DatabaseHelper.ENQUIRY_SHOP_PREVIOUS, DatabaseHelper.ENQUIRY_AGENCY_ID,
                DatabaseHelper.ENQUIRY_TYPE, DatabaseHelper.ENQUIRY_IMAGE, DatabaseHelper.ENQUIRY_LOC,
                DatabaseHelper.ENQUIRY_REMARKS, DatabaseHelper.ENQUIRY_CREDIT_LIMIT, DatabaseHelper.ENQUIRY_BRANCH_ID};
        if (database == null) {
            open();
        }
        Cursor cursor = database.query(DatabaseHelper.TABLE_ENQUIRY, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            System.out.println("NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        }
        return cursor;
    }


    public int delete_Enquiry(long _id) {
        return database.delete(DatabaseHelper.TABLE_ENQUIRY, DatabaseHelper.ENQUIRY_ID + "=" + _id, null);
    }

    /*************************
     *  STOCKIST
     * ************************/

    // order form
    public long insert_stockist(String stockist_id, String stockist_name) {
        System.out.println("### Insert Agency ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.STOCKIST_ID, stockist_id);
        contentValue.put(DatabaseHelper.STOCKIST_NAME, stockist_name);

        long response = database.insert(DatabaseHelper.TABLE_STOCKIST, null, contentValue);
        System.out.println("### insert_Stockist : response " + response);
        return response;
    }

    public Cursor Fetch_Stockist() {
        System.out.println("### Fetch_Stockist");
        String[] columns = new String[]{DatabaseHelper.STOCKIST_ID, DatabaseHelper.STOCKIST_NAME};

        Cursor cursor = database.query(DatabaseHelper.TABLE_STOCKIST, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_stockist() {
        return database.delete(DatabaseHelper.TABLE_STOCKIST, null, null);
    }


    /*************************
     *  DISTRIBUTER
     * ************************/
    public long insert_distributor(String user_id, String stockist_id, String distributor_name, String address1, String address2,
                                   String city, String distributor_email, String distributor_mobile) {
        System.out.println("### insert_distributor_Form ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.DISTRIBUTOR_USER_ID, user_id);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_STOCK_ID, stockist_id);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_NAME, distributor_name);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_ADD1, address1);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_ADD2, address2);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_CITY, city);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_EMAIL, distributor_email);
        contentValue.put(DatabaseHelper.DISTRIBUTOR_PHONE, distributor_mobile);


        long response = database.insert(DatabaseHelper.TABLE_DISTRIBUTOR, null, contentValue);
        System.out.println("### insert_Add_Distributor : response " + response);
        return response;
    }

    public Cursor Fetch_distributor() {
        System.out.println("### Fetch_Stockist");
        String[] columns = new String[]{DatabaseHelper.DISTRIBUTOR_USER_ID, DatabaseHelper.DISTRIBUTOR_STOCK_ID, DatabaseHelper.DISTRIBUTOR_NAME,
                 DatabaseHelper.DISTRIBUTOR_ADD1, DatabaseHelper.DISTRIBUTOR_ADD2, DatabaseHelper.DISTRIBUTOR_CITY, DatabaseHelper.DISTRIBUTOR_EMAIL
                , DatabaseHelper.DISTRIBUTOR_PHONE};

        Cursor cursor = database.query(DatabaseHelper.TABLE_DISTRIBUTOR, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_distributor() {
        return database.delete(DatabaseHelper.TABLE_DISTRIBUTOR, null, null);
    }

    /*************************
     *  AGENCY NAME
     * ************************/

    // order form
    public long insert_Agency(String agency_id, String agency_name) {
        System.out.println("### Insert Agency ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.AGENCY_ID, agency_id);
        contentValue.put(DatabaseHelper.AGENCY_NAME, agency_name);

        long response = database.insert(DatabaseHelper.TABLE_AGENCY, null, contentValue);
        System.out.println("### insert_Agency : response " + response);
        return response;
    }

    public Cursor Fetch_Agency() {
        System.out.println("### Fetch_Agency");
        String[] columns = new String[]{DatabaseHelper.AGENCY_ID, DatabaseHelper.AGENCY_NAME};

        Cursor cursor = database.query(DatabaseHelper.TABLE_AGENCY, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_Agency() {
        return database.delete(DatabaseHelper.TABLE_AGENCY, null, null);
    }

    /*************************
     *  AGENCY LOCATION
     * ************************/

    public long insert_Agency_Location(String location_id, String location_name) {
        System.out.println("### Insert Agency ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.LOCATION_ID, location_id);
        contentValue.put(DatabaseHelper.LOCATION_NAME, location_name);

        long response = database.insert(DatabaseHelper.TABLE_AGENCY_LOCATION, null, contentValue);
        System.out.println("### insert_Agency_Location : response " + response);
        return response;
    }

    public Cursor Fetch_Agency_Location() {
        System.out.println("### Fetch_Agency");
        String[] columns = new String[]{DatabaseHelper.LOCATION_ID, DatabaseHelper.LOCATION_NAME};

        Cursor cursor = database.query(DatabaseHelper.TABLE_AGENCY_LOCATION, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_Agency_Location() {
        return database.delete(DatabaseHelper.TABLE_AGENCY_LOCATION, null, null);
    }

    /*************************
     *  AGENCY STATE
     * ************************/

    public long insert_State(String state_id, String state_name) {
        System.out.println("### Insert State ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.STATE_ID, state_id);
        contentValue.put(DatabaseHelper.STATE_NAME, state_name);

        long response = database.insert(DatabaseHelper.TABLE_STATE, null, contentValue);
        System.out.println("### insert_State : response " + response);
        return response;
    }

    public Cursor Fetch_State() {
        System.out.println("### Fetch_State");
        String[] columns = new String[]{DatabaseHelper.STATE_ID, DatabaseHelper.STATE_NAME};

        Cursor cursor = database.query(DatabaseHelper.TABLE_STATE, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_State() {
        return database.delete(DatabaseHelper.TABLE_STATE, null, null);
    }

    /*************************
     *  SHOP
     * ************************/
    public long insert_shop(String shop_id, String shop_name, String shop_type) {
        System.out.println("### Insert Agency ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.SHOP_ID, shop_id);
        contentValue.put(DatabaseHelper.SHOP_NAME, shop_name);
        contentValue.put(DatabaseHelper.SHOP_TYPE, shop_type);

        long response = database.insert(DatabaseHelper.TABLE_SHOP, null, contentValue);
        System.out.println("### insert_Shop : response " + response);
        return response;
    }

    public Cursor Fetch_Shop() {
        System.out.println("### Fetch_Shop");
        String[] columns = new String[]{DatabaseHelper.SHOP_ID, DatabaseHelper.SHOP_NAME, DatabaseHelper.SHOP_TYPE};

        Cursor cursor = database.query(DatabaseHelper.TABLE_SHOP, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_shop() {
        return database.delete(DatabaseHelper.TABLE_SHOP, null, null);
    }

    /*************************
     *  GROUP
     * ************************/
    public long insert_group(String group_id, String group_name) {
        System.out.println("### Insert Group ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.GROUP_ID, group_id);
        contentValue.put(DatabaseHelper.GROUP_NAME, group_name);

        long response = database.insert(DatabaseHelper.TABLE_GROUP, null, contentValue);
        System.out.println("### insert_Group : response " + response);
        return response;
    }

    public Cursor Fetch_group() {
        System.out.println("### Fetch_Shop");
        String[] columns = new String[]{DatabaseHelper.GROUP_ID, DatabaseHelper.GROUP_NAME};

        Cursor cursor = database.query(DatabaseHelper.TABLE_GROUP, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_group() {
        return database.delete(DatabaseHelper.TABLE_GROUP, null, null);
    }

    /*************************
     *  product
     * ************************/
    public long insert_PRODUCT(String product_id, String product_name, String gst, String productgroup_id, String retail_price,
                               String distributor_price, String live_stock, String transit) {
        System.out.println("### Insert PRODUCT ");
        System.out.println("### GST " + gst);

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.PRODUCT_ID, product_id);
        contentValue.put(DatabaseHelper.PRODUCT_NAME, product_name);
        contentValue.put(DatabaseHelper.PRODUCT_GST, gst);
        contentValue.put(DatabaseHelper.PRODUCT_GROUP_ID, productgroup_id);
        contentValue.put(DatabaseHelper.PRODUCT_RETAIL_PRICE, retail_price);
        contentValue.put(DatabaseHelper.PRODUCT_DIS_PRICE, distributor_price);
        contentValue.put(DatabaseHelper.PRODUCT_LIVE_STOCK, live_stock);
        contentValue.put(DatabaseHelper.PRODUCT_TRANSIT, transit);

        long response = database.insert(DatabaseHelper.TABLE_PRODUCT, null, contentValue);
        System.out.println("### insert_Product : response " + response);
        return response;
    }

    public Cursor Fetch_product(String str_group_id) {
        System.out.println("### Fetch_Product");
        String[] columns = new String[]{DatabaseHelper.PRODUCT_ID, DatabaseHelper.PRODUCT_NAME, DatabaseHelper.PRODUCT_GROUP_ID
                , DatabaseHelper.PRODUCT_RETAIL_PRICE, DatabaseHelper.PRODUCT_GST, DatabaseHelper.PRODUCT_DIS_PRICE
                , DatabaseHelper.PRODUCT_LIVE_STOCK, DatabaseHelper.PRODUCT_TRANSIT};

        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCT, columns, "productgroup_id=?", new String[]{str_group_id}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_product() {
        return database.delete(DatabaseHelper.TABLE_PRODUCT, null, null);
    }


    /*************************
     *  PRIMARY SHOP
     * ************************/
    public long insert_primary_shop(String shop_id, String shop_name, String shop_type) {
        System.out.println("### Insert Agency ");

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.PRIMARY_SHOP_ID, shop_id);
        contentValue.put(DatabaseHelper.PRIMARY_SHOP_NAME, shop_name);
        contentValue.put(DatabaseHelper.PRIMARY_SHOP_TYPE, shop_type);

        long response = database.insert(DatabaseHelper.TABLE_PRIMARY_SHOP, null, contentValue);
        System.out.println("### insert_Shop : response " + response);
        return response;
    }

    public Cursor Fetch_primary_Shop() {
        System.out.println("### Fetch_Shop");
        String[] columns = new String[]{DatabaseHelper.PRIMARY_SHOP_ID, DatabaseHelper.PRIMARY_SHOP_NAME, DatabaseHelper.PRIMARY_SHOP_TYPE};

        Cursor cursor = database.query(DatabaseHelper.TABLE_PRIMARY_SHOP, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int delete_primary_shop() {
        return database.delete(DatabaseHelper.TABLE_PRIMARY_SHOP, null, null);
    }


}
