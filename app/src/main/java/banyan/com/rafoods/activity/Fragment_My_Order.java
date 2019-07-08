package banyan.com.rafoods.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.My_Order_Adapter;
import banyan.com.rafoods.global.SessionManager;


/**
 * Created by Jo 05-03-2018.
 */
public class Fragment_My_Order extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Fragment_My_Order() {
        // Required empty public constructor
    }

    ProgressDialog pDialog;
    public static RequestQueue queue;

    // Session Manager Class
    SessionManager session;

    String TAG = "add task";

    public static final String TAG_NAME = "product_name";
    public static final String TAG_QTY = "product_qty";
    public static final String TAG_PRICE = "price";
    public static final String TAG_DATE = "date";
    public static final String TAG_TOTAL = "grandtotal";

    public static final String TAG_REMARK = "remarks";

    String str_user_name, str_user_id, str_user_role;

    String str_grandtotal, str_date = "";

    String str_response = "";

    static ArrayList<HashMap<String, String>> complaint_list;

    HashMap<String, String> params = new HashMap<String, String>();

    public My_Order_Adapter adapter;

    private ListView List;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.activity_my_order, null);

        session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_user_name = user.get(SessionManager.KEY_USER);
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        str_user_role = user.get(SessionManager.KEY_USER_ROLE);

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();

        List = (ListView) x.findViewById(R.id.alloted_comp_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) x.findViewById(R.id.alloted_comp_swipe_refresh_layout);

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

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("response", str_response);
                editor.commit();

                Intent i = new Intent(getActivity(), Activity_MyOrder_Desc.class);
                startActivity(i);


            }

        });

        return x;

    }

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

    /*****************************
     * GET SHOP
     ***************/

    public void GetMyNewEnquiries() {

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME DA Enquiry" + AppConfig.url_closing_stock);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_closing_stock, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");

                    if (success == 1) {

                        str_response = response.toString();

                        String total = obj.getString(TAG_TOTAL);
                        String date = obj.getString(TAG_DATE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_TOTAL, total);
                        map.put(TAG_DATE, date);

                        complaint_list.add(map);

                        adapter = new My_Order_Adapter(getActivity(),
                                complaint_list);
                        List.setAdapter(adapter);

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
