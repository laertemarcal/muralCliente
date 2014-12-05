package muralufg.fabrica.inf.ufg.br.centralufg.linhasdeonibus.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import muralufg.fabrica.inf.ufg.br.centralufg.R;
import muralufg.fabrica.inf.ufg.br.centralufg.linhasdeonibus.receivers.AlarmReceiver;
import muralufg.fabrica.inf.ufg.br.centralufg.linhasdeonibus.adapters.LinhasDeOnibusArrayAdapter;
import muralufg.fabrica.inf.ufg.br.centralufg.linhasdeonibus.services.LinhasDeOnibusService;
import muralufg.fabrica.inf.ufg.br.centralufg.model.LinhaDeOnibus;
import muralufg.fabrica.inf.ufg.br.centralufg.util.ServiceCompliant;

public class LinhasDeOnibusFragment extends Fragment implements ServiceCompliant {

    private Button buttonPesquisar;
    private EditText editTextNumeroPonto;
    private ListView listViewLinhasDeOnibus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linhasdeonibus, container, false);
        buttonPesquisar = (Button) view.findViewById(R.id.buttonPesquisar);
        editTextNumeroPonto = (EditText) view.findViewById(R.id.editTextNumeroPonto);
        listViewLinhasDeOnibus = (ListView) view.findViewById(R.id.listViewLinhasDeOnibus);
        editTextNumeroPonto.requestFocus();
        editTextNumeroPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNumeroPonto.setText("");
            }
        });
        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeroPonto = editTextNumeroPonto.getText().toString();
                if (numeroPonto == null || ("").equals(numeroPonto)) {
                    createInfo("Por favor, insira o número do ponto desejado.");
                } else {
                    LinhasDeOnibusService service = new LinhasDeOnibusService(LinhasDeOnibusFragment.this, numeroPonto);
                    service.execute();
                    InputMethodManager imm = (InputMethodManager) getContextActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextNumeroPonto.getWindowToken(), 0);
                }
            }
        });
        registerForContextMenu(listViewLinhasDeOnibus);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getContextActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_linhas_de_onibus, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        LinhaDeOnibus linhaDeOnibus = (LinhaDeOnibus) listViewLinhasDeOnibus.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case R.id.action_item_add_alarm:
                showAlarmAlertDialog(linhaDeOnibus);
                return true;
            case R.id.action_item_remove_alarm:
                Log.d("REMOVER ALARME", "REMOVIDO ALARME PARA " + info.position + " " + info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void handleError(String error) {
        Crouton.makeText(this.getActivity(), error, Style.ALERT).show();
    }

    private void createInfo(String info) {
        Crouton.makeText(this.getActivity(), info, Style.INFO).show();
    }

    @Override
    public void readObject(Object object) {
        List<LinhaDeOnibus> linhasDeOnibus = (ArrayList<LinhaDeOnibus>) object;
        LinhasDeOnibusArrayAdapter linhaDeOnibusAdapter = new LinhasDeOnibusArrayAdapter(getContextActivity(), linhasDeOnibus);
        ListView listView = (ListView) getView().findViewById(R.id.listViewLinhasDeOnibus);
        listView.setAdapter(linhaDeOnibusAdapter);
    }

    public Activity getContextActivity() {
        return this.getActivity();
    }

    private void showAlarmAlertDialog(final LinhaDeOnibus linhaDeOnibus) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContextActivity());

        alert.setTitle("Alarme");
        alert.setMessage("Insira o tempo em minutos em que o alarme disparará antes do horário de chegada do ônibus");

        final TextView textViewLinhaDeOnibus = new TextView(getContextActivity());
        textViewLinhaDeOnibus.setText(linhaDeOnibus.getNome());
        alert.setView(textViewLinhaDeOnibus);

        final EditText editTextTempo = new EditText(getContextActivity());
        editTextTempo.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(editTextTempo);


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String editTextTempoValue = editTextTempo.getText().toString();
                int tempoEmMinutos;
                if (editTextTempoValue == null || ("").equals(editTextTempoValue)) {
                    tempoEmMinutos = 0;
                } else {
                    tempoEmMinutos = Integer.parseInt(editTextTempoValue);
                }
                if (tempoEmMinutos > linhaDeOnibus.getProximo()) {
                    handleError("O tempo em minutos deve ser menor ou igual ao horário de chegada do ônibus!");
                } else {
                    tempoEmMinutos = Integer.parseInt(editTextTempoValue);
                    setAlarm(linhaDeOnibus, tempoEmMinutos);
                    dialog.dismiss();
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void setAlarm(LinhaDeOnibus linhaDeOnibus, int minutesBefore) {
        int timeInMinutes = linhaDeOnibus.getProximo() - minutesBefore;

        if (timeInMinutes < 0) {
            timeInMinutes = 0;
        } else if (timeInMinutes > 60) {
            timeInMinutes = 60;
        }

        Context context = getContextActivity();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("linha_de_onibus", linhaDeOnibus);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, timeInMinutes);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        Toast.makeText(context, "Alarme para o ônibus: " + linhaDeOnibus.getNumero() + " " + linhaDeOnibus.getNome() + " programado com sucesso para daqui " + timeInMinutes + " minutos", Toast.LENGTH_LONG).show();
    }
}
