package thinkbig.telefonica.eco;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.neosistec.utils.logmanager.LogManager;
import thinkbig.telefonica.eco.database.GestorEmergencias;
import thinkbig.telefonica.eco.database.GestorHospitales;
import thinkbig.telefonica.eco.modelo.Emergencia;
import thinkbig.telefonica.eco.modelo.Hospital;

import java.util.ArrayList;

/**
 * Pantalla inicial de la aplicación
 */
public class SplashActivity extends Activity {

    private final LogManager logManager = new LogManager(SplashActivity.class.getName());

    private final static int REQUEST_CODE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logManager.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        //Comprobamos que tenemos los servicios de Google instalados (y, si no los tiene, le dirigimos al market para bajarlos)
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (statusCode == ConnectionResult.SUCCESS) {
            logManager.info("Tiene Services instalados");
            init();
        } else {
            //Si puede instalarse los servicios lo enviamos allí.
            if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
                Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                        this, REQUEST_CODE);
                errorDialog.show();
            } else {
                // Handle unrecoverable error
            }
        }


    }

    private void init() {
        logManager.info("Empezamos a meter cosas");
        //TODO Comprobar que tiene activado el GPS
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(EcoApplication.DATOS_INSERTADOS_KEY, false)) {
            insertarHospitales();
            insertarEmergencias();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(EcoApplication
                    .DATOS_INSERTADOS_KEY, true).commit();
        }

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
        logManager.info("Nos vamos al MainActivity");
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

    private void insertarEmergencias() {
        //TODO Insertar las emergencias
        ArrayList<Emergencia> listaEmergencias = new ArrayList<Emergencia>();

        listaEmergencias.add(new Emergencia("Accidente de tráfico", Emergencia.TipoEmergencia.AMBULANCIA,
                "He sufrido un accidente de tráfico", 0));
        listaEmergencias.add(new Emergencia("Accidente doméstico", Emergencia.TipoEmergencia.AMBULANCIA,
                "He sufrido un accidente doméstico", 0));
        listaEmergencias.add(new Emergencia("Accidente al aire libre", Emergencia.TipoEmergencia.AMBULANCIA,
                "He sufrido un accidente al aire libre", 0));
        listaEmergencias.add(new Emergencia("Accidente de otra persona", Emergencia.TipoEmergencia.AMBULANCIA,
                "Una persona ha sufrido un accidente", 0));
        listaEmergencias.add(new Emergencia("Robo sufrido", Emergencia.TipoEmergencia.POLICIA, "He sufrido un robo",
                0));
        listaEmergencias.add(new Emergencia("Atraco presenciado", Emergencia.TipoEmergencia.POLICIA,
                "Estoy presenciando un atraco", 0));
        listaEmergencias.add(new Emergencia("Incendio", Emergencia.TipoEmergencia.BOMBEROS, "Hay un incendio", 0));
        listaEmergencias.add(new Emergencia("Inundación", Emergencia.TipoEmergencia.BOMBEROS, "Hay una inundación", 0));

        GestorEmergencias.getInstance(SplashActivity.this).insertarEmergencias(listaEmergencias);

    }

    private void insertarHospitales() {
        ArrayList<Hospital> listaHospitales = new ArrayList<Hospital>();

        listaHospitales.add(new Hospital("Hospital Comarcal del Noroeste", "Av de Prolongación Miguel de Espinosa, 1\n30400", "Caravaca de la " +
                "Cruz", "968 70 91 00", 38.10393, -1.86792));
        listaHospitales.add(new Hospital("Hospital Virgen de la Arrixaca", "Ctra. Madrid-Cartagena, s/n\n30120", "El Palmar (Murcia)",
                "968 36 95 00", 37.9331, -1.1637));
        listaHospitales.add(new Hospital("Hospital Santa María del Rosell", " Paseo Alfonso XIII, 61,\n30203", "Cartagena",
                "968 32 50 00", 37.6089, -0.9745));
        listaHospitales.add(new Hospital("Hospital Rafael Méndez", "Ctra. Nacional 340, Km. 589, \n30817", "Lorca",
                "968 44 55 00", 37.64463, -1.73419));

        GestorHospitales.getInstance(this).insertHospitales(listaHospitales);
    }
}
