package banyan.com.rafoods.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.Edit_Order_Adapter;
import banyan.com.rafoods.global.SessionManager;
import banyan.com.rafoods.model.EDT_Hero;
import dmax.dialog.SpotsDialog;


/**
 * Created by Banyan on 05-Mar-18.
 */

public class Activity_MyOrder_Desc extends AppCompatActivity {

    // Session Manager Class
    SessionManager session;

    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "reg";

    private static long back_pressed;

    public static final String TAG_ID = "product_id";
    public static final String TAG_NAME = "product_name";
    public static final String TAG_QTY = "product_qty";
    public static final String TAG_PRICE = "price";

    static ArrayList<HashMap<String, String>> complaint_list;

    HashMap<String, String> params = new HashMap<String, String>();

    public static ArrayList<String> Arraylist_prod_name = null;
    public static ArrayList<String> Arraylist_prod_qty = null;
    public static ArrayList<String> Arraylist_prod_price = null;

    Edit_Order_Adapter adapter;

    private ListView List;

    private Toolbar mToolbar;

    EditText edt_rtgs, edt_amount;

    TextView txt_total;

    Button btn_edit_order;

    String str_rtgs, str_rtgs_amount = "";

    String str_response = "";
    String str_grand_total = "";

    //SESSION
    String str_user_email, str_user_id = "";

    public static java.util.List<EDT_Hero> heroList;

    String str_new_qty = "";

    int new_position;

    ArrayList<String> Arraylist_product_final = null;
    ArrayList<String> Arraylist_product_edited = null;

    String str_prod_id, str_prod_price = "" ;
    String str_final_products, str_final_total = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_desc);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order Description");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value", "Order Description");
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        str_user_email = user.get(SessionManager.KEY_USER);
        str_user_id = user.get(SessionManager.KEY_USER_ID);

        Arraylist_prod_name = new ArrayList<String>();
        Arraylist_prod_qty = new ArrayList<String>();
        Arraylist_prod_price = new ArrayList<String>();

        edt_rtgs = (EditText) findViewById(R.id.my_order_edt_rtgs);
        edt_amount = (EditText) findViewById(R.id.my_order_edt_amount);
        txt_total = (TextView) findViewById(R.id.txt_grand_total);
        btn_edit_order = (Button) findViewById(R.id.my_order_desc_btn_edit_order);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_MyOrder_Desc.this);

        str_response = sharedPreferences.getString("response", "response");

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();

        // Initialize Arraytlist
        heroList = new ArrayList<>();

        Arraylist_product_final = new ArrayList<String>();
        Arraylist_product_edited = new ArrayList<String>();

        List = (ListView) findViewById(R.id.alloted_comp_listView);

        try {
            System.out.println("INSIDE DESC : " + str_response);
            if (!str_response.equals("")) {
                LoadData(str_response);
            } else {

            }
        } catch (Exception e) {

        }


        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new_position = position;

                try {
                    EDT_Hero hero = heroList.get(new_position);
                    String str_price = hero.getPrice();
                    str_prod_id = hero.getId();

                    System.out.println("@@@ PRI " + str_price);

                    try {
                        queue = Volley.newRequestQueue(Activity_MyOrder_Desc.this);
                        GetPrice();

                    } catch (Exception e) {
                        // TODO: handle exceptions
                    }

                } catch (Exception e) {

                }
            }
        });

        btn_edit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_final_total = txt_total.getText().toString();

                str_rtgs = edt_rtgs.getText().toString();
                str_rtgs_amount = edt_amount.getText().toString();

                if (heroList.size() == 0) {

                } else {

                    if (str_rtgs.equals("")){

                        Call_Json();

                    }else {

                        if (str_rtgs_amount.equals("")){
                            Toast.makeText(getApplicationContext(), "Please Update the RTGS Amount", Toast.LENGTH_LONG).show();
                        }else {
                            Call_Json();
                        }
                    }

                }

            }
        });

    }

    /******************************
     *  Function Calling
     * ******************************/

    private void Call_Json() {

        for (int i = 0; i < heroList.size(); i++) {
            EDT_Hero hero = heroList.get(i);

            String str_id = hero.getId();
            String str_name = hero.getName();
            String str_qty = hero.getQty();
            String str_price = hero.getPrice();

            String str_final = str_id + "-" + str_qty + "-" + str_price;

            Arraylist_product_final.add(str_final);

            str_final_products = TextUtils.join(", ", Arraylist_product_final);
        }

                  /*  System.out.println("ARRAY : " + Arraylist_product_edited);

                    for (int i = 0; i <Arraylist_product_edited.size(); i++) {

                        str_final_products = TextUtils.join(", ", Arraylist_product_edited);
                    }*/

        System.out.println("RTGS from edit text ::::" + str_rtgs);
        System.out.println("RTGS Amount from edit text ::::" + str_rtgs_amount);
        System.out.println(" FINAL PRODUCTS : " + str_final_products);

        try {
            dialog = new SpotsDialog(Activity_MyOrder_Desc.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_MyOrder_Desc.this);
            Function_Send_RTGS();
        } catch (Exception e) {

        }
    }

    /*****************************
     *  Load Json
     * ***************************/

    private void LoadData(String response) {

        try {
            JSONObject obj = new JSONObject(response);
            int success = obj.getInt("status");

            if (success == 1) {

                str_grand_total = obj.getString("grandtotal");

                JSONArray arr;

                arr = obj.getJSONArray("data");

                for (int i = 0; arr.length() > i; i++) {

                    JSONObject obj1 = arr.getJSONObject(i);

                    String id = obj1.getString(TAG_ID);
                    String name = obj1.getString(TAG_NAME);
                    String price = obj1.getString(TAG_PRICE);
                    String qty = obj1.getString(TAG_QTY);

                    heroList.add(new EDT_Hero(id, name, qty, price));

                    adapter = new Edit_Order_Adapter(Activity_MyOrder_Desc.this,
                            R.layout.my_edt_ordercustom_list, heroList);
                    List.setAdapter(adapter);

                }

                Fun_Total_Cal();
            } else if (success == 0) {

                Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /******************************************
     *    Sum of Arraylist
     * *******************************************/

    public void Fun_Total_Cal() {

        System.out.println("WELCOMEEEEEEEEEEDDDDDDDDDDDDD ");
        float float_total_amount = 0;

        if (heroList.size() == 0) {
            txt_total.setText("Total : " + "0");
        } else {
            for (int i = 0; i < heroList.size(); i++) {
                EDT_Hero hero = heroList.get(i);
                String str_price = hero.getPrice();

                Float price = Float.parseFloat(str_price);
                float_total_amount = float_total_amount + price;
                int a = Math.round(float_total_amount);
                txt_total.setText("" + a);

            }
        }
    }

    /************************************
     *  Custom Alert Dialog
     * *********************************/

    private void Fun_Alert() {

        LayoutInflater li = LayoutInflater.from(Activity_MyOrder_Desc.this);
        View promptsView = li
                .inflate(R.layout.activity_alert_add_qty, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Activity_MyOrder_Desc.this);
        alertDialogBuilder.setTitle("Anil Distributor");

        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);

        alertDialogBuilder.setView(promptsView);

        final TextView txt_name = (TextView) promptsView
                .findViewById(R.id.alert_txt_nam);

        final EditText edt_qty = (EditText) promptsView
                .findViewById(R.id.alert_edt_qty);

        EDT_Hero hero = heroList.get(new_position);
        String str_name = hero.getName();

        txt_name.setText("" + str_name);

        alertDialogBuilder.setCancelable(false)

                .setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                str_new_qty = edt_qty.getText().toString();

                                if (str_new_qty.equals("")) {
                                    edt_qty.setError("Please Enter Quantity");
                                    TastyToast.makeText(getApplicationContext(), "Please Enter Quantity", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                                } else {

                                    EDT_Hero hero = heroList.get(new_position);
                                    String str_id = hero.getId();
                                    String str_name = hero.getName();
                                    String str_qty = hero.getQty();
                                    String str_price = hero.getPrice();

                                    int qty = Integer.parseInt(str_new_qty);
                                    float price = Float.parseFloat(str_price);
                                    float act_qty = Float.parseFloat(str_qty);
                                    System.out.println("### Price : " + price);
                                    System.out.println("### qty : " + act_qty);

                                    float single_price = Float.parseFloat(str_prod_price);
                                    System.out.println("### Single Price : " + single_price);

                                    float new_price = qty * single_price;
                                    System.out.println("### New Price : " + new_price);

                                    String new_qty = "" + qty;
                                    String new_price1 = "" + new_price;

                                    System.out.println("### Name : " + str_name);
                                    System.out.println("### ArrayPos : " + new_position);

                                    heroList.remove(new_position);
                                    heroList.add(new_position, new EDT_Hero(str_id, str_name, new_qty, new_price1));
                                    adapter.notifyDataSetChanged();

                                    String str_new = str_id + "-" + new_qty + "-" + new_price1;
                                    Arraylist_product_edited.add(str_new);

                                    Fun_Total_Cal();

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


    /*****************************
     * GET SHOP
     ***************/

    public void GetPrice() {

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME DA PRICE" + AppConfig.url_get_price);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_get_price, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 1) {

                        JSONObject obj_data = obj.getJSONObject("message");

                        str_prod_price = obj_data.getString("product_price");

                        System.out.println("$$$ Price : " + str_prod_price);

                        Fun_Alert();

                    } else if (success == 0) {


                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // stopping swipe refresh

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);
                params.put("product_id", str_prod_id);

                System.out.println("$$$ USER ID : " + str_user_id);
                System.out.println("$$$ PROD ID : " + str_prod_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }


    /********************************
     * RTGS UPDATE FUNCTION
     *********************************/
    private void Function_Send_RTGS() {

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_add_rtgs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        TastyToast.makeText(getApplicationContext(), "Your Order and RTGS Sent Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        new AlertDialog.Builder(Activity_MyOrder_Desc.this)
                                .setTitle("Anil Foods")
                                .setMessage("RTGS Added Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                i.putExtra("from_value", "Order Description");
                                                startActivity(i);

                                            }

                                        }).show();

                    } else {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Something Went Wrong Please Contact Anil Group", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);
                params.put("rfg", str_rtgs);
                params.put("amt_transfer", str_rtgs_amount);
                params.put("amount", str_final_total);
                params.put("product", str_final_products);

                System.out.println("URL : " + AppConfig.url_add_rtgs);

                System.out.println("user_id : " + str_user_id);
                System.out.println("rfg : " + str_rtgs);
                System.out.println("rfg amount : " + str_rtgs_amount);
                System.out.println("amount : " + str_final_total);
                System.out.println("product : " + str_final_products);

                //return params;
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

        int socketTimeout = 12000000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // Adding request to request queue
        queue.add(request);
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {

            this.moveTaskToBack(true);
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();

        }
        back_pressed = System.currentTimeMillis();
    }


}