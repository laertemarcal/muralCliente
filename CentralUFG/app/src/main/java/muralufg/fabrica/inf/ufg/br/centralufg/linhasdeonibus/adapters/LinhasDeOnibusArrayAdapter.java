package muralufg.fabrica.inf.ufg.br.centralufg.linhasdeonibus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import muralufg.fabrica.inf.ufg.br.centralufg.R;
import muralufg.fabrica.inf.ufg.br.centralufg.model.LinhaDeOnibus;

/**
 * Created by Laerte on 01/12/2014.
 */
public class LinhasDeOnibusArrayAdapter extends ArrayAdapter<LinhaDeOnibus> {
    public LinhasDeOnibusArrayAdapter(Context context, List<LinhaDeOnibus> linhasDeOnibus) {
        super(context, 0, linhasDeOnibus);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_linha_de_onibus, parent, false);
        }
        // Get the data item for this position
        LinhaDeOnibus linhaDeOnibus = getItem(position);
        // Lookup view for data population
        TextView textViewNome = (TextView) view.findViewById(R.id.textViewNome);
        TextView textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
        TextView textViewProximo = (TextView) view.findViewById(R.id.textViewProximo);
        // Populate the data into the template view using the data object
        textViewNome.setText(linhaDeOnibus.getNome());
        textViewNumero.setText(Integer.toString(linhaDeOnibus.getNumero()));
        textViewProximo.setText(Integer.toString(linhaDeOnibus.getProximo()));
        // Return the completed view to render on screen
        return view;
    }
}
