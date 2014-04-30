package thinkbig.telefonica.eco.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Objeto Hospital para pintar en el mapa
 */
public class Hospital implements Parcelable {

    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private double latitud;
    private double longitud;

    public Hospital(String nombre, String direccion, String ciudad, String telefono, double latitud, double longitud) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", telefono='" + telefono + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.direccion);
        dest.writeString(this.ciudad);
        dest.writeString(this.telefono);
        dest.writeDouble(this.latitud);
        dest.writeDouble(this.longitud);
    }

    private Hospital(Parcel in) {
        this.nombre = in.readString();
        this.direccion = in.readString();
        this.ciudad = in.readString();
        this.telefono = in.readString();
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
    }

    public static Creator<Hospital> CREATOR = new Creator<Hospital>() {
        public Hospital createFromParcel(Parcel source) {
            return new Hospital(source);
        }

        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };
}
