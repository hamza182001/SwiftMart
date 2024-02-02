package com.example.madgrocery_project;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Category  {
    String name,price,description,url,count;

    public Category() {
    }

    public Category(String name, String price, String description, String url, String count) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.url = url;
        this.count = count;
    }
    public void increaseCount(){
        int currentCount = Integer.parseInt(count);
        currentCount++;
        count = String.valueOf(currentCount);
    }
    public void decreaseCount(){
        int currentCount = Integer.parseInt(count);
        if (currentCount > 0) {
            currentCount--;
            count = String.valueOf(currentCount);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
