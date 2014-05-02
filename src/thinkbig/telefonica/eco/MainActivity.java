package thinkbig.telefonica.eco;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.neosistec.utils.logmanager.LogManager;
import thinkbig.telefonica.eco.fragment.FormularioFragment;
import thinkbig.telefonica.eco.fragment.MainFragment;
import thinkbig.telefonica.eco.service.LocationService;

public class MainActivity extends SherlockFragmentActivity {

    private final static int REQUEST_CODE = 112;

    private final int MENU_DATOS = 1;

    private final LogManager logManager = new LogManager(MainActivity.class.getName());

    private LocationService locationService;
    private boolean isBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        logManager.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setupActionBar();

        init();


    }

    private void init() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBound) {
            Intent intent = new Intent(MainActivity.this, LocationService.class);
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    init();
                    break;

            }
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.eco112_2);
        actionBar.setCustomView(R.layout.header);
        actionBar.setDisplayShowTitleEnabled(false);

    }

    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LocationService.MyBinder binder = (LocationService.MyBinder) service;
            locationService = binder.getService();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, MENU_DATOS, 1, "Rellenar mis datos").setIcon(R.drawable.menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DATOS:
                mostrarFormulario();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarFormulario() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FormularioFragment(), FormularioFragment.class.getCanonicalName())
                .addToBackStack(FormularioFragment.class.getName())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myConnection);
    }
}
