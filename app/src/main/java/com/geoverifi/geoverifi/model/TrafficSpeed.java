package com.geoverifi.geoverifi.model;

/**
 * Created by chriz on 7/10/2017.
 */

public class TrafficSpeed {
    private int _id;
    private String _traffic_speed;
    private String _weight;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_traffic_speed() {
        return _traffic_speed;
    }

    public void set_traffic_speed(String _traffic_speed) {
        this._traffic_speed = _traffic_speed;
    }

    public  String get_weight(){ return _weight;}

    public  void  set_weight(String _weight){this._weight=_weight;}


}
