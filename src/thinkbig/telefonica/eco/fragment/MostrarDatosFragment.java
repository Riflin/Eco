package thinkbig.telefonica.eco.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import thinkbig.telefonica.eco.EcoApplication;
import thinkbig.telefonica.eco.R;

import java.util.Calendar;

/**
 * Se encarga de mostrar los datos introducidos en el {@link thinkbig.telefonica.eco.fragment.FormularioFragment FormularioFragment}
 */
public class MostrarDatosFragment extends SherlockFragment {

    private SharedPreferences preferences;

    private LinearLayout llDatosPersonales;
    private LinearLayout llAlergias;
    private LinearLayout llDatosContacto;
    private LinearLayout llDiabetes;
    private LinearLayout llGrupoSanguineo;
    private LinearLayout llOtrasConsideraciones;
    private LinearLayout llNoDatos;

    private TextView tvNombre;
    private TextView tvEdad;
    private TextView tvAlergias;
    private TextView tvContacto;
    private TextView tvTelefono;
    private TextView tvGrupoSanguineo;
    private TextView tvConsideraciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mostrar_datos_fragment, null);

        setHasOptionsMenu(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initViews(view);

        mostrarDatos();

        return view;
    }

    private void initViews(View view) {
        llAlergias = (LinearLayout) view.findViewById(R.id.llAlergias);
        llDatosContacto = (LinearLayout) view.findViewById(R.id.llDatosContacto);
        llDatosPersonales = (LinearLayout) view.findViewById(R.id.llDatosPersonales);
        llDiabetes = (LinearLayout) view.findViewById(R.id.llDiabetes);
        llGrupoSanguineo = (LinearLayout) view.findViewById(R.id.llSangre);
        llOtrasConsideraciones = (LinearLayout) view.findViewById(R.id.llConsideracionesEspeciales);
        llNoDatos = (LinearLayout) view.findViewById(R.id.llNoDatos);

        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvEdad = (TextView) view.findViewById(R.id.tvEdad);
        tvAlergias = (TextView) view.findViewById(R.id.tvAlergias);
        tvConsideraciones = (TextView) view.findViewById(R.id.tvOtrasConsideraciones);
        tvContacto = (TextView) view.findViewById(R.id.tvPersonaContacto);
        tvTelefono = (TextView) view.findViewById(R.id.tvTelefonoContacto);
        tvGrupoSanguineo = (TextView) view.findViewById(R.id.tvGrupoSanguineo);

        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.logo_small);
        actionBar.setTitle(getString(R.string.mis_datos));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void mostrarDatos() {

        if (preferences.getAll() != null && !preferences.getAll().isEmpty()) {
            //Ocultamos lo de no datos
            llNoDatos.setVisibility(View.GONE);
            //Nombre
            if (preferences.contains(EcoApplication.NOMBRE_KEY)) {
                llDatosPersonales.setVisibility(View.VISIBLE);
                tvNombre.setText(preferences.getString(EcoApplication.NOMBRE_KEY, ""));
            } else {
                llDatosPersonales.setVisibility(View.GONE);
            }
            //Edad
            if (preferences.contains(EcoApplication.DIA_NACIMIENTO_KEY)) {
                tvEdad.setVisibility(View.VISIBLE);
                tvEdad.setText(calcularEdad());
            } else {
                tvEdad.setVisibility(View.GONE);
            }

            //Alergias
            if (preferences.contains(EcoApplication.ALERGIAS_KEY)) {
                llAlergias.setVisibility(View.VISIBLE);
                tvAlergias.setText(preferences.getString(EcoApplication.ALERGIAS_KEY, ""));
            } else {
                llAlergias.setVisibility(View.GONE);
            }

            //Contacto
            if (preferences.contains(EcoApplication.TELEFONO_KEY)) {
                llDatosContacto.setVisibility(View.VISIBLE);
                final String telefono = preferences.getString(EcoApplication.TELEFONO_KEY, "");
                tvTelefono.setText(telefono);
                tvTelefono.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + telefono));
                        startActivity(intent);
                    }
                });

                //Pondremos el nombre del contacto sólo si tiene teléfono asociado
                if (preferences.contains(EcoApplication.CONTACTO_KEY)) {
                    tvContacto.setVisibility(View.VISIBLE);
                    tvContacto.setText(preferences.getString(EcoApplication.CONTACTO_KEY, ""));
                } else {
                    tvContacto.setVisibility(View.GONE);
                }
            } else {
                llDatosContacto.setVisibility(View.GONE);
            }

            //Diabetes
            if (preferences.getBoolean(EcoApplication.DIABETES_KEY, false)) {
                llDiabetes.setVisibility(View.VISIBLE);
            } else {
                llDiabetes.setVisibility(View.GONE);
            }

            //Grupo Sanguíneo
            if (preferences.contains(EcoApplication.GRUPO_SANGUINEO_KEY)) {
                llGrupoSanguineo.setVisibility(View.VISIBLE);
                tvGrupoSanguineo.setText(preferences.getString(EcoApplication.GRUPO_SANGUINEO_KEY, ""));
            } else {
                llGrupoSanguineo.setVisibility(View.GONE);
            }

            //Otras consideraciones
            if (preferences.contains(EcoApplication.OTRAS_CONSIDERACIONES_KEY)) {
                llOtrasConsideraciones.setVisibility(View.VISIBLE);
                tvConsideraciones.setText(preferences.getString(EcoApplication.OTRAS_CONSIDERACIONES_KEY, ""));
            } else {
                llOtrasConsideraciones.setVisibility(View.GONE);
            }
        } else {
            //No tenemos datos
            llNoDatos.setVisibility(View.VISIBLE);
        }

    }

    private String calcularEdad() {
        int dia = preferences.getInt(EcoApplication.DIA_NACIMIENTO_KEY, 1);
        int mes = preferences.getInt(EcoApplication.MES_NACIMIENTO_KEY, 0);
        int year = preferences.getInt(EcoApplication.YEAR_NACIMIENTO_KEY, 1990);

        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);

        int edad = currentYear - year;
        if (currentMonth < mes) {
            edad--;
        } else if (mes == currentMonth) {
            if (currentDay <= dia) {
                edad--;
            }
        }
        return edad + " años";
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
