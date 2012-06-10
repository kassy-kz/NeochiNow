package jp.makkathon.neochi;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class LightSensorListener implements SensorEventListener {

	private LightItem m_item;

    public LightSensorListener(Context context) {
    	m_item = new LightItem(context);
    }
        
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        m_item.setLight(x);
    }    
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        assert true;
    }
}
