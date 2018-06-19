package com.mamahome.application;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProjectFragment extends Fragment {

    View view,rowView;
    FragmentTransaction fragmentTransaction;
   Button pro_images,pro_documents,bt_add,bt_delete,bt_location;
   LinearLayout linearLayout_parent, ll_addroom;
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_LOAD_DOC = 1;

    public AddProjectFragment() {
        // Required empty public constructor 
    } 
 
 
    @Override 
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment 
   view = inflater.inflate(R.layout.fragment_add_project, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Add New Project");
        linearLayout_parent = view.findViewById(R.id.parent_linear);
        bt_location = view.findViewById(R.id.bt_location);
        ll_addroom = view.findViewById(R.id.LL_addroom);
        bt_add = view.findViewById(R.id.bt_add_more);
        bt_delete = view.findViewById(R.id.bt_delete);
        pro_images  = view.findViewById(R.id.bt_projectimg_selectfile);
        pro_documents = view.findViewById(R.id.bt_gov_selectfile);
//        mapView.onCreate(null);
//        mapView.onResume();
//        mapView.getMapAsync(this);
         pro_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

            }
        });
        pro_images.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos 
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent 
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
 
            } 
        });
        //adding the more room type
       bt_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               LayoutInflater inflater2 = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          rowView = inflater2.inflate(R.layout.field_project, null);
               // Add the new row before the add field button.

               linearLayout_parent.addView(rowView, linearLayout_parent.getChildCount() - 1);

           }
       });
       // delete the added room type
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int count = linearLayout_parent.getChildCount();
                    if (count >2)
                    {
                        linearLayout_parent.removeViewAt(1);
                    }
                }
        });
        // select the location of the user
        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, new MapFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override 
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        try { 
            // When an Image is picked 
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data 
                Toast.makeText(getContext(), "You  picked Image", Toast.LENGTH_LONG).show();
 
            } else { 
                Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            } 
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show(); 
        }
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_DOC && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Toast.makeText(getContext(), "You  picked Image", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack("BS_HOME", 0);
                    return true;
                }
                return false;
            }
        });
    }

}
