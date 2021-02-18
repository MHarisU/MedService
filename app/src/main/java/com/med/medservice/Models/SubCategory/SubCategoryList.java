package com.med.medservice.Models.SubCategory;

import java.io.Serializable;

public class SubCategoryList implements Serializable {

    String sub_category_id, sub_category_name, parent_category_id, category_description, category_image;


    public String getSub_category_id() {
        return sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public String getParent_category_id() {
        return parent_category_id;
    }

    public String getCategory_description() {
        return category_description;
    }

    public String getCategory_image() {
        return category_image;
    }

    public SubCategoryList() {
    }

    public SubCategoryList(String sub_category_id, String sub_category_name, String category_description, String parent_category_id, String category_image) {
        this.sub_category_id = sub_category_id;
        this.sub_category_name = sub_category_name;
        this.parent_category_id = parent_category_id;
        this.category_description = category_description;
        this.category_image = category_image;
    }
}
