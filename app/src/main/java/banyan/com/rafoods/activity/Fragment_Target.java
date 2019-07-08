package banyan.com.rafoods.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import banyan.com.rafoods.R;
import banyan.com.rafoods.adapter.Target_Adapter;
import banyan.com.rafoods.global.SessionManager;
import dmax.dialog.SpotsDialog;


/**
 * Created by Jo 05-03-2018.
 */
public class Fragment_Target extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public Fragment_Target() {
        // Required empty public constructor
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mytask_listView;

    String TAG = "Complaints";
    public static RequestQueue queue;
    SpotsDialog dialog;

    Button btn_csr_target;

    SessionManager session;
    String str_user_name, str_user_id,str_user_type, str_user_role, str_gcm = "";

    public static final String TAG_FROM_DATE = "from_date";
    public static final String TAG_TO_DATE = "to_date";
    public static final String TAG_TARGET_AMOUNT = "target_amount";
    public static final String TAG_TARGET_NAME = "target_name";
    public static final String TAG_REACHED_AMOUNT = "reached_amount";
    public static final String TAG_TARGET_TYPE = "target_type";

    static ArrayList<HashMap<String, String>> complaint_list;

    HashMap<String, String> params = new HashMap<String, String>();

    public Target_Adapter adapter;


   /* int target = 0;
    int achive = 0;

    String str_from_date, str_to_date = "";

    private PieChart pieChart;
    private TextView txt_from , txt_to;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_target, container, false);

        session = new SessionManager(getActivity());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        str_user_name = user.get(SessionManager.KEY_USER);
        str_user_id = user.get(SessionManager.KEY_USER_ID);
        str_user_role = user.get(SessionManager.KEY_USER_ROLE);
        str_user_type = user.get(SessionManager.KEY_USER_TYPE);

      /*  pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        txt_from = (TextView) rootView.findViewById(R.id.target_txt_from);
        txt_to = (TextView) rootView.findViewById(R.id.target_txt_to);*/

        mytask_listView = (ListView) rootView.findViewById(R.id.task_comp_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.target_swipe_refresh_layout);

        btn_csr_target = (Button) rootView.findViewById(R.id.btn_target_csr);

        // Hashmap for ListView
        complaint_list = new ArrayList<HashMap<String, String>>();

        swipeRefreshLayout.setOnRefreshListener(this);

        if (str_user_role.equals("3")){

            btn_csr_target.setVisibility(View.VISIBLE);
        }else {
            btn_csr_target.setVisibility(View.GONE);
        }

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        try {
                                            queue = Volley.newRequestQueue(getActivity());
                                            GetMyTask();

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    }
                                }
        );



        mytask_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String str_select_target_name = complaint_list.get(position).get(TAG_TARGET_NAME);
                String str_select_target_from = complaint_list.get(position).get(TAG_FROM_DATE);
                String str_select_target_to = complaint_list.get(position).get(TAG_TO_DATE);
                String str_select_target_amount = complaint_list.get(position).get(TAG_TARGET_AMOUNT);
                String str_select_target_reached = complaint_list.get(position).get(TAG_REACHED_AMOUNT);

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("str_select_target_name", str_select_target_name);
                editor.putString("str_select_target_from", str_select_target_from);
                editor.putString("str_select_target_to", str_select_target_to);
                editor.putString("str_select_target_amount", str_select_target_amount);
                editor.putString("str_select_target_reached", str_select_target_reached);
                editor.commit();

                System.out.println("TARGET CLICK: " + str_select_target_amount);
                System.out.println("TARGET RE CLICK: " + str_select_target_reached);

                Intent i = new Intent(getActivity(), Activity_Target_View.class);
                startActivity(i);
            }

        });


        return rootView;
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        try {
            complaint_list.clear();

            queue = Volley.newRequestQueue(getActivity());
            GetMyTask();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /*****************************
     * GET My Task
     ***************************/

    public void GetMyTask() {

        System.out.println("CAME 1");
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_target, new Response.Listener<String>() {

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

                            String target = obj1.getString(TAG_TARGET_AMOUNT);
                            String achive = obj1.getString(TAG_REACHED_AMOUNT);
                            String str_from_date = obj1.getString(TAG_FROM_DATE);
                            String str_to_date = obj1.getString(TAG_TO_DATE);
                            String str_name = obj1.getString(TAG_TARGET_NAME);
                            String str_type = obj1.getString(TAG_TARGET_TYPE);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            if (!achive.equals("")){
                                map.put(TAG_REACHED_AMOUNT, achive);
                            }else {
                                map.put(TAG_REACHED_AMOUNT, "0");
                            }
                            // adding each child node to HashMap key => value
                            map.put(TAG_TARGET_NAME, str_name);
                            map.put(TAG_TO_DATE, str_to_date);
                            map.put(TAG_FROM_DATE, str_from_date);
                            map.put(TAG_TARGET_AMOUNT, target);

                            complaint_list.add(map);

                            System.out.println("HASHMAP ARRAY" + complaint_list);


                            adapter = new Target_Adapter(getActivity(),
                                    complaint_list);
                            mytask_listView.setAdapter(adapter);

                        }

                        swipeRefreshLayout.setRefreshing(false);
                    } else if (success == 0) {

                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "No Target Found For You", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id); // replace as str_id
                params.put("user_type", str_user_type);

                System.out.println("user_id" + str_user_id);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }


}
