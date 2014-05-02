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

    /**
     * Nos devuelve una lista con todas las emergencias disponibles según el tipo de urgencia sufrida (Policía,
     * Bomberos o Ambulancia)
     * @param tipoEmergencia    Tipo de emergencia sufrida (bomberos, policía o ambulancia)
     * @return  Un ArrayList con las emergencias disponibles en esa categoría. En caso de no haber nos devolverá
     *          una lista vacía (NO null).
     */
    public ArrayList<Emergencia> getEmergenciasByTipo(int tipoEmergencia) {
        ArrayList<Emergencia> listaEmergencias = new ArrayList<Emergencia>();

        String where = DBTables.TEmergencias.TIPO_EMERGENCIA + "=" + tipoEmergencia;

        Cursor c = db.query(DBTables.TEmergencias.TABLE_NAME, null, where, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String nombre = c.getString(c.getColumnIndex(DBTables.TEmergencias.NOMBRE));
                String texto = c.getString(c.getColumnIndex(DBTables.TEmergencias.TEXTO));
                int imageResource = c.getInt(c.getColumnIndex(DBTables.TEmergencias.IMAGEN));

                listaEmergencias.add(new Emergencia(nombre, tipoEmergencia, texto, imageResource));
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
        values.put(DBTables.TEmergencias.IMAGEN, emergencia.getImageResource());
        return values;
    }
}
