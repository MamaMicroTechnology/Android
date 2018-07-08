package com.mamahome.application.Products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {

    @SerializedName("category")
    @Expose
    private Category[] categories;

    @SerializedName("brand")
    @Expose
    private Brand[] brands;

    @SerializedName("sub_cat")
    @Expose
    private SubCategory[] subCategories;

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public Brand[] getBrands() {
        return brands;
    }

    public void setBrands(Brand[] brands) {
        this.brands = brands;
    }

    public SubCategory[] getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(SubCategory[] subCategories) {
        this.subCategories = subCategories;
    }
}
