package com.yonzhang.distance;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import android.view.View.OnLongClickListener;


public class GMapView extends MapView {
	
	OverlayItem pointA = null;
	OverlayItem pointB = null;
	GOverlay itemOverlay = null;
	Drawable mark = null;
	int count = 0;
	float x = 0;
	float y = 0;

	public GMapView(Context arg0) {
		super(arg0);
		
	}
	
	public GMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public GMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mark = getResources().getDrawable(com.yonzhang.distance.R.drawable.ic_launcher);
		itemOverlay = new GOverlay(mark, this);
		this.getOverlays().clear();
		this.getOverlays().add(itemOverlay);
	}

	
	
	public GOverlay getItemOverlay () {
		return itemOverlay;
	}
	
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			break;
		default:
			break;
		}
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//		GeoPoint point = this.getProjection().fromPixels(x, y);
//		mapHandler.handlePoint(point);
		return super.onTouchEvent(event);
	}


//	@Override
//	public void setOnLongClickListener(OnLongClickListener arg0) {
//		// TODO Auto-generated method stub
//		
//		
//		
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//		GeoPoint point = this.getProjection().fromPixels(x, y);
//		mapHandler.handlePoint(point);
//		return super.onTouchEvent(event);
//		super.setOnLongClickListener(arg0);
//		
//	}
	
	private boolean inCycle() {
		if (count > 2) {
			return false;
		} 
		
		return true;
	}
	
	public interface IMapView {
	
		public void handlePoint(GeoPoint point);
		
	}
	
	
	class GOverlay extends ItemizedOverlay<OverlayItem> {

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			// TODO Auto-generated method stub
			return super.onTap(arg0, arg1);
		}

		@Override
		protected boolean onTap(int arg0) {
			// TODO Auto-generated method stub
			return super.onTap(arg0);
		}

		public GOverlay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}
		
	}

}
