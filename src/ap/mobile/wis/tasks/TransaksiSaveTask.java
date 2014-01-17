package ap.mobile.wis.tasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import ap.mobile.utils.Rest;
import ap.mobile.wis.base.Transaksi;
import ap.mobile.wis.interfaces.TransaksiSaveInterface;

public class TransaksiSaveTask extends AsyncTask<Transaksi, Void, Boolean> {

	private String host;
	private TransaksiSaveInterface saveCompleteInterface;
	private String error;
	
	public TransaksiSaveTask(Context c, TransaksiSaveInterface callerObject) {
		this.saveCompleteInterface = callerObject;
		this.host = PreferenceManager.getDefaultSharedPreferences(c).getString("prefIPAddress", null);
	}
	
	@Override
	protected Boolean doInBackground(Transaksi... params) {
		
		String url = "http://" + this.host + "/ws/index.php/transaksi/save";
		try {
			Transaksi t = params[0];
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("tanggal", t.tanggal));
			data.add(new BasicNameValuePair("jam", t.jam));
			data.add(new BasicNameValuePair("kodejenistransaksi", t.kodeJenisTransaksi));
			data.add(new BasicNameValuePair("kodeakundebit", t.kodeAkunDebit));
			data.add(new BasicNameValuePair("kodeakunkredit", t.kodeAkunKredit));
			data.add(new BasicNameValuePair("nominal", t.nominal));
			data.add(new BasicNameValuePair("keterangan", t.keterangan));
			data.add(new BasicNameValuePair("username", t.user));
			Rest r = Rest.getInstance().post(url, data);
			Log.d("Response", r.getResponseString());
			JSONObject result = r.getResponseJSONObject();
			this.error = result.getString("error");
			return result.getBoolean("result");
		} catch (Exception e) {
			e.printStackTrace();
			this.error = e.getMessage();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		this.saveCompleteInterface.OnSaveComplete(result, this.error);
	}
}
