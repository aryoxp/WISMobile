package ap.mobile.wis.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import ap.mobile.utils.Rest;
import ap.mobile.wis.base.Transaksi;
import ap.mobile.wis.interfaces.TransaksiLoadingInterface;

public class TransaksiPengeluaranTask extends
		AsyncTask<Integer, Void, ArrayList<Transaksi>> {

	private String host;
	private TransaksiLoadingInterface loadingCompleteInterface;
	
	public TransaksiPengeluaranTask(Context c, Fragment callerFragment, ListView listContainerView) {
		this.host = PreferenceManager.getDefaultSharedPreferences(c).getString("prefIPAddress", null);
		this.loadingCompleteInterface = (TransaksiLoadingInterface) callerFragment;
	}
	
	@Override
	protected ArrayList<Transaksi> doInBackground(Integer... params) {
		try {
			String url = "http://" + this.host + "/ws/index.php/transaksi/pengeluaran/" + String.valueOf(params[0]);
			JSONObject result = Rest.getInstance().get(url).getResponseJSONObject();
			if(result != null) {
				JSONArray transaksis = (JSONArray) result.getJSONArray("result");
				/*
				 * {"no":"58495","tanggal":"2014-01-13","pengeluaran":"Biaya Promosi",
				 * "dana":"Tunai - Kas Kecil",
				 * "keterangan":"bayar kekurangan edufair Mojokerto",
				 * "nominal":"101150","tnominal":"Rp. 101,150",
				 * "kodeak":"102","kodead":"509"}
				 */
				ArrayList<Transaksi> listTransaksi = new ArrayList<Transaksi>();
				for(int i=0;i<transaksis.length();i++) {
					JSONObject transaksi = (JSONObject) transaksis.getJSONObject(i);
					Transaksi t = new Transaksi();
					t.no = (String) transaksi.getString("no");
					t.tanggal = (String) transaksi.getString("tanggal");
					t.jam = (String) transaksi.getString("jam");
					t.namaAkunPengeluaran = (String) transaksi.getString("pengeluaran");
					t.namaAkunDana = (String) transaksi.getString("dana");
					t.keterangan = (String) transaksi.getString("keterangan");
					t.nominal = (String) transaksi.getString("nominal");
					t.textNominal = (String) transaksi.getString("tnominal");
					t.kodeAkunKredit = (String) transaksi.getString("kodeak");
					t.kodeAkunDebit = (String) transaksi.getString("kodead");
					t.user = (String) transaksi.getString("username");
					listTransaksi.add(t);
				}
				return listTransaksi;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Transaksi> result) {
		if(result != null) {
			loadingCompleteInterface.OnLoadingComplete(result);
		}
	}

}
