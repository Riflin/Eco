package thinkbig.telefonica.eco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import thinkbig.telefonica.eco.R;
import thinkbig.telefonica.eco.database.GestorEmergencias;
import thinkbig.telefonica.eco.modelo.Emergencia;

import java.util.ArrayList;

/**
 * Fragment en el que el usuario seleccionará el tipo de urgencia sufrida
 */
public class UrgenciasFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.urgencias_layout, null);

        setHasOptionsMenu(true);

        setupActionBar();
        assert view != null;
        LinearLayout llAmbulancia = (LinearLayout) view.findViewById(R.id.llAmbulancia);
        LinearLayout llPolicia = (LinearLayout) view.findViewById(R.id.llPolicia);
        LinearLayout llBomberos = (LinearLayout) view.findViewById(R.id.llBomberos);

        llAmbulancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Emergencia> emergenciasAmbulancia = GestorEmergencias.getInstance(getActivity())
                        .getEmergenciasByTipo(Emergencia.TipoEmergencia.AMBULANCIA);
                goToEmergenciasFragment(emergenciasAmbulancia, Emergencia.TipoEmergencia.AMBULANCIA);
            }
        });

        llPolicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Emergencia> emergenciasPolicia = GestorEmergencias.getInstance(getActivity())
                        .getEmergenciasByTipo(Emergencia.TipoEmergencia.POLICIA);
                goToEmergenciasFragment(emergenciasPolicia, Emergencia.TipoEmergencia.POLICIA);

            }
        });

        llBomberos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Emergencia> emergenciasBomberos = GestorEmergencias.getInstance(getActivity())
                        .getEmergenciasByTipo(Emergencia.TipoEmergencia.BOMBEROS);
                goToEmergenciasFragment(emergenciasBomberos, Emergencia.TipoEmergencia.BOMBEROS);
            }
        });

        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.logo_small);
        actionBar.setTitle(getString(R.string.urgencias));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void goToEmergenciasFragment(ArrayList<Emergencia> listaEmergencias, int tipoEmergencia) {
        SherlockFragment fragment = new EmergenciasFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(Emergencia.class.getName(), listaEmergencias);
        b.putInt(Emergencia.TipoEmergencia.class.getName(), tipoEmergencia);
        fragment.setArguments(b);
        getSherlockActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, EmergenciasFragment.class.getName())
                .addToBackStack(EmergenciasFragment.class.getName())
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSherlockActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
}
