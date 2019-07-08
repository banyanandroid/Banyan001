package banyan.com.rafoods.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.fabtransitionactivity.SheetLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.Distributor_Adapter;
import banyan.com.rafoods.adapter.Enquiry_Adapter;
import banyan.com.rafoods.global.SessionManager;
import dmax.dialog.SpotsDialog;
import pugman.com.simplelocationgetter.SimpleLocationGetter;


/**
 * Created by Jo on 05/03/2018.
 */
public class Tab_Enquiry_Distributor_Fragment extends Fragment implements SheetLayout.OnFabAnimationEndListener,
        SwipeRefreshLayout.OnRefreshListener, SimpleLocationGetter.OnLocationGetListener {

    SheetLayout mSheetLayout;
    FloatingActionButton mFab;

    ProgressDialog pDialog;
    SpotsDialog dialog1;
    public static RequestQueue queue;

    // Session Manager Class
    SessionManager session;

    String TAG = "add task";

    private ListView List;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String TAG_DISTIR_ID = "distributor_id";
    public static final String TAG_STOCKIST_ID = "stockist_id";
    public static final String TAG_STOCKIST_NAME = "stockist_name";
    public static final String TAG_DISTRIBUTOR_CODE = "distributor_code";
    public static final String TAG_DISTRIBUTOR_NAME = "distributor_name";
    public static final String TAG_ADDRESS1 = "address1";
    public static final String TAG_ADDRESS2 = "address2";
    public static final String TAG_CITY = "city";
    public static final String TAG_DITRIBUTOR_EMAIL = "distributor_email";
    public static final String TAG_DISTRIBUTOR_MOBILE = "distributor_mobile";

    static ArrayList<HashMap<String, String>> complaint_list;

    HashMap<String, String> params = new HashMap<String, String>();

    public Distributor_Adapter adapter;

    String str_select_task_id;

    private static final int REQUEST_CODE = 1;
    String str_user_name, str_user_id, str_user_role, str_user_type;

    String str_final_shop_url = "";

    Double latitude, longitude;
    String str_lat, str_long;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tab_new_distributor_layout, null);

        // ButterKnife.bind(getActivity());

        // Session Manager
        session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_user_name = user.get(SessionManager.KEY_USER);
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        str_user_role = user.get(SessionManager.KEY_USER_ROLE);
        str_user_type = user.get(SessionManager.KEY_USER_TYPE);

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();

        mFab = (FloatingActionButton) rootview.findViewById(R.id.fab_add_task);
        mSheetLayout = (SheetLayout) rootview.findViewById(R.id.bottom_sheet);

        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

        List = (ListView) rootview.findViewById(R.id.alloted_comp_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.alloted_comp_swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            queue = Volley.newRequestQueue(getActivity());
                                            GetMyNewEnquiries();

                                        } catch (Exception e) {
                                            // TODO: handle exceptions
                                        }
                                    }
                                }
        );

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String dis_id = complaint_list.get(position).get(TAG_DISTIR_ID);
                String stock_id = complaint_list.get(position).get(TAG_STOCKIST_ID);
                String stock_name = complaint_list.get(position).get(TAG_STOCKIST_NAME);
                String dis_code = complaint_list.get(position).get(TAG_DISTRIBUTOR_CODE);
                String dis_name = complaint_list.get(position).get(TAG_DISTRIBUTOR_NAME);
                String add1 = complaint_list.get(position).get(TAG_ADDRESS1);
                String add2 = complaint_list.get(position).get(TAG_ADDRESS2);
                String city = complaint_list.get(position).get(TAG_CITY);
                String email = complaint_list.get(position).get(TAG_DITRIBUTOR_EMAIL);
                String mobile = complaint_list.get(position).get(TAG_DISTRIBUTOR_MOBILE);

                System.out.println("STATE :: " + city);

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("dis_id", dis_id);
                editor.putString("stock_id", stock_id);
                editor.putString("stock_name", stock_name);
                editor.putString("dis_code", dis_code);
                editor.putString("dis_name", dis_name);
                editor.putString("add1", add1);
                editor.putString("add2", add2);
                editor.putString("city", city);
                editor.putString("email", email);
                editor.putString("mobile", mobile);

                editor.commit();

                Intent i = new Intent(getActivity(), Activity_Shop_Description.class);
                startActivity(i);
            }

        });

        return rootview;
    }

    public void NewLocation() {

        SimpleLocationGetter getter = new SimpleLocationGetter(getActivity(), this);
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

    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            complaint_list.clear();
            queue = Volley.newRequestQueue(getActivity());
            GetMyNewEnquiries();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), Activity_Add_Distributor.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }


    /*****************************
     * GET SHOP
     ***************/

    public void GetMyNewEnquiries() {

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME DA Enquiry" + AppConfig.url_distributor_list);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_distributor_list, new Response.Listener<String>() {

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

                            String distributor_id = obj1.getString(TAG_DISTIR_ID);
                            String stockist_id = obj1.getString(TAG_STOCKIST_ID);
                            String stockist_name = obj1.getString(TAG_STOCKIST_NAME);
                            String distributor_code = obj1.getString(TAG_DISTRIBUTOR_CODE);
                            String distributor_name = obj1.getString(TAG_DISTRIBUTOR_NAME);
                            String address1 = obj1.getString(TAG_ADDRESS1);
                            String address2 = obj1.getString(TAG_ADDRESS2);
                            String city = obj1.getString(TAG_CITY);
                            String distributor_email = obj1.getString(TAG_DITRIBUTOR_EMAIL);
                            String distributor_mobile = obj1.getString(TAG_DISTRIBUTOR_MOBILE);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_DISTIR_ID, distributor_id);
                            map.put(TAG_STOCKIST_ID, stockist_id);
                            map.put(TAG_STOCKIST_NAME, stockist_name);
                            map.put(TAG_DISTRIBUTOR_CODE, distributor_code);
                            map.put(TAG_DISTRIBUTOR_NAME, distributor_name);
                            map.put(TAG_ADDRESS1, address1);
                            map.put(TAG_ADDRESS2, address2);
                            map.put(TAG_CITY, city);
                            map.put(TAG_DITRIBUTOR_EMAIL, distributor_email);
                            map.put(TAG_DISTRIBUTOR_MOBILE, distributor_mobile);

                            complaint_list.add(map);

                            adapter = new Distributor_Adapter(getActivity(),
                                    complaint_list);
                            List.setAdapter(adapter);

                        }

                        System.out.println("TESTTT ::"  + complaint_list);

                    } else if (success == 0) {

                        swipeRefreshLayout.setRefreshing(false);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // adapter.notifyDataSetChanged();
                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                //  pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);

                System.out.println("ENQUIRY ID : " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

}
