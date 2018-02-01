package com.geoverifi.geoverifi.server.model;

/**
 * Created by chriz on 8/31/2017.
 */

public class SubmissionAuditing {
    private String submission_date, brand, media_owner, town, other_comments,side;
    private Integer structure_id, media_size, media_size_other_height, media_size_other_width, material_type_id, run_up_id, illumination_type_id, visibility, angle, user_id,parentid,traffic_quantity, traffic_speed;
    private double latitude, longitude;

    public SubmissionAuditing(String submission_date, String brand, String media_owner, String town, String other_comments, Integer structure_id, Integer media_size, Integer media_size_other_height, Integer media_size_other_width, Integer material_type_id, Integer run_up_id, Integer illumination_type_id, Integer visibility, Integer angle, Integer latitude, Integer longitude, Integer user_id, String side, Integer parentid,Integer traffic_quantity,Integer traffic_speed) {
        this.submission_date = submission_date;
        this.brand = brand;
        this.media_owner = media_owner;
        this.town = town;
        this.other_comments = other_comments;
        this.structure_id = structure_id;
        this.media_size = media_size;
        this.media_size_other_height = media_size_other_height;
        this.media_size_other_width = media_size_other_width;
        this.material_type_id = material_type_id;
        this.run_up_id = run_up_id;
        this.illumination_type_id = illumination_type_id;
        this.visibility = visibility;
        this.angle = angle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
        this.side = side;
        this.parentid = parentid;
        this.traffic_quantity = traffic_quantity;
        this.traffic_speed = traffic_speed;



    }

    public SubmissionAuditing(){

    }

    public String getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(String submission_date) {
        this.submission_date = submission_date;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMedia_owner() {
        return media_owner;
    }

    public void setMedia_owner(String media_owner) {
        this.media_owner = media_owner;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getOther_comments() {
        return other_comments;
    }

    public void setOther_comments(String other_comments) {
        this.other_comments = other_comments;
    }

    public Integer getStructure_id() {
        return structure_id;
    }

    public void setStructure_id(Integer structure_id) {
        this.structure_id = structure_id;
    }

    public Integer getMedia_size() {
        return media_size;
    }

    public void setMedia_size(Integer media_size) {
        this.media_size = media_size;
    }

    public Integer getMedia_size_other_height() {
        return media_size_other_height;
    }

    public void setMedia_size_other_height(Integer media_size_other_height) {
        this.media_size_other_height = media_size_other_height;
    }

    public Integer getMedia_size_other_width() {
        return media_size_other_width;
    }

    public void setMedia_size_other_width(Integer media_size_other_width) {
        this.media_size_other_width = media_size_other_width;
    }

    public Integer getMaterial_type_id() {
        return material_type_id;
    }

    public void setMaterial_type_id(Integer material_type_id) {
        this.material_type_id = material_type_id;
    }

    public Integer getRun_up_id() {
        return run_up_id;
    }

    public void setRun_up_id(Integer run_up_id) {
        this.run_up_id = run_up_id;
    }

    public Integer getIllumination_type_id() {
        return illumination_type_id;
    }

    public void setIllumination_type_id(Integer illumination_type_id) {
        this.illumination_type_id = illumination_type_id;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getAngle() {
        return angle;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }


    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }


    public Integer getTraffic_quantity() {
        return traffic_quantity;
    }

    public void setTraffic_quantity(Integer traffic_quantity) {
        this.traffic_quantity = traffic_quantity;
    }


    public Integer getTraffic_speed() {
        return traffic_speed;
    }

    public void setTraffic_speed(Integer traffic_speed) {
        this.traffic_speed = traffic_speed;
    }
}
