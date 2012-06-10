package jp.makkathon.neochi;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import jp.makkathon.neochi.R;

/**
 * 
 * @author kuchitama
 *
 */
public class WakeUpActivity extends Activity {

	MediaPlayer mediaPlayer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("WakeãNìÆÅIÅI");
		setContentView(R.layout.wakeup);
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(this, R.raw.call);
		}
		
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		
		Toast.makeText(this, "WakeUp!", Toast.LENGTH_LONG).show();
		
		Button stopButton = (Button)findViewById(R.id.stop);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("neochi", "clicked stop button!");
				mediaPlayer.stop();
				stop();
				
				// TODO 
//				Intent intent = new Intent(getApplicationContext(), WakeUpActivity.class);
				
//				startActivity(intent);
			}
		});
	}
	
	/**
	 * Èü≥Â£∞„ÇíÂÅúÊ≠¢„Åô„Çã
	 */
	public void stop() {
		Log.d("neochi", "stop alearm");
		mediaPlayer.stop();
	}
}
