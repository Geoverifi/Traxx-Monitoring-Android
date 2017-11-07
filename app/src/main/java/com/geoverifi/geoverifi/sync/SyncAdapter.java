package com.geoverifi.geoverifi.sync;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.geoverifi.geoverifi.ErrorActivity;
import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.provider.SubmissionPhotosProvider;
import com.geoverifi.geoverifi.provider.SubmissionProvider;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.server.client.SubmissionClient;
import com.geoverifi.geoverifi.util.FileUtils;
import com.geoverifi.geoverifi.util.Picture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chriz on 10/15/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext());
        mBuilder.setSmallIcon(R.drawable.ic_traxx_notification);
        mBuilder.setContentTitle("Offline Data");
        int mNotificationId = 001;
        DatabaseHandler db = new DatabaseHandler(getContext());

        try {
            final Cursor cursor = mContentResolver.query(SubmissionProvider.CONTENT_URI, null, DatabaseHandler.KEY_STATUS + "=?", new String[]{"0"}, null);
            if (cursor.moveToFirst()){
                mNotificationId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID));
//                do {
                    Submission submission = db.draftSubmission(mNotificationId);
                    String media_owner_id = "0";
                    if (!submission.get_media_owner().isEmpty()) {
                        media_owner_id = submission.get_media_owner();
                    }

                    HashMap<String, RequestBody> params = new HashMap<>();
                    params.put("submission_date", createPartFromString(submission.get_submission_date()));
                    params.put("site_reference_number", createPartFromString(submission.get_site_reference_number()));
                    params.put("brand", createPartFromString(submission.get_brand()));
                    params.put("media_owner", createPartFromString(media_owner_id));
                    params.put("media_owner_name", createPartFromString(submission.get_media_owner_name()));
                    params.put("structure_id", createPartFromString(submission.get_structure()));
                    params.put("town", createPartFromString(submission.get_town()));
                    params.put("media_size", createPartFromString(submission.get_size()));
                    params.put("media_size_other_height", createPartFromString(submission.get_media_size_other_height()));
                    params.put("media_size_other_width", createPartFromString(submission.get_media_size_other_width()));
                    params.put("material_type_id", createPartFromString(submission.get_material()));
                    params.put("run_up_id", createPartFromString(submission.get_run_up()));
                    params.put("illumination_type_id", createPartFromString(submission.get_illumination()));
                    params.put("visibility", createPartFromString(submission.get_visibility()));
                    params.put("angle", createPartFromString(submission.get_angle()));
                    params.put("other_comments", createPartFromString(submission.get_other_comments()));
                    params.put("latitude", createPartFromString(submission.get_latitude()));
                    params.put("longitude", createPartFromString(submission.get_longitude()));
                    params.put("user_id", createPartFromString(String.valueOf(submission.get_user_id())));

                    List<MultipartBody.Part> parts = new ArrayList<>();
                    Cursor submissionPhotosCursor = mContentResolver.query(SubmissionPhotosProvider.CONTENT_URI, null, DatabaseHandler.KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID)))}, null);
                    if (submissionPhotosCursor.moveToFirst()) {
//                        Bitmap bitmap = BitmapFactory.decodeFile(submissionPhotosCursor.getString(submissionPhotosCursor.getColumnIndex(DatabaseHandler.KEY_THUMB)));
//                        mBuilder.setLargeIcon(bitmap);
                        int photo_counter = 1;
                        do {
                            parts.add(prepareFilePart("photos[]", Picture.getImageUri(getContext(), decodeFile(submissionPhotosCursor.getString(submissionPhotosCursor.getColumnIndex(DatabaseHandler.KEY_PHOTO_URL))))));
                            parts.add(prepareFilePart("thumbs[]", Picture.getImageUri(getContext(), decodeFile(submissionPhotosCursor.getString(submissionPhotosCursor.getColumnIndex(DatabaseHandler.KEY_THUMB))))));
                            photo_counter++;
                        }while (submissionPhotosCursor.moveToNext());
                    }
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.HOURS)
                            .connectTimeout(60, TimeUnit.HOURS)
                            .build();

                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(Variables.BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit = builder.build();
                    SubmissionClient client = retrofit.create(SubmissionClient.class);

                    Call<ResponseBody> call = client.uploadSubmission(params, parts);
                    final int finalMNotificationId = mNotificationId;
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code() <= 210) {
                                mBuilder.setContentText("Data successfully updated");
                                Intent successIntent = new Intent(getContext(), ErrorActivity.class);
                                successIntent.putExtra("error", response.raw().headers().toString());

                                PendingIntent successPendingIntent = PendingIntent.getActivity(getContext(), 0, successIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(successPendingIntent);
                                mContentResolver.delete(SubmissionPhotosProvider.CONTENT_URI, DatabaseHandler.KEY_SUBMISSION_ID + "=?", new String[]{String.valueOf(finalMNotificationId)});
                                mContentResolver.delete(SubmissionProvider.CONTENT_URI, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(finalMNotificationId)});
                            }else{
                                mBuilder.setContentText("Response: " + response.message());
                                Intent resultIntent = new Intent(getContext(), ErrorActivity.class);
                                try {
                                    resultIntent.putExtra("error", response.errorBody().string());
                                } catch (IOException e) {
                                    resultIntent.putExtra("error", e.getMessage());
                                }
                                PendingIntent resultPendingIntent =
                                        PendingIntent.getActivity(
                                                getContext(),
                                                0,
                                                resultIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );

                                mBuilder.setContentIntent(resultPendingIntent);
                            }
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
                            // Builds the notification and issues it.
                            mNotifyMgr.notify(finalMNotificationId, mBuilder.build());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            mBuilder.setContentText("Upload Error: " + t.getMessage());
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
                            // Builds the notification and issues it.
                            mNotifyMgr.notify(finalMNotificationId, mBuilder.build());
                        }
                    });

//                }while(cursor.moveToNext());
            }

        }catch(Exception ex){
            mBuilder.setContentText("Upload encountered an error " + ex.getMessage());

            String stacktrace = "";

            for (StackTraceElement element:
                 ex.getStackTrace()) {
                stacktrace += element.getFileName() + ":" + element.getLineNumber() + "=>" + element.getMethodName() + "\n";
            }
            Intent resultIntent = new Intent(getContext(), ErrorActivity.class);
            resultIntent.putExtra("error", ex.toString() + "\n" + stacktrace);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            getContext(),
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            // Sets an ID for the notification
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }

    @NonNull
    private RequestBody createPartFromString(String string){
        if (string.equals("")) {
            string = "Blank";
        }
        return RequestBody.create(
                MultipartBody.FORM, string
        );
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        File file = FileUtils.getFile(getContext(), fileUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse(mContentResolver.getType(fileUri)), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1000;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }
}
