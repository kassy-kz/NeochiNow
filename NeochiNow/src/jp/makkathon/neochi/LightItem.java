package jp.makkathon.neochi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class LightItem {

	Context m_Context;
	private float m_light;
	
    //---------------------------------
    //コンストラクタ
    //---------------------------------
	public LightItem(Context context){
		m_Context = context;
	}
	
	public float getLight() 
	{
		loadInfo();
		return(m_light);
	}

	public void setLight(float light)
	{
		m_light = light;
		saveInfo(light);
	}
	
	
    //---------------------------------
    //情報の保存
    //---------------------------------
	private boolean saveInfo(float light){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(m_Context);
		if(sp == null){
			return(false);
		}

		Editor edit = sp.edit();
		edit.putFloat("Light", light );
		edit.commit();
		
		return(true);
	}

    //---------------------------------
    //情報の呼び出し
    //---------------------------------
	private boolean loadInfo(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(m_Context);
		if(sp == null){
			return(false);
		}
		
		m_light = sp.getFloat("Light", (float) 0.0);

		
		return(true);
	}
	
}
