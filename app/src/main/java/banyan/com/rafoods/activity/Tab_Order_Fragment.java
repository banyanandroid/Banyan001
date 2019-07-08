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
import com.github.fabtransitionactivity.SheetLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.Order_Adapter;
import banyan.com.rafoods.global.SessionManager;


/**
 * Created by Jo on 05/03/2018.
 */
public class Tab_Order_Fragment extends Fragment implements SheetLayout.OnFabAnimationEndListener, SwipeRefreshLayout.OnRefreshListener {

    SheetLayout mSheetLayout;
    FloatingActionButton mFab;

    ProgressDialog pDialog;
    public static RequestQueue queue;

    // Session Manager Class
    SessionManager session;

    String TAG = "add task";

    private ListView List;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String TAG_ORDER_ID = "order_id";
    public static final String TAG_SHOP_NAME = "shop_name";
    public static final String TAG_SHOP_ID = "shop_id";
    public static final String TAG_ORDER_NUMBER = "order_number";
    public static final String TAG_ORDER_AMOUNT = "order_amount";
    public static final String TAG_PRICE = "order_amount";

    static ArrayList<HashMap<String, String>> complaint_list;

    HashMap<String, String> params = new HashMap<String, String>();

    public Order_Adapter adapter;

    String str_select_task_id;

    private static final int REQUEST_CODE = 1;
    String str_user_name, str_user_id, str_user_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tab_new_order_layout, null);

        // ButterKnife.bind(getActivity());

        // Session Manager
        session = new SessionManager(getActivity());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_user_name = user.get(SessionManager.KEY_USER);
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        str_user_type  = user.get(SessionManager.KEY_USER_TYPE);

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

                String shop_id1 = complaint_list.get(position).get(TAG_SHOP_ID);
                String shop_name1 = complaint_list.get(position).get(TAG_SHOP_NAME);
                String order_num1 = complaint_list.get(position).get(TAG_ORDER_ID);

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("order_shop_id", shop_id1);
                editor.putString("order_shop_name", shop_name1);
                editor.putString("order_order_num", order_num1);


                editor.commit();


                Intent i = new Intent(getActivity(), Activity_Order_Edit_Form.class);
                startActivity(i);
            }

        });

        return rootview;
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
        Intent intent = new Intent(getActivity(), Activity_Order_Form.class);
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
     * GET REQ
     ***************/

    public void GetMyNewEnquiries() {

        String tag_json_obj = "json_obj_req";

        System.out.println("CAME DA Enquiry" + AppConfig.url_dis_order_list);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_dis_order_list, new Response.Listener<String>() {

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

                            String order_id = obj1.getString(TAG_ORDER_ID);
                            String shop_name = obj1.getString(TAG_SHOP_NAME);
                            String shop_id = obj1.getString(TAG_SHOP_ID);
                            String order_number = obj1.getString(TAG_ORDER_NUMBER);
                            String order_amount= obj1.getString(TAG_ORDER_AMOUNT);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ORDER_ID, order_id);
                            map.put(TAG_SHOP_NAME, shop_name);
                            map.put(TAG_SHOP_ID, shop_id);
                            map.put(TAG_ORDER_NUMBER, order_number);
                            map.put(TAG_ORDER_AMOUNT, order_amount);

                            complaint_list.add(map);

                            adapter = new Order_Adapter(getActivity(),
                                    complaint_list);
                            List.setAdapter(adapter);

                        }

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
                params.put("user_type", str_user_type);

                 System.out.println("ENQUIRY ID : " + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

}
