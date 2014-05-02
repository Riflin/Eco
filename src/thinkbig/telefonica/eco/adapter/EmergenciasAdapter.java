package thinkbig.telefonica.eco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import thinkbig.telefonica.eco.R;
import thinkbig.telefonica.eco.modelo.Emergencia;

import java.util.ArrayList;

/**
 * Adapter para mostrar las emergencias en su lista corresponiente
 */
public class EmergenciasAdapter extends ArrayAdapter<Emergencia> {

    private ArrayList<Emergencia> listaEmergencias;
    private LayoutInflater inflater;

    public EmergenciasAdapter(Context context, ArrayList<Emergencia> listaEmergencias) {
        super(context, R.id.tvEmergencia, listaEmergencias);

        this.listaEmergencias = listaEmergencias;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listaEmergencias.size();
    }

    @Override
    public Emergencia getItem(int position) {
        return listaEmergencias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.emergencias_row, parent, false);
            holder = new ViewHolder();
            assert convertView != null;
            holder.ivEmergencia = (ImageView) convertView.findViewById(R.id.ivEmergencia);
            holder.tvEmergencia = (TextView) convertView.findViewById(R.id.tvEmergencia);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Emergencia emergencia = getItem(position);

        if (emergencia.getImageResource() != 0) {
            holder.ivEmergencia.setImageResource(emergencia.getImageResource());
        }
        holder.tvEmergencia.setText(emergencia.getNombre() );
        return convertView;
    }

    class ViewHolder {
        ImageView ivEmergencia;
        TextView tvEmergencia;
    }
}
