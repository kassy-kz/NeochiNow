package jp.makkathon.neochi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensorUnit {
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    private Context m_context;
    private final int DEF_DARK = 100;
    
    
    public LightSensorUnit(Context context){
    	m_context = context;
    	
    	onStart();
    }
    
    public boolean isCheck(){
    	LightItem item  = new LightItem(m_context);

    	float light = item.getLight();

    	if(light > DEF_DARK){
    		// 明るい。なおかつ時間をオーバー
    		
/*    		
	    	AlertDialog.Builder dlg; 
	    	dlg = new AlertDialog.Builder(m_context); 
	    	dlg.setPositiveButton("OK", null);
	    	dlg.setTitle("");
	    	dlg.setMessage("" + light);
	    	dlg.show();
	    	*/
    		Intent intent = new Intent(m_context, WakeUpActivity.class); 
//        	Intent intent = new Intent(m_context, TestacActivity.class); 
    	  	if(intent != null){
    	  		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
    	  		m_context.startActivity(intent);   
			}
    	
//    	Intent intent = new Intent(m_context, WakeUpActivity.class); 
////    	Intent intent = new Intent(m_context, TestacActivity.class); 
//	  	if(intent != null){
//	  		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
//	  		m_context.startActivity(intent);       
		}
    	
    	return(true);
    }
    
    
    public void onStart()
    {
    	
        sensorManager = (SensorManager) m_context.getSystemService(Context.SENSOR_SERVICE);
        // 光センサーを取得して登録する
        sensorEventListener = new LightSensorListener(m_context);  

        for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_LIGHT)) {
            sensorManager.registerListener(sensorEventListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);                 
        }
    }
    
    
    public void onStop()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
