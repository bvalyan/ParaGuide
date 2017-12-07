package com.optimalotaku.paraguide;

/**
 * Created by bvalyan on 12/7/17.
 */

class AffinityObject {
    private String title;
    private int image;
    private int color;

   public  AffinityObject(String title, int image, int color){
        this.title = title;
        this.image = image;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
