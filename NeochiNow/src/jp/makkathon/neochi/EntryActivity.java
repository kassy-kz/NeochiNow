package jp.makkathon.neochi;

import jp.makkathon.neochi.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
//import jp.makkathon.neochi.prefference.MainActivity; 

public class EntryActivity extends Activity implements OnClickListener{
	private static final long WAIT_TIME = 1 * 1000;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.entry);
    }
    
    public void swichNextView() {
		Intent intent = new Intent(this, MainActivity.class);
		//intent.setClass(this, MainActivity.class);
		startActivity(intent);
    }

	@Override
	public void onClick(View v) {
		Log.d(this.getClass().getName(), "touped");
		swichNextView();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		Log.d(this.getClass().getName(), "touped");
		
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		
		return super.onTouchEvent(e);
	}

}
