package banyan.com.rafoods.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import banyan.com.rafoods.R;
import banyan.com.rafoods.global.SessionManager;
import dmax.dialog.SpotsDialog;


/**
 * Created by Jothiprabhakar on 29-Mar-18.
 */

public class Activity_Target_View extends AppCompatActivity {

    private Toolbar mToolbar;
    SpotsDialog dialog;
    public static RequestQueue queue;
    String TAG = "Auto_Res";

    // Session Manager Class
    SessionManager session;

    private PieChart pieChart;
    private TextView txt_from , txt_to;

    String str_target, str_acheived, str_from, str_to, str_name = "";

    Float float_target, float_achive;
    int int_tartget, int_achived ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Target");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("from_value", "target");
                startActivity(i);
                finish();
            }
        });

        pieChart = (PieChart) findViewById(R.id.pie_chart);
        txt_from = (TextView) findViewById(R.id.target_txt_from);
        txt_to = (TextView) findViewById(R.id.target_txt_to);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(Activity_Target_View.this);

        str_target = sharedPreferences.getString("str_select_target_amount", "str_select_target_amount");
        str_acheived = sharedPreferences.getString("str_select_target_reached", "str_select_target_reached");
        str_from = sharedPreferences.getString("str_select_target_from", "str_select_target_from");
        str_to = sharedPreferences.getString("str_select_target_to", "str_select_target_to");
        str_name = sharedPreferences.getString("str_select_target_name", "str_select_target_name");

        if (str_target.equals("str_select_target_amount")) {
            int_tartget = 0;
        }else {
            int_tartget = Integer.parseInt(str_target);
        }

        System.out.println("str Achi" + str_acheived);
        if (str_acheived.equals("str_select_target_reached")) {
            int_achived = 0;
        }else {
            int_achived = Integer.parseInt(str_acheived);
        }

        int_achived = Integer.parseInt(str_acheived);

        System.out.println("TARGET : " + int_tartget);
        System.out.println("ACHIVE : " + int_achived);

        txt_from.setText("From : " + str_from);
        txt_to.setText("To : " + str_to);


        function_piechart();

    }

    private void function_piechart(){

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(int_tartget, 0));
        entries.add(new Entry(int_achived, 1));

        PieDataSet dataset = new PieDataSet(entries, "Anil Sales App");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Amount Target");
        labels.add("Amount Achived");

        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS); //
        pieChart.setDescription("");
        pieChart.setData(data);


        pieChart.animateY(2000);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Amount Target from : " + str_from + "\n To Date : " + str_to
                + "\n" + int_achived + " / " + int_tartget );

        return s;
    }


}