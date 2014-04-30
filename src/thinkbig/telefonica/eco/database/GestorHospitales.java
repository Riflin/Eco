package thinkbig.telefonica.eco.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import thinkbig.telefonica.eco.modelo.Hospital;

import java.util.ArrayList;

/**
 * Clase encargada de manejar la base de datos de hospitales
 */
public class GestorHospitales {

    private static GestorHospitales instance;
    private SQLiteDatabase db;

    private GestorHospitales(Context context) {
        db = DBHelper.getInstance(context).open(context);
    }

    public static GestorHospitales getInstance (Context context) {
        if (instance==null) {
            instance = new GestorHospitales(context);
        }
        return instance;
    }

    public void insertHospitales(ArrayList<Hospital> listaHospitales) {
        db.beginTransaction();
        for (Hospital hospital : listaHospitales) {
            db.insert(DBTables.THospitales.TABLE_NAME, null, getHospitalCV(hospital));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Hospital> getHospitales() {
        ArrayList<Hospital> listaHospitales = new ArrayList<Hospital>();

        Cursor c = db.query(DBTables.THospitales.TABLE_NAME, null, null, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                listaHospitales.add(getHospitalByCursor(c));
            }
            c.close();
        }

        return listaHospitales;
    }

    private Hospital getHospitalByCursor(Cursor c) {
        String nombre = c.getString(c.getColumnIndex(DBTables.THospitales.NOMBRE));
        String direccion = c.getString(c.getColumnIndex(DBTables.THospitales.DIRECCION));
        String ciudad = c.getString(c.getColumnIndex(DBTables.THospitales.CIUDAD));
        String telefono = c.getString(c.getColumnIndex(DBTables.THospitales.TELEFONO));
        double latitud = c.getDouble(c.getColumnIndex(DBTables.THospitales.LATITUD));
        double longitud = c.getDouble(c.getColumnIndex(DBTables.THospitales.LONGITUD));

        return new Hospital(nombre, direccion, ciudad, telefono, latitud, longitud);
    }

    private ContentValues getHospitalCV(Hospital hospital) {
        ContentValues values = new ContentValues();
        values.put(DBTables.THospitales.NOMBRE, hospital.getNombre());
        values.put(DBTables.THospitales.DIRECCION, hospital.getDireccion());
        values.put(DBTables.THospitales.CIUDAD, hospital.getCiudad());
        values.put(DBTables.THospitales.TELEFONO, hospital.getTelefono());
        values.put(DBTables.THospitales.LATITUD, hospital.getLatitud());
        values.put(DBTables.THospitales.LONGITUD, hospital.getLongitud());

        return values;
    }
}

