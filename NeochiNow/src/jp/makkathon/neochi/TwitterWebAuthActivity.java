package jp.makkathon.neochi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class TwitterWebAuthActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String strUrl = "";
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_auth_web);

		setResult(-1);

		Button btnPin = (Button)findViewById(R.id.btnPin);
		btnPin.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			if(extras.containsKey("authurl")) {
				strUrl = extras.getString("authurl");
			}
		} else {
			finish();
		}
		WebView webview = (WebView)findViewById(R.id.webview);
		webview.setWebViewClient(new WebViewClient() {});  
		webview.loadUrl(strUrl);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		/**
		 *  Pinボタンが押された時
		 *  pin codeを元のアクティビティに返す
		 */
		case R.id.btnPin:
			EditText editPin = (EditText)findViewById(R.id.editPin);
			String PinNo = editPin.getText().toString();
			if(PinNo.trim().length() > 0) {
				Intent intent = new Intent();
				intent.putExtra("pincode", editPin.getText().toString());
				setResult(0, intent);
				finish();
			}
			break;
		}
	}
}
