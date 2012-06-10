package jp.makkathon.neochi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LightService extends Service{
	private LightSensorUnit unit;

	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		System.out.println("‹N“®II");
		unit = new LightSensorUnit(this);
    	unit.isCheck();
	}
	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
