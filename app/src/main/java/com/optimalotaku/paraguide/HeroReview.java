package com.optimalotaku.paraguide;

import java.io.Serializable;

/**
 * Created by Brandon on 7/20/17.
 */

public class HeroReview implements Serializable {

    int score;
    String text;
    String pic;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
