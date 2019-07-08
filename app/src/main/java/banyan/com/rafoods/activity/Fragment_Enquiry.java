package banyan.com.rafoods.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import banyan.com.rafoods.R;


/**
 * Created by Jo 05-03-2018.
 */
public class Fragment_Enquiry extends Fragment {



    public Fragment_Enquiry() {
        // Required empty public constructor
    }

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    String value = "nothing";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.fragment_enquiry,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new Tab_Enquiry_Shop_Fragment();
                case 1 : return new Tab_Edit_Shop_Fragment();
           }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }
        /**
         * This method returns the title of the tab according to the position.
         */
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Shops";
                case 1 :
                    return "Edit Shop";

              /*  case 2 :
                    return "Locations";*/
            }
            return null;
        }
    }

}
