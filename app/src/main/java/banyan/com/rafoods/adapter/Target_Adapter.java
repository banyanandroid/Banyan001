package banyan.com.rafoods.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import banyan.com.rafoods.R;
import banyan.com.rafoods.activity.Fragment_Target;


public class Target_Adapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private LinearLayout singleMessageContainer;

    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    private String[] bgColors;

    public Target_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null)
            v = inflater.inflate(R.layout.list_row_target, null);

        TextView title = (TextView) v.findViewById(R.id.target_title);
        TextView date = (TextView) v.findViewById(R.id.target_date);

        HashMap<String, String> result = new HashMap<String, String>();
        result = data.get(position);

        title.setText(result.get(Fragment_Target.TAG_TARGET_NAME));

        String str_from = result.get(Fragment_Target.TAG_TARGET_AMOUNT);
        String str_to = result.get(Fragment_Target.TAG_REACHED_AMOUNT);

        System.out.println("TOOOOOOOOO :: " + str_to);

        if (!str_to.equals("")) {

            date.setText(str_to + "/" + str_from);
        }else {

            int int_to = 0;

            date.setText( int_to + "/" + str_from);
        }


        String color = bgColors[position % bgColors.length];
        title.setBackgroundColor(Color.parseColor(color));

        return v;

    }

}