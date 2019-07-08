package banyan.com.rafoods.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.MyList_Edt_Order_Adapter;
import banyan.com.rafoods.database.DBManager;
import banyan.com.rafoods.database.DatabaseHelper;
import banyan.com.rafoods.global.SessionManager;
import banyan.com.rafoods.model.Hero;
import dmax.dialog.SpotsDialog;

/**
 * Created by Jothiprabhakar on 29-Mar-18.
 */

public class Activity_Edit_Order_Form extends AppCompatActivity {

    private Toolbar mToolbar;
    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "Auto_Res";

    // Session Manager Class
    SessionManager session;

    Button btn_add_to_cart, btn_submit;

    SearchableSpinner spinner_region, spinner_shop_type, spinner_shop, spinner_category, spinner_product;


    EditText edt_qty;

    public static TextView txt_total;

    ListView list_cart;


    // New Listview
    public static List<Hero> heroList;

    // creating new HashMap
    HashMap<String, String> map = new HashMap<String, String>();

    static ArrayList<HashMap<String, String>> complaint_list;
    HashMap<String, String> params = new HashMap<String, String>();

    TextView t1;

    ArrayList<String> Arraylist_selected_product = null;

    ArrayList<String> Arraylist_shop_id = null;
    ArrayList<String> Arraylist_shop_name = null;
    ArrayList<String> Arraylist_shop_type = null;

    ArrayList<String> Arraylist_group_id = null;
    ArrayList<String> Arraylist_group_name = null;

    ArrayList<String> Arraylist_product_id = null;
    ArrayList<String> Arraylist_product_name = null;
    ArrayList<String> Arraylist_product_price = null;


    ArrayList<String> Arraylist_id = null;
    ArrayList<String> Arraylist_groupid = null;
    ArrayList<String> Arraylist_qty = null;
    ArrayList<String> Arraylist_price = null;
    ArrayList<String> Arraylist_final = null;

    //SESSION
    String str_user_email, str_user_id, str_user_type = "";
    String str_final_order = "";
    String str_shop, str_Shop_id, str_Shop_type_id, str_group, str_group_id, str_product, str_product_id, str_price, str_qty, str_shop_type = "";

    String str_combo_offer = "";

    public static float float_total_amount = 0;

    private DBManager dbManager;

    public static String str_product_name,str_grand_total;

    public static Float flot_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_form);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order Form");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value", "order");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        str_user_email = user.get(SessionManager.KEY_USER);
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        str_user_type = user.get(SessionManager.KEY_USER_TYPE);

        spinner_shop = (SearchableSpinner) findViewById(R.id.order_spinner_shop);
        spinner_category = (SearchableSpinner) findViewById(R.id.order_spinner_category);
        spinner_product = (SearchableSpinner) findViewById(R.id.order_spinner_product);

        edt_qty = (EditText) findViewById(R.id.order_edt_qty);

        txt_total = (TextView) findViewById(R.id.order_txt_total_amt);

        list_cart = (ListView) findViewById(R.id.order_list_product_display);

        btn_add_to_cart = (Button) findViewById(R.id.order_btn_add_to_Cart);
        btn_submit = (Button) findViewById(R.id.order_btn_submit_order);

        System.out.println("SESSION USER EMAIL   " + str_user_email);
        System.out.println("SESSION USER ID      " + str_user_id);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Edit_Order_Form.this);

        str_grand_total = sharedPreferences.getString("grand_total", "grand_total");

        Arraylist_shop_id = new ArrayList<String>();
        Arraylist_shop_name = new ArrayList<String>();
        Arraylist_shop_type = new ArrayList<String>();

        Arraylist_group_id = new ArrayList<String>();
        Arraylist_group_name = new ArrayList<String>();

        Arraylist_product_id = new ArrayList<String>();
        Arraylist_product_name = new ArrayList<String>();
        Arraylist_product_price = new ArrayList<String>();

        Arraylist_id = new ArrayList<String>();
        Arraylist_groupid = new ArrayList<String>();
        Arraylist_qty = new ArrayList<String>();
        Arraylist_price = new ArrayList<String>();
        Arraylist_final = new ArrayList<String>();

        // Initialize Arraytlist
        heroList = new ArrayList<>();

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();
        Arraylist_selected_product = new ArrayList<String>();

        spinner_shop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Shop");

                t1 = (TextView) view;
                str_shop = t1.getText().toString();
                str_Shop_id = Arraylist_shop_id.get(position);
                str_Shop_type_id = Arraylist_shop_type.get(position);

                System.out.println("Shop ID : " + str_Shop_id);
                System.out.println("TYPE ID : " + str_Shop_type_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Group");

                t1 = (TextView) view;
                str_group = t1.getText().toString();
                str_group_id = Arraylist_group_id.get(position);

                System.out.println("Group ID : " + str_group_id);

                Arraylist_product_id.clear();
                Arraylist_product_name.clear();
                Arraylist_product_price.clear();

                if (str_Shop_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select a Shop", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    if (isNetworkConnected()) {

                        dialog = new SpotsDialog(Activity_Edit_Order_Form.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Edit_Order_Form.this);
                        Function_Get_Product();

                    } else {

                        getLocal_Product(str_group_id);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Product");

                t1 = (TextView) view;

                str_product = t1.getText().toString();
                str_product_name = Arraylist_product_name.get(position);
                str_product_id = Arraylist_product_id.get(position);
                str_price = Arraylist_product_price.get(position);

                System.out.println("Product ID : " + str_product_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_qty = edt_qty.getText().toString().trim();

                if (str_Shop_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select a Shop", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_shop_type.equals("Customer Type")) {
                    TastyToast.makeText(getApplicationContext(), "Select Customer Type", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_group_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Category", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_product_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Product", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_qty.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Please Enter the Quantity", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    Float flot_qty = Float.parseFloat(str_qty);
                    Float flot_price = Float.parseFloat(str_price);
                    flot_total = flot_qty * flot_price;

                    String str_amt = "" + flot_total;

                    System.out.println("QTY : "  + flot_qty);
                    System.out.println("AMOUNT : "  + str_amt);

                    heroList.add(new Hero(str_product_id, str_group_id, str_amt,"","", str_product,
                            str_qty, "", "","",""));

                    MyList_Edt_Order_Adapter adapter = new MyList_Edt_Order_Adapter(Activity_Edit_Order_Form.this, R.layout.my_edt_ordercustom_list, heroList);
                    list_cart.setAdapter(adapter);
                    Fun_Total_Cal();
                    TastyToast.makeText(getApplicationContext(), "Product Added Into Cart", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Get_VALUE();
            }
        });


        /***************************
         *  Get Agency On/off Line
         * ************************/

        if (isNetworkConnected()) {

            dialog = new SpotsDialog(Activity_Edit_Order_Form.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Edit_Order_Form.this);
            Function_Get_SHOP();

        } else {

            getLocal_Shop();

            getLocal_Category();
        }


    }

    /******************************************
     *    Sum of Arraylist
     * *******************************************/

    public static void Fun_Total_Cal() {

        System.out.println("WELCOMEEEEEEEEEEDDDDDDDDDDDDD ");
        float_total_amount = 0;

        if (heroList.size() == 0) {
            txt_total.setText("Total : " + str_grand_total);
        } else {
            for (int i = 0; i < heroList.size(); i++) {
                Hero hero = heroList.get(i);
                String str_price = hero.getPrice();

                Float price = Float.parseFloat(str_price);
                float_total_amount = float_total_amount + price;
                int a = Math.round(float_total_amount);
                txt_total.setText("Total : " + a);

            }
        }
    }

    /***************************
     * GET Shop Info
     ***************************/

    public void Function_Get_SHOP() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        System.out.println("### AppConfig.url_user_shop_list " + AppConfig.url_dis_prod_shop);
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_prod_shop, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("message");

                        DBManager dbManager = new DBManager(Activity_Edit_Order_Form.this);
                        dbManager.open();
                        int result = dbManager.delete_shop();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("shop_id");
                            String name = obj1.getString("shop_name");
                            String type = obj1.getString("shop_type");

                            long response_shop = dbManager.insert_shop(id, name, type);
                            System.out.println("@@@ Inside shop : " + response_shop);
                            if (response_shop == -1) {
                                TastyToast.makeText(getApplicationContext(), "Oops...!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }

                            Arraylist_shop_name.add(name);
                            Arraylist_shop_id.add(id);
                            Arraylist_shop_type.add(type);

                            try {
                                spinner_shop
                                        .setAdapter(new ArrayAdapter<String>(Activity_Edit_Order_Form.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_shop_name));

                            } catch (Exception e) {

                            }
                        }

                        Arraylist_group_id.clear();
                        Arraylist_group_name.clear();

                        queue = Volley.newRequestQueue(Activity_Edit_Order_Form.this);
                        Function_Get_ProdcutGroup();

                        queue = Volley.newRequestQueue(Activity_Edit_Order_Form.this);
                        Function_Get_Product_Offline();

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No Agency Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Internal Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);

                System.out.println("### user_id : " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Product Group
     ***************************/

    public void Function_Get_ProdcutGroup() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_prod_group, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("message");

                        DBManager dbManager = new DBManager(Activity_Edit_Order_Form.this);
                        dbManager.open();
                        int result = dbManager.delete_group();
                        System.out.println("DELETE GROUP : " + result);

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("productgroup_id");
                            String name = obj1.getString("productgroup_name");

                            long response_group = dbManager.insert_group(id, name);
                            System.out.println("!!! Group RES : " + response_group);
                            if (response_group == -1) {
                                TastyToast.makeText(getApplicationContext(), "Oops...!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }

                            Arraylist_group_name.add(name);
                            Arraylist_group_id.add(id);

                            try {
                                spinner_category
                                        .setAdapter(new ArrayAdapter<String>(Activity_Edit_Order_Form.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_group_name));


                            } catch (Exception e) {

                            }
                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No Category Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Internal Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Product By Group
     ***************************/

    public void Function_Get_Product() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_prod_details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("message");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("product_id");
                            String name = obj1.getString("product_name");
                            String price = obj1.getString("product_price");

                            Arraylist_product_id.add(id);
                            Arraylist_product_name.add(name);
                            Arraylist_product_price.add(price);

                            try {
                                spinner_product
                                        .setAdapter(new ArrayAdapter<String>(Activity_Edit_Order_Form.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_product_name));

                            } catch (Exception e) {

                            }
                        }
                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No Product Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Internal Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("group_id", str_group_id);
                params.put("shop_id", str_Shop_id);

                System.out.println("GROUP IDDD :: " + str_group_id);
                System.out.println("SHOPPP IDDD :: " + str_Shop_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Product Offline
     ***************************/

    public void Function_Get_Product_Offline() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_offline_prod_details, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("PRODUCT COUNT", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        DBManager dbManager = new DBManager(Activity_Edit_Order_Form.this);
                        dbManager.open();
                        int result = dbManager.delete_product();

                        System.out.println("ARRAY SIZE : " + arr.length());

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("product_id");
                            String name = obj1.getString("product_name");
                            String group_id = obj1.getString("productgroup_id");
                            String retail_price = obj1.getString("retail_price");
                            String dis_price = obj1.getString("distributor_price");

                            long response_product = dbManager.insert_PRODUCT(id,name,group_id,retail_price,dis_price,"gst","","");
                            System.out.println("@@@ Inside product : " + response_product);
                            if (response_product == -1) {
                                TastyToast.makeText(getApplicationContext(), "Internal Local Data Error", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }
                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "No Product Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Internal Error !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);
                params.put("user_type", str_user_type);

                System.out.println("LOC USER ID :: " + str_user_id);
                System.out.println("LOC USER TYPE :: " + str_user_type);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     * Vivekanandhan
     *
     * Function Get Product
     * ******************************/
    public void Get_VALUE() {

        str_combo_offer = "";

        for (int i = 0; i < heroList.size(); i++) {

            Hero hero = heroList.get(i);
            String str_id = hero.getId();
            String str_groupid = hero.getgroupId();
            String str_qty = hero.getQty();
            String str_price = hero.getPrice();

            Arraylist_id.add(str_id);
            Arraylist_groupid.add(str_groupid);
            Arraylist_qty.add(str_qty);
            Arraylist_price.add(str_price);
        }

        Arraylist_final.clear();

        for (int j = 0; j < Arraylist_id.size(); j++) {

            String str_final_id = Arraylist_id.get(j);
            String str_final_groupid = Arraylist_groupid.get(j);
            String str_final_qty = Arraylist_qty.get(j);
            String str_final_price = Arraylist_price.get(j);

            String final_order = str_final_id + "-" + str_final_groupid + "-" + str_final_qty;

            Arraylist_final.add(final_order);
        }

        //Ordered Items
        str_final_order = TextUtils.join(", ", Arraylist_final);

        System.out.println("SESSION_ID    :::::" + str_user_id);
        System.out.println("PRODUCT_DETAILS   :::::" + str_final_order);
        System.out.println("SHOP ID   :::::" + str_Shop_id);

        if (str_Shop_id != null && !str_Shop_id.isEmpty() && !str_Shop_id.equals("null")) {
            if (str_final_order.equals("")) {

                TastyToast.makeText(getApplicationContext(), "Select Your Product", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

            } else {

                System.out.println("SESSION_ID    :::::" + str_user_id);
                System.out.println("PRODUCT_DETAILS   :::::" + str_final_order);
                System.out.println("SHOP ID   :::::" + str_Shop_id);

                try {

                    // Vivekanandhan
                    // check network is connected
                    if (isNetworkConnected()) {

                        dialog = new SpotsDialog(Activity_Edit_Order_Form.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Edit_Order_Form.this);
                        Function_Proceed();

                    } else {

                        //get detai
                        //str_Shop_id
                        //str_final_order
                        //str_user_id
                        String str_offer = "";
                        //store data in local db
                        DBManager dbManager = new DBManager(this);
                        dbManager.open();
                        long response = dbManager.insert_Order_Form(str_Shop_id, str_final_order, str_user_id, str_user_type, str_offer);
                        System.out.println("### response : " + response);
                        if (response == -1) {

                            TastyToast.makeText(getApplicationContext(), "Oops...! Try again Later..!", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        } else {

                            TastyToast.makeText(getApplicationContext(), "Order Placed Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            new AlertDialog.Builder(Activity_Edit_Order_Form.this)
                                    .setTitle("Anil Foods")
                                    .setMessage("Order Placed Successfully !")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,

                                                                    int which) {
                                                    // TODO Auto-generated method stub

                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    i.putExtra("from_value", "order");
                                                    startActivity(i);

                                                }

                                            }).show();


                        }

                    }

                } catch (Exception e) {
                    System.out.println("### Exception");
                }
            }
        } else {
            TastyToast.makeText(getApplicationContext(), "Select Shop", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        }


    }

    /******************************************
     *    Proceed Function
     * ****************************************/
    private void Function_Proceed() {

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_add_product, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Log.d("USER_LOGIN", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println("REG 00" + obj);

                    int success = obj.getInt("status");

                    System.out.println("REG" + success);

                    if (success == 1) {

                        dialog.dismiss();

                        TastyToast.makeText(getApplicationContext(), "Order Placed Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        new AlertDialog.Builder(Activity_Edit_Order_Form.this)
                                .setTitle("Anil Foods")
                                .setMessage("Order Placed Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                i.putExtra("from_value", "order");
                                                startActivity(i);

                                            }

                                        }).show();
                    } else {

                        TastyToast.makeText(getApplicationContext(), "Oops...! Try again Later..!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                    }

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);
                params.put("user_type", str_user_type);
                params.put("shop_id", str_Shop_id);
                params.put("product_details", str_final_order);


                System.out.println("SHOP ID   :::::" + str_Shop_id);
                System.out.println("USER ID        :::::" + str_user_id);
                System.out.println("PRODUCT_DETAILS   :::::" + str_final_order);

                return checkParams(params);
                //return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        // Adding request to request queue
        queue.add(request);
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /***********************
     *  Local Shop
     * *********************/

    private void getLocal_Shop() {

        Arraylist_shop_name.clear();
        Arraylist_shop_id.clear();
        Arraylist_shop_type.clear();

        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        Cursor cursor = dbManager.Fetch_Shop();
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String Shop_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_ID));
                String Shop_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_NAME));
                String Shop_type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHOP_TYPE));

                Arraylist_shop_id.add(Shop_id);
                Arraylist_shop_name.add(Shop_name);
                Arraylist_shop_type.add(Shop_type);

                Log.e(TAG, "### SHOP : " + Arraylist_shop_id);
                Log.e(TAG, "### SHOP NAME: " + Arraylist_shop_name);
                Log.e(TAG, "### SHOP TYPE: " + Arraylist_shop_type);

                spinner_shop
                        .setAdapter(new ArrayAdapter<String>(Activity_Edit_Order_Form.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_shop_name));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }

    /***********************
     *  Local Category
     * *********************/

    private void getLocal_Category() {

        System.out.println("FUnction CALLED");

        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        Cursor cursor = dbManager.Fetch_group();
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String Group_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_ID));
                String Group_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_NAME));

                Arraylist_group_id.add(Group_id);
                Arraylist_group_name.add(Group_name);

                Log.e(TAG, "### GROUP : " + Arraylist_group_id);
                Log.e(TAG, "### GROUP NAME: " + Arraylist_group_name);

                spinner_category
                        .setAdapter(new ArrayAdapter<String>(Activity_Edit_Order_Form.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_group_name));

                getLocal_Shop();

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }

    /*************************************
     *  Local Product by Category
     * ***********************************/

    private void getLocal_Product(String str_group_id) {

        System.out.println("FUnction CALLED");

        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        Cursor cursor = dbManager.Fetch_product(str_group_id);
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String prod_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ID));
                String prod_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_NAME));
                String prod_group = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_GROUP_ID));

                Arraylist_product_id.add(prod_id);
                Arraylist_product_name.add(prod_name);


                Log.e(TAG, "### PRODUCT : " + Arraylist_product_id);
                Log.e(TAG, "### PRODUCT NAME: " + Arraylist_product_name);

                spinner_product
                        .setAdapter(new ArrayAdapter<String>(Activity_Edit_Order_Form.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_product_name));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }


}