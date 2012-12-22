package com.brianbeech.android.software;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.brianbeech.android.software.stats.HuntStats;
import com.google.android.maps.*;

import java.util.Date;
import java.util.List;

public class TrackHunt extends MapActivity {
    LocationListener locationListener;
    GeoPoint previousPoint = null;
    Location previousLocation = null;
    HuntStats huntStats = new HuntStats();
    private final int _LOCATION_UPDATE_INTERVAL = 10000;
    private final int _MINIMAL_DISTANCE_TO_UPDATE = 15;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    private GeoPoint getPoint(double lat, double lon) {
        return (new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6)));
    }

    private void setPoint(double lat, double lon, Location location) {
        MapView mapView = (MapView) findViewById(R.id.mapview);

        previousLocation = location;
        GeoPoint point = getPoint(lat, lon);

        mapView.getController().setCenter(point);

        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(drawable);

        OverlayItem overlayitem = new OverlayItem(point, "Title", "Snippet");

        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        if (previousPoint != null) {
            mapView.getOverlays().add(new DirectionPathOverlay(point, previousPoint));
            Toast.makeText(this, "Total distance walked: " + huntStats.getTotalDistance() + " meters.", Toast.LENGTH_LONG).show();
        }
        previousPoint = point;
    }

    @Override
    public void onPause() {
        /*if(locationListener != null){
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(locationListener);
            Toast.makeText(this,"The app will not track progress anymore...", Toast.LENGTH_LONG).show();
        }*/
        super.onPause();
    }

    @Override
    protected void onResume() {
        /*if(locationListener != null){
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, locationListener);
        }*/
        super.onResume();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public void findParty(View view) {
        Toast.makeText(this, "Finding all members of your party...", Toast.LENGTH_SHORT).show();
    }

    public void notifyParty(View view) {
        Toast.makeText(this, "Notifying party of potential kill; place a marker at last place animal was seen.", Toast.LENGTH_LONG).show();
    }

    public void beginHunt(View view) {
        startTracking();
        huntStats.setStartTrackTime(new Date());
    }

    public void endHunt(View view) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
        huntStats.setEndTrackTime(new Date());
        setContentView(R.layout.track_summary);
        TextView distanceText = (TextView) findViewById(R.id.distanceSummary);
        distanceText.append(String.valueOf(Math.round(huntStats.getTotalDistance())));
        locationManager = null;
    }

    public void showMainScreen(View view) {
        setContentView(R.layout.main);
    }

    public void setSatelliteView(View view) {
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setSatellite(true);
    }

    public void startTracking() {
        setContentView(R.layout.track_map);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setZoom(19);

       /* mapView.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v) {
                Log.v("debug", "LONG CLICK!");
                return true;
            }
        }
        );*/

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is not enabled. Please enable and try again.", Toast.LENGTH_LONG).show();
        } else {
            // Define a listener that responds to location updates
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    if (previousLocation == null || location.distanceTo(previousLocation) > Float.parseFloat("15.00")) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        long time = location.getTime();
                        double altitude = location.getAltitude();
                        float bearing = location.getBearing();
                        if (previousLocation != null) {
                            huntStats.setAscentAmount(altitude);
                            huntStats.setTotalDistance(location.distanceTo(previousLocation));


                        }

                        setPoint(latitude, longitude, location);
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            // Register the listener with the Location Manager to receive location updates
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, _LOCATION_UPDATE_INTERVAL, _MINIMAL_DISTANCE_TO_UPDATE, locationListener);
            } catch (Exception e) {
                Log.v("debug", "Error registering the Location Manager: " + e.getMessage());
            }
        }
    }

    public class DirectionPathOverlay extends Overlay {

        private GeoPoint gp1;
        private GeoPoint gp2;

        public DirectionPathOverlay(GeoPoint gp1, GeoPoint gp2) {
            this.gp1 = gp1;
            this.gp2 = gp2;
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
                            long when) {
            // TODO Auto-generated method stub
            Projection projection = mapView.getProjection();
            if (!shadow) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                Point point = new Point();
                projection.toPixels(gp1, point);
                paint.setColor(Color.BLUE);
                Point point2 = new Point();
                projection.toPixels(gp2, point2);
                paint.setStrokeWidth(2);
                canvas.drawLine((float) point.x, (float) point.y, (float) point2.x,
                        (float) point2.y, paint);
            }
            return super.draw(canvas, mapView, shadow, when);
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);
        }

    }
}
