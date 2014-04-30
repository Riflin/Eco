package thinkbig.telefonica.eco.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;
import thinkbig.telefonica.eco.MapActivity;
import thinkbig.telefonica.eco.R;

/**
 * Fragment principal de la aplicación
 */
public class MainFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, null);

        LinearLayout llHospitales = (LinearLayout) view.findViewById(R.id.ll_hospitales);
        llHospitales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MapActivity.class);
                startActivity(i);
            }
        });

        LinearLayout llUrgencia = (LinearLayout) view.findViewById(R.id.ll_urgencia);
        LinearLayout llDatos = (LinearLayout) view.findViewById(R.id.ll_datos);

        return view;
    }
}
