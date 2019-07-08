package banyan.com.rafoods.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import banyan.com.rafoods.R;
import banyan.com.rafoods.activity.Activity_Order_Form;
import banyan.com.rafoods.model.EDT_Hero;


/**
 * Created by Jo on 12/28/2017.
 */

//we need to extend the ArrayAdapter class as we are building an adapter
public class Edit_Order_Adapter extends ArrayAdapter<EDT_Hero> {

    //the list values in the List of type hero
    List<EDT_Hero> heroList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public Edit_Order_Adapter(Context context, int resource, List<EDT_Hero> heroList) {
        super(context, resource, heroList);
        this.context = context;
        this.resource = resource;
        this.heroList = heroList;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        TextView textViewName = view.findViewById(R.id.serial);
        TextView textViewQty = view.findViewById(R.id.qty);
        TextView textViewprice = view.findViewById(R.id.price);

        //getting the hero of the specified position
        EDT_Hero hero = heroList.get(position);

        //adding values to the list item
        textViewName.setText(hero.getName());
        textViewQty.setText(hero.getQty());
        textViewprice.setText(hero.getPrice());


        //finally returning the view
        return view;
    }

    //this method will remove the item from the list
    private void removeHero(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                heroList.remove(position);

                //reloading the list
                notifyDataSetChanged();

                Activity_Order_Form.Fun_Total_Cal();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
