package thinkbig.telefonica.eco.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
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

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setIcon(R.drawable.eco112_2);
        actionBar.setDisplayShowTitleEnabled(false);

        LinearLayout llHospitales = (LinearLayout) view.findViewById(R.id.ll_hospitales);
        llHospitales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Pasar esto a fragment
                Intent i = new Intent(getActivity(), MapActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
               /* getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MapActivity(), MapActivity.class.getName())
                        .addToBackStack(MapActivity.class.getName())
                        .commit();*/
            }
        });

        LinearLayout llUrgencia = (LinearLayout) view.findViewById(R.id.ll_urgencia);
        llUrgencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new UrgenciasFragment(), UrgenciasFragment.class.getName())
                        .addToBackStack(UrgenciasFragment.class.getName())
                        .commit();
            }
        });

        LinearLayout llDatos = (LinearLayout) view.findViewById(R.id.ll_datos);
        llDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MostrarDatosFragment(), MostrarDatosFragment.class.getName())
                        .addToBackStack(MostrarDatosFragment.class.getName()).commit();
            }
        });

        return view;
    }
}
