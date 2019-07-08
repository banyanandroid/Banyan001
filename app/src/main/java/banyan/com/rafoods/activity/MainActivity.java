package banyan.com.rafoods.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import banyan.com.rafoods.R;
import banyan.com.rafoods.broadcast.ConnectivityReceiver;
import banyan.com.rafoods.global.GPSTracker;
import banyan.com.rafoods.global.SessionManager;
import banyan.com.rafoods.service.Service_Add_Enquiry;
import banyan.com.rafoods.service.Service_Order_Form;
import banyan.com.rafoods.service.Service_Sr_Order_Form;
import pugman.com.simplelocationgetter.SimpleLocationGetter;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, SimpleLocationGetter.OnLocationGetListener
        , ConnectivityReceiver.ConnectivityReceiverListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private static long back_pressed;

    // 6.0 Location & Call
    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    static final Integer GPS_SETTINGS = 0x7;

    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;

    String str_lat, str_long;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    // Session Manager Class
    SessionManager session;

    String str_name;
    public static String str_id;
    String value = "nothing";
    String str_status;
    String str_out_going_call;

    private boolean detecting = false;

    String TAG1 = "Attendance";

    ProgressDialog pDialog;
    public static RequestQueue queue;

    GPSTracker gps;
    Double latitude, longitude;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_name = user.get(SessionManager.KEY_USER);
        str_id = user.get(SessionManager.KEY_USER_ID);

        isInternetOn();

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                value = extras.getString("from_value");

                System.out.println("value : " + value);

                if (value != null && !value.isEmpty()) {

                } else {
                    value = "nothing";
                }
            } else {
                value = "nothing";
            }

        } catch (Exception e) {

        }

        if (!value.equals("") && !value.isEmpty() && value != null) {

            if (value.equals("enquiry")) {

                displayView(0);

            } else if (value.equals("order")) {
                displayView(1);
            } else if (value.equals("primaryorder")) {
                displayView(2);
            } else if (value.equals("Order Description")) {
                displayView(3);
            } else {
                displayView(0);
            }

        } else {
            displayView(0);
        }
        // Location Service

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            System.out.println("Called");
            turnGPSOn();
            System.out.println("Done");
        } catch (Exception e) {

        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        } else {
            showGPSDisabledAlertToUser();
        }

        try {
            NewLocation();
        }catch (Exception e) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            session.logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {

        try {
            System.out.println("Check Internet");
            bg_isInternetOn();
        } catch (Exception e) {

        }
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new Fragment_Enquiry();
                title = getString(R.string.title_enquiry);
                break;
            case 1:
                fragment = new Fragment_Order();
                title = getString(R.string.title_order);
                break;
            case 2:
                fragment = new Fragment_Primary_Order();
                title = getString(R.string.title_primary_order);
                break;
            case 3:
                fragment = new Fragment_Target();
                title = getString(R.string.title_my_order);
                break;
            case 4:
                stoptimertask();
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                session.logoutUser();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Brgin Timer task
        //startTimer();
         //MyApplication.getInstance().setConnectivityListener(this);
    }

    public void startTimer() {

        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 30000, 30000); // 30 Sec

    }

    public void stoptimertask() {

        if (timer != null) {

            timer.cancel();

            timer = null;

        }

    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


    }

    /********************************
     * Check GPS Connection is Enabled
     *********************************/

    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        finishAffinity();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                // use a handler to run a toast that shows the current timestamp

                handler.post(new Runnable() {

                    public void run() {

                        try {
                             NewLocation();
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }

                });

            }

        };

    }


    /**********************************
     * Check Internet Connection
     ********************************/

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Anil Foods")
                    .setMessage("!Oops no internet :(")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    // finish();
                                    finishAffinity();
                                }
                            }).show();

            return false;
        }
        return false;
    }

    /**********************************
     * Check Internet for Bg Service
     ********************************/

    public final boolean bg_isInternetOn() {

        System.out.println("@@@ Its ONLINE");

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            // if connected with internet
            System.out.println("### MyReceiver : onReceive");

            // run service to upload form details data
            Intent order_intent = new Intent(getApplicationContext(), Service_Order_Form.class);
            getApplicationContext().startService(order_intent);

            // if connected with internet
            System.out.println("### SR ORDER : onReceive");

            // run service to upload form details data
            Intent sr_order_intent = new Intent(getApplicationContext(), Service_Sr_Order_Form.class);
            getApplicationContext().startService(sr_order_intent);

            System.out.println("### call Service_Order_Details");
            // run service to upload enquiry data
            Intent enquiry_intent = new Intent(getApplicationContext(), Service_Add_Enquiry.class);
            getApplicationContext().startService(enquiry_intent);
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
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

       /* try {
            System.out.println("LATT :: " + str_lat);
            System.out.println("LONGG :: " + str_long);
            queue = Volley.newRequestQueue(MainActivity.this);
            UpdateMyLocation();
        } catch (Exception e) {

        }*/
    }

    @Override
    public void onError(String error) {
        Log.e("LOCATION", "Error: " + error);
    }


    /*********************************
     * For Loaction
     ********************************/


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
                //Call
                case 2:

                    break;
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        try {

            result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, GPS_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });

        }catch (Exception e) {

        }
    }

    /*******************************
     * Enable GPS
     ******************************/

    private void turnGPSOn() {

        System.out.println("Inside GPS");

        System.out.println("Inside GPS 0 ");

        String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.getApplicationContext().sendBroadcast(poke);

            System.out.println("Inside GPS 1");
        }
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