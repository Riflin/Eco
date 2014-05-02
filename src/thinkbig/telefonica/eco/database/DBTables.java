package thinkbig.telefonica.eco.database;

/**
 * Tablas de la base de datos
 */
public class DBTables {

    public static class THospitales {

        public static final String TABLE_NAME = "Hospitales";

        public static final String ID = "_id";
        public static final String NOMBRE = "Nombre";
        public static final String DIRECCION = "Direccion";
        public static final String CIUDAD = "Ciudad";
        public static final String TELEFONO = "Telefono";
        public static final String LATITUD = "Latitud";
        public static final String LONGITUD = "Longitud";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+
                ID + " INTEGER NOT NULL CONSTRAINT pk_hospitales PRIMARY KEY AUTOINCREMENT, " +
                NOMBRE + " TEXT NOT NULL, " +
                DIRECCION + " TEXT NOT NULL, " +
                CIUDAD + " TEXT NOT NULL, " +
                TELEFONO + " TEXT, " +
                LATITUD + " REAL NOT NULL, " +
                LONGITUD + " REAL NOT NULL)";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class TEmergencias {

        public static final String TABLE_NAME = "Emergencias";

        public static final String ID = "_id";
        public static final String NOMBRE = "Nombre";
        public static final String TIPO_EMERGENCIA = "Tipo_Emergencia";
        public static final String TEXTO = "Texto_a_enviar";
        public static final String IMAGEN = "Icono";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL CONSTRAINT pk_emergencias PRIMARY KEY AUTOINCREMENT, " +
                NOMBRE + " TEXT NOT NULL, " +
                TIPO_EMERGENCIA + " INTEGER NOT NULL DEFAULT 1, " +
                TEXTO + " TEXT NOT NULL, " +
                IMAGEN + " INTEGER NOT NULL DEFAULT 0)";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
