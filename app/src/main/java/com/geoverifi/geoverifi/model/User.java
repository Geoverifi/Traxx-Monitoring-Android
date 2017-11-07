package com.geoverifi.geoverifi.model;

/**
 * Created by chriz on 6/14/2017.
 */

public class User {
    private int _id;
    private String _firstname, _lastname, _email, _phone, _photo, _user_type, _user_about;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_firstname() {
        return _firstname;
    }

    public void set_firstname(String _firstname) {
        this._firstname = _firstname;
    }

    public String get_lastname() {
        return _lastname;
    }

    public void set_lastname(String _lastname) {
        this._lastname = _lastname;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public String get_photo() {
        return _photo;
    }

    public void set_photo(String _photo) {
        this._photo = _photo;
    }

    public String get_user_type() {
        return _user_type;
    }

    public void set_user_type(String _user_type) {
        this._user_type = _user_type;
    }

    public String get_user_about() {
        return _user_about;
    }

    public void set_user_about(String _user_about) {
        this._user_about = _user_about;
    }
}
