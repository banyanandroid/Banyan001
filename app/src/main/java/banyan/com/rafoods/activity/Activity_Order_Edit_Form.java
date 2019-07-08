package banyan.com.rafoods.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.MyListAdapterForEdit;
import banyan.com.rafoods.database.DBManager;
import banyan.com.rafoods.database.DatabaseHelper;
import banyan.com.rafoods.global.SessionManager;
import banyan.com.rafoods.model.HeroForEdit;
import dmax.dialog.SpotsDialog;

/**
 * Created by Jothiprabhakar on 29-Mar-18.
 */

public class Activity_Order_Edit_Form extends AppCompatActivity {

    private Toolbar mToolbar;

    SpotsDialog dialog;

    public static RequestQueue queue;
    String TAG = "Auto_Res";

    // Session Manager Class
    SessionManager session;

    Button btn_submit;
    ImageView btn_add_to_cart, img_network_status, img_info;
    TextView txt_shop_name, txt_marquee_offer;
    public static TextView txt_total;
    TextView t1;
    SearchableSpinner spinner_region, spinner_shop_type, spinner_shop, spinner_category, spinner_product;
    Spinner spn_discount_type;
    EditText edt_qty, edt_discount, edt_over_all_discount;

    ListView list_cart;

    int order_edt_new_position;
    String new_qty;


    // New Listview
    public static List<HeroForEdit> heroList;

    // creating new HashMap
    HashMap<String, String> map = new HashMap<String, String>();

    static ArrayList<HashMap<String, String>> complaint_list;
    HashMap<String, String> params = new HashMap<String, String>();

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
    ArrayList<String> Arraylist_discount = null;
    ArrayList<String> Arraylist_discount_type = null;
    ArrayList<String> Arraylist_gst = null;
    ArrayList<String> Arraylist_final = null;

    //SESSION
    String str_user_email, str_user_id, str_user_type = "";
    String str_final_order = "";
    String str_shop, str_Shop_id, str_Shop_type_id, str_group, str_group_id, str_product, str_product_id, str_act_price, str_price,
            str_gst, str_live_stock, str_transit, str_qty, str_discount, str_shop_type, str_discount_type, str_discount_type_id = "";
    String str_over_all_discount = "";
    String str_combo_offer = "";
    String str_amt = "";

    String sp_shop_id, sp_shop_name, sp_order_num;

    public static float float_total_amount = 0;

    private DBManager dbManager;

    MyListAdapterForEdit adapter;

    String str_status = "";

    int flag_individual_discount = 0;
    int flag_stop_timer = 0;

    // For Alert Dialog

    String str_alert_discount_type, str_alert_discount_type_id, str_alert_new_qty, str_alert_new_dis, str_alert_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit_form);

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
        spn_discount_type = (Spinner) findViewById(R.id.order_spn_discount_type);

        edt_qty = (EditText) findViewById(R.id.order_edt_qty);
        edt_discount = (EditText) findViewById(R.id.order_edt_discount);
        edt_over_all_discount = (EditText) findViewById(R.id.order_edt_overall_offer);

        list_cart = (ListView) findViewById(R.id.order_list_product_display);

        btn_add_to_cart = (ImageView) findViewById(R.id.order_btn_add_to_Cart);
        btn_submit = (Button) findViewById(R.id.order_btn_submit_order);

        img_info = (ImageView) findViewById(R.id.order_img_info);

        txt_shop_name = (TextView) findViewById(R.id.order_text_shop);
        txt_total = (TextView) findViewById(R.id.order_txt_total_amt);
        txt_marquee_offer = (TextView) this.findViewById(R.id.order_marquee_offer);
        txt_marquee_offer.setSelected(true);


        System.out.println("SESSION USER EMAIL   " + str_user_email);
        System.out.println("SESSION USER ID      " + str_user_id);
        System.out.println("SESSION USER TYPE      " + str_user_type);

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
        Arraylist_discount = new ArrayList<String>();
        Arraylist_discount_type = new ArrayList<String>();
        Arraylist_price = new ArrayList<String>();
        Arraylist_gst = new ArrayList<String>();
        Arraylist_final = new ArrayList<String>();

        // Initialize Arraytlist
        heroList = new ArrayList<>();

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();
        Arraylist_selected_product = new ArrayList<String>();


        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Order_Edit_Form.this);

        sp_shop_id = sharedPreferences.getString("order_shop_id", "order_shop_id");
        sp_shop_name = sharedPreferences.getString("order_shop_name", "order_shop_name");
        sp_order_num = sharedPreferences.getString("order_order_num", "order_order_num");

        txt_shop_name.setText("" + sp_shop_name);

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
                Arraylist_product_live_stock.clear();
                Arraylist_product_transit.clear();

                if (sp_shop_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select a Shop", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    if (isNetworkConnected()) {

                        /*dialog = new SpotsDialog(Activity_Order_Form.this);
                        dialog.show();*/
                        queue = Volley.newRequestQueue(Activity_Order_Edit_Form.this);
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
                str_price = Arraylist_product_price.get(position);
                str_gst = Arraylist_product_gst.get(position);
                str_live_stock = Arraylist_product_live_stock.get(position);
                str_transit = Arraylist_product_transit.get(position);

                System.out.println("Product ID : " + str_product_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_discount_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                str_discount_type = parent.getItemAtPosition(pos).toString();

                if (str_discount_type.equals("%")) {
                    str_discount_type_id = "1";
                } else if (str_discount_type.equals("Rs")) {
                    str_discount_type_id = "2";
                } else {

                }

                System.out.println("### str_discount_type : " + str_discount_type);
                System.out.println("### str_discount_type id : " + str_discount_type_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }

        });

        edt_over_all_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (heroList.size() > 0) {

                    for (int i = 0; i < heroList.size(); i++) {

                        HeroForEdit hero = heroList.get(i);

                        String list_prod_id = hero.getId();
                        String list_group_id = hero.getgroupId();
                        String list_act_value = hero.getAct_price();
                        String list_value = hero.getPrice();
                        String list_gst = hero.getTax();
                        String list_name = hero.getName();
                        String list_qty = hero.getQty();
                        String list_dis = hero.getDiscount();
                        String list_dis_type = hero.getDiscount_type();
                        String list_live_stock = hero.getLive_stock();
                        String list_transit = hero.getTransit();


                        String str_over_all_discount = "" + s;

                        if (!str_over_all_discount.equals("")) {
                            Float float_list_act_value = Float.parseFloat(list_act_value);
                            Float float_list_qty = Float.parseFloat(list_qty);
                            Float float_list_gst = Float.parseFloat(list_gst);
                            Float float_over_all_dis = Float.parseFloat(str_over_all_discount);

                            Float float_amt = float_list_act_value * float_list_qty;
                            Float float_dis_amt = float_amt * float_over_all_dis / 100;
                            Float float_aft_dis_amt = float_amt - float_dis_amt;
                            Float float_gst_amt = float_aft_dis_amt * float_list_gst / 100;
                            Float float_final_price = float_aft_dis_amt + float_gst_amt;

                            String str_final_price = "" + float_final_price;

                            heroList.remove(i);
                            heroList.add(i, new HeroForEdit(list_prod_id, list_group_id, list_act_value, str_final_price,
                                    list_gst, list_name,
                                    list_qty, list_dis, list_dis_type, "", "", "", list_live_stock, list_transit));
                            adapter.notifyDataSetChanged();

                            Fun_Total_Cal();

                            edt_discount.setTag(edt_discount.getKeyListener());
                            edt_discount.setKeyListener(null);

                        } else {

                        }
                    }

                } else {

                }
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_qty = edt_qty.getText().toString().trim();
                str_discount = edt_discount.getText().toString().trim();

                if (str_group_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Category", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_product_id == null) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Product", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_qty.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Please Enter the Quantity", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    str_act_price = str_price;
                    Float flot_qty = Float.parseFloat(str_qty);
                    Float flot_price = Float.parseFloat(str_price);
                    Float flot_tax_percent = Float.parseFloat(str_gst);

                    if (str_discount_type.equals("Discount")) {

                        Float flot_price_tax = flot_price * flot_tax_percent / 100;
                        Float flot_price_incl_tax = flot_price + flot_price_tax;
                        Float flot_total = flot_qty * flot_price_incl_tax;
                        str_amt = "" + flot_total;

                    } else {

                        if (!str_discount.equals("")) {

                            Float flot_discount = Float.parseFloat(str_discount);
                            Float float_act_price = flot_price * flot_qty;

                            if (str_discount_type_id.equals("1")) {

                                Float float_dis_amt = float_act_price * flot_discount / 100;
                                Float per_float_amt = float_act_price - float_dis_amt;
                                Float gst_per = per_float_amt * flot_tax_percent / 100;
                                Float total_per_amt = per_float_amt + gst_per;
                                str_amt = "" + total_per_amt;

                                flag_individual_discount = 1;
                                edt_over_all_discount.setTag(edt_over_all_discount.getKeyListener());
                                edt_over_all_discount.setKeyListener(null);

                            } else if (str_discount_type_id.equals("2")) {

                                Float rs_float_amt = float_act_price - flot_discount;
                                Float gst_rs = rs_float_amt * flot_tax_percent / 100;
                                Float total_rs_amt = rs_float_amt + gst_rs;
                                str_amt = "" + total_rs_amt;

                                flag_individual_discount = 1;

                                edt_over_all_discount.setTag(edt_over_all_discount.getKeyListener());
                                edt_over_all_discount.setKeyListener(null);

                            } else {

                                TastyToast.makeText(getApplicationContext(), "Internal Error", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                            }


                        } else {
                            Float flot_price_tax = flot_price * flot_tax_percent / 100;
                            Float flot_price_incl_tax = flot_price + flot_price_tax;
                            Float flot_total = flot_qty * flot_price_incl_tax;
                            str_amt = "" + flot_total;
                        }

                    }

                    // int int_price = Math.round(flot_total);

                    System.out.println("QTY " + str_qty);
                    System.out.println("PRICE " + str_price);
                    System.out.println("TATAL " + str_amt);
                    System.out.println("QTY " + str_qty);


                    System.out.println("@@@ Array Size : " + heroList.size());

                    System.out.println("### heroList.size() " + heroList.size());
                    if (heroList.size() > 0) {

                        // check for product is same or not
                        boolean ismatch = false;
                        for (int i = 0; i < heroList.size(); i++) {

                            HeroForEdit hero = heroList.get(i);
                            String value = hero.getName();

                            if (value.equals(str_product)) {
                                ismatch = true;

                                HeroForEdit hero1 = heroList.get(i);
                                String qty = hero1.getQty();
                                String price = hero1.getPrice();

                                Float ft_qty = Float.parseFloat(qty);
                                Float ft_act_qty = Float.parseFloat(str_qty);
                                Float ft_price = Float.parseFloat(price);
                                Float ft_act_price = Float.parseFloat(str_amt);

                                Float ft_final_qty = ft_qty + ft_act_qty;
                                Float ft_final_price = ft_price + ft_act_price;

                                int fin_qty = Math.round(ft_final_qty);
                                //int fin_price = Math.round(ft_final_price);

                                String up_qty = "" + fin_qty;
                                String up_price = "" + ft_final_price;

                                if (str_discount_type_id.equals("")) {
                                    str_discount_type_id = "0";
                                } else {

                                }
                                if (str_discount.equals("")) {
                                    str_discount = "0";
                                } else {

                                }

                                heroList.remove(i);
                                heroList.add(i, new HeroForEdit(str_product_id, str_group_id, str_act_price, up_price,
                                        str_gst, str_product, up_qty, str_discount, str_discount_type_id, "", "",
                                        "", str_live_stock, str_transit));
                                adapter.notifyDataSetChanged();

                                str_discount_type_id = "";
                                str_discount = "";

                                Fun_Total_Cal();

                                break;
                            }
                        }

                        // if product is new then add product
                        if (ismatch == false) {
                            System.out.println("### new product is added.");

                            if (str_discount_type_id.equals("")) {
                                str_discount_type_id = "0";
                            } else {

                            }
                            if (str_discount.equals("")) {
                                str_discount = "0";
                            } else {

                            }
                            heroList.add(new HeroForEdit(str_product_id, str_group_id, str_act_price, str_amt, str_gst,
                                    str_product, str_qty, str_discount, str_discount_type_id, str_live_stock, "",
                                    "", "", str_transit));
                            adapter = new MyListAdapterForEdit(Activity_Order_Edit_Form.this, R.layout.my_custom_list, heroList);
                            list_cart.setAdapter(adapter);

                            str_discount_type_id = "";
                            str_discount = "";

                            Fun_Total_Cal();

                        }
                    }

                    if (heroList.size() == 0) {
                        System.out.println("### first product is added.");

                        if (str_discount_type_id.equals("")) {
                            str_discount_type_id = "0";
                        } else {

                        }
                        if (str_discount.equals("")) {
                            str_discount = "0";
                        } else {

                        }
                        // insert first product
                        heroList.add(new HeroForEdit(str_product_id, str_group_id, str_act_price, str_amt, str_gst,
                                str_product, str_qty, str_discount, str_discount_type_id, "", "", "",
                                str_live_stock, str_transit));
                        adapter = new MyListAdapterForEdit(Activity_Order_Edit_Form.this, R.layout.my_custom_list, heroList);
                        list_cart.setAdapter(adapter);

                        str_discount_type_id = "";
                        str_discount = "";

                        Fun_Total_Cal();
                    }

                    TastyToast.makeText(getApplicationContext(), "Product Added Into Cart", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                }

            }
        });

        list_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                order_edt_new_position = position;

                HeroForEdit hero = heroList.get(position);
                String str_name = hero.getName();
                String str_qty = hero.getQty();
                String str_dis_type = hero.getDiscount_type();
                String str_dis = hero.getDiscount();

                try {

                    System.out.println("str_name :" + str_name);
                    System.out.println("str_qty :" + str_qty);
                    System.out.println("str_dis_type :" + str_dis_type);
                    System.out.println("str_dis :" + str_dis);

                    Fun_Alert();
                } catch (Exception e) {

                }
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_over_all_discount = edt_over_all_discount.getText().toString().trim();

                Get_VALUE();
            }
        });

        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("collection_shop_id", sp_shop_id);
                editor.commit();

                Intent i = new Intent(getApplicationContext(), Activity_Shop_Info.class);
                startActivity(i);
            }
        });


        /***************************
         *  Get Agency On/off Line
         * ************************/

        if (isNetworkConnected()) {

            dialog = new SpotsDialog(Activity_Order_Edit_Form.this);
            dialog.show();

            Arraylist_group_id.clear();
            Arraylist_group_name.clear();

            queue = Volley.newRequestQueue(Activity_Order_Edit_Form.this);
            Function_Get_Sesson_Offer();

            queue = Volley.newRequestQueue(Activity_Order_Edit_Form.this);
            Function_Get_ProdcutGroup();

            heroList.clear();
            queue = Volley.newRequestQueue(Activity_Order_Edit_Form.this);
            Function_Get_Ordered_Items();

        } else {

            TastyToast.makeText(getApplicationContext(), "Network Error !", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

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
                HeroForEdit hero = heroList.get(i);
                String str_price = hero.getPrice();

                System.out.println("JO TEST : " + str_price);

                Float price = Float.parseFloat(str_price);
                float_total_amount = float_total_amount + price;
                int a = Math.round(float_total_amount);
                txt_total.setText("Total : " + a);

            }
        }
    }

    /***************************
     * GET Sesson Offers
     ***************************/

    public void Function_Get_Sesson_Offer() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        final StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_seson_offer, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        String str_offer = obj.getString("data");
                        dialog.dismiss();
                        txt_marquee_offer.setText(str_offer);
                        txt_marquee_offer.setSelected(true);

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

//                dialog.dismiss();

                TastyToast.makeText(getApplicationContext(), "Network Error !", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("shop_id", sp_shop_id);

                System.out.println("### shop_id : " + str_Shop_id);

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
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        DBManager dbManager = new DBManager(Activity_Order_Edit_Form.this);
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Order_Edit_Form.this,
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
                                        .setAdapter(new ArrayAdapter<String>(Activity_Order_Edit_Form.this,
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
                params.put("shop_id", sp_shop_id);

                System.out.println("GROUP IDDD :: " + str_group_id);
                System.out.println("SHOPPP IDDD :: " + sp_shop_id);

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

                        DBManager dbManager = new DBManager(Activity_Order_Edit_Form.this);
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

                            long response_product = dbManager.insert_PRODUCT(id, name, gst, group_id, retail_price, dis_price, live_stock, transit);
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

    /***************************
     * GET Ordered Items
     ***************************/

    public void Function_Get_Ordered_Items() {

        String tag_json_obj = "json_obj_req";
        System.out.println("CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_ordered_items, new Response.Listener<String>() {

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

                            String or_category_id = obj1.getString("category_id");
                            String or_product_type = obj1.getString("product_type");
                            String or_product_id = obj1.getString("product_id");
                            String or_product_name = obj1.getString("product_name");
                            String or_quantity = obj1.getString("quantity");
                            String or_unit_price = obj1.getString("unit_price");
                            String or_tax = obj1.getString("tax");
                            String or_tax_amount = obj1.getString("tax_amount");
                            String or_discount_type = obj1.getString("discount_type");
                            String or_discount = obj1.getString("discount");
                            String or_discount_amount = obj1.getString("discount_amount");
                            String or_total_price = obj1.getString("total_price");

                            String ordered_prod_id = or_product_id + "-" + or_product_type;

                            if (!or_discount_type.equals("0")) {
                                flag_individual_discount = 1;
                                edt_over_all_discount.setTag(edt_over_all_discount.getKeyListener());
                                edt_over_all_discount.setKeyListener(null);
                            }
                            heroList.add(new HeroForEdit(ordered_prod_id, or_category_id, or_unit_price, or_total_price, "",
                                    or_product_name, or_quantity, or_discount, or_discount_type,
                                    or_tax, or_tax_amount, "", "", ""));
                            adapter = new MyListAdapterForEdit(Activity_Order_Edit_Form.this, R.layout.my_custom_list, heroList);
                            list_cart.setAdapter(adapter);

                        }
                        Fun_Total_Cal();
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

                params.put("order_id", sp_order_num);

                System.out.println("ORDER IDDD :: " + sp_order_num);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    /********************************
     *
     * Function Get Product
     * ******************************/
    public void Get_VALUE() {

        str_combo_offer = "";

        for (int i = 0; i < heroList.size(); i++) {

            HeroForEdit hero = heroList.get(i);
            String str_id = hero.getId();
            String str_groupid = hero.getgroupId();
            String str_qty = hero.getQty();
            String str_discount = hero.getDiscount();
            String str_discount_type = hero.getDiscount_type();
            String str_price = hero.getPrice();

            Arraylist_id.add(str_id);
            Arraylist_groupid.add(str_groupid);
            Arraylist_qty.add(str_qty);
            Arraylist_discount.add(str_discount);
            Arraylist_discount_type.add(str_discount_type);
            Arraylist_price.add(str_price);
        }

        Arraylist_final.clear();

        for (int j = 0; j < Arraylist_id.size(); j++) {

            String str_final_id = Arraylist_id.get(j);
            String str_final_groupid = Arraylist_groupid.get(j);
            String str_final_qty = Arraylist_qty.get(j);
            String str_final_dis = Arraylist_discount.get(j);
            String str_final_dis_type = Arraylist_discount_type.get(j);
            String str_final_price = Arraylist_price.get(j);

            String final_order = str_final_id + "-" + str_final_groupid + "-" + str_final_qty + "-"
                    + str_final_dis_type + "-" + str_final_dis;

            Arraylist_final.add(final_order);
        }

        //Ordered Items
        str_final_order = TextUtils.join(", ", Arraylist_final);

        System.out.println("SESSION_ID    :::::" + str_user_id);
        System.out.println("PRODUCT_DETAILS   :::::" + str_final_order);
        System.out.println("SHOP ID   :::::" + sp_shop_id);

        if (sp_shop_id != null && !sp_shop_id.isEmpty() && !sp_shop_id.equals("null")) {
            if (str_final_order.equals("")) {

                TastyToast.makeText(getApplicationContext(), "Select Your Product", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

            } else {

                System.out.println("SESSION_ID    :::::" + str_user_id);
                System.out.println("PRODUCT_DETAILS   :::::" + str_final_order);
                System.out.println("SHOP ID   :::::" + sp_shop_id);

                try {

                    // check network is connected
                    if (isNetworkConnected()) {

                        dialog = new SpotsDialog(Activity_Order_Edit_Form.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Order_Edit_Form.this);
                        Function_Proceed();

                    } else {

                        TastyToast.makeText(getApplicationContext(), "Oops...! Try again Later..!", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        /*String str_offer = "";
                        //store data in local db
                        DBManager dbManager = new DBManager(this);
                        dbManager.open();
                        long response = dbManager.insert_Order_Form(sp_shop_id, str_final_order, str_user_id, str_user_type, str_over_all_discount);
                        System.out.println("### response : " + response);
                        if (response == -1) {

                            TastyToast.makeText(getApplicationContext(), "Oops...! Try again Later..!", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                        } else {

                            TastyToast.makeText(getApplicationContext(), "Order Placed Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            new AlertDialog.Builder(Activity_Order_Edit_Form.this)
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


                        }*/

                    }

                } catch (Exception e) {
                    System.out.println("### Exception");
                }
            }
        } else {
            TastyToast.makeText(getApplicationContext(), "Select Shop", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        }


    }

    /**********************************
     *    Proceed Function
     * *********************************/
    private void Function_Proceed() {

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_update_order, new Response.Listener<String>() {
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

                        TastyToast.makeText(getApplicationContext(), "Order Updated Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        new AlertDialog.Builder(Activity_Order_Edit_Form.this)
                                .setTitle("Anil Foods")
                                .setMessage("Order Updated Successfully !")
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
                    } else if (success == 2) {
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

                params.put("order_id", sp_order_num);
                params.put("shop_id", sp_shop_id);
                params.put("product_details", str_final_order);
                params.put("overall_discount", str_over_all_discount);


                System.out.println("order_id   :::::" + sp_order_num);
                System.out.println("shop_id        :::::" + sp_shop_id);
                System.out.println("product_details        :::::" + str_final_order);
                System.out.println("overall_discount   :::::" + str_over_all_discount);

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

    /************************************
     *  Custom Alert Dialog
     **********************************/

    private void Fun_Alert() {

        LayoutInflater li = LayoutInflater.from(Activity_Order_Edit_Form.this);
        View promptsView = li
                .inflate(R.layout.activity_alert_order_edit, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Activity_Order_Edit_Form.this);
        alertDialogBuilder.setTitle("Anil Billing");

        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);

        alertDialogBuilder.setView(promptsView);

        final TextView alert_txt_name = (TextView) promptsView
                .findViewById(R.id.order_alert_txt_nam);

        final EditText alert_edt_qty = (EditText) promptsView
                .findViewById(R.id.order_alert_edt_qty);

        final EditText alert_edt_discount = (EditText) promptsView
                .findViewById(R.id.order_edt_discount);

        final Spinner alert_spn_alert_discount_type = (Spinner) promptsView
                .findViewById(R.id.order_spn_discount_type);

        HeroForEdit hero = heroList.get(order_edt_new_position);
        String str_name = hero.getName();
        String str_qty = hero.getQty();
        String str_dis_type = hero.getDiscount_type();
        String str_dis = hero.getDiscount();

        alert_txt_name.setText("" + str_name);
        alert_edt_qty.setText("" + str_qty);
        alert_edt_discount.setText("" + str_dis);

        if (!str_dis_type.equals("")) {
            if (str_dis_type.equals("1")) {
                alert_spn_alert_discount_type.setSelection(1);
                alert_edt_qty.setVisibility(View.VISIBLE);
            } else if (str_dis_type.equals("2")) {
                alert_spn_alert_discount_type.setSelection(2);
                alert_edt_qty.setVisibility(View.VISIBLE);
            }
        } else {

        }

        alert_spn_alert_discount_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                str_alert_discount_type = parent.getItemAtPosition(pos).toString();

                if (str_alert_discount_type.equals("%")) {
                    str_alert_discount_type_id = "1";
                    alert_edt_discount.setVisibility(View.VISIBLE);

                } else if (str_alert_discount_type.equals("Rs")) {
                    str_alert_discount_type_id = "2";
                    alert_edt_discount.setVisibility(View.VISIBLE);
                } else {
                    alert_edt_discount.setVisibility(View.INVISIBLE);
                }

                System.out.println("### str_discount_type : " + str_alert_discount_type);
                System.out.println("### str_discount_type id : " + str_alert_discount_type_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }

        });

        alertDialogBuilder.setCancelable(false)

                .setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                str_alert_new_qty = alert_edt_qty.getText().toString();
                                str_alert_discount_type = alert_spn_alert_discount_type.getSelectedItem().toString();
                                str_alert_new_dis = alert_edt_discount.getText().toString();

                                if (str_alert_new_qty.equals("")) {
                                    alert_edt_qty.setError("Please Enter Quantity");
                                    TastyToast.makeText(getApplicationContext(), "Please Enter Quantity", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                                } else {

                                    HeroForEdit hero = heroList.get(order_edt_new_position);
                                    String alert_str_id = hero.getId();
                                    String alert_str_group_id = hero.getgroupId();
                                    String alert_str_act_price = hero.getAct_price();
                                    String alert_str_price = hero.getPrice();
                                    String alert_str_gst = hero.getGst();
                                    String alert_str_name = hero.getName();
                                    String alert_str_qty = hero.getQty();
                                    String alert_str_discount = hero.getDiscount();
                                    String alert_str_discount_type = hero.getDiscount_type();
                                    String alert_str_tax = hero.getTax();
                                    String alert_str_tax_amt = hero.getTax_amt();
                                    String alert_str_discount_amt = hero.getDiscount_amt();

                                    System.out.println("str_alert_new_qty " + str_alert_new_qty);
                                    System.out.println("alert_str_act_price " + alert_str_act_price);
                                    System.out.println("alert_str_gst " + alert_str_tax);

                                    Float flot_qty = Float.parseFloat(str_alert_new_qty);
                                    Float flot_price = Float.parseFloat(alert_str_act_price);
                                    Float flot_tax_percent = Float.parseFloat(alert_str_tax);

                                    if (str_alert_discount_type.equals("Discount")) {

                                        Float flot_price_tax = flot_price * flot_tax_percent / 100;
                                        Float flot_price_incl_tax = flot_price + flot_price_tax;
                                        Float flot_total = flot_qty * flot_price_incl_tax;
                                        str_alert_amt = "" + flot_total;

                                    } else {

                                        if (!str_alert_new_dis.equals("")) {

                                            Float flot_discount = Float.parseFloat(str_alert_new_dis);
                                            Float float_act_price = flot_price * flot_qty;

                                            if (str_alert_discount_type_id.equals("1")) {

                                                Float float_dis_amt = float_act_price * flot_discount / 100;
                                                Float per_float_amt = float_act_price - float_dis_amt;
                                                Float gst_per = per_float_amt * flot_tax_percent / 100;
                                                Float total_per_amt = per_float_amt + gst_per;
                                                str_alert_amt = "" + total_per_amt;

                                                flag_individual_discount = 1;
                                                edt_over_all_discount.setTag(edt_over_all_discount.getKeyListener());
                                                edt_over_all_discount.setKeyListener(null);

                                            } else if (str_alert_discount_type_id.equals("2")) {

                                                Float rs_float_amt = float_act_price - flot_discount;
                                                Float gst_rs = rs_float_amt * flot_tax_percent / 100;
                                                Float total_rs_amt = rs_float_amt + gst_rs;
                                                str_alert_amt = "" + total_rs_amt;

                                                flag_individual_discount = 1;

                                                edt_over_all_discount.setTag(edt_over_all_discount.getKeyListener());
                                                edt_over_all_discount.setKeyListener(null);

                                            } else {

                                                TastyToast.makeText(getApplicationContext(), "Internal Error", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                                            }


                                        } else {
                                            Float flot_price_tax = flot_price * flot_tax_percent / 100;
                                            Float flot_price_incl_tax = flot_price + flot_price_tax;
                                            Float flot_total = flot_qty * flot_price_incl_tax;
                                            str_alert_amt = "" + flot_total;
                                        }

                                    }

                                    heroList.remove(order_edt_new_position);
                                    heroList.add(order_edt_new_position, new HeroForEdit(alert_str_id, alert_str_group_id,
                                            alert_str_act_price, str_alert_amt, alert_str_gst, alert_str_name, str_alert_new_qty,
                                            str_alert_new_dis, str_alert_discount_type_id, alert_str_tax, alert_str_tax_amt,
                                            alert_str_discount_amt, "", ""));
                                    adapter.notifyDataSetChanged();

                                    Fun_Total_Cal();

                                    TastyToast.makeText(getApplicationContext(), "Product Updated Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    // dialog.cancel();
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    /************************************
     *  Network Checking
     * *********************************/

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
                        .setAdapter(new ArrayAdapter<String>(Activity_Order_Edit_Form.this,
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
                        .setAdapter(new ArrayAdapter<String>(Activity_Order_Edit_Form.this,
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
                String prod_retail_price = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_RETAIL_PRICE));
                String prod_gst = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_GST));
                String prod_distributor_price = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_DIS_PRICE));
                String prod_live_stock = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_LIVE_STOCK));
                String prod_transit = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_TRANSIT));

                Arraylist_product_id.add(prod_id);
                Arraylist_product_name.add(prod_name);
                Arraylist_product_gst.add(prod_gst);
                Arraylist_product_live_stock.add(prod_live_stock);
                Arraylist_product_transit.add(prod_transit);

                if (str_Shop_type_id.equals("3") || str_Shop_type_id.equals("4")) {
                    Arraylist_product_price.add(prod_distributor_price);
                } else {
                    Arraylist_product_price.add(prod_retail_price);
                }

                Log.e(TAG, "### PRODUCT : " + Arraylist_product_id);
                Log.e(TAG, "### PRODUCT NAME: " + Arraylist_product_name);

                spinner_product
                        .setAdapter(new ArrayAdapter<String>(Activity_Order_Edit_Form.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_product_name));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }


}