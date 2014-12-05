package muralufg.fabrica.inf.ufg.br.centralufg.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Created by Laerte on 27/11/2014.
 */
public class LinhaDeOnibus implements Parcelable{

    private static final Logger LOGGER = Logger.getLogger(LinhaDeOnibus.class.getName());

    private String nome;

    private int numero;
    private int proximo;

    private LinhaDeOnibus(Parcel in) {
        nome = in.readString();
        numero = in.readInt();
        proximo = in.readInt();
    }

    public LinhaDeOnibus(JSONObject object) {
        try {
            this.nome = object.getString("name");
            this.numero = object.getInt("number");
            this.proximo = object.getInt("next");
        } catch (JSONException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getProximo() {
        return proximo;
    }

    public void setProximo(int proximo) {
        this.proximo = proximo;
    }

    public static List<LinhaDeOnibus> fromJson(JSONArray jsonObjects) {
        List<LinhaDeOnibus> linhasDeOnibus = new ArrayList<LinhaDeOnibus>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                linhasDeOnibus.add(new LinhaDeOnibus(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                LOGGER.info(e.getMessage(), e);
            }
        }
        return linhasDeOnibus;
    }

    public static List<LinhaDeOnibus> getGeneric() {
        List<LinhaDeOnibus> linhasDeOnibus = new ArrayList<LinhaDeOnibus>();
        for (int i = 0; i < 2; i++) {
            try {
                JSONObject linha = new JSONObject();
                linha.put("name", "T FICTICIO");
                linha.put("number", 1337);
                linha.put("next", 15);
                linhasDeOnibus.add(new LinhaDeOnibus(linha));
            } catch (JSONException e) {
                LOGGER.info(e.getMessage(), e);
            }
        }
        return linhasDeOnibus;
    }

    public static final Parcelable.Creator<LinhaDeOnibus> CREATOR
            = new Parcelable.Creator<LinhaDeOnibus>() {
        public LinhaDeOnibus createFromParcel(Parcel in) {
            return new LinhaDeOnibus(in);
        }

        public LinhaDeOnibus[] newArray(int size) {
            return new LinhaDeOnibus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeInt(numero);
        parcel.writeInt(proximo);
    }
}
