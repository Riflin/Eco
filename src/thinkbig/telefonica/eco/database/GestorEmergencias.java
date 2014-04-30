package thinkbig.telefonica.eco.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import thinkbig.telefonica.eco.modelo.Emergencia;

import java.util.ArrayList;

/**
 * Gestor encargado de manejar las emergencias almacenadas en la base de datos
 */
public class GestorEmergencias {

    private static GestorEmergencias instance;
    private SQLiteDatabase db;

    private GestorEmergencias(Context context) {
        db = DBHelper.getInstance(context).open(context);
    }

    public static GestorEmergencias getInstance (Context context) {
        if (instance==null) {
            instance = new GestorEmergencias(context);
        }
        return instance;
    }

    public void insertarEmergencias(ArrayList<Emergencia> listaEmergencias) {
        db.beginTransaction();
        for (Emergencia emergencia : listaEmergencias) {
            db.insert(DBTables.TEmergencias.TABLE_NAME, null, getEmergenciaCV(emergencia));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Emergencia> getEmergenciasByTipo(String tipoEmergencia) {
        ArrayList<Emergencia> listaEmergencias = new ArrayList<Emergencia>();

        String where = DBTables.TEmergencias.TIPO_EMERGENCIA + "=?";
        String[] args = new String[]{tipoEmergencia};

        Cursor c = db.query(DBTables.TEmergencias.TABLE_NAME, null, where, args, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String nombre = c.getString(c.getColumnIndex(DBTables.TEmergencias.NOMBRE));
                String texto = c.getString(c.getColumnIndex(DBTables.TEmergencias.TEXTO));

                listaEmergencias.add(new Emergencia(nombre, tipoEmergencia, texto));
            }
            c.close();
        }
        return listaEmergencias;
    }

    private ContentValues getEmergenciaCV(Emergencia emergencia) {
        ContentValues values = new ContentValues();
        values.put(DBTables.TEmergencias.NOMBRE, emergencia.getNombre());
        values.put(DBTables.TEmergencias.TEXTO, emergencia.getTexto());
        values.put(DBTables.TEmergencias.TIPO_EMERGENCIA, emergencia.getTipoEmergencia());
        return values;
    }
}
