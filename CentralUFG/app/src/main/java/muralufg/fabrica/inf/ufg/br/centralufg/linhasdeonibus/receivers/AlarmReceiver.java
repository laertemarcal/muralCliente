package muralufg.fabrica.inf.ufg.br.centralufg.linhasdeonibus.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Vibrator;
import android.widget.Toast;

import muralufg.fabrica.inf.ufg.br.centralufg.model.LinhaDeOnibus;

/**
 * Created by Laerte on 05/12/2014.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LinhaDeOnibus linhaDeOnibus = intent.getParcelableExtra("linha_de_onibus");

        Toast.makeText(context, "O ônibus " + linhaDeOnibus.getNumero() + " chegará em breve!", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }
}
