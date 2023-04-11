package com.karma.photocollage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class View_Pager extends Activity {

	ViewPager viewPager;
	MyPagerAdapter myPagerAdapter;

	ImageView image_1, image_2,Image_3;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.view_pager);
		viewPager = (ViewPager) findViewById(R.id.myviewpager);
		myPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(myPagerAdapter);

		image_1 = (ImageView) findViewById(R.id.Image_1);
		image_2 = (ImageView) findViewById(R.id.Image_2);
		Image_3 = (ImageView) findViewById(R.id.Image_3);

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			int item_count = 0, max_count = 5;

			@Override
			public void run() {
				viewPager.setCurrentItem(item_count);
				if (item_count == max_count) {
					item_count = 0;
				} else {
					item_count++;
				}
				handler.postDelayed(this, 1000);
			}
		}, 1000);
		
		

		image_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
			}
		});
		
		image_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=UNITYANDROIDS")));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=UNITYANDROIDS")));
				}
				
			}
		});
		
		Image_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final String appName = getPackageName();
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
				}
				
			}
		});
	}

	private class MyPagerAdapter extends PagerAdapter {

		int NumberOfPages = 5;

		int[] res = { R.drawable.i1, R.drawable.i2, R.drawable.i3,
				R.drawable.i4, R.drawable.i5, R.drawable.i6 };

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NumberOfPages;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView imageView = new ImageView(View_Pager.this);
			imageView.setImageResource(res[position]);
			LayoutParams imageParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(imageParams);

			LinearLayout layout = new LinearLayout(View_Pager.this);
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(layoutParams);
			layout.addView(imageView);
			container.addView(layout);
			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}

	}
}
