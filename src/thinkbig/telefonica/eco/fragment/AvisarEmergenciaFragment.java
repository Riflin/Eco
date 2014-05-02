package thinkbig.telefonica.eco.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import thinkbig.telefonica.eco.R;
import thinkbig.telefonica.eco.dialog.EcoDialog;
import thinkbig.telefonica.eco.modelo.Emergencia;

/**
 * Último fragment de la aplicación, en el que avisaremos al 112 de la emergencia sufrida por el usuario
 */
public class AvisarEmergenciaFragment extends SherlockFragment {

    private Emergencia emergencia;
    private TextView tvTipoAyuda;
    private TextView tvEmergenciaSufrida;
    private EditText etComentarios;
    private Button btLLamar;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        emergencia = args.getParcelable(Emergencia.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.avisar_emergencia_fragment, null);

        setHasOptionsMenu(true);
        initViews(view);

        setupActionBar();

        mostrarDatosEmergencia();

        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.logo_small);
        actionBar.setTitle(getString(R.string.avisar_emergencias));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void mostrarDatosEmergencia() {
        //Ponemos el tipo de ayuda solicitada
        switch (emergencia.getTipoEmergencia()) {
            case Emergencia.TipoEmergencia.AMBULANCIA:
                tvTipoAyuda.setText(getString(R.string.ambulancia));
                break;
            case Emergencia.TipoEmergencia.BOMBEROS:
                tvTipoAyuda.setText(getString(R.string.bomberos));
                break;
            case Emergencia.TipoEmergencia.POLICIA:
                tvTipoAyuda.setText(getString(R.string.policia));
                break;
        }
        //Mostramos la emergencia sufrida
        tvEmergenciaSufrida.setText(emergencia.getNombre());
    }

    private void initViews(View view) {

        tvTipoAyuda = (TextView) view.findViewById(R.id.tvTipoAyuda);
        tvEmergenciaSufrida = (TextView) view.findViewById(R.id.tvEmergenciaSufrida);
        etComentarios = (EditText) view.findViewById(R.id.etComentarios);
        btLLamar = (Button) view.findViewById(R.id.btLlamar);

        btLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisar112();
            }
        });
    }

    private void avisar112() {

        //TODO Llamar al 112
        //Ahora simplemente mostraremos un dialog
        EcoDialog dialog = new EcoDialog(getActivity());
        dialog.show();

//        Dialog dialog = new Dialog(getActivity());
//        dialog.setTitle("Emergencia avisada");
//        dialog.show();

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
