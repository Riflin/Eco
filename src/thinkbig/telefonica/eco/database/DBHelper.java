package thinkbig.telefonica.eco.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase que controla la creación y actualización de la base de datos
 */
public class DBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "Eco112.db";
    private final static int DATABASE_VERSION = 1;

    private static DBHelper instance;
    private static SQLiteDatabase sqLiteDatabase;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context){

        if(instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }
    public SQLiteDatabase open(Context context){
        if(sqLiteDatabase ==null){
            sqLiteDatabase = getInstance(context).getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    public void close(){
        if(sqLiteDatabase !=null){
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBTables.TEmergencias.CREATE_TABLE);
        db.execSQL(DBTables.THospitales.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }
}
