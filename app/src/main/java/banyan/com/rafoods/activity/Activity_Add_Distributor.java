package banyan.com.rafoods.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.asksira.bsimagepicker.BSImagePicker;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.database.DBManager;
import banyan.com.rafoods.database.DatabaseHelper;
import banyan.com.rafoods.global.GPSTracker;
import banyan.com.rafoods.global.SessionManager;
import dmax.dialog.SpotsDialog;
import pugman.com.simplelocationgetter.SimpleLocationGetter;

/**
 * Created by Jothiprabhakar on 29-Mar-18.
 */

public class Activity_Add_Distributor extends AppCompatActivity implements SimpleLocationGetter.OnLocationGetListener{

    private Toolbar mToolbar;
    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "Auto_Res";

    // Session Manager Class
    SessionManager session;

    // PIC Upload
    String listString = "";
    String encodedstring = "";
    String image_type = "";
    ArrayList<String> Arraylist_image_encode = null;
    ArrayList<String> Arraylist_dummy = null;
    private int REQUEST_CODE_PICKER = 2000;

    ArrayList<String> Arraylist_stockist = null;
    ArrayList<String> Arraylist_stockist_id = null;

    Button btn_add_pic, btn_submit;

    TextView txt_img_count;

    Spinner spn_agency_location, spn_shop_type;

    SearchableSpinner spn_stockist;

    EditText edt_distributor_name, edt_address1, edt_address2, edt_city, edt_email, edt_phone;

    String str_dis_name , str_add1, str_Add2, str_city, str_email, str_phone = "";
    String str_select_stockist, str_select_stockist_id = "";

    String str_latitude = "", str_longitude = "";

    /*SearchableSpinner spinner_region;*/

    ArrayList<String> Arraylist_region_id = null;
    ArrayList<String> Arraylist_region_name = null;

    //SESSION
    String str_user_email, str_user_id, str_user_type = "";

    GPSTracker gps;
    Double latitude, longitude;

    TextView t1;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_distributor);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Distributor");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value", "distributor");
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

        System.out.println("### SESSION USER EMAIL   " + str_user_email);
        System.out.println("### SESSION USER ID      " + str_user_id);
        System.out.println("### SESSION USER TYPE      " + str_user_type);

        edt_distributor_name = (EditText) findViewById(R.id.edt_dis_name);
        edt_address1 = (EditText) findViewById(R.id.edt_dis_addr1);
        edt_address2 = (EditText) findViewById(R.id.edt_dis_addr2);
        edt_city  = (EditText) findViewById(R.id.edt_dis_city);
        edt_email = (EditText) findViewById(R.id.edt_dis_email);
        edt_phone = (EditText) findViewById(R.id.edt_dis_phonr);

        /*spinner_region = (SearchableSpinner) findViewById(R.id.order_spinner_type);*/

        spn_stockist = (SearchableSpinner) findViewById(R.id.add_dis_stockist);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        Arraylist_stockist = new ArrayList<String>();
        Arraylist_stockist_id = new ArrayList<String>();

        spn_stockist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                str_select_stockist = Arraylist_stockist.get(arg2);
                str_select_stockist_id = Arraylist_stockist_id.get(arg2);

                System.out.println("### ID : " + Arraylist_stockist + " :   " + Arraylist_stockist_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        try {
            NewLocation();

            str_latitude = String.valueOf(latitude);
            str_longitude = String.valueOf(longitude);

            System.out.println("123 :: " + str_latitude);
            System.out.println("123 :: " + str_longitude);

        } catch (Exception e) {

        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_dis_name = edt_distributor_name.getText().toString();
                str_add1 = edt_address1.getText().toString();
                str_Add2 = edt_address2.getText().toString();
                str_city = edt_address2.getText().toString();
                str_email = edt_email.getText().toString();
                str_phone = edt_phone.getText().toString();

                if (str_select_stockist_id.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "PLease Select Stockist", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_dis_name.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Distributor Name", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_distributor_name.setError("Distributor Name cannot be empty");
                } else if (str_add1.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Address Line 1", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_address1.setError("Address Line 1 cannot be empty");
                } else if (str_Add2.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Address Line 2", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_address2.setError("Address Line 2 cannot be empty");
                }
                else if (str_email.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Email ID", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_address2.setError("Email cannot be empty");
                }
                else if (str_phone.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Phone number", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_address2.setError("Phone Number cannot be empty");
                } else {


                    try {

                        // check network is connected
                        if (isNetworkConnected()) {

                            dialog = new SpotsDialog(Activity_Add_Distributor.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Add_Distributor.this);
                            Function_Submit_Enquiry();

                        } else {

                            System.out.println("### insert_Add_Enquiry str_user_id : " + str_user_id);
                            //store data in local db
                            DBManager dbManager = new DBManager(Activity_Add_Distributor.this);
                            dbManager.open();

                            long response = dbManager.insert_distributor(str_user_id, str_select_stockist_id, str_dis_name, str_add1,
                                    str_Add2,str_city, str_email, str_phone);
                            System.out.println("### response : " + response);
                            if (response == -1) {

                                TastyToast.makeText(getApplicationContext(), "Oops...! Try again Later..!", TastyToast.LENGTH_LONG, TastyToast.WARNING);

                            } else {

                                TastyToast.makeText(getApplicationContext(), "Enquiry Added Successfully!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            }

                        }

                    } catch (Exception e) {
                        System.out.println("### Exception");
                    }

                }
            }
        });

        /***************************
         *  Get Agency On/off Line
         * ************************/

        if (isNetworkConnected()) {

            dialog = new SpotsDialog(Activity_Add_Distributor.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Add_Distributor.this);
            Function_Get_Stockist();

        } else {

            getLocal_Stockist();
        }

    }


    public void NewLocation() {

        SimpleLocationGetter getter = new SimpleLocationGetter(this, this);
        getter.getLastLocation();
    }

    @Override
    public void onLocationReady(Location location) {
        Log.d("LOCATION", "onLocationReady: lat=" + location.getLatitude() + " lon=" + location.getLongitude());

        System.out.println("### LOCATION 1 :: " + location.getLatitude());
        System.out.println("### LOCATION 2 :: " + location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(Activity_Add_Distributor.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            System.out.println("### getAddressLine "+ obj.getAddressLine(0));
            System.out.println("### getCountryName "+ obj.getCountryName());
            System.out.println("### getCountryCode "+ obj.getCountryCode());
            System.out.println("### getAdminArea "+ obj.getAdminArea());
            System.out.println("### getPostalCode "+ obj.getPostalCode());
            System.out.println("### getSubAdminArea "+ obj.getSubAdminArea());
            System.out.println("### getLocality "+ obj.getLocality());
            System.out.println("### getSubThoroughfare "+ obj.getSubThoroughfare());


            System.out.println("### address "+ address);
            System.out.println("### city "+ city);
            System.out.println("### state "+ state);
            System.out.println("### country "+ country);
            System.out.println("### postalCode "+ postalCode);
            System.out.println("### knownName "+ knownName);

           // edt_ordervalue.setText("" + address);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(Activity_Add_Distributor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }


    /***************************
     * GET STOCKIST
     ***************************/

    public void Function_Get_Stockist() {

        System.out.println("### CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_stockist_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        DBManager dbManager = new DBManager(Activity_Add_Distributor.this);
                        dbManager.open();
                        int result = dbManager.delete_stockist();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("stockist_id");
                            String name = obj1.getString("stockist_name");

                            long response_stockist = dbManager.insert_stockist(id, name);
                            System.out.println("@@@ Inside STOCKIST : " + response_stockist);
                            if (response_stockist == -1) {
                                TastyToast.makeText(getApplicationContext(), "Internal Local Data Error", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }

                            Arraylist_stockist.add(name);
                            Arraylist_stockist_id.add(id);

                            try {
                                spn_stockist
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Distributor.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_stockist));

                            } catch (Exception e) {

                            }

                            dialog.dismiss();
                        }

                        dialog.dismiss();

                    } else if (success == 0) {

                        dialog.dismiss();

                        TastyToast.makeText(getApplicationContext(), "No Stockist Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                        Arraylist_stockist.clear();
                        Arraylist_stockist_id.clear();

                        try {
                            spn_stockist
                                    .setAdapter(new ArrayAdapter<String>(Activity_Add_Distributor.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_stockist));


                        } catch (Exception e) {

                        }

                    } else {
                        Arraylist_stockist.clear();
                        Arraylist_stockist_id.clear();

                        try {
                            spn_stockist
                                    .setAdapter(new ArrayAdapter<String>(Activity_Add_Distributor.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_stockist));

                        } catch (Exception e) {

                        }

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    dialog.dismiss();
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

        System.out.println("###  AppConfig.url_add_shop " + AppConfig.url_add_distributor);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_add_distributor, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    System.out.println("###  response" + response);
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
//                    String message = obj.getString("message");

                    if (success == 1) {
                        dialog.dismiss();

                        TastyToast.makeText(getApplicationContext(), "Shop Added Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        new AlertDialog.Builder(Activity_Add_Distributor.this)
                                .setTitle("RA Foods")
                                .setMessage("Distributor Added Successfully !")
                                .setIcon(R.drawable.rafoods)
                                .setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,

                                                                int which) {
                                                // TODO Auto-generated method stub

                                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                i.putExtra("from_value", "distributor");
                                                startActivity(i);

                                            }

                                        }).show();

                    } else if (success == 2) {
                        dialog.dismiss();

                        TastyToast.makeText(getApplicationContext(), "Distributor Data Already Exist", TastyToast.LENGTH_SHORT, TastyToast.INFO);


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
                params.put("stockist_id", str_select_stockist_id);
                params.put("distributor_name", str_dis_name);
                params.put("address1", str_add1);
                params.put("address2", str_Add2);
                params.put("city", str_city);
                params.put("distributor_email", str_email);
                params.put("distributor_mobile", str_phone);

                System.out.println("### user_id  " + str_user_id);
                System.out.println("### stockist_id " + str_select_stockist_id);
                System.out.println("### distributor_name  " + str_dis_name);
                System.out.println("### address1  " + str_add1);
                System.out.println("### address2  " + str_Add2);
                System.out.println("### city  " + str_city);
                System.out.println("### distributor_email  " + str_email);
                System.out.println("### distributor_mobile  " + str_phone);
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


    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /************************
     *  Local Agency Location
     * **********************/

    private void getLocal_Stockist() {

        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        Cursor cursor = dbManager.Fetch_Stockist();
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String stockist_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STOCKIST_ID));
                String stockist_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STOCKIST_NAME));

                Arraylist_stockist.add(stockist_name);
                Arraylist_stockist_id.add(stockist_id);

                Log.e(TAG, "### STATE : " + Arraylist_stockist_id);
                Log.e(TAG, "### STATE NAME: " + Arraylist_stockist);

                spn_stockist
                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Distributor.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_stockist));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }

}