package com.mamahome.application;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProjectFragment extends Fragment {

    View view;
 Button pro_images;
    private static int RESULT_LOAD_IMG = 1;
 
 
    public AddProjectFragment() { 
        // Required empty public constructor 
    } 
 
 
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment 
        view = inflater.inflate(R.layout.fragment_add_project, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Add New Project");
        pro_images  = view.findViewById(R.id.bt_projectimg_selectfile);
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
 
    } 

}
