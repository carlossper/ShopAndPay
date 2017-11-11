package com.feup.ei12078.shopandpay.data_classes;

/**
 * Created by andremachado on 11/11/2017.
 */

public class Category {
    private String id;
    private String name;

    public Category(String id, String name) {
        this.id=id;
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
