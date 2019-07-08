package banyan.com.rafoods.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.global.SessionManager;
import dmax.dialog.SpotsDialog;
import pugman.com.simplelocationgetter.SimpleLocationGetter;

/**
 * Created by Jarvis on 29-03-2018.
 */

public class Activity_Shop_Edit extends AppCompatActivity implements SimpleLocationGetter.OnLocationGetListener{

    private Toolbar mToolbar;
    SpotsDialog dialog;

    String TAG = "Auto_Res";
    public static RequestQueue queue;

    // Session Manager Class
    SessionManager session;

    EditText edt_agency ,edt_shop_name, edt_owner_name, edt_contact_number, edt_landline,
            edt_location, edt_previous_supply, edt_shop_type,edt_remark;

    TextView txt_state;

    ImageView img_view;

    SearchableSpinner spn_state;

    Spinner spn_shop_type;

    Button btn_update;

    String str_shop_id,str_agency,str_shop_name, str_owner_name, str_contact_number, str_landline,
            str_location, str_previous_supply,str_shop_type,str_state, str_img, str_remark = "";

    String str_shop_type_id, str_landmark,str_up_remark, str_order_value = "";

    String str_select_state, str_select_state_id, str_final_state;

    String str_latitude, str_longitude = "";
    Double latitude, longitude;

    //SESSION
    String str_user_email, str_user_id, str_user_type = "";

    ArrayList<String> Arraylist_state = null;
    ArrayList<String> Arraylist_state_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Shop Edit");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value","enquiry");
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

        Arraylist_state = new ArrayList<String>();
        Arraylist_state_id = new ArrayList<String>();

        System.out.println("SESSION USER EMAIL   " + str_user_email);
        System.out.println("SESSION USER ID      " + str_user_id);
        System.out.println("SESSION USER TYPE      " + str_user_type);

        edt_agency = (EditText) findViewById(R.id.edt_agency);
        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);
        edt_owner_name = (EditText) findViewById(R.id.edt_owner_name);
        edt_contact_number = (EditText) findViewById(R.id.edt_contact_number);
        edt_landline = (EditText) findViewById(R.id.edt_landline_number);
        edt_location = (EditText) findViewById(R.id.edt_location);
        edt_previous_supply = (EditText) findViewById(R.id.edt_previous_supply);
        edt_shop_type = (EditText) findViewById(R.id.edt_shop_type);
        edt_remark = (EditText) findViewById(R.id.edt_remark);

        img_view = (ImageView) findViewById(R.id.shop_img);

        spn_shop_type = (Spinner) findViewById(R.id.spn_shop_type);

        txt_state = (TextView) findViewById(R.id.edt_shop_txt_state);

        spn_state = (SearchableSpinner) findViewById(R.id.edt_shop_state);

        btn_update = (Button) findViewById(R.id.btn_update_submit);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Shop_Edit.this);

        str_shop_id = sharedPreferences.getString("shop_id", "shop_id");
        str_agency = sharedPreferences.getString("agencies_name", "agencies_name");
        str_shop_name = sharedPreferences.getString("shop_name", "shop_name");
        str_owner_name = sharedPreferences.getString("shop_owner_name", "shop_owner_name");
        str_contact_number = sharedPreferences.getString("shop_contact", "shop_contact");
        str_landline = sharedPreferences.getString("shop_landline", "shop_landline");
        str_location = sharedPreferences.getString("shop_location", "shop_location");
        str_previous_supply = sharedPreferences.getString("shop_previous", "shop_previous");
        str_shop_type = sharedPreferences.getString("shop_type", "shop_type");
        str_state = sharedPreferences.getString("state", "state");
        str_img = sharedPreferences.getString("shop_photos", "shop_photos");
        str_remark = sharedPreferences.getString("remark", "remark");

        try {

            System.out.println("shop_type : shop_type : " + str_shop_type);
            edt_agency.setText(""+str_agency);
            edt_shop_name.setText(""+str_shop_name);
            edt_owner_name.setText(""+str_owner_name);
            edt_contact_number.setText(""+str_contact_number);
            edt_landline.setText(""+str_landline);
            edt_location.setText(""+str_location);
            edt_previous_supply.setText(""+str_previous_supply);
            edt_shop_type.setText(""+str_shop_type);
            txt_state.setText("State : "+str_state);
            edt_remark.setText(""+str_remark);

            Glide.with(getApplicationContext())
                    .load(str_img)
                    .thumbnail(0.5f)
                    .into(img_view);

        }catch (Exception e){

        }

        try {
            NewLocation();
        } catch (Exception e) {

        }

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                str_select_state = Arraylist_state.get(arg2);
                str_select_state_id = Arraylist_state_id.get(arg2);

                txt_state.setText("State : " + str_select_state);

                System.out.println("### ID : " + str_select_state + " :   " + str_select_state_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spn_shop_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                str_shop_type = parent.getItemAtPosition(pos).toString();

                if (str_shop_type.equals("Shop Type")) {

                } else {

                    edt_shop_type.setText(""+str_shop_type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }

        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_shop_name = edt_shop_name.getText().toString();
                str_owner_name = edt_owner_name.getText().toString();
                str_contact_number = edt_contact_number.getText().toString();
                str_landmark = edt_landline.getText().toString();
                str_order_value = edt_location.getText().toString();
                str_previous_supply = edt_previous_supply.getText().toString();
                str_up_remark = edt_remark.getText().toString();
                str_final_state = txt_state.getText().toString();

                str_shop_type = edt_shop_type.getText().toString().trim();

                if (str_shop_name.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Shop Name", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_shop_name.setError("Shop Name cannot be empty");
                } else if (str_owner_name.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Owner Name", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_owner_name.setError("Owner Name cannot be empty");
                } else if (str_contact_number.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Contact Number", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_contact_number.setError("Contact Number cannot be empty");
                }else if (10 > str_contact_number.length()) {
                    TastyToast.makeText(getApplicationContext(), "Enter a Valid Contact Number", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_contact_number.setError("Contact Number should be 10 digit");
                } else if (str_shop_type.equals("Shop Type")) {
                    TastyToast.makeText(getApplicationContext(), "Select Shop Type", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_final_state.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Select State", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    try {

                        System.out.println("SHOP TYPE :: " + str_shop_type);

                        if (str_shop_type.equals("Shop Type")) {
                            str_shop_type_id = "";
                        } else  if (str_shop_type.equals("Retail")) {
                            str_shop_type_id = "1";
                        } if (str_shop_type.equals("Whole sale")) {
                            str_shop_type_id = "2";
                        } if (str_shop_type.equals("Distributor")) {
                            str_shop_type_id = "3";
                        }

                        dialog = new SpotsDialog(Activity_Shop_Edit.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Shop_Edit.this);
                        Function_Submit_Enquiry();
                    } catch (Exception e) {

                    }
                }

            }
        });

        try {
            dialog = new SpotsDialog(Activity_Shop_Edit.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Shop_Edit.this);
            Function_Get_State();
        }catch (Exception e) {

        }
        
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

    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }


    /***************************
     * GET STATE
     ***************************/

    public void Function_Get_State() {

        String tag_json_obj = "json_obj_req";
        System.out.println("### CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_state_list, new Response.Listener<String>() {

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

                            String id = obj1.getString("state_id");
                            String name = obj1.getString("state_name");

                            Arraylist_state.add(name);
                            Arraylist_state_id.add(id);

                            try {
                                spn_state
                                        .setAdapter(new ArrayAdapter<String>(Activity_Shop_Edit.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_state));

                            } catch (Exception e) {

                            }
                        }

                        dialog.dismiss();

                    } else if (success == 0) {

                        TastyToast.makeText(getApplicationContext(), "No State Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                        Arraylist_state.clear();
                        Arraylist_state_id.clear();

                        try {
                            spn_state
                                    .setAdapter(new ArrayAdapter<String>(Activity_Shop_Edit.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_state));

                        } catch (Exception e) {

                        }

                        dialog.dismiss();
                    } else {
                        Arraylist_state.clear();
                        Arraylist_state_id.clear();

                        try {
                            spn_state
                                    .setAdapter(new ArrayAdapter<String>(Activity_Shop_Edit.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_state));

                        } catch (Exception e) {

                        }

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

                System.out.println("### USEERRR IDDD :: " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }



    /********************************
     *FUNCTION SUBMIT ENQUIRY
     *********************************/
    private void Function_Submit_Enquiry() {

        str_latitude = String.valueOf(latitude);
        str_longitude = String.valueOf(longitude);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_edit_shop, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);

                    int success = obj.getInt("status");
//                    String message = obj.getString("message");

                    if (success == 1) {
                        dialog.dismiss();

                        TastyToast.makeText(getApplicationContext(), "Shop Updated Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        new AlertDialog.Builder(Activity_Shop_Edit.this)
                                .setTitle("Anil Foods")
                                .setMessage("Shop Data Updated Successfully !")
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                i.putExtra("from_value", "enquiry");
                                                startActivity(i);

                                            }

                                        }).show();

                    } else {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), " Oops..! Enquiry Submission Failed", TastyToast.LENGTH_LONG, TastyToast.ERROR);

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
                params.put("user_type", str_user_type);
                params.put("shop_id", str_shop_id);
                params.put("shop_name", str_shop_name);
                params.put("state", str_final_state);
                params.put("name", str_owner_name);
                params.put("mobileno", str_contact_number);
                params.put("landline", str_landmark);
                params.put("lat", str_latitude);
                params.put("lon", str_longitude);
                params.put("area", str_order_value);
                params.put("shop_previous", str_previous_supply);
                params.put("type", str_shop_type_id);
                params.put("loc", "");
                params.put("remarks", str_up_remark);


                System.out.println("str_shop_id  " + str_shop_id);
                System.out.println("str_user_id  " + str_user_id);
                System.out.println("str_user_type  " + str_user_type);
                System.out.println("shop_name " + str_shop_name);
                System.out.println("state " + str_final_state);
                System.out.println("name  " + str_owner_name);
                System.out.println("mobileno  " + str_contact_number);
                System.out.println("landline  " + str_landmark);
                System.out.println("lat  " + str_latitude);
                System.out.println("lon  " + str_longitude);
                System.out.println("area  " + str_order_value);
                System.out.println("shop_previous  " + str_previous_supply);
                System.out.println("str_shop_type_id  " + str_shop_type_id);
                System.out.println("remarks " + str_up_remark);

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
    }
