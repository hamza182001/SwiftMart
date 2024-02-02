package com.example.madgrocery_project;

public class Product {
    String name,url,price,count;
    private boolean isFavorite;
    public Product() {
    }

    public Product(String name, String url, String price, String count) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.count = count;

    }


    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    public void increaseCount() {
        int currentCount = Integer.parseInt(count);
        currentCount++;
        count = String.valueOf(currentCount);
    }

    public void decreaseCount() {
        int currentCount = Integer.parseInt(count);
        if (currentCount > 0) {
            currentCount--;
            count = String.valueOf(currentCount);
        }
    }



    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
