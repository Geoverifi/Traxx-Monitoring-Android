package com.geoverifi.geoverifi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chriz on 7/17/2017.
 */

public class Submission implements Parcelable {

    private int _id, _user_id, _status;
    private String _brand, _media_owner, _town, _submission_date, _created_at, _structure, _size, _user_firstname, _user_lastname, _media_size_other_height, _media_size_other_width, _material,
    _run_up, _illumination, _angle, _other_comments, _visibility, _latitude, _longitude, _photo_1, _photo_2, _site_id, _site_reference_number, _media_owner_name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_user_id() {
        return _user_id;
    }

    public void set_user_id(int _user_id) {
        this._user_id = _user_id;
    }

    public String get_brand() {
        return _brand;
    }

    public void set_brand(String _brand) {
        this._brand = _brand;
    }

    public String get_media_owner() {
        return _media_owner;
    }

    public void set_media_owner(String _media_owner) {
        this._media_owner = _media_owner;
    }

    public String get_town() {
        return _town;
    }

    public void set_town(String _town) {
        this._town = _town;
    }

    public String get_submission_date() {
        return _submission_date;
    }

    public void set_submission_date(String _submission_date) {
        this._submission_date = _submission_date;
    }

    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String _created_at) {
        this._created_at = _created_at;
    }

    public String get_structure() {
        return _structure;
    }

    public void set_structure(String _structure) {
        this._structure = _structure;
    }

    public String get_size() {
        return _size;
    }

    public void set_size(String _size) {
        this._size = _size;
    }

    public String get_user_firstname() {
        return _user_firstname;
    }

    public void set_user_firstname(String _user_firstname) {
        this._user_firstname = _user_firstname;
    }

    public String get_user_lastname() {
        return _user_lastname;
    }

    public void set_user_lastname(String _user_lastname) {
        this._user_lastname = _user_lastname;
    }

    public String get_media_size_other_height() {
        return _media_size_other_height;
    }

    public void set_media_size_other_height(String _media_size_other_height) {
        this._media_size_other_height = _media_size_other_height;
    }

    public String get_media_size_other_width() {
        return _media_size_other_width;
    }

    public void set_media_size_other_width(String _media_size_other_width) {
        this._media_size_other_width = _media_size_other_width;
    }

    public String get_material() {
        return _material;
    }

    public void set_material(String _material) {
        this._material = _material;
    }

    public String get_run_up() {
        return _run_up;
    }

    public void set_run_up(String _run_up) {
        this._run_up = _run_up;
    }

    public String get_illumination() {
        return _illumination;
    }

    public void set_illumination(String _illumination) {
        this._illumination = _illumination;
    }

    public String get_angle() {
        return _angle;
    }

    public void set_angle(String _angle) {
        this._angle = _angle;
    }

    public String get_other_comments() {
        return _other_comments;
    }

    public void set_other_comments(String _other_comments) {
        this._other_comments = _other_comments;
    }

    public String get_visibility() {
        return _visibility;
    }

    public void set_visibility(String _visibility) {
        this._visibility = _visibility;
    }

    public String get_latitude() {
        return _latitude;
    }

    public void set_latitude(String _latitude) {
        this._latitude = _latitude;
    }

    public String get_longitude() {
        return _longitude;
    }

    public void set_longitude(String _longitude) {
        this._longitude = _longitude;
    }

    public String get_photo_1() {
        return _photo_1;
    }

    public void set_photo_1(String _photo_1) {
        this._photo_1 = _photo_1;
    }

    public String get_photo_2() {
        return _photo_2;
    }

    public void set_photo_2(String _photo_2) {
        this._photo_2 = _photo_2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public int get_status() {
        return _status;
    }

    public void set_status(int _status) {
        this._status = _status;
    }

    public String get_site_reference_number() {
        return _site_reference_number;
    }

    public void set_site_reference_number(String _site_reference_number) {
        this._site_reference_number = _site_reference_number;
    }

    public String get_site_id() {
        return _site_id;
    }

    public void set_site_id(String _site_id) {
        this._site_id = _site_id;
    }

    public String get_media_owner_name() {
        return _media_owner_name;
    }

    public void set_media_owner_name(String _media_owner_name) {
        this._media_owner_name = _media_owner_name;
    }
}
