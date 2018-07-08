package com.mamahome.application;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mamahome.application.Products.Category;
import com.mamahome.application.Products.ProductList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEnquiryFragment extends Fragment {

    View view;

    String ROOT_URL = "http://mamahome360.com";
    APIKeys APIKeys;

    String User_ID;
    private ArrayList<Category> categories;
    private ArrayList<com.mamahome.application.Products.Brand> brands;
    private ArrayList<com.mamahome.application.Products.SubCategory> subCategories;

    EditText et_project_id, et_contactNumber, et_location, et_remarks;

    Button bt_submit, bt_date;

    Spinner spin_Category, spin_Brand, spin_SubCategory;

    String ProjectID, ContactNumber, Location, Remarks, MainCategory,
            SubCategory, Brand, RequirementDate, UserID, Quantity, datepicker;

    FragmentTransaction fragmentTransaction;
    Calendar cal, cal1;

    public AddEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_enquiry, container, false);
        ButterKnife.bind(this, view);
        cal = Calendar.getInstance(TimeZone.getDefault());
        cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_YEAR, 2);

        et_project_id = view.findViewById(R.id.et_project_id);
        et_contactNumber = view.findViewById(R.id.et_contactnumber);
        et_location = view.findViewById(R.id.et_location);
        et_remarks = view.findViewById(R.id.et_remarks);
        spin_Category = view.findViewById(R.id.spin_Category);
        spin_Brand = view.findViewById(R.id.spin_Brand);
        spin_SubCategory = view.findViewById(R.id.spin_SubCategory);

        /*et_project_id.setEnabled(false);
        et_project_id.setInputType(InputType.TYPE_NULL);
        et_contactNumber.setEnabled(false);
        et_contactNumber.setInputType(InputType.TYPE_NULL);
        et_location.setEnabled(false);
        et_location.setInputType(InputType.TYPE_NULL);*/

        bt_submit = view.findViewById(R.id.bt_submit);
        bt_date = view.findViewById(R.id.bt_date);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(RequirementDate)){
                    Toast.makeText(getContext(), "Please Select Requirement Date", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(et_contactNumber.getText().toString())){
                    et_contactNumber.setError(getString(R.string.cannot_empty));
                    et_contactNumber.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(et_project_id.getText().toString())){
                    et_project_id.setError(getString(R.string.cannot_empty));
                    et_project_id.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(et_location.getText().toString())){
                    et_location.setError(getString(R.string.cannot_empty));
                    et_location.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(et_remarks.getText().toString())){
                    et_remarks.setError(getString(R.string.cannot_empty));
                    et_remarks.requestFocus();
                    return;
                }

                addEnquiry();
            }
        });

        bt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        R.style.DialogTheme, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH+2));
                datePicker.setCancelable(false);
                datePicker.getDatePicker().setMinDate(cal1.getTimeInMillis());
                //datePicker.setTitle("Select Requirement Date");
                datePicker.show();

            }
        });

        populateSpinners();

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Make New Enquiry");
        return view;
    }

    private void addEnquiry(){
        /*ProjectID, ContactNumber, Location, Remarks, MainCategory,
                SubCategory, Brand, RequirementDate, UserID, ProjectID, Quantity;*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIKeys = retrofit.create(APIKeys.class);
        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE);


        ProjectID = et_project_id.getText().toString();
        ContactNumber = et_contactNumber.getText().toString();
        Location = et_location.getText().toString();
        Remarks = et_remarks.getText().toString();
        MainCategory = "Steel";
        Brand = "Indus";
        SubCategory = "18mm";
        Quantity = "50";
        UserID = prefs.getString("USER_ID", null);


        AddEnquiryRequest addEnquiryRequest = new AddEnquiryRequest();

        addEnquiryRequest.setProject_id(ProjectID);
        addEnquiryRequest.setA_contact(ContactNumber);
        //addEnquiryRequest.setLocation(Location);
        addEnquiryRequest.setNotes(Remarks);
        addEnquiryRequest.setMain_category(MainCategory);
        addEnquiryRequest.setBrand(Brand);
        addEnquiryRequest.setSub_category(SubCategory);
        addEnquiryRequest.setQuantity(Quantity);
        addEnquiryRequest.setGenerated_by(UserID);
        addEnquiryRequest.setRequirement_date(RequirementDate);

        Call<AddEnquiryResponse> addEnquiryResponseCall = APIKeys.Addenquiry(addEnquiryRequest);

        addEnquiryResponseCall.enqueue(new Callback<AddEnquiryResponse>() {
            @Override
            public void onResponse(Call<AddEnquiryResponse> call, Response<AddEnquiryResponse> response) {
                int statusCode = response.code();

                AddEnquiryResponse addEnquiryResponse  = response.body();
                String APIresponse = response.body().getMessage();
                Toast.makeText(getContext(), "on Success " + statusCode , Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "on Success " + APIresponse , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AddEnquiryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();
            }
        });


    }

    private void populateSpinners(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIKeys = retrofit.create(APIKeys.class);

        Call<ProductList> productListCall = APIKeys.PRODUCT_LIST_CALL();

        productListCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                ProductList productList = response.body();
                categories = new ArrayList<>(Arrays.asList(productList.getCategories()));
                brands = new ArrayList<>(Arrays.asList(productList.getBrands()));
                subCategories = new ArrayList<>(Arrays.asList(productList.getSubCategories()));

                List<String> listCategory = new ArrayList<String>();
                List<String> listBrand = new ArrayList<String>();
                List<String> listSubCategory = new ArrayList<String>();

                for (int i = 0; i < categories.size(); i++){
                    listCategory.add(categories.get(i).getCategory_name());
                }
                for (int i = 0; i < brands.size(); i++){
                    listBrand.add(brands.get(i).getBrand());
                }
                for (int i = 0; i < subCategories.size(); i++){
                    listSubCategory.add(subCategories.get(i).getSub_cat_name());
                }

                ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listCategory);
                CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ArrayAdapter<String> BrandAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listBrand);
                BrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ArrayAdapter<String> SubCategoryAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listSubCategory);
                SubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin_Category.setAdapter(CategoryAdapter);
                //spin_Category.setPrompt("--Select Category--");
                spin_Brand.setAdapter(BrandAdapter);
                spin_SubCategory.setAdapter(SubCategoryAdapter);
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            if(Integer.parseInt(month1)<10){
                month1 = "0"+month1;
            }
            if(Integer.parseInt(day1)<10){
                day1 = "0"+day1;
            }
            RequirementDate = (year1 + "-" + month1 + "-" + day1);
            datepicker = (day1 + "/" + month1 + "/" + year1);
            Toast.makeText(getContext(), datepicker, Toast.LENGTH_LONG).show();

        }
    };

}
