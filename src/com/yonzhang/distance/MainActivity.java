package com.yonzhang.distance;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Color;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.yonzhang.distance.GMapView.GOverlay;
import com.yonzhang.distance.GMapView.IMapView;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnLongClickListener, BDLocationListener {

	
	BMapManager mBMapManager = null;
	GMapView mMapView = null;
	LocationClient mLocationClient = null;
	MyLocationOverlay myLocationOverlay = null;
	GraphicsOverlay graphicsOverlay = null;
	GeoPoint[] endPoints = new GeoPoint[2];
	int index = 0;
	long placeLineID = -1;
	int x = 0;
	int y = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init("B41784ccb836a231dff66369a867e005", null);
		mLocationClient = new LocationClient(this);
		mLocationClient.setAK("B41784ccb836a231dff66369a867e005");
		mLocationClient.registerLocationListener(this);
		setContentView(R.layout.activity_main);
		
		
		mMapView = (GMapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setOnLongClickListener(this);
		graphicsOverlay = new GraphicsOverlay(mMapView);
		
//		mMapView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativelayout);
//		layout.addView(mMapView);
		
		myLocationOverlay = new MyLocationOverlay(mMapView);
		mMapView.getOverlays().add(myLocationOverlay);
		
		MapController mapController = mMapView.getController();
		GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));  
		mapController.setCenter(point);
		mapController.setZoom(12);
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd0911");
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);    //最多返回POI个数   
		option.setPoiDistance(1000); //poi查询距离        
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息        
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		
		
		
		Button location = (Button) findViewById(R.id.button1);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mLocationClient != null && mLocationClient.isStarted()) {
					Toast.makeText(getApplicationContext(), "locate your location", Toast.LENGTH_LONG).show();
					mLocationClient.requestLocation();
				}				
			}
		});
		
		
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.destroy();
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
	}



	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		if (mBMapManager != null) {
			mBMapManager.start();
		}
		
		
		super.onResume();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		
		LocationData data = new LocationData();
		data.latitude = location.getLatitude();
		data.longitude = location.getLongitude();
		data.direction = location.getDerect();
		myLocationOverlay.setData(data);
//		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		mMapView.getController().animateTo(new GeoPoint((int)(data.latitude*1e6),  
				(int)(data.longitude* 1e6)));  
		
 	}



	@Override
	public void onReceivePoi(BDLocation poiLocation) {
		// TODO Auto-generated method stub
		if (poiLocation == null){
            return ;
      }
     StringBuffer sb = new StringBuffer(256);
      sb.append("Poi time : ");
      sb.append(poiLocation.getTime());
      sb.append("\nerror code : ");
      sb.append(poiLocation.getLocType());
      sb.append("\nlatitude : ");
      sb.append(poiLocation.getLatitude());
      sb.append("\nlontitude : ");
      sb.append(poiLocation.getLongitude());
      sb.append("\nradius : ");
      sb.append(poiLocation.getRadius());
      if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
          sb.append("\naddr : ");
          sb.append(poiLocation.getAddrStr());
     } 
      if(poiLocation.hasPoi()){
           sb.append("\nPoi:");
           sb.append(poiLocation.getPoi());
     }else{             
           sb.append("noPoi information");
      }
      Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
    }


	@Override
	public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		GeoPoint point = mMapView.getProjection().fromPixels((int)mMapView.getX(), 
				(int)mMapView.getY());
//		handlePoint(point);
		OverlayItem item = new OverlayItem(point, "item", "item");
		Drawable mark = null;//getResources().getDrawable(R.drawable.ic_launcher);
//		item.setMarker(mark);
		GOverlay itemOverlay = mMapView.getItemOverlay();
		
		if (index == 0) {
			//add the first point
			mark = getResources().getDrawable(R.drawable.icon_marka);
			item.setMarker(mark);
			itemOverlay.addItem(item);
			mMapView.refresh();
			endPoints[index] = point;
			index ++;
		} else if (index == 1) {
			mark = getResources().getDrawable(R.drawable.icon_markb);
			item.setMarker(mark);
			itemOverlay.addItem(item);
			endPoints[index] = point;
//			drawLine();
			
			//draw the line
			Geometry lineMetry = new Geometry();
			lineMetry.setPolyLine(endPoints);
			Symbol lineSymbol = new Symbol();
			Symbol.Color lineColor = lineSymbol.new Color();
			lineColor.red = 255;
			lineColor.green = 0;
			lineColor.blue = 0;
			lineColor.alpha = 126;
			lineSymbol.setSurface(lineColor, 1, 3);
			Graphic lineGraphic = new Graphic(lineMetry, lineSymbol);
			placeLineID = graphicsOverlay.setData(lineGraphic);
			mMapView.getOverlays().add(graphicsOverlay);
			mMapView.refresh();
			double distance = DistanceUtil.getDistance(endPoints[0], endPoints[1]);
			
			if (distance > 0) {
				Toast.makeText(getApplicationContext(), Double.toString(distance), 
						Toast.LENGTH_LONG).show();
			}
			index ++;
		} else {
			index = 0;
			itemOverlay.removeAll();
			mMapView.getOverlays().remove(graphicsOverlay);
			graphicsOverlay.removeGraphic(placeLineID);
		}
		return true;
	}
	
	private void drawLine() {
		Geometry lineMetry = new Geometry();
		lineMetry.setPolyLine(endPoints);
		Symbol lineSymbol = new Symbol();
		Symbol.Color lineColor = lineSymbol.new Color();
		lineColor.red = 255;
		lineColor.green = 0;
		lineColor.blue = 0;
		lineColor.alpha = 126;
		lineSymbol.setSurface(lineColor, 1, 3);
		Graphic lineGraphic = new Graphic(lineMetry, lineSymbol);
	
		placeLineID = graphicsOverlay.setData(lineGraphic);
		
	}

}
