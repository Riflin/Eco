package thinkbig.telefonica.eco.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Objeto Emergencia para pintar en el fragment correspondiente
 */
public class Emergencia implements Parcelable {

    public static class TipoEmergencia {
        public static final int AMBULANCIA = 1;
        public static final int POLICIA = 2;
        public static final int BOMBEROS = 3;
    }

    private String nombre;
    private int tipoEmergencia;
    private String texto;
    private int imageResource;

    public Emergencia(String nombre, int tipoEmergencia, String texto, int imageResource) {
        this.nombre = nombre;
        this.tipoEmergencia = tipoEmergencia;
        this.texto = texto;
        this.imageResource = imageResource;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipoEmergencia() {
        return tipoEmergencia;
    }

    public void setTipoEmergencia(int tipoEmergencia) {
        this.tipoEmergencia = tipoEmergencia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return "Emergencia{" +
                "nombre='" + nombre + '\'' +
                ", tipoEmergencia='" + tipoEmergencia + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeInt(this.tipoEmergencia);
        dest.writeString(this.texto);
        dest.writeInt(this.imageResource);
    }

    private Emergencia(Parcel in) {
        this.nombre = in.readString();
        this.tipoEmergencia = in.readInt();
        this.texto = in.readString();
        this.imageResource = in.readInt();
    }

    public static Creator<Emergencia> CREATOR = new Creator<Emergencia>() {
        public Emergencia createFromParcel(Parcel source) {
            return new Emergencia(source);
        }

        public Emergencia[] newArray(int size) {
            return new Emergencia[size];
        }
    };
}
