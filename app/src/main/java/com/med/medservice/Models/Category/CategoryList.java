package com.med.medservice.Models.Category;

import java.io.Serializable;

public class CategoryList implements Serializable {

    String category_id, category_name, category_type, category_description, category_image;


    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_type() {
        return category_type;
    }

    public String getCategory_description() {
        return category_description;
    }

    public String getCategory_image() {
        return category_image;
    }

    public CategoryList() {
    }

    public CategoryList(String category_id, String category_name, String category_type, String category_description, String category_image) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_type = category_type;
        this.category_description = category_description;
        this.category_image = category_image;
    }
}
