package thinkbig.telefonica.eco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import thinkbig.telefonica.eco.R;
import thinkbig.telefonica.eco.adapter.EmergenciasAdapter;
import thinkbig.telefonica.eco.modelo.Emergencia;

import java.util.ArrayList;

/**
 * Fragment que muestra la lista de emergencias disponibles para cada tipo de urgencia (policía, ambulancia o bomberos)
 */
public class EmergenciasFragment extends SherlockFragment {

    private ArrayList<Emergencia> listaEmergencias;
    private int tipoEmergencia;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        listaEmergencias = args.getParcelableArrayList(Emergencia.class.getName());
        tipoEmergencia = args.getInt(Emergencia.TipoEmergencia.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergencias_fragment, null);

        setHasOptionsMenu(true);
        setupActionBar();

        assert view != null;
        ListView lvEmergencias = (ListView) view.findViewById(R.id.lvEmergencias);

        EmergenciasAdapter adapter = new EmergenciasAdapter(getActivity(), listaEmergencias);
        lvEmergencias.setAdapter(adapter);

        lvEmergencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emergencia emergencia = listaEmergencias.get(position);
                goToAvisarFragment(emergencia);
            }
        });

        return view;
    }

    private void goToAvisarFragment(Emergencia emergencia) {
        SherlockFragment fragment = new AvisarEmergenciaFragment();
        Bundle b = new Bundle();
        b.putParcelable(Emergencia.class.getName(), emergencia);
        fragment.setArguments(b);
        getSherlockActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment, AvisarEmergenciaFragment.class.getName())
                .addToBackStack(AvisarEmergenciaFragment.class.getName())
                .commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.logo_small);
        int titleResourceID = 0;
        switch (tipoEmergencia) {
            case Emergencia.TipoEmergencia.AMBULANCIA:
                titleResourceID = R.string.emergencias_ambulancia;
                break;
            case Emergencia.TipoEmergencia.POLICIA:
                titleResourceID = R.string.emergencias_policia;
                break;
            case Emergencia.TipoEmergencia.BOMBEROS:
                titleResourceID = R.string.emergencias_bomberos;
                break;
        }
        actionBar.setTitle(getString(titleResourceID));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
