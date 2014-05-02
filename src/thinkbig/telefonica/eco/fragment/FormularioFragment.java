package thinkbig.telefonica.eco.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.neosistec.utils.logmanager.LogManager;
import thinkbig.telefonica.eco.EcoApplication;
import thinkbig.telefonica.eco.R;

import java.util.Calendar;

/**
 * Pantalla donde el usuario rellenará sus datos
 */
public class FormularioFragment extends SherlockFragment {

    private final LogManager logManager = new LogManager(FormularioFragment.class.getName());

    private EditText etNombre;
    private EditText etFechaNacimiento;
    private EditText etContacto;
    private EditText etTelefono;
    private EditText etAlergias;
    private CheckBox cbDiabetes;
    private Spinner spGrupoSanguineo;
    private EditText etConsideracionesEspeciales;
    private Button btAceptar;
    private String grupoSanguineo;

    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rellenar_datos_fragment, null);

        setHasOptionsMenu(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initViews(view);

        setListeners();

        return view;
    }

    private void initViews(View view) {
        etNombre = (EditText) view.findViewById(R.id.etNombre);
        etFechaNacimiento = (EditText) view.findViewById(R.id.etFechaNacimiento);
        etContacto = (EditText) view.findViewById(R.id.etContacto);
        etTelefono = (EditText) view.findViewById(R.id.etTelefono);
        etAlergias = (EditText) view.findViewById(R.id.etAlergias);
        cbDiabetes = (CheckBox) view.findViewById(R.id.cbDiabetes);
        spGrupoSanguineo = (Spinner) view.findViewById(R.id.spGrupoSanguineo);
        etConsideracionesEspeciales = (EditText) view.findViewById(R.id.etConsideracionesEspeciales);
        btAceptar = (Button) view.findViewById(R.id.btAceptar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.grupos_sanguineos,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrupoSanguineo.setAdapter(adapter);

        //Si tenemos ya los datos guardados, los pondremos
        initValues();


        setupActionBar();

    }

    private void initValues() {
        etNombre.setText(preferences.getString(EcoApplication.NOMBRE_KEY, ""));
        if (preferences.contains(EcoApplication.DIA_NACIMIENTO_KEY)) {
            int dia = preferences.getInt(EcoApplication.DIA_NACIMIENTO_KEY, 1);
            String mes = getMes(preferences.getInt(EcoApplication.MES_NACIMIENTO_KEY, 0));
            int year = preferences.getInt(EcoApplication.YEAR_NACIMIENTO_KEY, 1990);
            etFechaNacimiento.setText(dia + "/" + mes + "/" + year);
        }
        etContacto.setText(preferences.getString(EcoApplication.CONTACTO_KEY, ""));
        etTelefono.setText(preferences.getString(EcoApplication.TELEFONO_KEY, ""));
        etAlergias.setText(preferences.getString(EcoApplication.ALERGIAS_KEY, ""));
        etConsideracionesEspeciales.setText(preferences.getString(EcoApplication.OTRAS_CONSIDERACIONES_KEY, ""));
        cbDiabetes.setChecked(preferences.getBoolean(EcoApplication.DIABETES_KEY, false));
        String grupoElegido = preferences.getString(EcoApplication.GRUPO_SANGUINEO_KEY, "");
        if (!grupoElegido.equals("")) {
            int index = 0;
            String[] gruposSanguineos = getResources().getStringArray(R.array.grupos_sanguineos);
            for (int i = 0; i < gruposSanguineos.length; i++) {
                if (gruposSanguineos[i].equals(grupoElegido)) {
                    index = i;
                    break;
                }
            }
            spGrupoSanguineo.setSelection(index);
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.logo_small);
        actionBar.setTitle(getString(R.string.datos_personales));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners() {
        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SherlockDialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), DatePickerFragment.class.getName());
            }
        });
        etFechaNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SherlockDialogFragment dateFragment = new DatePickerFragment();
                    dateFragment.show(getActivity().getSupportFragmentManager(), DatePickerFragment.class.getName());
                }
            }
        });

        spGrupoSanguineo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                grupoSanguineo = getResources().getStringArray(R.array.grupos_sanguineos)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });
    }

    private void guardarDatos() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        String nombre = "";
        String contacto = "";
        String telefono = "";
        String alergias = "";
        String otrasConsideraciones = "";
        if (etNombre.getText() != null && etNombre.getText().length() > 0) {
            nombre = etNombre.getText().toString();
        }

        if (etContacto.getText() != null && etContacto.getText().length() > 0) {
            contacto = etContacto.getText().toString();
        }

        if (etTelefono.getText() != null && etTelefono.getText().length() > 0) {
            telefono = etTelefono.getText().toString();
        }

        if (etAlergias.getText() != null && etAlergias.getText().length() > 0) {
            alergias = etAlergias.getText().toString();
        }
        if (etConsideracionesEspeciales.getText() != null && etConsideracionesEspeciales.getText().length() > 0) {
            otrasConsideraciones = etConsideracionesEspeciales.getText().toString();
        }

        if (!nombre.equals("")) {
            editor.putString(EcoApplication.NOMBRE_KEY, nombre);
        } else {
            editor.remove(EcoApplication.NOMBRE_KEY);
        }
        if (!contacto.equals("")) {
            editor.putString(EcoApplication.CONTACTO_KEY, contacto);
        } else {
            editor.remove(EcoApplication.CONTACTO_KEY);
        }
        if (!telefono.equals("")) {
            editor.putString(EcoApplication.TELEFONO_KEY, telefono);
        } else {
            editor.remove(EcoApplication.TELEFONO_KEY);
        }
        if (!alergias.equals("")) {
            editor.putString(EcoApplication.ALERGIAS_KEY, alergias);
        } else {
            editor.remove(EcoApplication.ALERGIAS_KEY);
        }
        if (!otrasConsideraciones.equals("")) {
            editor.putString(EcoApplication.OTRAS_CONSIDERACIONES_KEY, otrasConsideraciones);
        } else {
            editor.remove(EcoApplication.OTRAS_CONSIDERACIONES_KEY);
        }
        editor.putBoolean(EcoApplication.DIABETES_KEY, cbDiabetes.isChecked());
        editor.putString(EcoApplication.GRUPO_SANGUINEO_KEY, grupoSanguineo);
        editor.commit();
        
        Toast.makeText(getActivity(), getString(R.string.datos_guardados), Toast.LENGTH_LONG).show();

        //Y cerramos el fragment
        getSherlockActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        logManager.info("OnCreateOptionsMenu");
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        logManager.info("OnPrepareOptionsMenu");
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                guardarDatos();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dialog mostrado para seleccionar la fecha de nacimiento de un usuario
     */
    private class DatePickerFragment extends SherlockDialogFragment
            implements android.app.DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            //Comprobamos si tenemos la fecha de nacimiento ya guardada. Si no, pondremos el día de hoy
            int year, month, day;
            if (preferences.contains(EcoApplication.YEAR_NACIMIENTO_KEY)) {
                year = preferences.getInt(EcoApplication.YEAR_NACIMIENTO_KEY, c.get(Calendar.YEAR));
            } else {
                year = c.get(Calendar.YEAR);
            }
            if (preferences.contains(EcoApplication.MES_NACIMIENTO_KEY)) {
                month = preferences.getInt(EcoApplication.MES_NACIMIENTO_KEY, c.get(Calendar.MONTH));
            } else {
                month = c.get(Calendar.MONTH);
            }

            if (preferences.contains(EcoApplication.DIA_NACIMIENTO_KEY)) {
                day = preferences.getInt(EcoApplication.DIA_NACIMIENTO_KEY, c.get(Calendar.DAY_OF_MONTH));
            } else {
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialgo and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String strMes = getMes(month);

            String strDia = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
            etFechaNacimiento.setText(strDia + "/" + strMes + "/" + year);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
            editor.putInt(EcoApplication.DIA_NACIMIENTO_KEY, day);
            editor.putInt(EcoApplication.MES_NACIMIENTO_KEY, month);
            editor.putInt(EcoApplication.YEAR_NACIMIENTO_KEY, year);
            editor.commit();
        }
    }

    private String getMes(int month) {
        switch (month) {
            case 0:
                return "Enero";
            case 1:
                return "Febrero";
            case 2:
                return "Marzo";
            case 3:
                return "Abril";
            case 4:
                return "Mayo";
            case 5:
                return "Junio";
            case 6:
                return "Julio";
            case 7:
                return "Agosto";
            case 8:
                return "Septiembre";
            case 9:
                return "Octubre";
            case 10:
                return "Noviembre";
            case 11:
                return "Diciembre";
        }
        return "";
    }
}
