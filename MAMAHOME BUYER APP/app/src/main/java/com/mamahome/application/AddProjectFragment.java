package com.mamahome.application;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProjectFragment extends Fragment {

    View view, rowView;
    ImagePopup imagePopup;
    Boolean isProjectImage;

    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    int NoOfCheckboxTicked = 0;

    android.support.v4.app.FragmentTransaction fragmentTransaction;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    Button pro_images, pro_documents, bt_add, bt_delete, bt_submit, bt_location;

    EditText et_projectName, et_roadName, et_roadWidth, et_address, et_length, et_breadth,
            et_basementCount, et_floorCount, et_projectSize, et_budget;

    CheckBox cb_planning, cb_digging, cb_foundation, cb_pillars, cb_walls, cb_roofing,
            cb_electrical, cb_plumbing, cb_plastering, cb_flooring, cb_carpentry, cb_paintings,
            cb_fixtures, cb_completion, cb_closed;

    ImageView iv_project_imageselected, iv_document_imageselected;

    Spinner spinner_floor;

    RadioGroup rg_constructionType, rg_RMC, rg_loans, rg_UPVC, rg_budgetType;

    RadioButton rb_constructionType, rb_RMC, rb_loans, rb_UPVC, rb_budgetType;

    String Project_Name, Road_Name, Road_Width, Address, Length, Breadth, Basement_Count, Floor_Count, Project_Size,
            Budget, Construction_Type, RMC, Loan, UPVC, Budget_Type, Project_Type, User_ID, imgString, Latitude, Longitude,
            DocumentString;
    ArrayList<String> projectStatus;

    TextView tv_total_floor_count, tv_latitude, tv_logitude, tv_resetTick;

    List<String> spinnerArray;
    ArrayAdapter<String> adapter;

    ProgressDialog progressDialog;

    //Project Status Tickbox

    LinearLayout linearLayout_parent, ll_addroom;

    String ROOT_URL = "https://mamahome360.com";
    APIKeys APIKeys;

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
        ButterKnife.bind(this, view);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Add New Project");
        requestStoragePermission();
        init();

        SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_PROJECT_LOCATION", MODE_PRIVATE);
        Latitude = prefs.getString("LATITUDE", null);
        Longitude = prefs.getString("LONGITUDE", null);

        if (!TextUtils.isEmpty(Latitude)) {
            tv_latitude.setText("Latitude:\n" + Latitude);
        }
        if (!TextUtils.isEmpty(Longitude)) {
            tv_logitude.setText("Longitude:\n" + Longitude);
        }

        projectStatus = new ArrayList<>();

        et_basementCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Basement_Count = et_basementCount.getText().toString();
                Floor_Count = et_floorCount.getText().toString();
                if (!TextUtils.isEmpty(Basement_Count) && !TextUtils.isEmpty(Floor_Count)) {
                    int basement = Integer.parseInt(Basement_Count);
                    int floor = Integer.parseInt(Floor_Count);
                    int total = basement + floor + 1;
                    tv_total_floor_count.setText("Total (" + total + ") = Basement (" + basement + ") + Ground + Floors (" + floor + ")");
                }
                if (TextUtils.isEmpty(Basement_Count) && TextUtils.isEmpty(Floor_Count)) {
                    tv_total_floor_count.setText("Total () = Basement () + Ground + Floors ()");
                }
                if (TextUtils.isEmpty(Basement_Count) && !TextUtils.isEmpty(Floor_Count)) {
                    int total = Integer.parseInt(Floor_Count)+1;
                    tv_total_floor_count.setText("Total ("+total+") = Basement () + Ground + Floors ("+Floor_Count+")");
                }
                if (!TextUtils.isEmpty(Basement_Count) && TextUtils.isEmpty(Floor_Count)) {
                    int total = Integer.parseInt(Basement_Count)+1;
                    tv_total_floor_count.setText("Total ("+total+") = Basement ("+Basement_Count+") + Ground + Floors ()");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        et_floorCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Basement_Count = et_basementCount.getText().toString();
                Floor_Count = et_floorCount.getText().toString();
                if (!TextUtils.isEmpty(Basement_Count) && !TextUtils.isEmpty(Floor_Count)) {
                    spinnerArray = new ArrayList<String>();
                    int basement = Integer.parseInt(Basement_Count);
                    int floor = Integer.parseInt(Floor_Count);
                    int total = basement + floor + 1;
                    tv_total_floor_count.setText("Total (" + total + ") = Basement (" + basement + ") + Ground + Floors (" + floor + ")");
                }
                if (TextUtils.isEmpty(Basement_Count) && TextUtils.isEmpty(Floor_Count)) {
                    tv_total_floor_count.setText("Total () = Basement () + Ground + Floors ()");
                }
                if (TextUtils.isEmpty(Floor_Count) && !TextUtils.isEmpty(Basement_Count)) {
                    int total = Integer.parseInt(Basement_Count)+1;
                    tv_total_floor_count.setText("Total ("+total+") = Basement ("+Basement_Count+") + Ground + Floors ()");
                }
                if (!TextUtils.isEmpty(Floor_Count) && TextUtils.isEmpty(Basement_Count)) {
                    int total = Integer.parseInt(Floor_Count)+1;
                    tv_total_floor_count.setText("Total ("+total+") = Basement () + Ground + Floors ("+Floor_Count+")");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et_floorCount.getText().toString())){
                    int floor = Integer.parseInt(et_floorCount.getText().toString());
                    spinnerArray = new ArrayList<String>();
                    for (int i = 1; i <= floor; i++) {
                        spinnerArray.add("Floor " + i);
                    }
                    adapter = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_floor.setAdapter(adapter);
                }


            }
        });
        pro_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProjectImage = false;
                showFileChooser();
            }
        });
        pro_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProjectImage = true;
                showFileChooser();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater2.inflate(R.layout.field_project, null);
                // Add the new row before the add field button.

                Spinner spinner_floor1 = rowView.findViewById(R.id.spin_floor);
                spinner_floor1.setAdapter(adapter);
                Floor_Count = et_floorCount.getText().toString();
                if (!TextUtils.isEmpty(Floor_Count)){
                    linearLayout_parent.addView(rowView, linearLayout_parent.getChildCount() - 1);
                }

            }
        });

        tv_resetTick.setVisibility(View.GONE);

        tv_resetTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetAllTickBox();
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = linearLayout_parent.getChildCount();
                if (count > 2) {
                    linearLayout_parent.removeViewAt(count - 2);
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
                Length =  et_length.getText().toString();
                Breadth = et_breadth.getText().toString();
                Basement_Count = et_basementCount.getText().toString();
                Floor_Count = et_floorCount.getText().toString();
                if (!TextUtils.isEmpty(et_basementCount.getText().toString()) && !TextUtils.isEmpty(et_floorCount.getText().toString())){
                    int total = Integer.parseInt(Basement_Count)+Integer.parseInt(Floor_Count);
                    Project_Type = String.valueOf(total);
                }
                Project_Size = et_projectSize.getText().toString();
                Budget = et_budget.getText().toString();

                populateProjectStatus();

                SharedPreferences prefs = getActivity().getSharedPreferences("SP_USER_DATA", MODE_PRIVATE);
                User_ID = prefs.getString("USER_ID", null);

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
                rb_UPVC = view.findViewById(radiobutton3);
                UPVC = rb_UPVC.getText().toString();

                int radiobutton4 = rg_budgetType.getCheckedRadioButtonId();
                rb_budgetType = view.findViewById(radiobutton4);
                Budget_Type = rb_budgetType.getText().toString();

                if (TextUtils.isEmpty(Project_Name)) {
                    et_projectName.setError(getString(R.string.cannot_empty));
                    et_projectName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Road_Name)) {
                    et_roadName.setError(getString(R.string.cannot_empty));
                    et_roadName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Road_Width)) {
                    et_roadWidth.setError(getString(R.string.cannot_empty));
                    et_roadWidth.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Address)) {
                    et_address.setError(getString(R.string.cannot_empty));
                    et_address.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Length)) {
                    et_length.setError(getString(R.string.cannot_empty));
                    et_length.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Breadth)) {
                    et_breadth.setError(getString(R.string.cannot_empty));
                    et_breadth.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Basement_Count)) {
                    et_basementCount.setError(getString(R.string.cannot_empty));
                    et_basementCount.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Floor_Count)) {
                    et_floorCount.setError(getString(R.string.cannot_empty));
                    et_floorCount.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Project_Size)) {
                    et_projectSize.setError(getString(R.string.cannot_empty));
                    et_projectSize.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Budget)) {
                    et_budget.setError(getString(R.string.cannot_empty));
                    et_budget.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(Latitude)) {
                    Toast.makeText(getContext(), "Please Select Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(imgString)) {
                    Toast.makeText(getContext(), "Please Select Project Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(projectStatus.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Project Status", Toast.LENGTH_SHORT).show();
                    return;
                }


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ProjectsFragment.ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(selfSigningClientBuilder.createClient(getContext()))
                        .build();

                APIKeys = retrofit.create(APIKeys.class);
                if(isNetWorkAvailable(type)){
                    showProgressDialog();
                    addProject();
                }
                else{
                    ShowAlert();
                }


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

    public void init(){
        imagePopup = new ImagePopup(getContext());

        cb_planning = view.findViewById(R.id.cb_planning);
        cb_digging = view.findViewById(R.id.cb_digging);
        cb_foundation = view.findViewById(R.id.cb_foundation);
        cb_pillars = view.findViewById(R.id.cb_pillars);
        cb_walls = view.findViewById(R.id.cb_walls);
        cb_roofing = view.findViewById(R.id.cb_roofing);
        cb_electrical = view.findViewById(R.id.cb_electrical);
        cb_plumbing = view.findViewById(R.id.cb_plumbing);
        cb_plastering = view.findViewById(R.id.cb_plastering);
        cb_flooring = view.findViewById(R.id.cb_flooring);
        cb_carpentry = view.findViewById(R.id.cb_carpentry);
        cb_paintings = view.findViewById(R.id.cb_paintings);
        cb_fixtures = view.findViewById(R.id.cb_fixtures);
        cb_completion = view.findViewById(R.id.cb_completion);
        cb_closed = view.findViewById(R.id.cb_closed);

        TickBoxFlow();

        linearLayout_parent = view.findViewById(R.id.parent_linear);
        ll_addroom = view.findViewById(R.id.LL_addroom);
        bt_location = view.findViewById(R.id.bt_location);
        bt_add = view.findViewById(R.id.bt_add_more);
        bt_delete = view.findViewById(R.id.bt_delete);
        bt_submit = view.findViewById(R.id.bt_submit);
        pro_images = view.findViewById(R.id.bt_projectimg_selectfile);
        pro_documents = view.findViewById(R.id.bt_gov_selectfile);
        et_projectName = view.findViewById(R.id.et_project_id);
        et_roadName = view.findViewById(R.id.et_roadname);
        et_roadWidth = view.findViewById(R.id.et_roadwidth);
        et_address = view.findViewById(R.id.et_address);
        et_length = view.findViewById(R.id.et_length);
        et_breadth = view.findViewById(R.id.et_breadth);
        et_basementCount = view.findViewById(R.id.et_basement_count);
        et_floorCount = view.findViewById(R.id.et_upperfloor_count);
        et_projectSize = view.findViewById(R.id.et_project_size);
        et_budget = view.findViewById(R.id.et_budget);
        rg_constructionType = view.findViewById(R.id.rg_constructiontype);
        rg_RMC = view.findViewById(R.id.rg_rmc);
        rg_loans = view.findViewById(R.id.rg_loan);
        rg_UPVC = view.findViewById(R.id.rg_upvc);
        rg_budgetType = view.findViewById(R.id.rg_budget_type);
        tv_total_floor_count = view.findViewById(R.id.tv_total_floor_count);
        tv_latitude = view.findViewById(R.id.tv_latitude);
        tv_logitude = view.findViewById(R.id.tv_logitude);
        tv_resetTick = view.findViewById(R.id.tv_resetTick);
        spinner_floor = view.findViewById(R.id.spin_floor);
        iv_project_imageselected = view.findViewById(R.id.iv_project_imageselected);
        iv_document_imageselected = view.findViewById(R.id.iv_document_imageselected);
    }



    public void addProject() {
        AddProjectRequest addProjectRequest = new AddProjectRequest();

        addProjectRequest.setProject_name(Project_Name);
        addProjectRequest.setRoad_name(Road_Name);
        addProjectRequest.setRoad_width(Road_Width);
        addProjectRequest.setAddress(Address);
        addProjectRequest.setConstruction_type(Construction_Type);
        addProjectRequest.setInterested_in_rmc(RMC);
        addProjectRequest.setInterested_in_loan(Loan);
        addProjectRequest.setInterested_in_doorsandwindows(UPVC);
        addProjectRequest.setMunicipality_approval(DocumentString);
        addProjectRequest.setProject_status(projectStatus);
        addProjectRequest.setLength(Length);
        addProjectRequest.setBreadth(Breadth);
        addProjectRequest.setProject_type(Project_Type);
        addProjectRequest.setProject_size(Project_Size);
        addProjectRequest.setBudgetType(Budget_Type);
        addProjectRequest.setBudget(Budget);
        addProjectRequest.setImage(imgString);
        addProjectRequest.setUserid(User_ID);
        addProjectRequest.setBasement(Basement_Count);
        addProjectRequest.setGround(Floor_Count);
        addProjectRequest.setLatitude(Latitude);
        addProjectRequest.setLongitude(Longitude);

        Call<AddProjectResponse> addProjectResponseCall = APIKeys.Addproject(addProjectRequest);

        addProjectResponseCall.enqueue(new Callback<AddProjectResponse>() {
            @Override
            public void onResponse(Call<AddProjectResponse> call, Response<AddProjectResponse> response) {
                int statusCode = response.code();

                AddProjectResponse addProjectResponse = response.body();
                String APIresponse = response.body().getMessage();
                Toast.makeText(getContext(), "on Success " + statusCode , Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "on Success " + APIresponse , Toast.LENGTH_LONG).show();
                SharedPreferences pref = getActivity().getSharedPreferences("SP_USER_PROJECT_LOCATION", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("LATITUDE", "");
                editor.putString("LONGITUDE", "");
                editor.apply();
                progressDialog.cancel();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<AddProjectResponse> call, Throwable t) {
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                if(isProjectImage){
                    iv_project_imageselected.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteFormat = stream.toByteArray();
                    // get the base 64 string
                    imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                    iv_project_imageselected.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePopup.setImageOnClickClose(true);
                            imagePopup.setFullScreen(true);
                            imagePopup.setHideCloseIcon(true);
                            imagePopup.initiatePopup(iv_project_imageselected.getDrawable());
                            imagePopup.viewPopup();
                        }
                    });
                }

                else{
                    iv_document_imageselected.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteFormat = stream.toByteArray();
                    // get the base 64 string
                    DocumentString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    iv_document_imageselected.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePopup.setImageOnClickClose(true);
                            imagePopup.setFullScreen(true);
                            imagePopup.setHideCloseIcon(true);
                            imagePopup.initiatePopup(iv_document_imageselected.getDrawable());
                            imagePopup.viewPopup();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void populateProjectStatus(){
        if(cb_planning.isChecked()){
            projectStatus.add(cb_planning.getText().toString());
        }
        if(cb_digging.isChecked()){
            projectStatus.add(cb_digging.getText().toString());
        }
        if(cb_foundation.isChecked()){
            projectStatus.add(cb_foundation.getText().toString());
        }
        if(cb_pillars.isChecked()){
            projectStatus.add(cb_pillars.getText().toString());
        }
        if(cb_walls.isChecked()){
            projectStatus.add(cb_walls.getText().toString());
        }
        if(cb_roofing.isChecked()){
            projectStatus.add(cb_roofing.getText().toString());
        }
        if(cb_electrical.isChecked()){
            projectStatus.add(cb_electrical.getText().toString());
        }
        if(cb_plumbing.isChecked()){
            projectStatus.add(cb_plumbing.getText().toString());
        }
        if(cb_plastering.isChecked()){
            projectStatus.add(cb_plastering.getText().toString());
        }
        if(cb_flooring.isChecked()){
            projectStatus.add(cb_flooring.getText().toString());
        }
        if(cb_carpentry.isChecked()){
            projectStatus.add(cb_carpentry.getText().toString());
        }
        if(cb_paintings.isChecked()){
            projectStatus.add(cb_paintings.getText().toString());
        }
        if(cb_fixtures.isChecked()){
            projectStatus.add(cb_fixtures.getText().toString());
        }
        if(cb_completion.isChecked()){
            projectStatus.add(cb_completion.getText().toString());
        }
        if(cb_closed.isChecked()){
            projectStatus.add(cb_closed.getText().toString());
        }
    }

    public void TickBoxFlow(){
        cb_planning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if (NoOfCheckboxTicked == 0) {
                    if (isChecked) {
                    /*cb_planning, cb_digging, cb_foundation, cb_pillars, cb_walls, cb_roofing,
                            cb_electrical, cb_plumbing, cb_plastering, cb_flooring, cb_carpentry, cb_paintings,
                            cb_fixtures, cb_completion, cb_closed;*/

                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    } else {
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }

                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_digging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if (NoOfCheckboxTicked == 0) {
                    if (isChecked) {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    } else {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_foundation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if (NoOfCheckboxTicked == 0) {
                    if (isChecked) {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    } else {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_pillars.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                    /*cb_walls.setChecked(false);
                    cb_walls.setVisibility(View.GONE);
                    cb_roofing.setChecked(false);
                    cb_roofing.setVisibility(View.GONE);*/
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_walls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                    /*cb_walls.setChecked(false);
                    cb_walls.setVisibility(View.GONE);
                    cb_roofing.setChecked(false);
                    cb_roofing.setVisibility(View.GONE);*/
                        /*cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);*/
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_roofing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                    /*cb_walls.setChecked(false);
                    cb_walls.setVisibility(View.GONE);
                    cb_roofing.setChecked(false);
                    cb_roofing.setVisibility(View.GONE);*/
                        /*cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);*/
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_electrical.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if (isChecked) {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        /*cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);*/
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        /*cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);*/
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        /*cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);*/
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    } else {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_plumbing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        /*cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);*/
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        /*cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);*/
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        /*cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);*/
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_plastering.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        /*cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);*/
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_flooring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        /*cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);*/
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        /*cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);*/
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });


        cb_carpentry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        /*cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);*/
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_paintings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        /*cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);*/
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        /*cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);*/
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_fixtures.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        /*cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);*/
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        /*cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);*/
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_completion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if(NoOfCheckboxTicked == 0){
                    if(isChecked){
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        /*cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);*/
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        /*cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);*/
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.GONE);
                    }
                    else{
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                        cb_closed.setChecked(false);
                        cb_closed.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });

        cb_closed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NoOfCheckboxTicked > 0 && !isChecked){
                    NoOfCheckboxTicked--;
                }
                if (NoOfCheckboxTicked == 0) {
                    if (isChecked) {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.GONE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.GONE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.GONE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.GONE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.GONE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.GONE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.GONE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.GONE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.GONE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.GONE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.GONE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.GONE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.GONE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.GONE);
                    } else {
                        cb_planning.setChecked(false);
                        cb_planning.setVisibility(View.VISIBLE);
                        cb_digging.setChecked(false);
                        cb_digging.setVisibility(View.VISIBLE);
                        cb_foundation.setChecked(false);
                        cb_foundation.setVisibility(View.VISIBLE);
                        cb_pillars.setChecked(false);
                        cb_pillars.setVisibility(View.VISIBLE);
                        cb_walls.setChecked(false);
                        cb_walls.setVisibility(View.VISIBLE);
                        cb_roofing.setChecked(false);
                        cb_roofing.setVisibility(View.VISIBLE);
                        cb_electrical.setChecked(false);
                        cb_electrical.setVisibility(View.VISIBLE);
                        cb_plumbing.setChecked(false);
                        cb_plumbing.setVisibility(View.VISIBLE);
                        cb_plastering.setChecked(false);
                        cb_plastering.setVisibility(View.VISIBLE);
                        cb_flooring.setChecked(false);
                        cb_flooring.setVisibility(View.VISIBLE);
                        cb_carpentry.setChecked(false);
                        cb_carpentry.setVisibility(View.VISIBLE);
                        cb_paintings.setChecked(false);
                        cb_paintings.setVisibility(View.VISIBLE);
                        cb_fixtures.setChecked(false);
                        cb_fixtures.setVisibility(View.VISIBLE);
                        cb_completion.setChecked(false);
                        cb_completion.setVisibility(View.VISIBLE);
                    }
                }
                if(isChecked){
                    NoOfCheckboxTicked++;
                }
                if(NoOfCheckboxTicked == 0){
                    tv_resetTick.setVisibility(View.GONE);
                }
                else{
                    tv_resetTick.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void ResetAllTickBox(){
        cb_planning.setChecked(false);
        cb_planning.setVisibility(View.VISIBLE);
        cb_digging.setChecked(false);
        cb_digging.setVisibility(View.VISIBLE);
        cb_foundation.setChecked(false);
        cb_foundation.setVisibility(View.VISIBLE);
        cb_pillars.setChecked(false);
        cb_pillars.setVisibility(View.VISIBLE);
        cb_walls.setChecked(false);
        cb_walls.setVisibility(View.VISIBLE);
        cb_roofing.setChecked(false);
        cb_roofing.setVisibility(View.VISIBLE);
        cb_electrical.setChecked(false);
        cb_electrical.setVisibility(View.VISIBLE);
        cb_plumbing.setChecked(false);
        cb_plumbing.setVisibility(View.VISIBLE);
        cb_plastering.setChecked(false);
        cb_plastering.setVisibility(View.VISIBLE);
        cb_flooring.setChecked(false);
        cb_flooring.setVisibility(View.VISIBLE);
        cb_carpentry.setChecked(false);
        cb_carpentry.setVisibility(View.VISIBLE);
        cb_paintings.setChecked(false);
        cb_paintings.setVisibility(View.VISIBLE);
        cb_fixtures.setChecked(false);
        cb_fixtures.setVisibility(View.VISIBLE);
        cb_completion.setChecked(false);
        cb_completion.setVisibility(View.VISIBLE);
        cb_closed.setChecked(false);
        cb_closed.setVisibility(View.VISIBLE);
    }

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
                            addProject();
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
