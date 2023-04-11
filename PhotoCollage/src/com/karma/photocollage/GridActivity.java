package com.karma.photocollage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GridActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		
		Intent i = new Intent(this,SelectedImageActivity.class);
		startActivity(i);
		
		
	}

}
