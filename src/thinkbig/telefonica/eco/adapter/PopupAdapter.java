package thinkbig.telefonica.eco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import thinkbig.telefonica.eco.R;
import thinkbig.telefonica.eco.database.GestorHospitales;
import thinkbig.telefonica.eco.modelo.Hospital;


public class PopupAdapter implements InfoWindowAdapter {

    private LayoutInflater inflater;
    private Context context;

    public PopupAdapter(Context context) {

        this.inflater= LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup=inflater.inflate(R.layout.popup, null);

        assert popup != null;
        TextView tv=(TextView)popup.findViewById(R.id.title);
        ImageView ivTelefono = (ImageView) popup.findViewById(R.id.ivTelefono);

        tv.setText(marker.getTitle());
        tv=(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());
        //Comprobamos que el hospital asociado a este marker tiene teléfono
        Hospital hospital = GestorHospitales.getInstance(context).getHospitalByLatLng(marker.getPosition());
        if (hospital != null && hospital.getTelefono() != null) {
            ivTelefono.setVisibility(View.VISIBLE);
        } else {
            ivTelefono.setVisibility(View.GONE);
        }

        return(popup);
    }
}
