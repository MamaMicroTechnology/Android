package com.mamahome.application._sliders;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mamahome.application.HomeActivity;
import com.mamahome.application.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewHomeFragment extends Fragment {

    View view;
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    public NewHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_home, container, false);

        sliderView = view.findViewById(R.id.sliderView);
        mLinearLayout = view.findViewById(R.id.pagesContainer);
        setupSlider();

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("MAMAHOME");
        return view;
    }


    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("https://mamahome360.com/img/1920x1080/1.jpg"));
        fragments.add(FragmentSlider.newInstance("http://images.newindianexpress.com/uploads/user/imagelibrary/2018/4/8/w600X300/Ambiguity_constr.jpg"));
        fragments.add(FragmentSlider.newInstance("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSc2EPcUH9_1pgnl_DYZCGfOFFPnd8CHMI72fWOiZPFPCU2NOVaFA"));
        fragments.add(FragmentSlider.newInstance("https://theconstructor.org/wp-content/uploads/2016/09/density-of-construction-materials.jpg"));
        fragments.add(FragmentSlider.newInstance("http://afrilandproperties.com/wp-content/uploads/2015/11/Building-Materials-2-548x300.jpg"));

        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    getActivity().finish();
                }
                return false;
            }
        });


    }

}
