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

public class Activity_Add_Enquiry extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener, SimpleLocationGetter.OnLocationGetListener{

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


    ArrayList<String> Arraylist_dealers = null;
    String[] Arraylist_agency;

    ArrayList<String> Arraylist_ageny = null;
    ArrayList<String> Arraylist_agency_id = null;

    ArrayList<String> Arraylist_location = null;
    ArrayList<String> Arraylist_location_id = null;

    ArrayList<String> Arraylist_state = null;
    ArrayList<String> Arraylist_state_id = null;

    Button btn_add_pic, btn_submit;

    TextView txt_img_count;

    Spinner spn_agency_location, spn_shop_type;

    SearchableSpinner spn_state;

    EditText edt_shop_name, edt_owner_name, edt_contact_number, edt_credit_limit,edt_landmark,
            edt_ordervalue, edt_previous_supply, edt_remarks;

    String str_shop_name = "", str_owner_name = "", str_contact_number = "", str_landmark = "",
            str_order_value = "", str_previous_supply = "", str_shop_type = "", str_shop_type_id = "", str_agency = "", str_agency_id = "",
            str_loc = "", str_loc_id = "", str_remarks = "", str_state = "", str_state_id = "", str_credit_limit = "";

    String str_latitude = "", str_longitude = "";
    String str_attendance = "";

    /*SearchableSpinner spinner_region;*/

    ArrayList<String> Arraylist_region_id = null;
    ArrayList<String> Arraylist_region_name = null;

    //SESSION
    String str_user_email, str_user_id, str_user_type = "";

    GPSTracker gps;
    Double latitude, longitude;

    TextView t1;

    String str_region, str_region_id = "";

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enquiry);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Shop");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value", "enquiry");
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

        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);
        edt_owner_name = (EditText) findViewById(R.id.edt_owner_name);
        edt_contact_number = (EditText) findViewById(R.id.edt_contact_number);
        edt_credit_limit  = (EditText) findViewById(R.id.shop_edt_credit_limit);
        edt_landmark = (EditText) findViewById(R.id.edt_landmark);
        edt_ordervalue = (EditText) findViewById(R.id.shop_edt_area);
        edt_previous_supply = (EditText) findViewById(R.id.edt_previous_supply);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);

        /*spinner_region = (SearchableSpinner) findViewById(R.id.order_spinner_type);*/

        spn_shop_type = (Spinner) findViewById(R.id.spn_shop_type);
        spn_agency_location = (Spinner) findViewById(R.id.spn_agency_location);
        spn_state = (SearchableSpinner) findViewById(R.id.add_shop_state);

        btn_add_pic = (Button) findViewById(R.id.btn_add_photos);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        txt_img_count = (TextView) findViewById(R.id.txt_img_count);

        Arraylist_region_id = new ArrayList<String>();
        Arraylist_region_name = new ArrayList<String>();

        // IMG PIC
        Arraylist_image_encode = new ArrayList<String>();
        Arraylist_dummy = new ArrayList<String>();

        Arraylist_ageny = new ArrayList<String>();
        Arraylist_agency_id = new ArrayList<String>();

        Arraylist_location = new ArrayList<String>();
        Arraylist_location_id = new ArrayList<String>();

        Arraylist_state = new ArrayList<String>();
        Arraylist_state_id = new ArrayList<String>();

        spn_shop_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                str_shop_type = parent.getItemAtPosition(pos).toString();

                System.out.println("### str_shop_type : " + str_shop_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }

        });


        spn_agency_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                str_loc = Arraylist_location.get(arg2);
                str_loc_id = Arraylist_location_id.get(arg2);

                System.out.println("### ID : " + str_loc + " :   " + str_loc_id);



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                str_state = Arraylist_state.get(arg2);
                    str_state_id = Arraylist_state_id.get(arg2);

                System.out.println("### ID : " + str_state + " :   " + str_state_id);
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

        btn_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                image_type = "Location photos";
                ImagePicker();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_agency_id = str_user_id;

                str_shop_name = edt_shop_name.getText().toString();
                str_owner_name = edt_owner_name.getText().toString();
                str_contact_number = edt_contact_number.getText().toString();
                str_landmark = edt_landmark.getText().toString();
                str_order_value = edt_ordervalue.getText().toString();
                str_previous_supply = edt_previous_supply.getText().toString();
                str_remarks = edt_remarks.getText().toString();
                str_credit_limit = edt_credit_limit.getText().toString();

                str_shop_type = spn_shop_type.getSelectedItem().toString();


                if (str_shop_name.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Shop Name", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_shop_name.setError("Shop Name cannot be empty");
                } else if (str_owner_name.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Owner Name", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_owner_name.setError("Owner Name cannot be empty");
                } else if (str_contact_number.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Enter Contact Number", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_contact_number.setError("Contact Number cannot be empty");
                } else if (10 > str_contact_number.length()) {
                    TastyToast.makeText(getApplicationContext(), "Enter a Valid Contact Number", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    edt_contact_number.setError("Contact Number should be 10 digit");
                } else if (str_shop_type.equals("Shop Type")) {
                    TastyToast.makeText(getApplicationContext(), "Select Shop Type", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_agency_id.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Select Agency", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (listString.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Please Select Atleast 1 Image", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_loc_id.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Select Location", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_state_id.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Select State", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_latitude.equals("")) {
                    TastyToast.makeText(getApplicationContext(), "Try Again Once More", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {


                    try {

                        // Vivekanandhan

                        if (str_shop_type.equals("Shop Type")) {
                            str_shop_type_id = "";
                        } else if (str_shop_type.equals("Retail")) {
                            str_shop_type_id = "1";
                        }
                        if (str_shop_type.equals("Whole sale")) {
                            str_shop_type_id = "2";
                        }

                        System.out.println("### str_shop_type_id : as id : " + str_shop_type_id);
                        // check network is connected
                        if (isNetworkConnected()) {

                            dialog = new SpotsDialog(Activity_Add_Enquiry.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Add_Enquiry.this);
                            Function_Submit_Enquiry();

                        } else {

                            System.out.println("### insert_Add_Enquiry str_user_id : " + str_user_id);
                            //store data in local db
                            DBManager dbManager = new DBManager(Activity_Add_Enquiry.this);
                            dbManager.open();

                            long response = dbManager.insert_Add_Enquiry(
                                    str_user_id, str_user_type, str_shop_name, str_state_id, str_owner_name, str_contact_number,
                                    str_landmark, str_latitude, str_longitude, str_order_value,
                                    str_previous_supply, str_agency_id, str_shop_type_id,
                                    listString, str_loc_id, str_remarks,str_credit_limit ,str_region_id);
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

            dialog = new SpotsDialog(Activity_Add_Enquiry.this);
            dialog.show();
            queue = Volley.newRequestQueue(Activity_Add_Enquiry.this);
            Function_Get_Agency_Location();

        } else {

            getLocal_Agency_Location();
            getLocal_State();
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

        Geocoder geocoder = new Geocoder(Activity_Add_Enquiry.this, Locale.getDefault());
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

            edt_ordervalue.setText("" + address);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(Activity_Add_Enquiry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }


    /*******************************
     *  PIC UPLOADER
     * ***************************/

    // Recomended builder
    public void ImagePicker() {

        BSImagePicker pickerDialog = new BSImagePicker.Builder("banyan.com.myapplication.fileprovider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setMinimumMultiSelectCount(3)
                .setMaximumMultiSelectCount(6)
                .build();
        pickerDialog.show(getSupportFragmentManager(), "picker");
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        //Do something with your Uri
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        //Do something with your Uri list

        for (int i = 0, l = uriList.size(); i < l; i++) {

            String str_img_path = uriList.get(i).getPath();

            Bitmap bmBitmap = BitmapFactory.decodeFile(str_img_path);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bmBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bao);
            byte[] ba = bao.toByteArray();
            encodedstring = Base64.encodeToString(ba, 0);
            Log.e("base64", "-----" + encodedstring);

            Arraylist_image_encode.add(encodedstring);

            txt_img_count.setText("(" + Arraylist_image_encode.size() + ")" + " Images Added");
            btn_add_pic.setText("Change Pic");
        }

        Encode_Image1();
    }

    public void Encode_Image1() {

        //listString = TextUtils.join("IMAGE:", Arraylist_image_encode);  Array to String

        for (String s : Arraylist_image_encode) {
            listString += s + "IMAGE:";
        }

    }


    /***************************
     * GET Agency Location
     ***************************/

    public void Function_Get_Agency_Location() {

        String tag_json_obj = "json_obj_req";
        System.out.println("### CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_agency_location, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                    try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        JSONArray arr;

                        arr = obj.getJSONArray("data");

                        DBManager dbManager = new DBManager(Activity_Add_Enquiry.this);
                        dbManager.open();
                        int result = dbManager.delete_Agency_Location();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("location_id");
                            String name = obj1.getString("location_name");

                            long response_agency_location = dbManager.insert_Agency_Location(id, name);
                            System.out.println("@@@ Inside location : " + response_agency_location);
                            if (response_agency_location == -1) {
                                TastyToast.makeText(getApplicationContext(), "Internal Local Data Error", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }

                            Arraylist_location.add(name);
                            Arraylist_location_id.add(id);

                            try {
                                spn_agency_location
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_location));

                            } catch (Exception e) {

                            }
                        }

                        queue = Volley.newRequestQueue(Activity_Add_Enquiry.this);
                        Function_Get_State();

                        dialog.dismiss();

                    } else if (success == 0) {

                        TastyToast.makeText(getApplicationContext(), "No Location Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                        Arraylist_location.clear();
                        Arraylist_location_id.clear();

                        try {
                            spn_agency_location
                                    .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_location));

                        } catch (Exception e) {

                        }

                        dialog.dismiss();
                    } else {
                        Arraylist_location.clear();
                        Arraylist_location_id.clear();

                        try {
                            spn_agency_location
                                    .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_location));

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
                params.put("user_type", str_user_type);

                System.out.println("### USEERRR IDDD :: " + str_user_id);
                System.out.println("### USEERRR TYPE :: " + str_user_type);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
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

                        DBManager dbManager = new DBManager(Activity_Add_Enquiry.this);
                        dbManager.open();
                        int result = dbManager.delete_State();

                        for (int i = 0; arr.length() > i; i++) {
                            JSONObject obj1 = arr.getJSONObject(i);

                            String id = obj1.getString("state_id");
                            String name = obj1.getString("state_name");

                            long response_state = dbManager.insert_State(id, name);
                            System.out.println("@@@ Inside state : " + response_state);
                            if (response_state == -1) {
                                TastyToast.makeText(getApplicationContext(), "Internal Local Data Error", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            } else {

                            }

                            Arraylist_state.add(name);
                            Arraylist_state_id.add(id);

                            try {
                                spn_state
                                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                                android.R.layout.simple_spinner_dropdown_item,
                                                Arraylist_state));

                            } catch (Exception e) {

                            }

                        }


                    } else if (success == 0) {

                        TastyToast.makeText(getApplicationContext(), "No State Found", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                        Arraylist_state.clear();
                        Arraylist_state_id.clear();

                        try {
                            spn_state
                                    .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_state));


                        } catch (Exception e) {

                        }

                    } else {
                        Arraylist_state.clear();
                        Arraylist_state_id.clear();

                        try {
                            spn_state
                                    .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            Arraylist_state));

                        } catch (Exception e) {

                        }

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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

        System.out.println("###  AppConfig.url_add_shop " + AppConfig.url_dis_add_shop);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_add_shop, new Response.Listener<String>() {

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

                        new AlertDialog.Builder(Activity_Add_Enquiry.this)
                                .setTitle("Anil Foods")
                                .setMessage("Shop Added Successfully !")
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

                    } else if (success == 2) {
                        dialog.dismiss();

                        TastyToast.makeText(getApplicationContext(), "Shop Data Already Exist", TastyToast.LENGTH_SHORT, TastyToast.INFO);


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
                params.put("shop_name", str_shop_name);
                params.put("state", str_state_id);
                params.put("name", str_owner_name);
                params.put("mobileno", str_contact_number);
                params.put("landline", str_landmark);
                params.put("lat", str_latitude);
                params.put("lon", str_longitude);
                params.put("area", str_order_value);
                params.put("shop_previous", str_previous_supply);
                params.put("agency_id", str_agency_id);
                params.put("type", str_shop_type_id);
                params.put("image", listString);
                params.put("loc", str_loc_id);
                params.put("remarks", str_remarks);
                params.put("credit_limit", str_credit_limit);
                params.put("branch_id", str_region_id);

                System.out.println("### user_id  " + str_user_id);
                System.out.println("### shop_name " + str_shop_name);
                System.out.println("### name  " + str_owner_name);
                System.out.println("### mobileno  " + str_contact_number);
                System.out.println("### landline  " + str_landmark);
                System.out.println("### lat  " + str_latitude);
                System.out.println("### lon  " + str_longitude);
                System.out.println("### area  " + str_order_value);
                System.out.println("### shop_previous  " + str_previous_supply);
                System.out.println("### agency_id  " + str_agency_id);
                System.out.println("### type  " + str_shop_type_id);
                System.out.println("### image  " + listString);
                System.out.println("### loc  " + str_loc_id);
                System.out.println("### remarks " + str_remarks);
                System.out.println("### CREDIT LIMIT " + str_credit_limit);
                System.out.println("### branch_id " + str_region_id);

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

    private void getLocal_Agency_Location() {

        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        Cursor cursor = dbManager.Fetch_Agency_Location();
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String Location_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_ID));
                String Location_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_NAME));

                Arraylist_location.add(Location_name);
                Arraylist_location_id.add(Location_id);

                Log.e(TAG, "### LOCATION : " + Arraylist_location_id);
                Log.e(TAG, "### LOCATION NAME: " + Arraylist_location);

                spn_agency_location
                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_location));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }

    /************************
     *  Local Agency Location
     * **********************/

    private void getLocal_State() {

        dbManager = new DBManager(getApplicationContext());
        dbManager.open();

        Cursor cursor = dbManager.Fetch_State();
        // get form order details from local database
        if (cursor.moveToFirst()) {
            do {

                String state_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE_ID));
                String state_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE_NAME));

                Arraylist_state.add(state_name);
                Arraylist_state_id.add(state_id);

                Log.e(TAG, "### STATE : " + Arraylist_state_id);
                Log.e(TAG, "### STATE NAME: " + Arraylist_state);

                spn_state
                        .setAdapter(new ArrayAdapter<String>(Activity_Add_Enquiry.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                Arraylist_state));

            } while (cursor.moveToNext());
        }
        //Stop service once it finishes its task
        cursor.close();
    }

}