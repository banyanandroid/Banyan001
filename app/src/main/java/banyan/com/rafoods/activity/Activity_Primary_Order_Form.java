package banyan.com.rafoods.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Timer;
import java.util.TimerTask;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.My_SR_ListAdapter;
import banyan.com.rafoods.database.DBManager;
import banyan.com.rafoods.database.DatabaseHelper;
import banyan.com.rafoods.global.SessionManager;
import banyan.com.rafoods.model.Hero;
import dmax.dialog.SpotsDialog;
import pugman.com.simplelocationgetter.SimpleLocationGetter;

/**
 * Created by Jothiprabhakar on 29-Mar-18.
 */

public class Activity_Primary_Order_Form extends AppCompatActivity implements SimpleLocationGetter.OnLocationGetListener{

    private Toolbar mToolbar;
    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "Auto_Res";

    // Session Manager Class
    SessionManager session;

    Button btn_submit;

    SearchableSpinner spinner_region, spinner_shop_type, spinner_shop, spinner_category, spinner_product;
    Spinner spn_return_type;

    EditText edt_qty, edt_remark;

    ListView list_cart;

    ImageView btn_add_to_cart;


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
    ArrayList<String> Arraylist_product_gst = null;
    ArrayList<String> Arraylist_product_live_stock = null;
    ArrayList<String> Arraylist_product_transit = null;


    ArrayList<String> Arraylist_id = null;
    ArrayList<String> Arraylist_groupid = null;
    ArrayList<String> Arraylist_qty = null;
    ArrayList<String> Arraylist_price = null;
    ArrayList<String> Arraylist_gst = null;
    ArrayList<String> Arraylist_final = null;

    //SESSION
    String str_user_email, str_user_id, str_user_type = "";
    String str_final_order = "";
    String str_shop, str_Shop_id, str_Shop_type_id, str_group, str_group_id, str_product, str_product_id, str_gst,
            str_act_price, str_price, str_qty, str_shop_type, str_return_type, str_return_type_id, str_remarks = "";

    String str_combo_offer = "";

    public static float float_total_amount = 0;

    private DBManager dbManager;
    String str_amt = "";
    My_SR_ListAdapter adapter;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    Double latitude, longitude;

    String str_lat, str_long, str_address = "";

    String str_status = "";

    int flag_individual_discount = 0;
    int flag_stop_timer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return_order_form);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sales Return");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value", "primaryorder");
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
        edt_remark = (EditText) findViewById(R.id.order_edt_remarks);

        list_cart = (ListView) findViewById(R.id.order_list_product_display);

        btn_add_to_cart = (ImageView) findViewById(R.id.order_btn_add_to_Cart);
        btn_submit = (Button) findViewById(R.id.order_btn_submit_order);

        spn_return_type = (Spinner) findViewById(R.id.spn_return_type);

        System.out.println("SESSION USER EMAIL   " + str_user_email);
        System.out.println("SESSION USER ID      " + str_user_id);

        Arraylist_shop_id = new ArrayList<String>();
        Arraylist_shop_name = new ArrayList<String>();
        Arraylist_shop_type = new ArrayList<String>();

        Arraylist_group_id = new ArrayList<String>();
        Arraylist_group_name = new ArrayList<String>();

        Arraylist_product_id = new ArrayList<String>();
        Arraylist_product_name = new ArrayList<String>();
        Arraylist_product_price = new ArrayList<String>();
        Arraylist_product_gst = new ArrayList<String>();
        Arraylist_product_live_stock = new ArrayList<String>();
        Arraylist_product_transit = new ArrayList<String>();

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

                stoptimertask();

                t1 = (TextView) view;
                str_shop = t1.getText().toString();
                str_Shop_id = Arraylist_shop_id.get(position);
                str_Shop_type_id = Arraylist_shop_type.get(position);

                flag_stop_timer = 1;

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

                        /*dialog = new SpotsDialog(Activity_Primary_Order_Form.this);
                        dialog.show();*/
                        queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
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
                str_product_id = Arraylist_product_id.get(position);
                str_gst = Arraylist_product_gst.get(position);
                str_price = Arraylist_product_price.get(position);

                System.out.println("Product ID : " + str_product_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_return_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                str_return_type = parent.getItemAtPosition(pos).toString();

                if (str_return_type.equals("Amount")) {
                    str_return_type_id = "1";
                } else if (str_return_type.equals("Product")) {
                    str_return_type_id = "2";
                } else {

                }

                System.out.println("### str_return_type : " + str_return_type);
                System.out.println("### str_return_type id : " + str_return_type_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }

        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_qty = edt_qty.getText().toString().trim();
                str_remarks = edt_remark.getText().toString().trim();

                stoptimertask();

                if (str_Shop_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select a Shop", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_group_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Category", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_product_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Product", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_qty.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Please Enter the Quantity", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_return_type_id.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Please Select the Return Type", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    System.out.println("QTY " + str_qty);

                    System.out.println("@@@ Array Size : " + heroList.size());

                    System.out.println("### heroList.size() "+heroList.size());

                    str_act_price = str_price;
                    Float flot_qty = Float.parseFloat(str_qty);
                    Float flot_price = Float.parseFloat(str_price);
                    Float flot_tax_percent = Float.parseFloat(str_gst);

                    Float flot_price_tax = flot_price * flot_tax_percent / 100;
                    Float flot_price_incl_tax = flot_price + flot_price_tax;
                    Float flot_total = flot_qty * flot_price_incl_tax;
                    str_amt = "" + flot_total;


                    if (heroList.size() > 0){

                        // check for product is same or not
                        boolean ismatch = false;
                        for (int i = 0; i < heroList.size() ; i++) {

                            Hero hero = heroList.get(i);
                            String value = hero.getName();

                            if (value.equals(str_product)){
                                ismatch = true;

                                Hero hero1 = heroList.get(i);
                                String qty = hero1.getQty();

                                Float ft_qty = Float.parseFloat(qty);
                                Float ft_act_qty = Float.parseFloat(str_qty);

                                Float ft_final_qty = ft_qty + ft_act_qty;

                                int fin_qty = Math.round(ft_final_qty);

                                String up_qty = "" + fin_qty;

                                heroList.remove(i);
                                heroList.add(i, new Hero(str_product_id, str_group_id, str_amt,"","", str_product, up_qty,"","","",""));
                                adapter.notifyDataSetChanged();

                                Fun_Total_Cal();

                                break;
                            }

                        }

                        // if product is new then add product
                        if (ismatch == false) {
                            System.out.println("### new product is added.");
                            heroList.add(new Hero(str_product_id, str_group_id, str_act_price, str_amt, str_gst,
                                    str_product, str_qty, "", "", "",""));
                            adapter = new My_SR_ListAdapter(Activity_Primary_Order_Form.this, R.layout.my_custom_list, heroList);
                            list_cart.setAdapter(adapter);
                            Fun_Total_Cal();

                        }
                    }

                    if (heroList.size() == 0){
                        System.out.println("### first product is added.");
                        // insert first product
                        // insert first product
                        heroList.add(new Hero(str_product_id, str_group_id, str_act_price, str_amt, str_gst,
                                str_product, str_qty, "", "","",""));
                        adapter = new My_SR_ListAdapter(Activity_Primary_Order_Form.this, R.layout.my_custom_list, heroList);
                        list_cart.setAdapter(adapter);
                        Fun_Total_Cal();
                    }

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

            dialog = new SpotsDialog(Activity_Primary_Order_Form.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
            Function_Check_Order_Mode();

        } else {

            getLocal_Shop();

            getLocal_Category();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (flag_stop_timer == 1) {
            System.out.println("Timer FLAG :: " + flag_stop_timer);
        } else {
            startTimer();
        }

    }

    public void startTimer() {

        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 30000, 30000); // 30 Sec

    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }

    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                handler.post(new Runnable() {

                    public void run() {

                        try {
                            if (str_status.equals("Pending")) {
                                System.out.println("LOCATING?");
                                NewLocation();
                            } else {

                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                });
            }

        };
    }

    public void NewLocation() {

        SimpleLocationGetter getter = new SimpleLocationGetter(this, this);
        getter.getLastLocation();
    }

    @Override
    public void onLocationReady(Location location) {
        Log.d("LOCATION", "onLocationReady: lat=" + location.getLatitude() + " lon=" + location.getLongitude());

        System.out.println("LOCATION 1 :: " + location.getLatitude());
        System.out.println("LOCATION 2 :: " + location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        str_lat = String.valueOf(latitude);
        str_long = String.valueOf(longitude);

        try {
            System.out.println("LATT :: " + str_lat);
            System.out.println("LONGG :: " + str_long);
            Arraylist_shop_name.clear();
            Arraylist_shop_id.clear();
            Arraylist_shop_type.clear();

            queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
            Function_Get_Near_By_SHOP();
        } catch (Exception e) {

        }

    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }

    /******************************************
     *    Sum of Arraylist
     * *******************************************/

    public static void Fun_Total_Cal() {

        System.out.println("WELCOMEEEEEEEEEEDDDDDDDDDDDDD ");
        float_total_amount = 0;

        if (heroList.size() == 0) {

        } else {
            for (int i = 0; i < heroList.size(); i++) {
                Hero hero = heroList.get(i);
                String str_price = hero.getPrice();


            }
        }
    }


    /***************************
     * Check Offline Mode
     ***************************/

    public void Function_Check_Order_Mode() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.base_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        str_status = obj.getString("data");
                        dialog.dismiss();

                        if (str_status.equals("Approved")) {

                            dialog.dismiss();
                            dialog = new SpotsDialog(Activity_Primary_Order_Form.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
                            Function_Get_SHOP();

                        } else {
                            dialog.dismiss();
                            NewLocation();
                        }

                        dialog.dismiss();
                    } else if (success == 0) {

                        dialog.dismiss();

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

                System.out.println("### user_id : " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
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
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        DBManager dbManager = new DBManager(Activity_Primary_Order_Form.this);
                        dbManager.open();
                        int result = dbManager.delete_primary_shop();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("shop_id");
                            String name = obj1.getString("shop_name");
                            String type = obj1.getString("shop_type");

                            long response_primary_shop = dbManager.insert_primary_shop(id, name, type);
                            System.out.println("@@@ Inside shop : " + response_primary_shop);
                            if (response_primary_shop == -1) {
                                TastyToast.makeText(getApplicationContext(), "Oops...!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }

                            Arraylist_shop_name.add(name);
                            Arraylist_shop_id.add(id);
                            Arraylist_shop_type.add(type);

                            try {
                                spinner_shop
                                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_shop_name));

                                dialog.dismiss();
                                dialog.cancel();

                            } catch (Exception e) {

                            }
                        }

                        Arraylist_group_id.clear();
                        Arraylist_group_name.clear();

                        queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
                        Function_Get_ProdcutGroup();

                        queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
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
                params.put("user_type", str_user_type);

                System.out.println("### user_id : " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /***************************
     * GET Near by Shop Info
     ***************************/

    public void Function_Get_Near_By_SHOP() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        System.out.println("### AppConfig.url_user_shop_list " + AppConfig.base_url);
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.base_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("shop_id");
                            String name = obj1.getString("shop_name");
                            String type = obj1.getString("shop_type");

                            Arraylist_shop_name.add(name);
                            Arraylist_shop_id.add(id);
                            Arraylist_shop_type.add(type);

                            try {
                                spinner_shop
                                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_shop_name));

                                DBManager dbManager = new DBManager(Activity_Primary_Order_Form.this);
                                dbManager.open();
                                int result = dbManager.delete_product();
                                int result1 = dbManager.delete_group();
                                int result2 = dbManager.delete_shop();

                            } catch (Exception e) {

                            }
                        }

                        Arraylist_group_id.clear();
                        Arraylist_group_name.clear();

                        queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
                        Function_Get_ProdcutGroup();

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
                params.put("user_type", str_user_type);
                params.put("latitude", str_lat);
                params.put("longitude", str_long);
                params.put("address", str_address);

                System.out.println("@@@ user_id : " + str_user_id);
                System.out.println("@@@ str_user_type : " + str_user_type);
                System.out.println("@@@ str_lat : " + str_lat);
                System.out.println("@@@ str_long : " + str_long);
                System.out.println("@@@ str_address : " + str_address);

                return checkParams(params);
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
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        DBManager dbManager = new DBManager(Activity_Primary_Order_Form.this);
                        dbManager.open();
                        int result = dbManager.delete_group();
                        System.out.println("DELETE GROUP : " + result);

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("productgroup_id");
                            String name = obj1.getString("productgroup_name");

                            if (str_status.equals("Approved")) {
                                long response_group = dbManager.insert_group(id, name);
                                System.out.println("!!! Group RES : " + response_group);
                                if (response_group == -1) {
                                    TastyToast.makeText(getApplicationContext(), "Oops...!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                } else {

                                }
                            } else {

                            }

                            Arraylist_group_name.add(name);
                            Arraylist_group_id.add(id);

                            try {
                                spinner_category
                                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
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
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("product_id");
                            String name = obj1.getString("product_name");
                            String price = obj1.getString("product_price");
                            String gst = obj1.getString("gst");
                            String live_stock = obj1.getString("live_stock");
                            String transit = obj1.getString("transit");

                            Arraylist_product_id.add(id);
                            Arraylist_product_name.add(name);
                            Arraylist_product_price.add(price);
                            Arraylist_product_gst.add(gst);
                            Arraylist_product_live_stock.add(live_stock);
                            Arraylist_product_transit.add(transit);

                            try {
                                spinner_product
                                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
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

                        DBManager dbManager = new DBManager(Activity_Primary_Order_Form.this);
                        dbManager.open();
                        int result = dbManager.delete_product();

                        System.out.println("ARRAY SIZE : " + arr.length());

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("product_id");
                            String name = obj1.getString("product_name");
                            String group_id = obj1.getString("productgroup_id");
                            String retail_price = obj1.getString("retail_price");
                            String gst = obj1.getString("gst");
                            String dis_price = obj1.getString("distributor_price");
                            String live_stock = obj1.getString("live_stock");
                            String transit = obj1.getString("transit");

                            long response_product = dbManager.insert_PRODUCT(id, name, "GST", group_id, retail_price, dis_price, live_stock, transit);
                            System.out.println("@@@ Inside product : " + response_product);

                            if (response_product == -1) {
                                TastyToast.makeText(getApplicationContext(), "Internal Local Data Error", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }
                        }

                        dialog.dismiss();

                        try {

                            Function_Network_status();

                        } catch (Exception e) {

                        }

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

    /*****************************
     *  Fun Network Status
     * **********************************/

    private void Function_Network_status() {

        new AlertDialog.Builder(Activity_Primary_Order_Form.this)
                .setTitle("Anil Foods")
                .setMessage("OFFLine Mode Enaled Successfully. Please Trun Off your Network")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,

                                                int which) {
                                // TODO Auto-generated method stub

                            }

                        }).show();
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

                        dialog = new SpotsDialog(Activity_Primary_Order_Form.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Primary_Order_Form.this);
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
                        long response = dbManager.insert_SR_Order_Form(str_Shop_id, str_final_order, str_user_id,
                                str_user_type, str_return_type_id, str_remarks);

                        System.out.println("### response : " + response);
                        if (response == -1) {

                            TastyToast.makeText(getApplicationContext(), "Oops...! Try again Later..!", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        } else {

                            TastyToast.makeText(getApplicationContext(), "Order Placed Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            new AlertDialog.Builder(Activity_Primary_Order_Form.this)
                                    .setTitle("Anil Foods")
                                    .setMessage("Sales Returned Successfully !")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,

                                                                    int which) {
                                                    // TODO Auto-generated method stub

                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    i.putExtra("from_value", "primaryorder");
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
                AppConfig.url_add_sales_return, new Response.Listener<String>() {
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

                        TastyToast.makeText(getApplicationContext(), "Sales Returned Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        new AlertDialog.Builder(Activity_Primary_Order_Form.this)
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
                                                i.putExtra("from_value", "primaryorder");
                                                startActivity(i);

                                            }

                                        }).show();
                    }else if(success == 2){
                        TastyToast.makeText(getApplicationContext(), "Order Already Placed!", TastyToast.LENGTH_LONG, TastyToast.INFO);
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
                params.put("return_type", str_return_type_id);
                params.put("remarks", str_remarks);

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

        Cursor cursor = dbManager.Fetch_primary_Shop();
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String Shop_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRIMARY_SHOP_ID));
                String Shop_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRIMARY_SHOP_NAME));
                String Shop_type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRIMARY_SHOP_TYPE));

                Arraylist_shop_id.add(Shop_id);
                Arraylist_shop_name.add(Shop_name);
                Arraylist_shop_type.add(Shop_type);

                Log.e(TAG, "### SHOP : " + Arraylist_shop_id);
                Log.e(TAG, "### SHOP NAME: " + Arraylist_shop_name);
                Log.e(TAG, "### SHOP TYPE: " + Arraylist_shop_type);

                spinner_shop
                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
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
                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
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
                        .setAdapter(new ArrayAdapter<String>(Activity_Primary_Order_Form.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_product_name));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }


}