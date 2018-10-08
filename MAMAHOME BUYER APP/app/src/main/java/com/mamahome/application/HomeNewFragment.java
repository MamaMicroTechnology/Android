package com.mamahome.application;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mamahome.application.Products.BrandAdapter;
import com.mamahome.application.Products.Category;
import com.mamahome.application.Products.CategoryAdapter;
import com.mamahome.application.Products.ProductList;
import com.mamahome.application.Products.SubCategoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeNewFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    View view;
    Boolean destroy = false;
    HashMap<String, String> image_list;
    SliderLayout sliderLayout_home;
    APIKeys APIKeys;
    SwipeRefreshLayout swipeLayout;
    RecyclerView rv_category, rv_brand, rv_sub_category;
    CategoryAdapter categoryAdapter;
    BrandAdapter brandAdapter;
    SubCategoryAdapter subCategoryAdapter;
    private ArrayList<Category> categories;
    private ArrayList<com.mamahome.application.Products.Brand> brands, newBrands;
    String CategoryID;
    private ArrayList<com.mamahome.application.Products.SubCategory> subCategories, listSubCategory;
    TextView tv_brand, tv_product, tv_category;
    ProgressDialog progressDialog;
    int[] type = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
    ArrayList<Banner> banners;
    Banner banner;
    Dialog dialog;
    String Brand_Name, Category_Name;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    public HomeNewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_new, container, false);
        rv_category = view.findViewById(R.id.rv_category);
        rv_brand = view.findViewById(R.id.rv_brands);
        rv_sub_category = view.findViewById(R.id.rv_sub_category);
        tv_brand = view.findViewById(R.id.tv_brand);
        tv_product = view.findViewById(R.id.tv_product);
        tv_category = view.findViewById(R.id.tv_category);
        dialog = new Dialog(getContext());

            String text1 = "<center><b><font color='#f58220'>MAMA</font> " +
                "<font color='#388e3c'> HOME</font></b></center> <br>";

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml(text1));
        //((HomeActivity)getActivity()).getSupportActionBar().set
        ((HomeActivity)getActivity()).MarkHomeItemSelected(0);


        if(isNetWorkAvailable(type)){
            showProgressDialog();
            populateSpinners();
            getBanners();
        }
        else {
            ShowAlert();
        }

        return view;
    }


    private void getBanners(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProjectsFragment.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(selfSigningClientBuilder.createClient(getContext()))
                .build();

        APIKeys = retrofit.create(APIKeys.class);
        Call<BannerList> bannerCall = APIKeys.BANNER_LIST_CALL();

        bannerCall.enqueue(new Callback<BannerList>() {
            @Override
            public void onResponse(Call<BannerList> call, Response<BannerList> response) {

                BannerList newbanner = response.body();
                banners = new ArrayList<>(Arrays.asList(newbanner.getBanner()));
                progressDialog.cancel();
                getImages();
            }

            @Override
            public void onFailure(Call<BannerList> call, Throwable t) {
                Log.e("faliure message", t.getMessage());
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void ShowAlert(){
        new AlertDialog.Builder(getContext())
                .setTitle("Oops.. No Internet Connection")
                .setMessage("Please Connect To The Internet To Use Our Services! \nThank You.")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetWorkAvailable(type)){
                            showProgressDialog();
                            populateSpinners();
                            getImages();
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

    private void getImages() {
        sliderLayout_home = view.findViewById(R.id.home_slider);
        //image_list = new HashMap<>();

        /*fragments.add(FragmentSlider.newInstance("https://mamahome360.com/img/1920x1080/1.jpg"));
        fragments.add(FragmentSlider.newInstance("http://images.newindianexpress.com/uploads/user/imagelibrary/2018/4/8/w600X300/Ambiguity_constr.jpg"));
        fragments.add(FragmentSlider.newInstance("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSc2EPcUH9_1pgnl_DYZCGfOFFPnd8CHMI72fWOiZPFPCU2NOVaFA"));
        fragments.add(FragmentSlider.newInstance("https://theconstructor.org/wp-content/uploads/2016/09/density-of-construction-materials.jpg"));
        fragments.add(FragmentSlider.newInstance("http://afrilandproperties.com/wp-content/uploads/2015/11/Building-Materials-2-548x300.jpg"));
*/


        //image_list.put("Building_01", "https://mamahome360.com/img/1920x1080/1.jpg");
        for(int i=0; i<banners.size(); i++){
            banner = banners.get(i);
            /*GlideApp.with(getContext()).load("https://mamamicrotechnology.com/clients/MH/webapp/public/"+banner.getImage()
                    + banner.getImage().trim())
                    */
            //com.mamahome.application.BaseSliderView.initImageLoader(getContext());


            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .description(banner.getTitle())
                    .image("https://mamamicrotechnology.com/clients/MH/webapp/public/banner/"+banner.getImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("ID", banner.getId().toString());
            textSliderView.getBundle().putString("TITLE", banner.getTitle());
            textSliderView.getBundle().putString("INFO", banner.getInfo());
            textSliderView.getBundle().putString("IMG", banner.getImage());
            sliderLayout_home.addSlider(textSliderView);
            //image_list.put(banner.getTitle(), "http://mamamicrotechnology.com/clients/MH/webapp/public/"+banner.getImage());
        }

        /*image_list.put("Equipment_05", "http://afrilandproperties.com/wp-content/uploads/2015/11/Building-Materials-2-548x300.jpg");
        image_list.put("Steel_04", "https://theconstructor.org/wp-content/uploads/2016/09/density-of-construction-materials.jpg");
        image_list.put("Cement_03", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSc2EPcUH9_1pgnl_DYZCGfOFFPnd8CHMI72fWOiZPFPCU2NOVaFA");
        image_list.put("Building_02", "http://images.newindianexpress.com/uploads/user/imagelibrary/2018/4/8/w600X300/Ambiguity_constr.jpg");
*/



        /*for(String key:image_list.keySet()){
            *//*String[] keySplit = key.split("_");
            final String OfferName = keySplit[0];
            String id = keySplit[1];*//*

            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .description(OfferName)
                    .image(image_list.get(key))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Toast.makeText(getContext(), "Image Clicked - "+OfferName, Toast.LENGTH_SHORT).show();
                        }
                    });
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("Id", id);
            sliderLayout_home.addSlider(textSliderView);
        }*/

        sliderLayout_home.setPresetTransformer(SliderLayout.Transformer.CubeIn);
        sliderLayout_home.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        //sliderLayout_home.setCustomAnimation(new DescriptionAnimation());
        sliderLayout_home.setDuration(4000);
        sliderLayout_home.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(getContext(),slider.getBundle().get("INFO") + "",Toast.LENGTH_SHORT).show();
        DisplayBannerPopUp(slider.getBundle().get("TITLE").toString(),slider.getBundle().get("INFO").toString(),
                slider.getBundle().get("IMG").toString());
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

                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_from_right);
                categoryAdapter = new CategoryAdapter(getContext(), categories, HomeNewFragment.this, staggeredGridLayoutManager, rv_category);
                rv_category.setAdapter(categoryAdapter);
                progressDialog.cancel();
                tv_category.setVisibility(View.VISIBLE);
                tv_brand.setVisibility(View.GONE);

                rv_category.setLayoutAnimation(controller);
                rv_category.setLayoutManager(staggeredGridLayoutManager);
                //rv_category.findViewHolderForAdapterPosition(0).itemView.performClick();



                /*listCategory = new ArrayList<String>();

                for (int i = 0; i < categories.size(); i++){
                    listCategory.add(categories.get(i).getCategory_name());
                }

                CategoryAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, listCategory);
                CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin_Category.setAdapter(CategoryAdapter);*/
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                Toast.makeText(getContext(), "on Failure " + t.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
    }

    public void getBrands(String id){
        newBrands = new ArrayList<>();
        for (int i = 0; i < brands.size(); i++){
            if (id.equals(brands.get(i).getCategory_id())){
                newBrands.add(brands.get(i));
            }
        }
        if(newBrands.size()>0) {
            tv_brand.setVisibility(View.VISIBLE);
        }
        brandAdapter = new BrandAdapter(getContext(), newBrands, HomeNewFragment.this, id, rv_brand);
        rv_brand.setAdapter(brandAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_from_right);
        rv_brand.setLayoutAnimation(controller);
        rv_brand.setLayoutManager(staggeredGridLayoutManager);
        brandAdapter.notifyDataSetChanged();
        tv_product.setVisibility(View.GONE);
        rv_sub_category.setVisibility(View.GONE);
        rv_brand.requestFocus();
        //rv_brand.findViewHolderForAdapterPosition(0).itemView.performClick();

    }

    public void getSubCatgories(String CategoryID, String BrandID){
        listSubCategory = new ArrayList<>();
        for (int i = 0; i < subCategories.size(); i++){
            String SubBID = subCategories.get(i).getBrand_id();
            String SubCID = subCategories.get(i).getCategory_id();
            if (BrandID.equals(SubBID) && CategoryID.equals(SubCID)){
                listSubCategory.add(subCategories.get(i));
            }
        }
        if(listSubCategory.size()>0) {
            tv_product.setVisibility(View.VISIBLE);
            rv_sub_category.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i<categories.size(); i++){
            if(categories.get(i).getId().equals(CategoryID)) {
                Category_Name = categories.get(i).getCategory_name();
            }
        }
        for (int i = 0; i<brands.size(); i++){
            if(brands.get(i).getId().equals(BrandID)) {
                Brand_Name = brands.get(i).getBrand();
            }
        }
        subCategoryAdapter = new SubCategoryAdapter(getContext(), listSubCategory, HomeNewFragment.this, rv_sub_category, Category_Name,Brand_Name);
        rv_sub_category.setAdapter(subCategoryAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_from_right);
        rv_sub_category.setLayoutAnimation(controller);
        rv_sub_category.setLayoutManager(staggeredGridLayoutManager);
        subCategoryAdapter.notifyDataSetChanged();
        rv_sub_category.requestFocus();
        //rv_sub_category.findViewHolderForAdapterPosition(0).itemView.performClick();
    }

    public void DisplayPopUp(final String Category, final String Brand, final String Sub_Cat, String IMG, int row_index){
        rv_sub_category.scrollToPosition(row_index);

        ImageView iv_close, iv_image;
        String
        final Bundle bundle = new Bundle();
        TextView tv_category, tv_brand, tv_sub_category;
        Button bt_enquire;
        //Toast.makeText(getContext(), Category+", "+Brand+", "+Sub_Cat, Toast.LENGTH_LONG).show();
        dialog.setContentView(R.layout.custompopup);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        iv_close = dialog.findViewById(R.id.iv_close);
        iv_image = dialog.findViewById(R.id.iv_image);
        if(!TextUtils.isEmpty(IMG)) {
            Glide.with(getActivity()).load("http://mamamicrotechnology.com/clients/MH/webapp/public/subcat/"
                    + IMG).into(iv_image);

        }
        else{
            for (int i = 0; i < categories.size(); i++){
                String CategoryName = categories.get(i).getCategory_name();
                if (CategoryName.equals(Category)){
                    Glide.with(getActivity()).load("http://mamamicrotechnology.com/clients/MH/webapp/public/category/"
                            + categories.get(i).getCatimage()).into(iv_image);
                    break;
                }
            }
        }
        tv_category = dialog.findViewById(R.id.tv_category);
        tv_brand = dialog.findViewById(R.id.tv_brand);
        tv_sub_category = dialog.findViewById(R.id.tv_sub_category);
        bt_enquire = dialog.findViewById(R.id.bt_enquire);
        //iv_close.setImageDrawable(getResources().getDrawable(R.drawable.close));
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectProjectFragment selectProjectFragment = new SelectProjectFragment();
                bundle.putString("Category", Category);
                bundle.putString("Brand", Brand);
                bundle.putString("Sub_Cat", Sub_Cat);
                selectProjectFragment.setArguments(bundle);
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.home_container, selectProjectFragment, "SELECT_PROJECT_FRAGMENT");
                fragmentTransaction.addToBackStack("BS_SELECT_PROJECT");
                fragmentTransaction.commit();
            }
        });
        tv_category.setText("Category : "+Category);
        tv_brand.setText("Brand : "+Brand);
        tv_sub_category.setText("Product : "+Sub_Cat);
        dialog.setCancelable(false);
        dialog.show();


    }

    public void DisplayBannerPopUp(final String Title, final String Info, final String IMG){

        ImageView iv_close, iv_image;
        TextView tv_category, tv_brand, tv_sub_category;
        Button bt_enquire;
        //Toast.makeText(getContext(), Category+", "+Brand+", "+Sub_Cat, Toast.LENGTH_LONG).show();
        dialog.setContentView(R.layout.custompopup);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        iv_close = dialog.findViewById(R.id.iv_close);
        iv_image = dialog.findViewById(R.id.iv_image);
        Glide.with(getActivity()).load("http://mamamicrotechnology.com/clients/MH/webapp/public/banner/"
                    + IMG).into(iv_image);

        tv_category = dialog.findViewById(R.id.tv_category);
        tv_brand = dialog.findViewById(R.id.tv_brand);
        tv_sub_category = dialog.findViewById(R.id.tv_sub_category);
        bt_enquire = dialog.findViewById(R.id.bt_enquire);
        bt_enquire.setText("Enquire Offer");
        //iv_close.setImageDrawable(getResources().getDrawable(R.drawable.close));
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_brand.setTextSize(12);
        tv_sub_category.setTextSize(12);
        tv_category.setText("Title : "+Title);
        tv_brand.setText("Info : "+Info);
        tv_sub_category.setText("Enquire about this offer and you will soon receive a Call from a MamaHome Executive");
        dialog.setCancelable(false);
        dialog.show();


    }


    @Override
    public void onStop() {
        super.onStop();
        sliderLayout_home.stopAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).MarkHomeItemSelected(0);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    getActivity().finish();
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.app_name);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setMessage("Do You Want To Exit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                                            R.anim.slide_out_left, R.anim.slide_in_left,
                                            R.anim.slide_out_right);
                                    fragmentTransaction.replace(R.id.home_container, new HomeNewFragment(), "HOME_FRAGMENT");
                                    fragmentTransaction.addToBackStack("BS_HOME");
                                    fragmentTransaction.commit();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();*/
                }
                return false;
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
