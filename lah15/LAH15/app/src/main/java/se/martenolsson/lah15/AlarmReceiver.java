package se.martenolsson.lah15;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import se.martenolsson.lah15.classes.HttpRequest;
import se.martenolsson.lah15.classes.TinyDB;

public class AlarmReceiver extends BroadcastReceiver {
    public static boolean BackgroundTaskStarted;
    NotificationManager notificationManager;

    ArrayList<String> listofitems = new ArrayList<>();
    ArrayList<String> followList = new ArrayList<>();

    TinyDB tinydb;

    private static final String TAG_ARTICLE = "article";
    private static final String TAG_TITLE = "title";
    private static final String TAG_MUSIK = "musik";
    private static final String TAG_PLACE = "place";
    private static final String TAG_TEXT = "text";
    private static final String TAG_MP3 = "mp3";
    private static final String TAG_IMAGE = "image";

    /*private static final String TAG_ARTICLE = "payload";
    private static final String TAG_TITLE = "namenormalized";
    private static final String TAG_MUSIK = "category";
    private static final String TAG_PLACE = "city";
    private static final String TAG_TEXT = "description";
    private static final String TAG_MP3 = "songurl";
    private static final String TAG_IMAGE = "image";*/

    JSONArray allmarkers = null;

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        tinydb = new TinyDB(arg0);
        listofitems = tinydb.getList("listofitems");
        followList = tinydb.getList("followList");

        new JSONParse(arg0).execute();
        new saveFile(arg0, "tab1").execute("http://martenolsson.se/lah15/tab1.php");
        new saveFile(arg0, "tab2").execute("http://martenolsson.se/lah15/tab2.php");
        new saveFile(arg0, "tab3").execute("http://martenolsson.se/lah15/tab3.php");
        new saveFile(arg0, "tab4").execute("http://martenolsson.se/lah15/tab4.php");

        BackgroundTaskStarted = true;

    }

    void sendNotification(Context arg0, String title){
         /*Notification*/
        PendingIntent pi = PendingIntent.getActivity(arg0, 0, new Intent(arg0, MainActivity.class), 0);
        Random r = new Random();
        int randomNumber = r.nextInt(1000 - 0) + 0;
        int NOTIFICATION_ID = randomNumber;
        Notification notification = new NotificationCompat.Builder(arg0)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText("Text")
                .setSmallIcon(R.drawable.mapinfobg)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        notificationManager = (NotificationManager) arg0.getSystemService(arg0.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
		/*end Notification*/
    }

    void ifTextChange(Context context){
        listofitems = tinydb.getList("listofitems");
        followList = tinydb.getList("followList");
        for(int i = 0; i < listofitems.size(); i++){
            String[] listitem = listofitems.get(i).split(";;");
            String currenttitle = listitem[0];
            String currentmusik = listitem[1];
            String currentplace = listitem[2];
            String currenttext = listitem[3];
            String currentmp3 = listitem[4];
            String currentimage = listitem[5];

            for(int index = 0; index < followList.size(); index++) {
                String[] followlistitem = followList.get(index).split(";;");
                String followtitle = followlistitem[0];
                String followmusik = followlistitem[1];
                String followplace = followlistitem[2];
                String followtext = followlistitem[3];
                String followmp3 = followlistitem[4];
                String followimage = followlistitem[5];

                if(followList.get(index).startsWith(currenttitle)){
                    if(!currenttext.trim().equals(followtext.trim())){
                        //Log.e("text1", String.valueOf(followtext.trim().length()));
                        //Log.e("text2", String.valueOf(currenttext.trim().length()));
                        followList.remove(index);
                        followList.add(index, currenttitle + ";;" + currentmusik + ";;" + currentplace + ";;" + currenttext + ";;" + currentmp3 + ";;" + currentimage);
                        tinydb.putList("followList", followList);
                        sendNotification(context, currenttitle);
                    }
                }

            }

        }
    }

    /*Get json for map*/
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        Context context;
        public JSONParse(Context arg0) {
            context = arg0;
        }

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Calendar c;
            SimpleDateFormat sdfday;
            String strDateday;
            c = Calendar.getInstance();
            sdfday = new SimpleDateFormat("yyyyMMddHHm");
            strDateday = sdfday.format(c.getTime());

            JSONParser jParser = new JSONParser();
            //JSONObject json = jParser.getJSONFromUrl("http://lah16.bastardcreative.se/api/artists?"+strDateday);
            JSONObject json = jParser.getJSONFromUrl("http://martenolsson.se/lah15/lah15_2.js?"+strDateday);

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            if(json!=null){
                try {
                    // Getting JSON Array from URL
                    allmarkers = json.getJSONArray(TAG_ARTICLE);
                    followList = tinydb.getList("followList");
                    //Log.e("test","run");

                    listofitems.clear();
                    for(int i = 0; i < allmarkers.length(); i++){
                        JSONObject c = allmarkers.getJSONObject(i);
                        if (c.has(TAG_TITLE)) {
                            String title = c.getString(TAG_TITLE);
                            String musik = c.getString(TAG_MUSIK);
                            String place = c.getString(TAG_PLACE);
                            String text = c.getString(TAG_TEXT);
                            String mp3 = c.getString(TAG_MP3);
                            String image = c.getString(TAG_IMAGE);

                            /*if(i==3){
                                tinydb.putString("adPos", String.valueOf(i));
                                listofitems.add("adPos" + ";;" + "url" + ";;" + "url" + ";;" + "url" + ";;" + "url" + ";;" + "url");
                            }else{*/
                                listofitems.add(title + ";;" + musik + ";;" + place + ";;" + text + ";;" + mp3 + ";;" + image);
                           //}

                        }
                    }
                    Collections.sort(listofitems, String.CASE_INSENSITIVE_ORDER);
                    tinydb.putList("listofitems", listofitems);
                    //ifTextChange(context);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*Save Files*/
    static class saveFile extends AsyncTask<String, Long, File>{
        File saveFile;
        Context newContext;
        String fileName;

        public saveFile(Context context, String string) {
            newContext = context;
            fileName = string;
        }

        @Override
        protected File doInBackground(String... urls) {
            try{
                String theFile;
                theFile = urls[0];
                HttpRequest request = HttpRequest.get(theFile);
                saveFile = null;
                if (request.ok()) {
                    File cacheDir = newContext.getCacheDir();
                    if(fileName != null){
                        saveFile = new File(cacheDir, fileName+".html");
                    }
                    request.receive(saveFile);
                }
                return saveFile;
            }
            catch (Exception e) {
                return null;
            }
        }
    }
    /*end Save Files*/

}