package com.xtremelabs.droidsugar.view;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.location.LocationManager;
import android.test.mock.*;
import android.view.*;

import static org.mockito.Mockito.*;

@SuppressWarnings({"UnusedDeclaration"})
public class FakeContextWrapper {
    static public ViewLoader viewLoader;
    protected static Context contextForInflation = new ContextWrapper(null);
    private LocationManager locationManager;

    public Resources getResources() {
        return new Resources(null, null, null);
    }

    public Context getApplicationContext() {
        return new ContextWrapper(null);
    }

    public ContentResolver getContentResolver() {
        return new MockContentResolver();
    }

    public Object getSystemService(String name) {
        if (name.equals(Context.LAYOUT_INFLATER_SERVICE)) {
            return getFakeLayoutInflater();
        } else if (name.equals(Context.ALARM_SERVICE)) {
            return mock(AlarmManager.class);
        } else if (name.equals(Context.LOCATION_SERVICE)) {
            if (locationManager == null) {
                locationManager = mock(LocationManager.class);
            }
            return locationManager;
        }
        return null;
    }

    public FakeLayoutInflater getFakeLayoutInflater() {
        return new FakeLayoutInflater(viewLoader);
    }

    public static class FakeLayoutInflater extends LayoutInflater {

        private final ViewLoader viewLoader;

        public FakeLayoutInflater(ViewLoader viewLoader) {
            super(null);
            this.viewLoader = viewLoader;
        }

        @Override
        public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
            View view = viewLoader.inflateView(contextForInflation, resource);
            if (root != null && attachToRoot) {
                root.addView(view);
            }
            return view;
        }

        @Override
        public View inflate(int resource, ViewGroup root) {
            return inflate(resource, root, true);
        }

        @Override
        public LayoutInflater cloneInContext(Context context) {
            return this;
        }
    }
}