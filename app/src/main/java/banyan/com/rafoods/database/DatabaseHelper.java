package banyan.com.rafoods.database;

/**
 * Vivekanandhan
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Information
    static final String DB_NAME = "JOURNALDEV_COUNTRIES.DB";

    // database version
    static final int DB_VERSION = 1;


    /****************************
     * // Table Name Order Form
     * ***********************/
    public static final String TABLE_ORDER_FORM = "dis_order_form";
    // Table columns
    public static final String ORDER_FORM_ID = "order_form_id";
    public static final String ORDER_FORM_SHOP_ID = "shop_id";
    public static final String ORDER_FORM_FINAL_ORDER = "final_order";
    public static final String ORDER_FORM_USER_ID = "user_id";
    public static final String ORDER_FORM_USER_TYPE = "user_type";
    public static final String ORDER_FORM_COMBO_OFFER = "str_combo_offer";

    // Creating table query
    private static final String CREATE_TABLE_ORDER_FORM = "create table " + TABLE_ORDER_FORM + "("
            + ORDER_FORM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ORDER_FORM_SHOP_ID + " TEXT NOT NULL, " +
            ORDER_FORM_FINAL_ORDER + " TEXT NOT NULL, " +
            ORDER_FORM_USER_ID + " TEXT NOT NULL, " +
            ORDER_FORM_USER_TYPE + " TEXT NOT NULL, " +
            ORDER_FORM_COMBO_OFFER + " TEXT NOT NULL );";

    /****************************
     * // Table Name Sales Return
     * ***********************/
    public static final String TABLE_SR_FORM = "dis_sr_form";
    // Table columns
    public static final String ORDER_SR_ID = "order_sr_id";
    public static final String ORDER_SR_SHOP_ID = "shop_id";
    public static final String ORDER_SR_FINAL_ORDER = "final_order";
    public static final String ORDER_SR_USER_ID = "user_id";
    public static final String ORDER_SR_USER_TYPE = "user_type";
    public static final String ORDER_SR_RETURN_TYPE = "return_type";
    public static final String ORDER_SR_REMARK = "remarks";

    // Creating table query
    private static final String CREATE_TABLE_SR_ORDER_FORM = "create table " + TABLE_SR_FORM + "("
            + ORDER_SR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ORDER_SR_SHOP_ID + " TEXT NOT NULL, " +
            ORDER_SR_FINAL_ORDER + " TEXT NOT NULL, " +
            ORDER_SR_USER_ID + " TEXT NOT NULL, " +
            ORDER_SR_USER_TYPE + " TEXT NOT NULL, " +
            ORDER_SR_RETURN_TYPE + " TEXT NOT NULL, " +
            ORDER_SR_REMARK + " TEXT NOT NULL);";

    /****************************
     * // Table Name : enquiry
     * ******************************/

    public static final String TABLE_ENQUIRY = "enquiry";
    // Table columns
    public static final String ENQUIRY_ID = "enquiry_id";
    public static final String ENQUIRY_USER_ID = "user_id";
    public static final String ENQUIRY_USER_TYPE = "user_type";
    public static final String ENQUIRY_SHOP_NAME = "shop_name";
    public static final String ENQUIRY_STATE = "state";
    public static final String ENQUIRY_NAME = "name";
    public static final String ENQUIRY_MOBILE_NO = "mobileno";
    public static final String ENQUIRY_LANDLINE = "landline";
    public static final String ENQUIRY_LAT = "lat";
    public static final String ENQUIRY_LON = "lon";
    public static final String ENQUIRY_AREA = "area";
    public static final String ENQUIRY_SHOP_PREVIOUS = "shop_previous";
    public static final String ENQUIRY_AGENCY_ID = "agency_id";
    public static final String ENQUIRY_TYPE = "type";
    public static final String ENQUIRY_IMAGE = "image";
    public static final String ENQUIRY_LOC = "loc";
    public static final String ENQUIRY_REMARKS = "remarks";
    public static final String ENQUIRY_CREDIT_LIMIT = "credit_limit";
    public static final String ENQUIRY_BRANCH_ID = "branch_id";

    // Creating table query
    private static final String CREATE_TABLE_ENQUIRY = "create table " + TABLE_ENQUIRY + "("
            + ENQUIRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ENQUIRY_USER_ID + " TEXT NOT NULL, " +
            ENQUIRY_USER_TYPE + " TEXT NOT NULL, " +
            ENQUIRY_SHOP_NAME + " TEXT NOT NULL, " +
            ENQUIRY_STATE + " TEXT NOT NULL, " +
            ENQUIRY_NAME + " TEXT NOT NULL, " +
            ENQUIRY_MOBILE_NO + " TEXT NOT NULL, " +
            ENQUIRY_LANDLINE + " TEXT NOT NULL, " +
            ENQUIRY_LAT + " TEXT NOT NULL, " +
            ENQUIRY_LON + " TEXT NOT NULL, " +
            ENQUIRY_AREA + " TEXT NOT NULL, " +
            ENQUIRY_SHOP_PREVIOUS + " TEXT NOT NULL, " +
            ENQUIRY_AGENCY_ID + " TEXT NOT NULL, " +
            ENQUIRY_TYPE + " TEXT NOT NULL, " +
            ENQUIRY_IMAGE + " TEXT NOT NULL, " +
            ENQUIRY_LOC + " TEXT NOT NULL, " +
            ENQUIRY_REMARKS + " TEXT NOT NULL, " +
            ENQUIRY_CREDIT_LIMIT + " TEXT NOT NULL, " +
            ENQUIRY_BRANCH_ID + " TEXT NOT NULL);";


    /****************************
     * // Table Name Agency Name
     * **************************/

    public static final String TABLE_AGENCY = "agency";
    // Table columns
    public static final String AGENCIES_ID = "agency_id";
    public static final String AGENCY_ID = "agencies_id";
    public static final String AGENCY_NAME = "agencies_name";

    // Creating table query
    private static final String CREATE_TABLE_AGENCY = "create table " + TABLE_AGENCY + "("
            + AGENCIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AGENCY_ID + " TEXT NOT NULL, " +
            AGENCY_NAME + " TEXT NOT NULL );";



    /************************************
     * //  Table Name Agency Location
     * ***********************************/
    public static final String TABLE_AGENCY_LOCATION = "dis_agency_location";
    // Table columns
    public static final String LOCATIONS_ID = "locations_id";
    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_NAME = "location_name";

    // Creating table query
    private static final String CREATE_TABLE_AGENCY_LOCATION = "create table " + TABLE_AGENCY_LOCATION + "("
            + LOCATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LOCATION_ID + " TEXT NOT NULL, " +
            LOCATION_NAME + " TEXT NOT NULL );";


    /************************************
     * //  Table Name State
     * ***********************************/
    public static final String TABLE_STATE = "dis_state";
    // Table columns
    public static final String STATES_ID = "states_id";
    public static final String STATE_ID = "state_id";
    public static final String STATE_NAME = "state_name";

    // Creating table query
    private static final String CREATE_TABLE_STATE = "create table " + TABLE_STATE + "("
            + STATES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STATE_ID + " TEXT NOT NULL, " +
            STATE_NAME + " TEXT NOT NULL );";


    /******************************
     * // Table Name Shop
     * *****************************/
    public static final String TABLE_SHOP = "dis_shop";
    // Table columns
    public static final String SHOP_IDD = "shop_idd";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_TYPE = "shop_type";

    // Creating table query
    private static final String CREATE_TABLE_SHOP = "create table " + TABLE_SHOP + "("
            + SHOP_IDD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SHOP_ID + " TEXT NOT NULL, " +
            SHOP_NAME + " TEXT NOT NULL," +
            SHOP_TYPE + " TEXT NOT NULL );";


    /******************************
     * // Table Product_Group
     * **************************/
    public static final String TABLE_GROUP = "disprod_group";
    // Table columns
    public static final String GROUP_IDD = "group_idd";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";

    // Creating table query
    private static final String CREATE_TABLE_GROUP = "create table " + TABLE_GROUP + "("
            + GROUP_IDD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GROUP_ID + " TEXT NOT NULL, " +
            GROUP_NAME + " TEXT NOT NULL );";

    /******************************
     * // Table Product
     * **************************/
    public static final String TABLE_PRODUCT = "products";
    // Table columns
    public static final String PROD_IDD = "product_idd";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_GST = "product_gst";
    public static final String PRODUCT_GROUP_ID = "productgroup_id";
    public static final String PRODUCT_RETAIL_PRICE = "retail_price";
    public static final String PRODUCT_DIS_PRICE = "distributor_price";
    public static final String PRODUCT_LIVE_STOCK = "live_stock";
    public static final String PRODUCT_TRANSIT = "transit_stock";

    // Creating table query

    private static final String CREATE_TABLE_PRODUCT = "create table " + TABLE_PRODUCT + "("
            + PROD_IDD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRODUCT_ID + " TEXT NOT NULL, " +
            PRODUCT_NAME + " TEXT NOT NULL," +
            PRODUCT_GST + " TEXT NOT NULL," +
            PRODUCT_GROUP_ID + " TEXT NOT NULL," +
            PRODUCT_RETAIL_PRICE + " TEXT NOT NULL," +
            PRODUCT_DIS_PRICE + " TEXT NOT NULL, " +
            PRODUCT_LIVE_STOCK + " TEXT NOT NULL, " +
            PRODUCT_TRANSIT + " TEXT NOT NULL );";


    /******************************
     * // Table Name Primary Shop
     * *****************************/
    public static final String TABLE_PRIMARY_SHOP = "primary_dis_shop";
    // Table columns
    public static final String PRIMARY_SHOP_IDD = "shop_idd";
    public static final String PRIMARY_SHOP_ID = "shop_id";
    public static final String PRIMARY_SHOP_NAME = "shop_name";
    public static final String PRIMARY_SHOP_TYPE = "shop_type";

    // Creating table query
    private static final String CREATE_TABLE_PRIMARY_SHOP = "create table " + TABLE_PRIMARY_SHOP + "("
            + PRIMARY_SHOP_IDD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRIMARY_SHOP_ID + " TEXT NOT NULL, " +
            PRIMARY_SHOP_NAME + " TEXT NOT NULL," +
            PRIMARY_SHOP_TYPE + " TEXT NOT NULL );";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("### DatabaseHelper : onCreate");
        System.out.println("### CREATE_TABLE_ORDER_FORM : " + CREATE_TABLE_ORDER_FORM);
        db.execSQL(CREATE_TABLE_ORDER_FORM);

        System.out.println("### CREATE_TABLE_SR_ORDER_FORM : " + CREATE_TABLE_SR_ORDER_FORM);
        db.execSQL(CREATE_TABLE_SR_ORDER_FORM);

        System.out.println("### CREATE_TABLE_ENQUIRY : " + CREATE_TABLE_ENQUIRY);
        db.execSQL(CREATE_TABLE_ENQUIRY);

        System.out.println("### CREATE_TABLE_AGENCY : " + CREATE_TABLE_AGENCY);
        db.execSQL(CREATE_TABLE_AGENCY);

        System.out.println("### CREATE_TABLE_AGENCY_LOCATION : " + CREATE_TABLE_AGENCY_LOCATION);
        db.execSQL(CREATE_TABLE_AGENCY_LOCATION);

        System.out.println("### CREATE_TABLE_STATE : " + CREATE_TABLE_STATE);
        db.execSQL(CREATE_TABLE_STATE);

        System.out.println("### CREATE_TABLE_SHOP : " + CREATE_TABLE_SHOP);
        db.execSQL(CREATE_TABLE_SHOP);

        System.out.println("### CREATE_TABLE_GROUP : " + CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_GROUP);

        System.out.println("### CREATE_TABLE_PRODUCT : " + CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT);

        System.out.println("### CREATE_TABLE_PRODUCT : " + CREATE_TABLE_PRIMARY_SHOP);
        db.execSQL(CREATE_TABLE_PRIMARY_SHOP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("### DatabaseHelper : onUpgrade");

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_ORDER_FORM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_FORM);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_SR_FORM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SR_FORM);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_ENQUIRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENQUIRY);
        onCreate(db);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_AGENCY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENCY);
        onCreate(db);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_AGENCY_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENCY_LOCATION);
        onCreate(db);


        System.out.println("### DROP TABLE IF EXISTS " + TABLE_STATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
        onCreate(db);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
        onCreate(db);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        onCreate(db);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);

        System.out.println("### DROP TABLE IF EXISTS " + TABLE_PRIMARY_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRIMARY_SHOP);
        onCreate(db);
    }
}
