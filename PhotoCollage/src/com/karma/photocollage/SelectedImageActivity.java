package com.karma.photocollage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SelectedImageActivity extends Activity implements OnTouchListener {

	private static final String TAG = "Touch";
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	
	Matrix matrix1 = new Matrix();
	Matrix savedMatrix1 = new Matrix();
	
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private Bitmap m_bitmap1, m_bitmap2;
	ImageView iv_rotate_left, iv_rotate_right;

	private ImageView mIv_1;
	private ImageView mIv_2;

	boolean isSelected_one = true;

	String mImagename;

	RelativeLayout ll2;

	ImageView main_img,camera, share, save;
	ImageButton picture ;

	int imgid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_image);

		Intent i = getIntent();
		imgid = i.getIntExtra("img_id", 0);

		main_img = (ImageView) findViewById(R.id.main_img);

		main_img.setBackgroundResource(imgid);

		mIv_1 = (ImageView) findViewById(R.id.iv_1);
		mIv_2 = (ImageView) findViewById(R.id.iv_2);

		

		ll2 = (RelativeLayout) findViewById(R.id.ll2);

		picture = (ImageButton) findViewById(R.id.iv_btn_picture);
		camera = (ImageView) findViewById(R.id.iv_btn_camera);
		share = (ImageView) findViewById(R.id.iv_btn_share);
		save = (ImageView) findViewById(R.id.iv_btn_save);

		iv_rotate_left = (ImageView) findViewById(R.id.iv_rotate_left);
		iv_rotate_right = (ImageView) findViewById(R.id.iv_rotate_right);

		
		mIv_1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 0);
			}
		});
		
		mIv_2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 1);
			}
		});
		
		
		/*
		 * 
		 * mRr_1.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent i = new Intent( Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * startActivityForResult(i, 0); } });
		 * 
		 * mRr_2.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent i = new Intent( Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * startActivityForResult(i, 1); } });
		 */

		/*
		 * mIv_1.setOnTouchListener(this); mIv_2.setOnTouchListener(this);
		 */
		/*
		 * mRr_1.setOnTouchListener(this); mRr_2.setOnTouchListener(this);
		 */
		
		
		
		
		
		
		iv_rotate_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isSelected_one) {
					matrix.postRotate(12, mIv_1.getMeasuredWidth() / 2,
							mIv_1.getMeasuredHeight() / 2);
					mIv_1.setImageMatrix(matrix);
				} else {
						
					matrix1.postRotate(12, mIv_2.getMeasuredWidth() / 2,
							mIv_2.getMeasuredHeight() / 2);
					mIv_2.setImageMatrix(matrix1);
					
				}
			}
		});

		iv_rotate_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isSelected_one) {

					matrix.postRotate(-12, mIv_1.getMeasuredWidth() / 2,
							mIv_1.getMeasuredHeight() / 2);
					mIv_1.setImageMatrix(matrix);

				} else {
					matrix1.postRotate(-12, mIv_2.getMeasuredWidth() / 2,
							mIv_2.getMeasuredHeight() / 2);
					mIv_2.setImageMatrix(matrix1);
				
					
				}
			}
		});

		picture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelected_one) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, 0);
				} else  {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, 1);
				}

			}
		});
		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isSelected_one) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, 11);
				} else {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, 12);
				}

			}
		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String filename = captureImage();
				/*
				 * String completePath =
				 * Environment.getExternalStorageDirectory() + "/PhotoCollage/"
				 * + filename;
				 */
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				Uri screenshotUri = Uri.fromFile(file);
				sharingIntent.setType("image/*");
				sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
				startActivity(Intent.createChooser(sharingIntent,
						"Share image using"));
			}
		});
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String filename = captureImage();
			}
		});
		

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 0:

			if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
				m_bitmap1 = null;

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaColumns.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				System.err.println("Image Path =====>" + picturePath);
				m_bitmap1 = BitmapFactory.decodeFile(picturePath);

				Matrix mat = new Matrix();

				Bitmap bMapRotate = Bitmap.createBitmap(m_bitmap1, 0, 0,
						m_bitmap1.getWidth(), m_bitmap1.getHeight(), mat, true);
				mIv_1.setImageBitmap(bMapRotate);
				mIv_1.setOnTouchListener(this);

			}
			break;

		case 1:

			if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
				m_bitmap2 = null;
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaColumns.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				System.err.println("Image Path =====>" + picturePath);
				m_bitmap2 = BitmapFactory.decodeFile(picturePath);

				Matrix mat = new Matrix();

				Bitmap bMapRotate = Bitmap.createBitmap(m_bitmap2, 0, 0,
						m_bitmap2.getWidth(), m_bitmap2.getHeight(), mat, true);
				mIv_2.setImageBitmap(bMapRotate);
				mIv_2.setOnTouchListener(this);

			}
			break;

		case 11:

			if (requestCode == 11) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					Bitmap bm1 = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					Matrix mat = new Matrix();
					Bitmap bMapRotate = Bitmap.createBitmap(bm1, 0, 0,
							bm1.getWidth(), bm1.getHeight(), mat, true);
					mIv_1.setImageBitmap(bMapRotate);
					mIv_1.setOnTouchListener(this);
					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "Phoenix" + File.separator + "default";
					f.delete();
					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					try {
						fOut = new FileOutputStream(file);
						bm1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			break;

		case 12:

			if (requestCode == 12) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					Bitmap bm1 = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					Matrix mat = new Matrix();
					Bitmap bMapRotate = Bitmap.createBitmap(bm1, 0, 0,
							bm1.getWidth(), bm1.getHeight(), mat, true);
					mIv_2.setImageBitmap(bMapRotate);
					mIv_2.setOnTouchListener(this);
					
					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "Phoenix" + File.separator + "default";
					f.delete();
					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					try {
						fOut = new FileOutputStream(file);
						bm1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			break;

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;

		if(view == mIv_1){
		isSelected_one = true;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);

			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);

			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		view.setImageMatrix(matrix);

		}else if(view == mIv_2){
			isSelected_one = false;
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix1.set(matrix1);

				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 10f) {
					savedMatrix1.set(matrix1);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					// ...
					matrix1.set(savedMatrix1);
					matrix1.postTranslate(event.getX() - start.x, event.getY()
							- start.y);

				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						matrix1.set(savedMatrix1);
						float scale = newDist / oldDist;
						matrix1.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

			view.setImageMatrix(matrix1);

			}
		
		return true;
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	File file;

	private String captureImage() {
		// TODO Auto-generated method stub
		OutputStream output;

		Calendar cal = Calendar.getInstance();

		Bitmap bitmap = Bitmap.createBitmap(ll2.getWidth(), ll2.getHeight(),
				Config.ARGB_8888);

		bitmap = ThumbnailUtils.extractThumbnail(bitmap, ll2.getWidth(),
				ll2.getHeight());

		Canvas b = new Canvas(bitmap);
		ll2.draw(b);

		// Find the SD Card path
		File filepath = Environment.getExternalStorageDirectory();

		// Create a new folder in SD Card
		File dir = new File(filepath.getAbsolutePath() + "/PhotoCollage_app/");
		dir.mkdirs();

		mImagename = "image" + cal.getTimeInMillis() + ".png";

		// Create a name for the saved image
		file = new File(dir, mImagename);

		// Show a toast message on successful save
		Toast.makeText(SelectedImageActivity.this, "Image Saved to SD Card",
				Toast.LENGTH_SHORT).show();

		try {

			output = new FileOutputStream(file);
			// Compress into png format image from 0% - 100%
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
			output.flush();
			output.close();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mImagename;

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		show_alert_back("Exit","Are you sure you want to exit Editor ?");
		return super.onKeyDown(keyCode, event);
		
	}
	
	
	
	public void show_alert_back(String title,String msg) {

	  	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	  			SelectedImageActivity.this);

	  		// set title
	  		alertDialogBuilder.setTitle(title);

	  		// set dialog message
	  		alertDialogBuilder
	  			.setMessage(msg)
	  			.setCancelable(true)
	  			.setIcon(R.drawable.icon_).setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						
					}
				})
	  			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	  				public void onClick(DialogInterface dialog,int id) {
	  					// if this button is clicked, close
	  					// current activity
	  					finish();
	  					
	  				
	  				}
	  			  });

	  


	  		
	  			// create alert dialog
	  			AlertDialog alertDialog = alertDialogBuilder.create();
	  			alertDialog.setCancelable(false);
	  			// show it
	  			alertDialog.show();
	  			
	  			Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
	  			Button b1 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
	  			if(b != null)
	  			        b.setTextColor(Color.RED);
	  			if(b1 != null)
	  		        b1.setTextColor(Color.RED);
	  }
	

}
