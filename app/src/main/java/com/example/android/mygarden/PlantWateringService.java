package com.example.android.mygarden;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static com.example.android.mygarden.provider.PlantContract.BASE_CONTENT_URI;
import static com.example.android.mygarden.provider.PlantContract.PATH_PLANTS;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME;
import static com.example.android.mygarden.utils.PlantUtils.MAX_AGE_WITHOUT_WATER;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PlantWateringService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_WATER_PLANTS = "com.example.android.mygarden.action.water_plants";

    public PlantWateringService() {
        super("PlantWateringService");
    }

    public static PendingIntent startActionWaterPlants(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_WATER_PLANTS);
        //context.startService(intent);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return  pendingIntent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_WATER_PLANTS.equals(action)) {
                handleActionWaterPlants();
            }
        }
    }

    private void handleActionWaterPlants() {
        Uri contentProviderUri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();

        ContentValues values = new ContentValues(1);
        values.put(COLUMN_LAST_WATERED_TIME, System.currentTimeMillis());

        getContentResolver().update(contentProviderUri, values,
                COLUMN_LAST_WATERED_TIME+">?", new String[] {String.valueOf(System.currentTimeMillis() - MAX_AGE_WITHOUT_WATER)});
    }
}
