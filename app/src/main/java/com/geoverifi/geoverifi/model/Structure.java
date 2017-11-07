package com.geoverifi.geoverifi.model;

/**
 * Created by chriz on 7/10/2017.
 */

public class Structure {
    private int _type_id, _type_photos, _type_sides;
    private String _type_name, _material_type;

    public int get_type_id() {
        return _type_id;
    }

    public void set_type_id(int _type_id) {
        this._type_id = _type_id;
    }

    public String get_type_name() {
        return _type_name;
    }

    public void set_type_name(String _type_name) {
        this._type_name = _type_name;
    }

    public int get_type_photos() {
        return _type_photos;
    }

    public void set_type_photos(int _type_photos) {
        this._type_photos = _type_photos;
    }

    public int get_type_sides() {
        return _type_sides;
    }

    public void set_type_sides(int _type_sides) {
        this._type_sides = _type_sides;
    }

    public String get_material_type() {
        return _material_type;
    }

    public void set_material_type(String _material_type) {
        this._material_type = _material_type;
    }
}
