package jp.makkathon.neochi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;
import twitter4j.auth.AccessToken;

public class AppUtils {
    public static final String CONSUMER_KEY = "EzDWTZgtnydVJN4zfoJ06g";
    public static final String CONSUMER_SECRET = "1aSMqh9TgQRDuaZCyyFu8NjA3zyjopOU9mSreTVXOY";
    public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
    public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";

    public static final String PREF_FILE_NAME = "pref_file";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACCESS_TOKEN_SECRET = "access_token_secret";
    public static final String AUTH_URL = "authurl";
    
    private static final String TAG = null;
    private static final String TWEET_TIMES = "tweetTimes";
    private static final String LAST_ID = "lastId";
    
    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences shPref = context.getSharedPreferences(AppUtils.PREF_FILE_NAME,Context.MODE_PRIVATE);
        String token       = shPref.getString(ACCESS_TOKEN, null);
        String tokenSecret = shPref.getString(ACCESS_TOKEN_SECRET, null);

        if(token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }

    public static void saveAccessToken(Context context, AccessToken accessToken) {
        
        SharedPreferences shPref = context.getSharedPreferences(AppUtils.PREF_FILE_NAME,Context.MODE_PRIVATE);
        String token       = accessToken.getToken();
        String tokenSecret = accessToken.getTokenSecret();

        Editor e = shPref.edit();
        e.putString(AppUtils.ACCESS_TOKEN, token);
        e.putString(AppUtils.ACCESS_TOKEN_SECRET, tokenSecret);
        e.commit();
        
    }
    
    static void sdCopy(Context context, String dbName) throws IOException{
        //保存先(SDカード)のディレクトリを確保
        String pathSd = new StringBuilder()
                            .append(Environment.getExternalStorageDirectory().getPath())
                            .append("/")
                            .append(context.getPackageName())
                            .toString();
        Log.e(TAG,"pathsd = "+pathSd);
        File filePathToSaved = new File(pathSd);
        
        if (!filePathToSaved.exists() && !filePathToSaved.mkdirs()) {
            throw new IOException("FAILED_TO_CREATE_PATH_ON_SD");
        }

        final String fileDb = context.getDatabasePath(dbName).getPath();
        final String fileSd = new StringBuilder()
                                .append(pathSd)
                                .append("/")
                                .append(dbName)
                                .append(".")
                                .append((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()))
                                .toString();

        Log.i(TAG, "copy from(DB): "+fileDb);
        Log.i(TAG, "copy to(SD)  : "+fileSd);

        FileChannel channelSource = new FileInputStream(fileDb).getChannel();
        FileChannel channelTarget = new FileOutputStream(fileSd).getChannel();

        channelSource.transferTo(0, channelSource.size(), channelTarget);

        channelSource.close();
        channelTarget.close();
    }
    
    
    public static void saveTweetTimes(Context context, int type) {
        SharedPreferences shPref = context.getSharedPreferences(AppUtils.PREF_FILE_NAME,Context.MODE_PRIVATE);
        Editor e = shPref.edit();
        e.putInt(TWEET_TIMES, type);
        e.commit();
    }
    public static int loadTweetTimes(Context context) {
        SharedPreferences shPref = context.getSharedPreferences(AppUtils.PREF_FILE_NAME,Context.MODE_PRIVATE);
        int tweetTims = shPref.getInt(TWEET_TIMES, 0);
        return tweetTims;
    }

    public static void saveLastId(Context context, long statusId) {
        SharedPreferences shPref = context.getSharedPreferences(AppUtils.PREF_FILE_NAME,Context.MODE_PRIVATE);
        Editor e = shPref.edit();
        e.putLong(LAST_ID, statusId);
        e.commit();
    }
    public static long loadLastId(Context context) {
        SharedPreferences shPref = context.getSharedPreferences(AppUtils.PREF_FILE_NAME,Context.MODE_PRIVATE);
        long tweetTims = shPref.getLong(LAST_ID, 0);
        return tweetTims;
    }

}
