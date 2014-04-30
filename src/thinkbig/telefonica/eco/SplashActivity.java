package thinkbig.telefonica.eco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import thinkbig.telefonica.eco.database.GestorHospitales;
import thinkbig.telefonica.eco.modelo.Hospital;

import java.util.ArrayList;

/**
 * Pantalla inicial de la aplicación
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        insertarHospitales();
        insertarEmergencias();

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void insertarEmergencias() {
        //TODO Insertar las emergencias
    }

    private void insertarHospitales() {
        ArrayList<Hospital> listaHospitales = new ArrayList<Hospital>();

        listaHospitales.add(new Hospital("Hospital Comarcal del Noroeste", "Av de Prolongación Miguel de Espinosa, 1\n30400", "Caravaca de la " +
                "Cruz", "968 70 91 00", 38.10393, -1.86792));
        listaHospitales.add(new Hospital("Hospital Virgen de la Arrixaca", "Ctra. Madrid-Cartagena, s/n\n30120", "El Palmar (Murcia)",
                "968 36 95 00", 37.9331, -1.1637));
        listaHospitales.add(new Hospital("Hospital Santa María del Rosell", " Paseo Alfonso XIII, 61,\n30203", "Cartagena",
                "968 32 50 00", 37.6089, -0.9745));

        GestorHospitales.getInstance(this).insertHospitales(listaHospitales);
    }
}
