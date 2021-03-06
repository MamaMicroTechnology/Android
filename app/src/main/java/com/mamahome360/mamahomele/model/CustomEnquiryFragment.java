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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomEnquiryFragment extends Fragment {

    View view;
    TextView tv_date;
    APIKeys APIKeys;
    String User_ID;
    EditText et_project_id, et_contactNumber, et_location, et_remarks, et_quantity;
    Button bt_submit, bt_date;
    String ProjectID, ContactNumber, Location, Remarks, MainCategory,
            SubCategory, Brand, RequirementDate, UserID, Quantity, datepicker;
    FragmentTransaction fragmentTransaction;
    Calendar cal, cal1;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    ProgressDialog progressDialog;
    TextView tv_product;

    public CustomEnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_custom_enquiry, container, false);

        cal = Calendar.getInstance(TimeZone.getDefault());
        cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_YEAR, 2);

        tv_date = view.findViewById(R.id.tv_date);
        et_project_id = view.findViewById(R.id.et_project_id);
        et_contactNumber = view.findViewById(R.id.et_contactnumber);
        et_location = view.findViewById(R.id.et_location);
        et_remarks = view.findViewById(R.id.et_remarks);
        et_quantity = view.findViewById(R.id.et_quantity);
        tv_product = view.findViewById(R.id.tv_product);

        final Bundle bundle = this.getArguments();
        ProjectID = bundle.getString("Project_id");
        MainCategory = bundle.getString("Category");
        Brand = bundle.getString("Brand");
        SubCategory = bundle.getString("Sub_Cat");
        tv_product.setText("Category: "+MainCategory+"\nBrand: "+Brand+"\nSub Category: "+SubCategory);
        //Toast.makeText(getContext(), "Category: "+MainCategory+"\nBrand: "+Brand+"\nSub Category: "+SubCategory, Toast.LENGTH_SHORT).show();
        et_project_id.setText(ProjectID);
        et_project_id.setEnabled(false);

        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
        //String userName = prefs.getString("USER_NAME", null);
        String phoneNumber = prefs.getString("USER_NUMBER", null);

        et_contactNumber.setText(phoneNumber);
        et_contactNumber.setEnabled(false);

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
                /*else if (TextUtils.isEmpty(et_remarks.getText().toString())){
                    et_remarks.setError(getString(R.string.cannot_empty));
                    et_remarks.requestFocus();
                    return;
                }*/

                if(isNetWorkAvailable(type)){
                    showProgressDialog();
                    addEnquiry();
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
        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);


        ProjectID = et_project_id.getText().toString();
        ContactNumber = et_contactNumber.getText().toString();
        Remarks = et_remarks.getText().toString();
//        MainCategory = spin_Category.getSelectedItem().toString();
//        Brand = spin_Brand.getSelectedItem().toString();
//        SubCategory = spin_SubCategory.getSelectedItem().toString();
        Quantity = et_quantity.getText().toString();
        UserID = prefs.getString("USER_ID", null);


        AddEnquiryRequest addEnquiryRequest = new AddEnquiryRequest();

        addEnquiryRequest.setProject_id(ProjectID);
        addEnquiryRequest.setA_contact(ContactNumber);
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
                progressDialog.cancel();
                //Toast.makeText(getContext(), "on Success " + statusCode , Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "on Success " + APIresponse , Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<AddEnquiryResponse> call, Throwable t) {
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
