package com.yh.parkingpartner.model;

public class Review {
    private int id;
    private String prk_plce_nm;
    private String prk_plce_adres;
    private String start_prk_at;
    private String end_prk;
    private String img_prk;
    private String prk_area;
    private String use_prk_at;
    private int end_pay;
    private int rating;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrk_plce_nm() {
        return prk_plce_nm;
    }

    public void setPrk_plce_nm(String prk_plce_nm) {
        this.prk_plce_nm = prk_plce_nm;
    }

    public String getPrk_plce_adres() {
        return prk_plce_adres;
    }

    public void setPrk_plce_adres(String prk_plce_adres) {
        this.prk_plce_adres = prk_plce_adres;
    }

    public String getStart_prk_at() {
        return start_prk_at;
    }

    public void setStart_prk_at(String start_prk_at) {
        this.start_prk_at = start_prk_at;
    }

    public String getEnd_prk() {
        return end_prk;
    }

    public void setEnd_prk(String end_prk) {
        this.end_prk = end_prk;
    }

    public String getImg_prk() {
        return img_prk;
    }

    public void setImg_prk(String img_prk) {
        this.img_prk = img_prk;
    }

    public String getPrk_area() {
        return prk_area;
    }

    public void setPrk_area(String prk_area) {
        this.prk_area = prk_area;
    }

    public String getUse_prk_at() {
        return use_prk_at;
    }

    public void setUse_prk_at(String use_prk_at) {
        this.use_prk_at = use_prk_at;
    }

    public int getEnd_pay() {
        return end_pay;
    }

    public void setEnd_pay(int end_pay) {
        this.end_pay = end_pay;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
