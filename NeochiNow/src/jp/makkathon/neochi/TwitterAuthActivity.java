package jp.makkathon.neochi;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * ツイッター認証を行うアクティビティ
 * 認証すれば以降、AppUtilsｍのメソッドからTwitterインスタンス生成可能
 * 戻り値
 *  resultCode  
 *    TWITTER_AUTH_RESULT_OK 認証成功
 *    TWITTER_AUTH_RESULT_OK 認証失敗
 *  intent
 *    getExtras().getString(ACTIVITY_RESULT_SCEEN_NAME) : 認証ユーザの名前
 */
public class TwitterAuthActivity extends Activity {
    public static final int     ACTIVITY_RESULT_OK = 1;
    public static final int     ACTIVITY_RESULT_NG = 0;
    public static final String  ACTIVITY_RESULT_SCREEN_NAME = "screenName";

    private static final String TAG = "TwitterAuth";
    private static final int TWITTER_AUTHORIZE = 0;
    private static TwitterAuthActivity mSelf;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;
	private AccessToken  mAccessToken = null;
	private String mAuthorizeUrl = "";
	private ProgressDialog mDialog = null;
    private String mScreenName;

    public static TwitterAuthActivity getInstance() {
        return mSelf;
    }

	/**
	 * アクティビティが呼び出された時
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelf = this;
        
        setContentView(R.layout.twitter_auth_main);        
        
        // 以前保存したAccessToken取得
        mAccessToken = AppUtils.loadAccessToken(this);

        // 認証済ならば名前だけ返して終了する
        if(mAccessToken != null) {
            mTwitter = new TwitterFactory().getInstance();
            mTwitter.setOAuthConsumer(AppUtils.CONSUMER_KEY, AppUtils.CONSUMER_SECRET);
            mTwitter.setOAuthAccessToken(mAccessToken);
            try {
                mScreenName = mTwitter.getScreenName();
            } catch (Exception e) {
                e.printStackTrace();
                // 失敗したと返す
                Intent intent = new Intent();
                intent.putExtra(ACTIVITY_RESULT_SCREEN_NAME, "null");
                setResult(ACTIVITY_RESULT_NG, intent);
                finish();
            }
            
            // アクティビティ終了
            Log.i(TAG,"already authenticated! screenName = " + mScreenName);
            Intent intent = new Intent();
            intent.putExtra(ACTIVITY_RESULT_SCREEN_NAME, mScreenName);
            setResult(ACTIVITY_RESULT_OK, intent);
            finish();
            return;
        }
        
        // 認証処理を開始する（通信するのでワーカースレッドで行う）
        AuthAsyncTask1 authTask = new AuthAsyncTask1(this);
        authTask.execute(0);
    }
    
	@Override
	protected void onStop() {
		super.onStop();
		if(mTwitter != null) mTwitter.shutdown();
	}

	// WebViewのOAuthページの認証処理から帰ってきたとき
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == TWITTER_AUTHORIZE) {
			if(resultCode == 0) {
				// 認証成功
				final String pincode = data.getExtras().getString("pincode");
				AuthAsyncTask2 task = new AuthAsyncTask2(mSelf, pincode);
				task.execute(0);
			}
		}
	}
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG,"onConfigurationChange");
        //super.onConfigurationChanged(newConfig);
    }

    public static Twitter getTwitter() {
        if(mSelf != null){
            return mSelf.mTwitter;
        } else{
            return null;
        }
    }
    
    /**
     * 認証処理の非同期タスク　WebViewでOAuthのページに飛ぶタスク
     */
    public class AuthAsyncTask1 extends AsyncTask<Integer, Void, Integer>{
        private Activity mActivity;
        private static final int RESULT_OK = 0;
        private static final int RESULT_NG = -1;

        public AuthAsyncTask1(Activity activity) {
            mActivity = activity;
        }

        // 前処理 これはUIスレッドでの処理
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mActivity);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }
        
        // ワーカースレッドでの処理
        @Override
        protected Integer doInBackground(Integer... arg0) {
            // 初回の認証処理
            mTwitter = new TwitterFactory().getInstance();
            mTwitter.setOAuthConsumer(AppUtils.CONSUMER_KEY, AppUtils.CONSUMER_SECRET);
            try {
                mRequestToken = mTwitter.getOAuthRequestToken();
                mAuthorizeUrl = mRequestToken.getAuthorizationURL();
                return RESULT_OK;
            } catch (TwitterException e) {
                e.printStackTrace();
                return RESULT_NG;
            }
        }

        // 後処理 これはUIスレッドでの処理 WebViewのOAuthページに飛ぶ
        @Override
        protected void onPostExecute (Integer result) {
            super.onPostExecute(result);
            if(mDialog != null) {
                mDialog.dismiss();
            }
            Intent intent = new Intent(mActivity, TwitterWebAuthActivity.class);
            intent.putExtra(AppUtils.AUTH_URL, mAuthorizeUrl);
            mActivity.startActivityForResult(intent, TWITTER_AUTHORIZE);
        }
    } // AuthAsyncTask

    /**
     * 認証処理の非同期タスクその２　WebViewから帰ってきた時の処理
     */
    public class AuthAsyncTask2 extends AsyncTask<Integer, Void, Integer>{
        private Activity mActivity;
        String mPincode;
        
        public AuthAsyncTask2(Activity activity, String pincode) {
            mActivity = activity;
            mPincode = pincode;
        }

        // 前処理 これはUIスレッドでの処理
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(mActivity);
            mDialog.setIndeterminate(true);
            mDialog.show();
        }
        
        // ワーカースレッドでの処理
        @Override
        protected Integer doInBackground(Integer... arg0) {
            try {
                // 認証が成功したあとの処理
                Log.i(TAG,"pincode  "+mPincode);
                mAccessToken = mTwitter.getOAuthAccessToken(mRequestToken, mPincode);
                mTwitter.setOAuthAccessToken(mAccessToken);
                mScreenName = mTwitter.getScreenName();
                // Preferenceに保存
                AppUtils.saveAccessToken(mSelf, mAccessToken);
            } catch(TwitterException e) {
                Log.d("TEST", "Exception", e);
            }
            return null;
        }

        // 後処理 これはUIスレッドでの処理 元のページに戻るなど
        @Override
        protected void onPostExecute (Integer result) {
            super.onPostExecute(result);
            if(mDialog != null) {
                mDialog.dismiss();
            }
            Intent intent = new Intent();
            intent.putExtra("screenName", mScreenName);
            setResult(10, intent);
            finish();
        }
    } // AuthAsyncTask2
}