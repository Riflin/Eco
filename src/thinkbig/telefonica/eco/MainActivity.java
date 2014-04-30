package thinkbig.telefonica.eco;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import thinkbig.telefonica.eco.fragment.MainFragment;

public class MainActivity extends SherlockFragmentActivity {

    private final int MENU_DATOS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setupActionBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.eco112_2);
        actionBar.setCustomView(R.layout.header);
        actionBar.setDisplayShowTitleEnabled(false);
    }

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
        //TODO Ir al formulario para rellenar los datos
    }
}
