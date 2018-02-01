package com.geoverifi.geoverifi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.geoverifi.geoverifi.model.Angle;
import com.geoverifi.geoverifi.model.Illumination;
import com.geoverifi.geoverifi.model.Material;
import com.geoverifi.geoverifi.model.MaterialStatus;
import com.geoverifi.geoverifi.model.MediaOwner;
import com.geoverifi.geoverifi.model.Menu;
import com.geoverifi.geoverifi.model.RunUp;
import com.geoverifi.geoverifi.model.Size;
import com.geoverifi.geoverifi.model.Structure;
import com.geoverifi.geoverifi.model.StructureCondition;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.model.SubmissionPhoto;
import com.geoverifi.geoverifi.model.SubmissionPhotoAuditing;
import com.geoverifi.geoverifi.model.SubmissionAuditing;
import com.geoverifi.geoverifi.model.Submitter;
import com.geoverifi.geoverifi.model.TrafficQuantity;
import com.geoverifi.geoverifi.model.TrafficSpeed;
import com.geoverifi.geoverifi.model.Visibility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chriz on 7/9/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 16;

    private static final String DATABASE_NAME = "geoverifi";

    // Menu table
    public static final String TABLE_MAINMENU = "mainmenu";
    public static final String TABLE_UTRAXXMENU = "utraxxmenu";
    public static final String TABLE_ITRAXXMENU = "itraxxmenu";

    public static final String KEY_ID = "id"; // Common column
    public static final String KEY_MENU_TITLE = "menu_title";
    public static final String KEY_MENU_ICON = "menu_icon";
    public static final String KEY_MENU_SLUG = "menu_slug";
    public static final String KEY_MENU_USERS = "menu_users";

    public static final String TABLE_OFFLINE = "offline";

    public static final String TABLE_OFFLINEAUDITING = "offlineauditing";

    public static final String KEY_STATUS = "status";

    // Submission Table
    public static final String TABLE_SUBMISSION = "submission";
    public static final String TABLE_AUDITING = "auditing";

    public static final String KEY_SUBMISSION_DATE = "submission_date";
    public static final String KEY_BRAND = "brand";
    public static final String KEY_SITE_ID = "site_id";
    public static final String KEY_SITE_REFERENCE = "site_reference_number";
    public static final String KEY_MEDIA_OWNER_ID = "media_owner";
    public static final String KEY_TOWN = "town";
    public static final String KEY_STRUCTURE_ID = "structure_id";
    public static final String KEY_MEDIA_SIZE_ID = "media_size_id";
    public static final String KEY_MEDIA_SIZE_HEIGHT = "media_size_height";
    public static final String KEY_MEDIA_SIZE_WIDTH = "media_size_width";
    public static final String KEY_MATERIAL_TYPE_ID = "material_type_id";
    public static final String KEY_RUN_UP_ID = "run_up_id";
    public static final String KEY_ILLUMINATION_ID = "illumination_id";
    public static final String KEY_VISIBILITY_ID = "visinility_id";
    public static final String KEY_ANGLE_ID = "angle_id";
    public static final String KEY_OTHER_COMMENTS = "other_comments";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_FIRSTNAME = "user_firstname";
    public static final String KEY_USER_LASTNAME = "user_lastname";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PHOTO_1 = "photo_1";
    public static final String KEY_PHOTO_2 = "photo_2";
    public static final String KEY_SIDE = "side";
    public static final String KEY_PARENTID = "parentid";

    public static final String KEY_CREATED_AT = "created_at";

    // Media Owners Table
    public static final String TABLE_MEDIA_OWNER = "media_owner";
    public static final String KEY_MEDIA_OWNER = "media_owner_name";

    // Angle Table
    public static final String TABLE_ANGLE = "angle";
    public static final String KEY_ANGLE = "angle";


    // Traffic Speed Table
    public static final String TABLE_TRAFFIC_SPEED = "traffic_speed";
    public static final String KEY_TRAFFICSPEED = "traffic_speed";


    // Traffic Quantity Table
    public static final String TABLE_TRAFFIC_QUANTITY = "traffic_quantity";
    public static final String KEY_TRAFFICQUANTITY = "traffic_quantity";

    // Illumination Table
    public static final String TABLE_ILLUMINATION = "illumination";
    public static final String KEY_ILLUMINATION_TYPE = "illumination_type";

    // Material Table
    public static final String TABLE_MATERIAL = "material";
    public static final String KEY_MATERIAL_TYPE = "material_type";

    // Material Status Table
    public static final String TABLE_MATERIAL_STATUS = "material_status";
    public static final String KEY_MATERIAL_STATUS_TYPE = "material_status_type";

    // Run Up Table
    public static final String TABLE_RUN_UP = "run_up";
    public static final String KEY_RUN_UP = "run_up";

    // Size Table
    public static final String TABLE_SIZES = "sizes";
    public static final String KEY_SIZE = "size";

    // Structure Table
    public static final String TABLE_STRUCTURE = "structure";
    public static final String KEY_TYPE_ID = "type_id";
    public static final String KEY_STRUCTURE_TYPE = "type_name";
    public static final String KEY_TYPE_PHOTOS = "type_photos";
    public static final String KEY_MEDIA_TYPE = "type_media_type";
    public static final String KEY_MEDIA_SIDES = "type_sides";

    // Structure Condition Table
    public static final String TABLE_STRUCTURE_CONDITION = "structure_condition";
    public static final String KEY_STRUCTURE_CONDITION = "condition";

    // Visibility Types Table
    public static final String TABLE_VISIBILITY = "visibility";
    public static final String KEY_VISIBILITY_TYPE = "visibility_type";

    public static final String TABLE_SUBMISSION_PHOTOS = "submission_photos";


    public static final String TABLE_AUDITING_PHOTOS = "auditing_photos";


    public static final String KEY_PHOTO_URL = "photo_path";
    public static final String KEY_SUBMISSION_ID = "submission_id";
    public static final String KEY_THUMB = "photo_thumb";

    public static final String TABLE_PHOTOS = "photos";
    public static final String TABLE_AUDITPHOTOS = "auditphotos";
    public static final String KEY_PHOTO_NAME = "photo_name";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAINMENU_TABLE = "CREATE TABLE " + TABLE_MAINMENU + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MENU_TITLE + " TEXT,"
                + KEY_MENU_ICON + " TEXT,"
                + KEY_MENU_SLUG + " TEXT,"
                + KEY_MENU_USERS + " TEXT"
                + ");";
        String CREATE_UTRAXXMENU_TABLE = "CREATE TABLE " + TABLE_UTRAXXMENU + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MENU_TITLE + " TEXT,"
                + KEY_MENU_ICON + " TEXT,"
                + KEY_MENU_SLUG + " TEXT,"
                + KEY_MENU_USERS + " TEXT"
                + ");";

        String CREATE_ITRAXXMENU_TABLE = "CREATE TABLE " + TABLE_ITRAXXMENU + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_MENU_TITLE + " TEXT,"
                + KEY_MENU_ICON + " TEXT,"
                + KEY_MENU_SLUG + " TEXT,"
                + KEY_MENU_USERS + " TEXT"
                + ");";


        String CREATE_MEDIA_OWNER_TABLE = "CREATE TABLE " + TABLE_MEDIA_OWNER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MEDIA_OWNER + " TEXT"
                + ");";

        String CREATE_ANGLE_TABLE = "CREATE TABLE " + TABLE_ANGLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ANGLE + " TEXT"
                + ");";

        String CREATE_TRAFFIC_QUANTITY_TABLE = "CREATE TABLE " + TABLE_TRAFFIC_QUANTITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TRAFFICQUANTITY + " TEXT"
                + ");";

        String CREATE_TRAFFIC_SPEED_TABLE = "CREATE TABLE " + TABLE_TRAFFIC_SPEED + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TRAFFICSPEED + " TEXT"
                + ");";

        String CREATE_ILLUMINATION_TABLE = "CREATE TABLE " + TABLE_ILLUMINATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ILLUMINATION_TYPE + " TEXT"
                + ");";

        String CREATE_MATERIAL_TABLE = "CREATE TABLE " + TABLE_MATERIAL + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MATERIAL_TYPE + " TEXT"
                + ");";

        String CREATE_MATERIAL_STATUS_TABLE = "CREATE TABLE " + TABLE_MATERIAL_STATUS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MATERIAL_STATUS_TYPE + " TEXT"
                + ");";

        String CREATE_RUN_UP_TABLE = "CREATE TABLE " + TABLE_RUN_UP + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_RUN_UP + " TEXT"
                + ");";

        String CREATE_SIZES_TABLE = "CREATE TABLE " + TABLE_SIZES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SIZE + " TEXT"
                + ");";

        String CREATE_STRUCTURE_TYPES_TABLE = "CREATE TABLE " + TABLE_STRUCTURE + "("
                + KEY_TYPE_ID + " INTEGER PRIMARY KEY,"
                + KEY_STRUCTURE_TYPE + " TEXT,"
                + KEY_TYPE_PHOTOS + " INTEGER,"
                + KEY_MEDIA_TYPE + " TEXT,"
                + KEY_MEDIA_SIDES + " INTEGER"
                + ");";

        String CREATE_STRUCTURE_CONDITION_TABLE = "CREATE TABLE " + TABLE_STRUCTURE_CONDITION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_STRUCTURE_CONDITION + " TEXT"
                + ");";

        String CREATE_VISIBILITY_TABLE = "CREATE TABLE " + TABLE_VISIBILITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VISIBILITY_TYPE + " TEXT"
                + ");";


        String CREATE_AUDITING_TABLE = "CREATE TABLE "+ TABLE_AUDITING +" ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SITE_ID + " TEXT,"
                + KEY_SUBMISSION_DATE + " TEXT,"
                + KEY_SITE_REFERENCE + " TEXT,"
                + KEY_BRAND + " TEXT,"
                + KEY_MEDIA_OWNER_ID + " TEXT,"
                + KEY_TOWN + " TEXT,"
                + KEY_STRUCTURE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_HEIGHT + " TEXT,"
                + KEY_MEDIA_SIZE_WIDTH + " TEXT,"
                + KEY_MATERIAL_TYPE_ID + " TEXT,"
                + KEY_RUN_UP_ID + " TEXT,"
                + KEY_ILLUMINATION_ID + " TEXT,"
                + KEY_VISIBILITY_ID + " TEXT,"
                + KEY_ANGLE_ID + " TEXT,"
                + KEY_TRAFFICSPEED + " TEXT,"
                + KEY_TRAFFICQUANTITY + " TEXT,"
                + KEY_OTHER_COMMENTS + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_USER_FIRSTNAME + " TEXT,"
                + KEY_USER_LASTNAME + " TEXT,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_PHOTO_1 + " TEXT,"
                + KEY_PHOTO_2 + " TEXT,"
                + KEY_SIDE + " TEXT,"
                + KEY_PARENTID  + " INTEGER,"
                + KEY_CREATED_AT + " TEXT"
                + ");";


        String CREATE_SUBMISSION_TABLE = "CREATE TABLE " + TABLE_SUBMISSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SITE_ID + " TEXT,"
                + KEY_SUBMISSION_DATE + " TEXT,"
                + KEY_SITE_REFERENCE + " TEXT,"
                + KEY_BRAND + " TEXT,"
                + KEY_MEDIA_OWNER_ID + " TEXT,"
                + KEY_TOWN + " TEXT,"
                + KEY_STRUCTURE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_HEIGHT + " TEXT,"
                + KEY_MEDIA_SIZE_WIDTH + " TEXT,"
                + KEY_MATERIAL_TYPE_ID + " TEXT,"
                + KEY_RUN_UP_ID + " TEXT,"
                + KEY_ILLUMINATION_ID + " TEXT,"
                + KEY_VISIBILITY_ID + " TEXT,"
                + KEY_ANGLE_ID + " TEXT,"
                + KEY_OTHER_COMMENTS + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_USER_FIRSTNAME + " TEXT,"
                + KEY_USER_LASTNAME + " TEXT,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_PHOTO_1 + " TEXT,"
                + KEY_PHOTO_2 + " TEXT,"
                + KEY_SIDE + " TEXT,"
                + KEY_PARENTID  + " INTEGER,"
                + KEY_CREATED_AT + " TEXT"
                + ");";

        String CREATE_OFFLINE_TABLE = "CREATE TABLE " + TABLE_OFFLINE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_DATE + " TEXT,"
                + KEY_SITE_REFERENCE + " TEXT,"
                + KEY_BRAND + " TEXT,"
                + KEY_MEDIA_OWNER_ID + " TEXT,"
                + KEY_MEDIA_OWNER + " TEXT,"
                + KEY_TOWN + " TEXT,"
                + KEY_STRUCTURE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_HEIGHT + " TEXT,"
                + KEY_MEDIA_SIZE_WIDTH + " TEXT,"
                + KEY_MATERIAL_TYPE_ID + " TEXT,"
                + KEY_RUN_UP_ID + " TEXT,"
                + KEY_ILLUMINATION_ID + " TEXT,"
                + KEY_VISIBILITY_ID + " TEXT,"
                + KEY_ANGLE_ID + " TEXT,"
                + KEY_OTHER_COMMENTS + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_PHOTO_1 + " TEXT,"
                + KEY_PHOTO_2 + " TEXT,"
                + KEY_SIDE + " TEXT,"
                + KEY_PARENTID  + " INTEGER,"
                + KEY_STATUS + " INTEGER"
                + ");";


        String CREATE_OFFLINEAUDITING_TABLE = "CREATE TABLE " + TABLE_OFFLINEAUDITING + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_DATE + " TEXT,"
                + KEY_SITE_REFERENCE + " TEXT,"
                + KEY_BRAND + " TEXT,"
                + KEY_MEDIA_OWNER_ID + " TEXT,"
                + KEY_MEDIA_OWNER + " TEXT,"
                + KEY_TOWN + " TEXT,"
                + KEY_STRUCTURE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_ID + " TEXT,"
                + KEY_MEDIA_SIZE_HEIGHT + " TEXT,"
                + KEY_MEDIA_SIZE_WIDTH + " TEXT,"
                + KEY_MATERIAL_TYPE_ID + " TEXT,"
                + KEY_RUN_UP_ID + " TEXT,"
                + KEY_ILLUMINATION_ID + " TEXT,"
                + KEY_VISIBILITY_ID + " TEXT,"
                + KEY_ANGLE_ID + " TEXT,"
                + KEY_TRAFFICSPEED + " TEXT,"
                + KEY_TRAFFICQUANTITY + " TEXT,"
                + KEY_OTHER_COMMENTS + " TEXT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_PHOTO_1 + " TEXT,"
                + KEY_PHOTO_2 + " TEXT,"
                + KEY_SIDE + " TEXT,"
                + KEY_PARENTID  + " INTEGER,"
                + KEY_STATUS + " INTEGER"
                + ");";
        String CREATE_SUBMISSION_PHOTOS_TABLE = "CREATE TABLE " + TABLE_SUBMISSION_PHOTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_ID + " INTEGER,"
                + KEY_THUMB + " TEXT,"
                + KEY_PHOTO_URL + " TEXT"
                + ");";

        String CREATE_AUDITING_PHOTOS_TABLE = "CREATE TABLE " + TABLE_AUDITING_PHOTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_ID + " INTEGER,"
                + KEY_THUMB + " TEXT,"
                + KEY_PHOTO_URL + " TEXT"
                + ");";




        String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_ID + " INTEGER,"
                + KEY_PHOTO_NAME + " TEXT,"
                + KEY_THUMB + " TEXT,"
                + KEY_PHOTO_URL + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ");";

        String CREATE_AUDITPHOTOS_TABLE = "CREATE TABLE " + TABLE_AUDITPHOTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SUBMISSION_ID + " INTEGER,"
                + KEY_PHOTO_NAME + " TEXT,"
                + KEY_THUMB + " TEXT,"
                + KEY_PHOTO_URL + " TEXT,"
                + KEY_CREATED_AT + " TEXT"
                + ");";

        db.execSQL(CREATE_MAINMENU_TABLE);
        db.execSQL(CREATE_UTRAXXMENU_TABLE);
        db.execSQL(CREATE_ITRAXXMENU_TABLE);
        db.execSQL(CREATE_ANGLE_TABLE);
        db.execSQL(CREATE_TRAFFIC_QUANTITY_TABLE);
        db.execSQL(CREATE_TRAFFIC_SPEED_TABLE);
        db.execSQL(CREATE_ILLUMINATION_TABLE);
        db.execSQL(CREATE_MATERIAL_STATUS_TABLE);
        db.execSQL(CREATE_MATERIAL_TABLE);
        db.execSQL(CREATE_MEDIA_OWNER_TABLE);
        db.execSQL(CREATE_RUN_UP_TABLE);
        db.execSQL(CREATE_SIZES_TABLE);
        db.execSQL(CREATE_STRUCTURE_CONDITION_TABLE);
        db.execSQL(CREATE_STRUCTURE_TYPES_TABLE);
        db.execSQL(CREATE_VISIBILITY_TABLE);
        db.execSQL(CREATE_SUBMISSION_TABLE);
        db.execSQL(CREATE_AUDITING_TABLE);
        db.execSQL(CREATE_OFFLINE_TABLE);
        db.execSQL(CREATE_OFFLINEAUDITING_TABLE);
        db.execSQL(CREATE_SUBMISSION_PHOTOS_TABLE);
        db.execSQL(CREATE_AUDITING_PHOTOS_TABLE);
        db.execSQL(CREATE_PHOTOS_TABLE);
        db.execSQL(CREATE_AUDITPHOTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINMENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTRAXXMENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITRAXXMENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANGLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAFFIC_SPEED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAFFIC_QUANTITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIBILITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRUCTURE_CONDITION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIZES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRUCTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ILLUMINATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_OWNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUN_UP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBMISSION_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITING_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFLINEAUDITING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDITPHOTOS);
        onCreate(db);
    }

    public void clearDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MAINMENU);
        db.execSQL("DELETE FROM " + TABLE_UTRAXXMENU);
        db.execSQL("DELETE FROM " + TABLE_ITRAXXMENU);
        db.execSQL("DELETE FROM " + TABLE_ANGLE);
        db.execSQL("DELETE FROM " + TABLE_TRAFFIC_QUANTITY);
        db.execSQL("DELETE FROM " + TABLE_TRAFFIC_SPEED);
        db.execSQL("DELETE FROM " + TABLE_VISIBILITY);
        db.execSQL("DELETE FROM " + TABLE_STRUCTURE_CONDITION);
        db.execSQL("DELETE FROM " + TABLE_SIZES);
        db.execSQL("DELETE FROM " + TABLE_STRUCTURE);
        db.execSQL("DELETE FROM " + TABLE_ILLUMINATION);
        db.execSQL("DELETE FROM " + TABLE_MATERIAL);
        db.execSQL("DELETE FROM " + TABLE_MATERIAL_STATUS);
        db.execSQL("DELETE FROM " + TABLE_MEDIA_OWNER);
        db.execSQL("DELETE FROM " + TABLE_RUN_UP);
        db.execSQL("DELETE FROM " + TABLE_SUBMISSION);
        db.execSQL("DELETE FROM " + TABLE_OFFLINEAUDITING);
        db.execSQL("DELETE FROM " + TABLE_AUDITING);
        db.execSQL("DELETE FROM " + TABLE_PHOTOS);
        db.execSQL("DELETE FROM " + TABLE_AUDITPHOTOS);
    }

    public void addMainMenu(Menu menu){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENU_TITLE, menu.get_menu_title());
        values.put(KEY_MENU_ICON, menu.get_menu_icon());
        values.put(KEY_MENU_SLUG, menu.get_menu_slug());
        values.put(KEY_MENU_USERS, menu.get_menu_users());

        db.insert(TABLE_MAINMENU, null, values);
        db.close();
    }

    public void addItraxxMenu(Menu menu){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENU_TITLE, menu.get_menu_title());
        values.put(KEY_MENU_ICON, menu.get_menu_icon());
        values.put(KEY_MENU_SLUG, menu.get_menu_slug());
        values.put(KEY_MENU_USERS, menu.get_menu_users());

        db.insert(TABLE_ITRAXXMENU, null, values);
        db.close();
    }
    public void addUtraxxMenu(Menu menu){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENU_TITLE, menu.get_menu_title());
        values.put(KEY_MENU_ICON, menu.get_menu_icon());
        values.put(KEY_MENU_SLUG, menu.get_menu_slug());
        values.put(KEY_MENU_USERS, menu.get_menu_users());

        db.insert(TABLE_UTRAXXMENU, null, values);
        db.close();
    }

    public void addMediaOwner(MediaOwner owner){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, owner.get_id());
        values.put(KEY_MEDIA_OWNER, owner.get_media_owner());

        db.insert(TABLE_MEDIA_OWNER, null, values);
        db.close();
    }

    public MediaOwner getMediaOwner(int id){
        MediaOwner mediaOwner = new MediaOwner();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MEDIA_OWNER, null, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            mediaOwner.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            mediaOwner.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER)));
        }

        return mediaOwner;
    }

    public void addAngle(Angle angle){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, angle.get_id());
        values.put(KEY_ANGLE, angle.get_angle());

        db.insert(TABLE_ANGLE, null, values);
        db.close();
    }

    public void addTrafficSpeed(TrafficSpeed trafficspeed){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, trafficspeed.get_id());
        values.put(KEY_TRAFFICSPEED, trafficspeed.get_traffic_speed());

        db.insert(TABLE_TRAFFIC_SPEED, null, values);
        db.close();
    }

    public void addTrafficQuantity(TrafficQuantity trafficquantity){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, trafficquantity.get_id());
        values.put(KEY_TRAFFICQUANTITY, trafficquantity.get_traffic_quantity());

        db.insert(TABLE_TRAFFIC_QUANTITY, null, values);
        db.close();
    }

    public void addVisibility(Visibility visibility){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, visibility.get_id());
        values.put(KEY_VISIBILITY_TYPE, visibility.get_visibility());

        db.insert(TABLE_VISIBILITY, null, values);
        db.close();
    }


    public void addStructureCondition(StructureCondition condition){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, condition.get_id());
        values.put(KEY_STRUCTURE_CONDITION, condition.get_condition());

        db.insert(TABLE_STRUCTURE_CONDITION, null, values);
        db.close();
    }

    public void addSizes(Size size){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, size.get_id());
        values.put(KEY_SIZE, size.get_size());

        db.insert(TABLE_SIZES, null, values);
        db.close();
    }

    public void addStructure(Structure structure){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TYPE_ID, structure.get_type_id());
        values.put(KEY_STRUCTURE_TYPE, structure.get_type_name());
        values.put(KEY_TYPE_PHOTOS, structure.get_type_photos());
        values.put(KEY_MEDIA_TYPE, structure.get_material_type());
        values.put(KEY_MEDIA_SIDES, structure.get_type_sides());

        db.insert(TABLE_STRUCTURE, null, values);
        db.close();
    }

    public void addIllumination(Illumination illumination){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, illumination.get_id());
        values.put(KEY_ILLUMINATION_TYPE, illumination.get_type());

        db.insert(TABLE_ILLUMINATION, null, values);
        db.close();
    }

    public void addMaterial(Material material){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, material.get_id());
        values.put(KEY_MATERIAL_TYPE, material.get_material());

        db.insert(TABLE_MATERIAL, null, values);
        db.close();
    }

    public void addMaterialStatus(MaterialStatus status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, status.get_id());
        values.put(KEY_MATERIAL_STATUS_TYPE, status.get_status());

        db.insert(TABLE_MATERIAL_STATUS, null, values);
        db.close();
    }

    public void addRunUp(RunUp runUp){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, runUp.get_id());
        values.put(KEY_RUN_UP, runUp.get_run_up());

        db.insert(TABLE_RUN_UP, null, values);
        db.close();
    }

    public List<Menu> Mainmenu(String user_type){
        List<Menu> menuList = new ArrayList<Menu>();

        String selectQuery = "SELECT * FROM " + TABLE_MAINMENU;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();

                menu.set_id(Integer.parseInt(cursor.getString(0)));
                menu.set_menu_title(cursor.getString(1));
                menu.set_menu_icon(Integer.parseInt(cursor.getString(2)));
                menu.set_menu_slug(cursor.getString(3));
                menu.set_menu_users(cursor.getString(4));

                String users = cursor.getString(4);
                String usersArray[]  = users.split(", ");

                if (Arrays.asList(usersArray).contains(user_type)) {
                    menuList.add(menu);
                }
            }while (cursor.moveToNext());
        }
        return menuList;
    }


    public List<Menu> MenuUtraxx(String user_type){
        List<Menu> menuList = new ArrayList<Menu>();

        String selectQuery = "SELECT * FROM " + TABLE_UTRAXXMENU;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();

                menu.set_id(Integer.parseInt(cursor.getString(0)));
                menu.set_menu_title(cursor.getString(1));
                menu.set_menu_icon(Integer.parseInt(cursor.getString(2)));
                menu.set_menu_slug(cursor.getString(3));
                menu.set_menu_users(cursor.getString(4));

                String users = cursor.getString(4);
                String usersArray[]  = users.split(", ");

                if (Arrays.asList(usersArray).contains(user_type)) {
                    menuList.add(menu);
                }
            }while (cursor.moveToNext());
        }
        return menuList;
    }


    public List<Menu> MenuItraxx(String user_type){
        List<Menu> menuList = new ArrayList<Menu>();

        String selectQuery = "SELECT * FROM " + TABLE_UTRAXXMENU;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();

                menu.set_id(Integer.parseInt(cursor.getString(0)));
                menu.set_menu_title(cursor.getString(1));
                menu.set_menu_icon(Integer.parseInt(cursor.getString(2)));
                menu.set_menu_slug(cursor.getString(3));
                menu.set_menu_users(cursor.getString(4));

                String users = cursor.getString(4);
                String usersArray[]  = users.split(", ");

                if (Arrays.asList(usersArray).contains(user_type)) {
                    menuList.add(menu);
                }
            }while (cursor.moveToNext());
        }
        return menuList;
    }

    public ArrayList<MediaOwner> allMediaOwners(){
        ArrayList<MediaOwner> mediaOwners = new ArrayList<MediaOwner>();

        String selectQuery = "SELECT * FROM " + TABLE_MEDIA_OWNER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MediaOwner owner = new MediaOwner();

                owner.set_id(Integer.parseInt(cursor.getString(0)));
                owner.set_media_owner(cursor.getString(1));

                mediaOwners.add(owner);
            }while(cursor.moveToNext());
        }

        return mediaOwners;
    }

    public List<Size> allSizes(){
        List<Size> sizes = new ArrayList<Size>();
        String selectQuery = "SELECT * FROM " + TABLE_SIZES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Size size = new Size();

                size.set_id(Integer.parseInt(cursor.getString(0)));
                size.set_size(cursor.getString(1));

                sizes.add(size);
            }while(cursor.moveToNext());
        }
        return sizes;
    }

    public int getmainMenuCount(){
        String countQuery = "SELECT  * FROM " + TABLE_MAINMENU;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }
    public int getUtraxxMenuCount(){
        String countQuery = "SELECT  * FROM " + TABLE_UTRAXXMENU;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }
    public int getItraxxMenuCount(){
        String countQuery = "SELECT  * FROM " + TABLE_ITRAXXMENU;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    public List<Material> allMaterials() {
        List<Material> materials = new ArrayList<Material>();
        String selectQuery = "SELECT * FROM " + TABLE_MATERIAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Material material = new Material();

                material.set_id(Integer.parseInt(cursor.getString(0)));
                material.set_material(cursor.getString(1));

                materials.add(material);
            }while(cursor.moveToNext());
        }
        return materials;
    }


    public List<TrafficQuantity> allTrafficQuantity() {
        List<TrafficQuantity> quantities = new ArrayList<TrafficQuantity>();
        String selectQuery = "SELECT * FROM " + TABLE_TRAFFIC_QUANTITY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrafficQuantity trafficQuantity = new TrafficQuantity();

                trafficQuantity.set_id(Integer.parseInt(cursor.getString(0)));
                trafficQuantity.set_traffic_quantity(cursor.getString(1));

                quantities.add(trafficQuantity);
            }while(cursor.moveToNext());
        }
        return quantities;
    }

    public List<TrafficSpeed> allTrafficSpeed() {
        List<TrafficSpeed> speeds = new ArrayList<TrafficSpeed>();
        String selectQuery = "SELECT * FROM " + TABLE_TRAFFIC_SPEED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrafficSpeed trafficSpeed = new TrafficSpeed();

                trafficSpeed.set_id(Integer.parseInt(cursor.getString(0)));
                trafficSpeed.set_traffic_speed(cursor.getString(1));

                speeds.add(trafficSpeed);
            }while(cursor.moveToNext());
        }
        return speeds;
    }

    public List<RunUp> allRunUps() {
        List<RunUp> runups = new ArrayList<RunUp>();
        String selectQuery = "SELECT * FROM " + TABLE_RUN_UP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RunUp runup = new RunUp();

                runup.set_id(Integer.parseInt(cursor.getString(0)));
                runup.set_run_up(cursor.getString(1));

                runups.add(runup);
            }while(cursor.moveToNext());
        }
        return runups;
    }

    public List<Illumination> allIlluminations() {
        List<Illumination> illuminatations = new ArrayList<Illumination>();
        String selectQuery = "SELECT * FROM " + TABLE_ILLUMINATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Illumination illumination = new Illumination();

                illumination.set_id(Integer.parseInt(cursor.getString(0)));
                illumination.set_type(cursor.getString(1));

                illuminatations.add(illumination);
            }while(cursor.moveToNext());
        }
        return illuminatations;
    }

    public List<Visibility> allVisibilityTypes() {
        List<Visibility> visibilityTypes = new ArrayList<Visibility>();
        String selectQuery = "SELECT * FROM " + TABLE_VISIBILITY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Visibility visibility = new Visibility();

                visibility.set_id(Integer.parseInt(cursor.getString(0)));
                visibility.set_visibility(cursor.getString(1));

                visibilityTypes.add(visibility);
            }while(cursor.moveToNext());
        }
        return visibilityTypes;
    }

    public List<Angle> allAngles() {
        List<Angle> angles = new ArrayList<Angle>();
        String selectQuery = "SELECT * FROM " + TABLE_ANGLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Angle angle = new Angle();

                angle.set_id(Integer.parseInt(cursor.getString(0)));
                angle.set_angle(cursor.getString(1));

                angles.add(angle);
            }while(cursor.moveToNext());
        }
        return angles;
    }

    public List<Structure> allStructures() {
        List<Structure> structures = new ArrayList<Structure>();
        String selectQuery = "SELECT * FROM " + TABLE_STRUCTURE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Structure structure = new Structure();

                structure.set_type_id(Integer.parseInt(cursor.getString(0)));
                structure.set_type_name(cursor.getString(1));
                structure.set_type_photos(cursor.getInt(2));
                structure.set_type_sides(cursor.getInt(cursor.getColumnIndex(KEY_MEDIA_SIDES)));
                structure.set_material_type(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_TYPE)));

                structures.add(structure);
            }while(cursor.moveToNext());
        }
        return structures;
    }

    public Structure getStructure(int id){
        Structure structure = new Structure();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_STRUCTURE, null, KEY_TYPE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            structure.set_type_id(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_ID)));
            structure.set_type_name(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_TYPE)));
            structure.set_type_photos(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_PHOTOS)));
            structure.set_type_sides(cursor.getInt(cursor.getColumnIndex(KEY_MEDIA_SIDES)));
            structure.set_material_type(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_TYPE)));
        }

        return structure;
    }

    public boolean deletePhotoByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TABLE_SUBMISSION_PHOTOS, KEY_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public boolean deleteAuditingPhotoByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TABLE_AUDITING_PHOTOS, KEY_ID + "=?", new String[]{String.valueOf(id)})) > 0;
    }

    public void addSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, submission.get_id());
        values.put(KEY_SITE_ID, submission.get_site_id());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_USER_FIRSTNAME, submission.get_user_firstname());
        values.put(KEY_USER_LASTNAME, submission.get_user_lastname());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());

        values.put(KEY_CREATED_AT, submission.get_created_at());

        db.insert(TABLE_SUBMISSION, null, values);
        db.close();
    }

    public void addSubmissionAuditing(SubmissionAuditing submissionAuditing){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, submissionAuditing.get_id());
        values.put(KEY_SITE_ID, submissionAuditing.get_site_id());
        values.put(KEY_SITE_REFERENCE, submissionAuditing.get_site_reference_number());
        values.put(KEY_BRAND, submissionAuditing.get_brand());
        values.put(KEY_SUBMISSION_DATE, submissionAuditing.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submissionAuditing.get_media_owner());
        values.put(KEY_TOWN, submissionAuditing.get_town());
        values.put(KEY_STRUCTURE_ID, submissionAuditing.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submissionAuditing.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submissionAuditing.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submissionAuditing.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submissionAuditing.get_material());
        values.put(KEY_RUN_UP_ID, submissionAuditing.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submissionAuditing.get_illumination());
        values.put(KEY_VISIBILITY_ID, submissionAuditing.get_visibility());
        values.put(KEY_ANGLE_ID, submissionAuditing.get_angle());
        values.put(KEY_OTHER_COMMENTS, submissionAuditing.get_other_comments());
        values.put(KEY_LATITUDE, submissionAuditing.get_latitude());
        values.put(KEY_LONGITUDE, submissionAuditing.get_longitude());
        values.put(KEY_USER_ID, submissionAuditing.get_user_id());
        values.put(KEY_USER_FIRSTNAME, submissionAuditing.get_user_firstname());
        values.put(KEY_USER_LASTNAME, submissionAuditing.get_user_lastname());
        values.put(KEY_PHOTO_1, submissionAuditing.get_photo_1());
        values.put(KEY_PHOTO_2, submissionAuditing.get_photo_2());
        values.put(KEY_PARENTID, submissionAuditing.get_parentid());
        values.put(KEY_SIDE, submissionAuditing.get_side());

        values.put(KEY_TRAFFICQUANTITY, submissionAuditing.get_trafficquantity());
        values.put(KEY_TRAFFICSPEED, submissionAuditing.get_trafficspeed());

        values.put(KEY_CREATED_AT, submissionAuditing.get_created_at());

        db.insert(TABLE_AUDITING, null, values);
        db.close();
    }

    public void clearSubmissions(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SUBMISSION, null, null);
        db.close();
    }

    public void clearSubmissionsAuditing(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_AUDITING, null, null);
        db.close();
    }

    public List<Submission> allSubmissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBMISSION + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_id(cursor.getString(cursor.getColumnIndex(KEY_SITE_ID)));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_user_firstname(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRSTNAME)));
                submission.set_user_lastname(cursor.getString(cursor.getColumnIndex(KEY_USER_LASTNAME)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_parentid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PARENTID))));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public List<SubmissionAuditing> allSubmissionsauditing(){
        List<SubmissionAuditing> submissionAuditings = new ArrayList<SubmissionAuditing>();
        String selectQuery = "SELECT * FROM " + TABLE_AUDITING + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubmissionAuditing submissionAuditing = new SubmissionAuditing();

                submissionAuditing.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submissionAuditing.set_site_id(cursor.getString(cursor.getColumnIndex(KEY_SITE_ID)));
                submissionAuditing.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submissionAuditing.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submissionAuditing.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submissionAuditing.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submissionAuditing.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submissionAuditing.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submissionAuditing.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submissionAuditing.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submissionAuditing.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submissionAuditing.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submissionAuditing.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submissionAuditing.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submissionAuditing.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submissionAuditing.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submissionAuditing.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submissionAuditing.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submissionAuditing.set_user_firstname(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRSTNAME)));
                submissionAuditing.set_user_lastname(cursor.getString(cursor.getColumnIndex(KEY_USER_LASTNAME)));
                submissionAuditing.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submissionAuditing.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submissionAuditing.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submissionAuditing.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submissionAuditing.set_parentid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PARENTID))));
                submissionAuditing.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submissionAuditing.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                submissionAuditing.set_trafficquantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
                submissionAuditing.set_trafficspeed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));

                submissionAuditings.add(submissionAuditing);
            }while (cursor.moveToNext());
        }

        return submissionAuditings;
    }

    public List<Submission> allSubmissions(int user_id){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBMISSION + " WHERE " + KEY_USER_ID + "=" + user_id + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_id(cursor.getString(cursor.getColumnIndex(KEY_SITE_ID)));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_user_firstname(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRSTNAME)));
                submission.set_user_lastname(cursor.getString(cursor.getColumnIndex(KEY_USER_LASTNAME)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_parentid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PARENTID))));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public List<SubmissionAuditing> allSubmissionsauditing(int user_id){
        List<SubmissionAuditing> submissions = new ArrayList<SubmissionAuditing>();
        String selectQuery = "SELECT * FROM " + TABLE_AUDITING + " WHERE " + KEY_USER_ID + "=" + user_id + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubmissionAuditing submission = new SubmissionAuditing();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_id(cursor.getString(cursor.getColumnIndex(KEY_SITE_ID)));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_user_firstname(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRSTNAME)));
                submission.set_user_lastname(cursor.getString(cursor.getColumnIndex(KEY_USER_LASTNAME)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_parentid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PARENTID))));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public Submission getSubmission(int id){
        Submission submission = new Submission();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SUBMISSION, null, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            submission.set_site_id(cursor.getString(cursor.getColumnIndex(KEY_SITE_ID)));
            submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
            submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
            submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
            submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
            submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
            submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
            submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
            submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
            submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
            submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
            submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
            submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
            submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
            submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
            submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
            submission.set_user_firstname(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRSTNAME)));
            submission.set_user_lastname(cursor.getString(cursor.getColumnIndex(KEY_USER_LASTNAME)));
            submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
            submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
            submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
            submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
            submission.set_parentid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PARENTID))));
            submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
            submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
        }
        return submission;
    }

    public SubmissionAuditing getSubmissionauditing(int id){
        SubmissionAuditing submission = new SubmissionAuditing();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_AUDITING, null, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            submission.set_site_id(cursor.getString(cursor.getColumnIndex(KEY_SITE_ID)));
            submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
            submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
            submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
            submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
            submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
            submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
            submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
            submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
            submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
            submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
            submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
            submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
            submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
            submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
            submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
            submission.set_user_firstname(cursor.getString(cursor.getColumnIndex(KEY_USER_FIRSTNAME)));
            submission.set_user_lastname(cursor.getString(cursor.getColumnIndex(KEY_USER_LASTNAME)));
            submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
            submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
            submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
            submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
            submission.set_parentid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_PARENTID))));
            submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
            submission.set_created_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
            submission.set_trafficquantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
            submission.set_trafficspeed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));



        }
        return submission;
    }

    public void addOfflineSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());


        values.put(KEY_STATUS, submission.get_status());

        db.insert(TABLE_OFFLINE, null, values);
        db.close();
    }

    public void addOfflineSubmissionAuditing(SubmissionAuditing submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_PHOTO_1, submission.get_photo_1());
        values.put(KEY_PHOTO_2, submission.get_photo_2());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());
        values.put(KEY_TRAFFICQUANTITY, submission.get_trafficquantity());
        values.put(KEY_TRAFFICSPEED, submission.get_trafficspeed());


        values.put(KEY_STATUS, submission.get_status());

        db.insert(TABLE_OFFLINEAUDITING, null, values);
        db.close();
    }

    public long addDraftSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_MEDIA_OWNER, submission.get_media_owner_name());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());
        values.put(KEY_STATUS, -1);

        long id = db.insert(TABLE_OFFLINE, null, values);
        db.close();

        return id;
    }

    public long addDraftSubmissionAuditing(SubmissionAuditing submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_MEDIA_OWNER, submission.get_media_owner_name());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());
        values.put(KEY_STATUS, -1);

        values.put(KEY_TRAFFICQUANTITY, submission.get_trafficquantity());
        values.put(KEY_TRAFFICSPEED, submission.get_trafficspeed());

        long id = db.insert(TABLE_OFFLINEAUDITING, null, values);
        db.close();

        return id;
    }

    public void updatedDraftSubmission(Submission submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());
        values.put(KEY_STATUS, submission.get_status());

        int updated = db.update(TABLE_OFFLINE, values, KEY_ID + "=?", new String[]{String.valueOf(submission.get_id())});
    }

    public void updatedDraftSubmissionAuditing(SubmissionAuditing submission){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, submission.get_brand());
        values.put(KEY_SITE_REFERENCE, submission.get_site_reference_number());
        values.put(KEY_SUBMISSION_DATE, submission.get_submission_date());
        values.put(KEY_MEDIA_OWNER_ID, submission.get_media_owner());
        values.put(KEY_TOWN, submission.get_town());
        values.put(KEY_STRUCTURE_ID, submission.get_structure());
        values.put(KEY_MEDIA_SIZE_ID, submission.get_size());
        values.put(KEY_MEDIA_SIZE_HEIGHT, submission.get_media_size_other_height());
        values.put(KEY_MEDIA_SIZE_WIDTH, submission.get_media_size_other_width());
        values.put(KEY_MATERIAL_TYPE_ID, submission.get_material());
        values.put(KEY_RUN_UP_ID, submission.get_run_up());
        values.put(KEY_ILLUMINATION_ID, submission.get_illumination());
        values.put(KEY_VISIBILITY_ID, submission.get_visibility());
        values.put(KEY_ANGLE_ID, submission.get_angle());
        values.put(KEY_TRAFFICQUANTITY, submission.get_trafficquantity());
        values.put(KEY_TRAFFICSPEED, submission.get_trafficspeed());
        values.put(KEY_OTHER_COMMENTS, submission.get_other_comments());
        values.put(KEY_LATITUDE, submission.get_latitude());
        values.put(KEY_LONGITUDE, submission.get_longitude());
        values.put(KEY_USER_ID, submission.get_user_id());
        values.put(KEY_PARENTID, submission.get_parentid());
        values.put(KEY_SIDE, submission.get_side());
        values.put(KEY_STATUS, submission.get_status());

        int updated = db.update(TABLE_OFFLINEAUDITING, values, KEY_ID + "=?", new String[]{String.valueOf(submission.get_id())});
    }

    public List<Submission> allOfflineSubmissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public List<SubmissionAuditing> allOfflineSubmissionsauditing(){
        List<SubmissionAuditing> submissions = new ArrayList<SubmissionAuditing>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINEAUDITING + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubmissionAuditing submission = new SubmissionAuditing();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                submission.set_trafficquantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
                submission.set_trafficspeed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));


                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public List<Submission> allDraftSubmissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_STATUS + " = -1" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }
        return submissions;
    }

    public List<SubmissionAuditing> allDraftSubmissionsAuditing(){
        List<SubmissionAuditing> submissions = new ArrayList<SubmissionAuditing>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINEAUDITING + " WHERE " + KEY_STATUS + " = -1" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubmissionAuditing submission = new SubmissionAuditing();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                submission.set_trafficquantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
                submission.set_trafficspeed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }
        return submissions;
    }

    public Submission draftSubmission(int id){
        Submission submission = new Submission();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_OFFLINE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
//        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
            submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
            submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
            submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
            submission.set_media_owner_name(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER)));
            submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
            submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
            submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
            submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
            submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
            submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
            submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
            submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
            submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
            submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
            submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
            submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
            submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
            submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
            submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
            submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
        }
        return submission;
    }

    public SubmissionAuditing draftSubmissionauditing(int id){
        SubmissionAuditing submission = new SubmissionAuditing();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_OFFLINEAUDITING, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
//        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
            submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
            submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
            submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
            submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
            submission.set_media_owner_name(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER)));
            submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
            submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
            submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
            submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
            submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
            submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
            submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
            submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
            submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
            submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
            submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
            submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
            submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
            submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
            submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
            submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
            submission.set_trafficquantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
            submission.set_trafficspeed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));


        }
        return submission;
    }

    public List<Submission> Submissions(){
        List<Submission> submissions = new ArrayList<Submission>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINE + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Submission submission = new Submission();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public List<SubmissionAuditing> Submissionsauditing(){
        List<SubmissionAuditing> submissions = new ArrayList<SubmissionAuditing>();
        String selectQuery = "SELECT * FROM " + TABLE_OFFLINEAUDITING + " WHERE " + KEY_STATUS + " = 0" + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubmissionAuditing submission = new SubmissionAuditing();

                submission.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                submission.set_site_reference_number(cursor.getString(cursor.getColumnIndex(KEY_SITE_REFERENCE)));
                submission.set_user_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID))));
                submission.set_submission_date(cursor.getString(cursor.getColumnIndex(KEY_SUBMISSION_DATE)));
                submission.set_brand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                submission.set_media_owner(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_OWNER_ID)));
                submission.set_town(cursor.getString(cursor.getColumnIndex(KEY_TOWN)));
                submission.set_structure(cursor.getString(cursor.getColumnIndex(KEY_STRUCTURE_ID)));
                submission.set_size(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_ID)));
                submission.set_media_size_other_height(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_HEIGHT)));
                submission.set_media_size_other_width(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_SIZE_WIDTH)));
                submission.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE_ID)));
                submission.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP_ID)));
                submission.set_illumination(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_ID)));
                submission.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_ID)));
                submission.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE_ID)));
                submission.set_other_comments(cursor.getString(cursor.getColumnIndex(KEY_OTHER_COMMENTS)));
                submission.set_latitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                submission.set_longitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                submission.set_photo_1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_1)));
                submission.set_photo_2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_2)));
                submission.set_side(cursor.getString(cursor.getColumnIndex(KEY_SIDE)));
                submission.set_parentid(cursor.getInt(cursor.getColumnIndex(KEY_PARENTID)));
                submission.set_status(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                submission.set_trafficquantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
                submission.set_trafficspeed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));


                submissions.add(submission);
            }while (cursor.moveToNext());
        }

        return submissions;
    }

    public ArrayList<SubmissionPhoto> getSubmissionPhotos(int submission_id){
        ArrayList<SubmissionPhoto> submissionPhotos = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SUBMISSION_PHOTOS, null, KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(submission_id)}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                SubmissionPhoto submissionPhoto = new SubmissionPhoto();

                submissionPhoto.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                submissionPhoto.setSubmission_id(cursor.getInt(cursor.getColumnIndex(KEY_SUBMISSION_ID)));
                submissionPhoto.setPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_URL)));
                submissionPhoto.setThumb(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));

                submissionPhotos.add(submissionPhoto);
            }while(cursor.moveToNext());
        }
        return submissionPhotos;
    }

    public ArrayList<SubmissionPhotoAuditing> getSubmissionAuditPhotos(int submission_id){
        ArrayList<SubmissionPhotoAuditing> submissionPhotos = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_AUDITING_PHOTOS, null, KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(submission_id)}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                SubmissionPhotoAuditing submissionPhoto = new SubmissionPhotoAuditing();

                submissionPhoto.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                submissionPhoto.setSubmission_id(cursor.getInt(cursor.getColumnIndex(KEY_SUBMISSION_ID)));
                submissionPhoto.setPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_URL)));
                submissionPhoto.setThumb(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));

                submissionPhotos.add(submissionPhoto);
            }while(cursor.moveToNext());
        }
        return submissionPhotos;
    }





    public boolean deleteOfflineSubmission(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OFFLINE, KEY_ID + "=" + id, null) > 0;
    }


    public boolean deleteOfflineSubmissionAuditing(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OFFLINEAUDITING, KEY_ID + "=" + id, null) > 0;
    }

    public List<Submitter> allSubmitters(){
        List<Submitter> submitters = new ArrayList<Submitter>();
        String selectQuery = "SELECT DISTINCT "+KEY_USER_ID+", "+KEY_USER_FIRSTNAME+", "+KEY_USER_LASTNAME+" FROM " + TABLE_SUBMISSION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Submitter submitter = new Submitter();

                submitter.setId(cursor.getInt(0));
                submitter.setName(cursor.getString(1) + " " + cursor.getString(2));

                submitters.add(submitter);
            }while(cursor.moveToNext());
        }
        return submitters;
    }

    public int addSubmissionPhoto(SubmissionPhoto photo) {
        ContentValues values = new ContentValues();

        values.put(KEY_SUBMISSION_ID, photo.getSubmission_id());
        values.put(KEY_THUMB, photo.getThumb());
        values.put(KEY_PHOTO_URL, photo.getPath());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(TABLE_SUBMISSION_PHOTOS, null, values);

        return Integer.parseInt(String.valueOf(id));
    }

    public int addSubmissionPhotoAuditing(SubmissionPhotoAuditing photo) {
        ContentValues values = new ContentValues();

        values.put(KEY_SUBMISSION_ID, photo.getSubmission_id());
        values.put(KEY_THUMB, photo.getThumb());
        values.put(KEY_PHOTO_URL, photo.getPath());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.insert(TABLE_AUDITING_PHOTOS, null, values);

        return Integer.parseInt(String.valueOf(id));
    }



    public Size getSize(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Size size = new Size();

        Cursor cursor = db.query(TABLE_SIZES, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            size.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            size.set_size(cursor.getString(cursor.getColumnIndex(KEY_SIZE)));
        }
        return size;
    }

    public Material getMaterial(int id){
        Material material = new Material();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MATERIAL, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            material.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            material.set_material(cursor.getString(cursor.getColumnIndex(KEY_MATERIAL_TYPE)));
        }
        return material;
    }

    public RunUp getRunUp(int id) {
        RunUp runUp = new RunUp();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RUN_UP, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            runUp.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            runUp.set_run_up(cursor.getString(cursor.getColumnIndex(KEY_RUN_UP)));
        }
        return runUp;
    }

    public Illumination getIllumination(int id) {
        Illumination illumination = new Illumination();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ILLUMINATION, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            illumination.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            illumination.set_type(cursor.getString(cursor.getColumnIndex(KEY_ILLUMINATION_TYPE)));
        }
        return illumination;
    }

    public Visibility getVisibility(int id) {
        Visibility visibility = new Visibility();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VISIBILITY, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            visibility.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            visibility.set_visibility(cursor.getString(cursor.getColumnIndex(KEY_VISIBILITY_TYPE)));
        }
        return visibility;
    }

    public Angle getAngle(int id) {
        Angle angle = new Angle();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANGLE, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            angle.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            angle.set_angle(cursor.getString(cursor.getColumnIndex(KEY_ANGLE)));
        }
        return angle;
    }


    public TrafficQuantity getTrafficQuality(int id) {
        TrafficQuantity trafficQuantity = new TrafficQuantity();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRAFFIC_QUANTITY, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            trafficQuantity.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            trafficQuantity.set_traffic_quantity(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICQUANTITY)));
        }
        return trafficQuantity;
    }


    public TrafficSpeed getTrafficSpeed(int id) {
        TrafficSpeed trafficSpeed = new TrafficSpeed();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRAFFIC_SPEED, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            trafficSpeed.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            trafficSpeed.set_traffic_speed(cursor.getString(cursor.getColumnIndex(KEY_TRAFFICSPEED)));
        }
        return trafficSpeed;
    }

    public void changeOfflineStatus(int submission_id, int status) {
        ContentValues values = new ContentValues();

        values.put(KEY_STATUS, status);

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_OFFLINE, values, KEY_ID + "=?", new String[]{String.valueOf(submission_id)});
    }

    public void addPhoto(SubmissionPhoto photo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, photo.getId());
        values.put(KEY_SUBMISSION_ID, photo.getSubmission_id());
        values.put(KEY_PHOTO_NAME, photo.getName());
        values.put(KEY_THUMB, photo.getThumb());
        values.put(KEY_PHOTO_URL, photo.getPath());
        values.put(KEY_CREATED_AT, photo.getCreated_at());

        db.insert(TABLE_PHOTOS, null, values);
    }


    public void addAuditingPhoto(SubmissionPhotoAuditing photo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, photo.getId());
        values.put(KEY_SUBMISSION_ID, photo.getSubmission_id());
        values.put(KEY_PHOTO_NAME, photo.getName());
        values.put(KEY_THUMB, photo.getThumb());
        values.put(KEY_PHOTO_URL, photo.getPath());
        values.put(KEY_CREATED_AT, photo.getCreated_at());

        db.insert(TABLE_AUDITPHOTOS, null, values);
    }

    public List<SubmissionPhoto> getAllPhotos(){
        List<SubmissionPhoto> photos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PHOTOS, null, null, null, null, null, null);

        photos = this.addPhotosToList(cursor);

        return photos;
    }

    public List<SubmissionPhotoAuditing> getAllAuditPhotos(){
        List<SubmissionPhotoAuditing> photos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AUDITPHOTOS, null, null, null, null, null, null);

        photos = this.addAuditPhotosToList(cursor);

        return photos;
    }

    public List<SubmissionPhoto> getPhotoBySubmissionID(int submission_id){
        List<SubmissionPhoto> photos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PHOTOS, null, KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(submission_id)}, null, null, null);

        photos = this.addPhotosToList(cursor);

        return photos;
    }

    public List<SubmissionPhotoAuditing> getPhotoByAuditID(int submission_id){
        List<SubmissionPhotoAuditing> photos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AUDITPHOTOS, null, KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(submission_id)}, null, null, null);

        photos = this.addAuditPhotosToList(cursor);

        return photos;
    }

    private List<SubmissionPhoto> addPhotosToList(Cursor cursor) {
        List<SubmissionPhoto> photos = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                SubmissionPhoto photo = new SubmissionPhoto();

                photo.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                photo.setSubmission_id(cursor.getInt(cursor.getColumnIndex(KEY_SUBMISSION_ID)));
                photo.setName(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_NAME)));
                photo.setThumb(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));
                photo.setPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_URL)));
                photo.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                photos.add(photo);
            }while (cursor.moveToNext());
        }
        return photos;
    }

    private List<SubmissionPhotoAuditing> addAuditPhotosToList(Cursor cursor) {
        List<SubmissionPhotoAuditing> photos = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                SubmissionPhotoAuditing photo = new SubmissionPhotoAuditing();

                photo.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                photo.setSubmission_id(cursor.getInt(cursor.getColumnIndex(KEY_SUBMISSION_ID)));
                photo.setName(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_NAME)));
                photo.setThumb(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));
                photo.setPath(cursor.getString(cursor.getColumnIndex(KEY_PHOTO_URL)));
                photo.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                photos.add(photo);
            }while (cursor.moveToNext());
        }
        return photos;
    }
}
