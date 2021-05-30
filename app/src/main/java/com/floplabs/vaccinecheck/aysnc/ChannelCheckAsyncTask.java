package com.floplabs.vaccinecheck.aysnc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.floplabs.vaccinecheck.R;
import com.floplabs.vaccinecheck.dao.CenterDAOImpl;
import com.floplabs.vaccinecheck.dao.NotifierChannelDao;
import com.floplabs.vaccinecheck.database.AppDatabase;
import com.floplabs.vaccinecheck.entity.NotifierChannel;
import com.floplabs.vaccinecheck.json.JsonConverter;
import com.floplabs.vaccinecheck.json.JsonFilter;
import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.Session;
import com.floplabs.vaccinecheck.model.Slot;
import com.floplabs.vaccinecheck.model.VaccineFees;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChannelCheckAsyncTask extends AsyncTask<Void, Void, List<Slot>> {

    private static final String TAG = "channel_check_async_task";
    private final Context context;
    private final int districtId;
    private JsonFilter jsonFilter;
    private CenterDAOImpl centerDAO;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ChannelCheckAsyncTask(Context context, int districtId) {
        this.context = context;
        this.districtId = districtId;
        jsonFilter = new JsonFilter();
        centerDAO = new CenterDAOImpl();
    }

    public Context getContext() {
        return context;
    }

    public int getDistrictId() {
        return districtId;
    }

    @Override
    protected List<Slot> doInBackground(Void... params) {
        Log.i(TAG, "doInBackground: " + "checking database");
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "notifier-channel").build();
        NotifierChannelDao notifierChannelDao = db.notifierChannelDao();
        NotifierChannel notifierChannel = notifierChannelDao.getWithId(getDistrictId());
        Log.i(TAG, "doInBackground: " + notifierChannel);
        List<Integer> centerIds = new ArrayList<>(notifierChannel.getCenters().keySet());
        List<Slot> slots = new ArrayList<>();

        List<Center> rawCenters = getRawCenterString();
        List<Center> centers = jsonFilter.getNotifierFilteredCenters(rawCenters, centerIds, notifierChannel.getFeeType());
        Log.i(TAG, "doInBackground: " + rawCenters);
        for (Center center : centers) {
            List<Session> filteredSesisons = jsonFilter.getNotifierFilteredSessions(center.getSessions(), notifierChannel.getVaccines(), notifierChannel.isSecondDose(), notifierChannel.getAge());
            for (Session session : filteredSesisons) {
                String fee;
                List<VaccineFees> vaccineFeesList = center.getVaccineFees();
                if (vaccineFeesList == null)
                    fee = "Free";
                else {
                    HashMap<String, String> vaccineFeesMap = new HashMap<>();
                    for (VaccineFees fees : vaccineFeesList)
                        vaccineFeesMap.put(fees.getVaccine(), fees.getFee());

                    fee = "\u20B9 " + vaccineFeesMap.get(session.getVaccine());
                }
                slots.add(new Slot(center.getName(), center.getBlockName(), center.getPincode(), session.getDate(), session.getAvailableCapacityDose1(), session.getSlots(), session.getVaccine(), session.getMinAgeLimit(), fee));
            }
        }
        return slots;
    }

    @Override
    protected void onPostExecute(List<Slot> param) {
        if (!param.isEmpty()) {
            Log.i(TAG, "onPostExecute: " + "!! SLOTS AVAILABLE !!");
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getDistrictId()+"")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("! Vaccine Alert !")
                    .setContentText("Hurry! Vaccine slot available, check on app")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(getDistrictId(), builder.build());
        }else
            Log.i(TAG, "onPostExecute: " + "No slots available");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "vaccine-notifier";
            String description = "Notification channel to notify vaccine slot availability";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getDistrictId()+"", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private List<Center> getRawCenterString(){
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + getDistrictId() + "&date=" + dateFormat.format(new Date()));
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return new JsonConverter().getCowinData(buffer.toString()).getCenters();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
