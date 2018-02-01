package com.geoverifi.geoverifi.model;

/**
 * Created by chriz on 7/10/2017.
 */

public class TrafficQuantity {
    private int _id;
    private String _traffic_quantity;
    private String _weight;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_traffic_quantity() {
        return _traffic_quantity;
    }

    public void set_traffic_quantity(String _traffic_quantity) {
        this._traffic_quantity = _traffic_quantity;
    }

    public String get_weight() {
        return _weight;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }
}
