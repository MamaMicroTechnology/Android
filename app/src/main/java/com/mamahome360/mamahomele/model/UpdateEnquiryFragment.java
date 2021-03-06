package com.mamahome.application;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mamahome.application.Products.Category;
import com.mamahome.application.Products.ProductList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
public class UpdateEnquiryFragment extends Fragment {

    View view;

    APIKeys APIKeys;

    String User_ID;
    private ArrayList<Category> categories;
    private ArrayList<com.mamahome.application.Products.Brand> brands;
    private ArrayList<com.mamahome.application.Products.SubCategory> subCategories;
    private ArrayList<com.mamahome.application.Products.Brand> newBrands;
    String categoryPosition;

    TextView tv_total_floor_count, tv_category, tv_brand, tv_sub_category, tv_date;
    Switch switchProducts;
    LinearLayout ll_SpinProducts, ll_TextProducts;

    EditText et_project_id, et_contactNumber, et_location, et_remarks, et_quantity;

    Button bt_submit, bt_date;

    Spinner spin_Category, spin_Brand, spin_SubCategory;

    String EnquiryID, ProjectID, ContactNumber, Location, Remarks, MainCategory,
            SubCategory, Brand, RequirementDate, UserID, Quantity, datepicker;
    Boolean checked = false;

    FragmentTransaction fragmentTransaction;
    Calendar cal, cal1;
    ArrayAdapter<String> CategoryAdapter, SubCategoryAdapter, BrandAdapter;
    List<String> listCategory, listBrand, listSubCategory;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    String visible_date;
    ProgressDialog progressDialog;


    public UpdateEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_enquiry, container, false);

        ButterKnife.bind(this, view);
        cal = Calendar.getInstance(TimeZone.getDefault());
        cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_YEAR, 2);

        et_project_id = view.findViewById(R.id.et_project_id);
        et_project_id.setEnabled(false);
        et_contactNumber = view.findViewById(R.id.et_contactnumber);
        et_contactNumber.setEnabled(false);
        et_location = view.findViewById(R.id.et_location);
        et_location.setEnabled(false);
        et_remarks = view.findViewById(R.id.et_remarks);
        et_quantity = view.findViewById(R.id.et_quantity);
        spin_Category = view.findViewById(R.id.spin_Category);
        spin_Brand = view.findViewById(R.id.spin_Brand);
        spin_SubCategory = view.findViewById(R.id.spin_SubCategory);
        switchProducts = view.findViewById(R.id.switchproducts);
        tv_category = view.findViewById(R.id.tv_category);
        tv_brand = view.findViewById(R.id.tv_brand);
        tv_date = view.findViewById(R.id.tv_date);
        tv_sub_category = view.findViewById(R.id.tv_sub_category);
        ll_SpinProducts = view.findViewById(R.id.ll_SpinProducts);
        ll_TextProducts = view.findViewById(R.id.ll_TextProducts);

        final Bundle bundle = this.getArguments();

        ll_SpinProducts.setVisibility(View.GONE);
        ll_TextProducts.setVisibility(View.VISIBLE);

        switchProducts.setVisibility(View.VISIBLE);

        switchProducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_SpinProducts.setVisibility(View.VISIBLE);
                    ll_TextProducts.setVisibility(View.GONE);
                    checked = true;
                }
                else{
                    ll_SpinProducts.setVisibility(View.GONE);
                    ll_TextProducts.setVisibility(View.VISIBLE);
                    checked = false;
                }
            }
        });


        EnquiryID = bundle.getString("id", null);
        ProjectID = bundle.getString("projectId", null);
        MainCategory = bundle.getString("main_category", null);
        Brand = bundle.getString("brand", null);
        SubCategory = bundle.getString("sub_category", null);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        RequirementDate = bundle.getString("requirement_date", null);
        if(RequirementDate != null){
            Date date = null;
            try {
                date = inputFormat.parse(RequirementDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            visible_date = outputFormat.format(date);
        }
        Remarks = bundle.getString("notes", null);
        ContactNumber = bundle.getString("A_contact", null);
        Location = bundle.getString("location", null);
        Quantity = bundle.getString("quantity", null);

        et_project_id.setText(ProjectID);
        et_project_id.setEnabled(false);
        et_project_id.setFocusable(false);

        et_contactNumber.setText(ContactNumber);
        et_contactNumber.setEnabled(false);
        et_contactNumber.setFocusable(false);

        et_remarks.setText(Remarks);

        et_quantity.setText(Quantity);

        tv_category.setText("Category : "+ MainCategory);
        tv_brand.setText("Brand : "+ Brand);
        tv_sub_category.setText("Product : "+ SubCategory);
        tv_date.setText("Selected Date: " + visible_date);



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
                else if (TextUtils.isEmpty(et_remarks.getText().toString())){
                    et_remarks.setError(getString(R.string.cannot_empty));
                    et_remarks.requestFocus();
                    return;
                }

                if(isNetWorkAvailable(type)){
                    addEnquiry();
                    showProgressDialog();
                }
                else{
                    ShowAlert();
                }
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

        spin_Brand.setVisibility(View.GONE);
        spin_SubCategory.setVisibility(View.GONE);
        spin_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spin_SubCategory.setVisibility(View.GONE);
                newBrands = new ArrayList<>();
                listBrand = new ArrayList<String>();
                for (int i = 0; i < brands.size(); i++){
                    if (categories.get(position).getId().equals(brands.get(i).getCategory_id())){
                        listBrand.add(brands.get(i).getBrand());
                        newBrands.add(brands.get(i));
                    }

                }
                BrandAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listBrand);
                BrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_Brand.setAdapter(BrandAdapter);
                spin_Brand.setVisibility(View.VISIBLE);
                categoryPosition = categories.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_Brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_SubCategory.setVisibility(View.GONE);
                listSubCategory = new ArrayList<String>();
                for (int i = 0; i < subCategories.size(); i++){
                    String BrandId = newBrands.get(position).getId();
                    String SubBID = subCategories.get(i).getBrand_id();
                    String SubCID = subCategories.get(i).getCategory_id();
                    if (BrandId.equals(SubBID) && categoryPosition.equals(SubCID))
                        listSubCategory.add(subCategories.get(i).getSub_cat_name());
                }
                SubCategoryAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listSubCategory);
                SubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_SubCategory.setAdapter(SubCategoryAdapter);
                spin_SubCategory.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Update Enquiry");

        return view;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void addEnquiry(){
        /*ProjectID, ContactNumber, Location, Remarks, MainCategory,
                SubCategory, Brand, RequirementDate, UserID, ProjectID, Quantity;*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProjectsFragment.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(selfSigningClientBuilder.createClient(getContext()))
                .build();

        APIKeys = retrofit.create(APIKeys.class);
        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE);


        ProjectID = et_project_id.getText().toString();
        ContactNumber = et_contactNumber.getText().toString();
        Remarks = et_remarks.getText().toString();
        if(checked){
            MainCategory = spin_Category.getSelectedItem().toString();
            Brand = spin_Brand.getSelectedItem().toString();
            SubCategory = spin_SubCategory.getSelectedItem().toString();
        }
        Quantity = et_quantity.getText().toString();
        UserID = prefs.getString("USER_ID", null);


        AddEnquiryRequest updateEnquiryRequest = new AddEnquiryRequest();

        updateEnquiryRequest.setId(EnquiryID);
        updateEnquiryRequest.setProject_id(ProjectID);
        updateEnquiryRequest.setA_contact(ContactNumber);
        updateEnquiryRequest.setNotes(Remarks);
        updateEnquiryRequest.setMain_category(MainCategory);
        updateEnquiryRequest.setBrand(Brand);
        updateEnquiryRequest.setSub_category(SubCategory);
        updateEnquiryRequest.setQuantity(Quantity);
        updateEnquiryRequest.setGenerated_by(UserID);
        updateEnquiryRequest.setRequirement_date(RequirementDate);

        Call<AddEnquiryResponse> updateEnquiryResponseCall = APIKeys.updateEnquiry(updateEnquiryRequest);

        updateEnquiryResponseCall.enqueue(new Callback<AddEnquiryResponse>() {
            @Override
            public void onResponse(Call<AddEnquiryResponse> call, Response<AddEnquiryResponse> response) {
                int statusCode = response.code();

                //AddEnquiryResponse addEnquiryResponse  = response.body();
                //String APIresponse = response.body().getMessage();
                //Toast.makeText(getContext(), "on Success " + statusCode , Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "on Success " + APIresponse , Toast.LENGTH_LONG).show();
                //getActivity().onBackPressed();
                /*fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, new EnquiriesFragment(), "ADD_ENQUIRY_FRAGMENT");
                fragmentTransaction.addToBackStack("BS_ADD_ENQUIRY");
                fragmentTransaction.commit();*/
                progressDialog.cancel();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<AddEnquiryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();
            }
        });


    }

    private void populateSpinners(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProjectsFragment.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(selfSigningClientBuilder.createClient(getContext()))
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

                listCategory = new ArrayList<String>();

                for (int i = 0; i < categories.size(); i++){
                    listCategory.add(categories.get(i).getCategory_name());
                }

                CategoryAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listCategory);
                CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin_Category.setAdapter(CategoryAdapter);
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
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            String inputDateStr = RequirementDate;
            Date date = null;
            try {
                date = inputFormat.parse(inputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datepicker = outputFormat.format(date);
            tv_date.setText("Selected Date : "+datepicker);
            //Toast.makeText(getContext(), datepicker, Toast.LENGTH_LONG).show();

        }
    };

    private boolean isNetWorkAvailable(int[] type){
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            for(int typeNetwork:type){
                assert cm != null;
                NetworkInfo networkInfo = cm.getNetworkInfo(typeNetwork);
                if(networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return false;
    }

    private void ShowAlert(){
        new AlertDialog.Builder(getContext())
                .setTitle("Oops.. No Internet Connection")
                .setMessage("Please Connect To The Internet To Use Our Services! \nThank You.")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetWorkAvailable(type)){
                            addEnquiry();
                        }
                        else{
                            ShowAlert();
                        }
                    }
                })
                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Aborted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .show();
    }

}
