package thinkbig.telefonica.eco.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Objeto Emergencia para pintar en el fragment correspondiente
 */
public class Emergencia implements Parcelable {

    private String nombre;
    private String tipoEmergencia;
    private String texto;

    public Emergencia(String nombre, String tipoEmergencia, String texto) {
        this.nombre = nombre;
        this.tipoEmergencia = tipoEmergencia;
        this.texto = texto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }

    public void setTipoEmergencia(String tipoEmergencia) {
        this.tipoEmergencia = tipoEmergencia;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
        dest.writeString(this.tipoEmergencia);
        dest.writeString(this.texto);
    }

    private Emergencia(Parcel in) {
        this.nombre = in.readString();
        this.tipoEmergencia = in.readString();
        this.texto = in.readString();
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
