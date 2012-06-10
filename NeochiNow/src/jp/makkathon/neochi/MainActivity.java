package jp.makkathon.neochi;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.makkathon.neochi.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity {
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	
	private Button startButton;
	private Button endButton;
	
	private Calendar startTime;
	private Calendar endTime;
	
	private TextView twitterId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_content);
		
		startButton = (Button)findViewById(R.id.start_time);
		endButton = (Button)findViewById(R.id.end_time);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		twitterId = (TextView)findViewById(R.id.twitter_id);
		twitterId.setText(intent.getExtras().getString("screenName"));
	}
	
	public void attestationTwitterId(View view) {
		Intent intent = new Intent(this, TwitterAuthActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public void setCheckTime(View view) {
		if (view == startButton) {
			showTimePicker(startHour, startMinute, "startTime");
		} else if (view == endButton) {
			showTimePicker(endHour, endMinute, "endTime");
		}
	}
	
	public void showTimePicker(int hour, int minute,final String result) {
		TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				String res = result;
				if (res.equals("startTime")) {
					startTime = Calendar.getInstance();
					startHour = hourOfDay;
					startTime.set(Calendar.HOUR_OF_DAY, startHour);
					startMinute = minute;
					startTime.set(Calendar.MINUTE, startMinute);
					String currentTime = new SimpleDateFormat("HH:mm").format(startTime.getTime());
					startButton.setText(currentTime);
					setAlarm("startTime");
				} else if (res.equals("endTime")) {
					endTime = Calendar.getInstance();
					endHour = hourOfDay;
					endTime.set(Calendar.HOUR_OF_DAY, endHour);
					endMinute = minute;
					endTime.set(Calendar.MINUTE, endMinute);
					String currentTime = new SimpleDateFormat("HH:mm").format(endTime.getTime());
					endButton.setText(currentTime);
					setAlarm("endTime");
				}
			}
		}, hour, minute, true);
		dialog.show();
	}
	
	public void setAlarm(String result) {
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		if (result.equals("startTime")) {
			Intent intent = new Intent(this, LightService.class);
			PendingIntent sendar = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			manager.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), 10 * 1000, sendar);
		} else if (result.equals("endTime")) {
			Intent intent = new Intent(this, LightService.class);
			PendingIntent sendar = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			manager.setRepeating(AlarmManager.RTC_WAKEUP, endTime.getTimeInMillis(), 10 * 1000, sendar);
		}
	}
}
