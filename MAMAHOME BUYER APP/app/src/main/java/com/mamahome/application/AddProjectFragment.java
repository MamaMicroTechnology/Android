package com.mamahome.application;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProjectFragment extends Fragment {

    View view, rowView;

    Button pro_images, pro_documents, bt_add, bt_delete, bt_submit;

    EditText et_projectName, et_roadName, et_roadWidth, et_address, et_plotSize, et_basementCount, et_floorCount,
             et_projectSize, et_budget;

    RadioGroup rg_constructionType, rg_RMC, rg_loans, rg_UPVC, rg_budgetType;

    RadioButton rb_constructionType, rb_RMC, rb_loans, rb_UPVC, rb_budgetType;

    String Project_Name, Road_Name, Road_Width, Address, Plot_Size, Basement_Count, Floor_Count, Project_Size,
    Budget, Construction_Type, RMC, Loan, UPVC, Budget_Type;

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
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Add New Project");
        linearLayout_parent = view.findViewById(R.id.parent_linear);
        ll_addroom = view.findViewById(R.id.LL_addroom);
        bt_add = view.findViewById(R.id.bt_add_more);
        bt_delete = view.findViewById(R.id.bt_delete);
        bt_submit = view.findViewById(R.id.bt_submit);
        pro_images = view.findViewById(R.id.bt_projectimg_selectfile);
        pro_documents = view.findViewById(R.id.bt_gov_selectfile);
        et_projectName = view.findViewById(R.id.et_projectname);
        et_roadName = view.findViewById(R.id.et_roadname);
        et_roadWidth = view.findViewById(R.id.et_roadwidth);
        et_address = view.findViewById(R.id.et_roadwidth);
        et_plotSize = view.findViewById(R.id.et_plotsize);
        et_basementCount = view.findViewById(R.id.et_basement_count);
        et_floorCount = view.findViewById(R.id.et_upperfloor_count);
        et_projectSize = view.findViewById(R.id.et_project_size);
        et_budget = view.findViewById(R.id.et_budget);
        rg_constructionType = view.findViewById(R.id.rg_constructiontype);
        rg_RMC = view.findViewById(R.id.rg_rmc);
        rg_loans = view.findViewById(R.id.rg_loan);
        rg_UPVC = view.findViewById(R.id.rg_upvc);
        rg_budgetType = view.findViewById(R.id.rg_budget_type);
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
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater2.inflate(R.layout.field_project, null);
                // Add the new row before the add field button.

                linearLayout_parent.addView(rowView, linearLayout_parent.getChildCount() - 1);

            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = linearLayout_parent.getChildCount();
                if (count > 2) {
                    linearLayout_parent.removeViewAt(1);
                }
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project_Name = et_projectName.getText().toString();
                Road_Name = et_roadName.getText().toString();
                Road_Width = et_roadWidth.getText().toString();
                Address = et_address.getText().toString();
                Plot_Size = et_plotSize.getText().toString();
                Basement_Count = et_basementCount.getText().toString();
                Floor_Count = et_floorCount.getText().toString();
                Project_Size = et_projectSize.getText().toString();
                Budget = et_budget.getText().toString();
                //Construction_Type RMC, Loan, UPVC, Budget_Type;
                int radiobutton = rg_constructionType.getCheckedRadioButtonId();
                rb_constructionType = view.findViewById(radiobutton);
                Construction_Type = rb_constructionType.getText().toString();

                int radiobutton1 = rg_RMC.getCheckedRadioButtonId();
                rb_RMC = view.findViewById(radiobutton1);
                RMC = rb_RMC.getText().toString();

                int radiobutton2 = rg_loans.getCheckedRadioButtonId();
                rb_loans = view.findViewById(radiobutton2);
                Loan = rb_loans.getText().toString();

                int radiobutton3 = rg_UPVC.getCheckedRadioButtonId();
                rb_UPVC = view.findViewById(radiobutton2);
                UPVC = rb_loans.getText().toString();

                int radiobutton4 = rg_budgetType.getCheckedRadioButtonId();
                rb_budgetType = view.findViewById(radiobutton2);
                Budget_Type = rb_budgetType.getText().toString();

                if (TextUtils.isEmpty(Project_Name)){
                    et_projectName.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Road_Name)){
                    et_roadName.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Road_Width)){
                    et_roadWidth.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Address)){
                    et_address.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Plot_Size)){
                    et_plotSize.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Basement_Count)){
                    et_basementCount.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Floor_Count)){
                    et_floorCount.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Project_Size)){
                    et_projectSize.setError(getString(R.string.cannot_empty));
                    return;
                }

                if (TextUtils.isEmpty(Budget)){
                    et_budget.setError(getString(R.string.cannot_empty));
                    return;
                }




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

    public void addProject(){

    }

}
