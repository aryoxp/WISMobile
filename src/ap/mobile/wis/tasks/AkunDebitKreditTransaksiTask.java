package ap.mobile.wis.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Spinner;
import ap.mobile.utils.Rest;
import ap.mobile.wis.R;
import ap.mobile.wis.adapters.AkunAdapter;
import ap.mobile.wis.base.Akun;
import ap.mobile.wis.base.JenisTransaksi;

public class AkunDebitKreditTransaksiTask extends
		AsyncTask<Void, Void, JenisTransaksi> {
		
	private Context c;
	private String kodeJenisTransaksi;
	private String host;
	
	private Spinner akunDebitSpinner;
	private Spinner akunKreditSpinner;
	private View containerView;
	
	public AkunDebitKreditTransaksiTask(Context c, String kodeJenisTransaksi) {
		this.c = c;
		this.kodeJenisTransaksi = kodeJenisTransaksi;
		this.host = PreferenceManager.getDefaultSharedPreferences(c).getString("prefIPAddress", null);
	}
	
	public void setAkunView(Spinner akunDebit, Spinner akunKredit) {
		this.akunDebitSpinner = akunDebit;
		this.akunKreditSpinner = akunKredit;
	}
	
	public void setContainerView(View containerView) {
		this.containerView = containerView;
		this.containerView.setVisibility(View.GONE);
	}

	@Override
	protected JenisTransaksi doInBackground(Void... params) {
		try {
			String url = "http://" + this.host + "/ws/index.php/akun/get/" + this.kodeJenisTransaksi;
			JSONObject result = Rest.getInstance().get(url).getResponseJSONObject();
			if(result != null) {
				JSONArray akunDebitJSON = (JSONArray) result.getJSONArray("debit");
				JSONArray akunKreditJSON = (JSONArray) result.getJSONArray("kredit");
				
				ArrayList<Akun> akunDebit = new ArrayList<Akun>();
				ArrayList<Akun> akunKredit = new ArrayList<Akun>();

				for(int i=0;i<akunDebitJSON.length();i++) {
					JSONObject akunJSON = (JSONObject) akunDebitJSON.getJSONObject(i);
					String kode = (String) akunJSON.getString("kodeakun");
					String nama = (String) akunJSON.getString("nama");
					String keterangan = (String) akunJSON.getString("keterangan");
					Akun akun = new Akun(kode, nama, keterangan);
					akunDebit.add(akun);
				}
				for(int i=0;i<akunKreditJSON.length();i++) {
					JSONObject akunJSON = (JSONObject) akunKreditJSON.getJSONObject(i);
					String kode = (String) akunJSON.getString("kodeakun");
					String nama = (String) akunJSON.getString("nama");
					String keterangan = (String) akunJSON.getString("keterangan");
					Akun akun = new Akun(kode, nama, keterangan);
					akunKredit.add(akun);
				}
				
				return new JenisTransaksi(this.kodeJenisTransaksi, akunDebit, akunKredit);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(JenisTransaksi result) {
		if(result != null) {
			AkunAdapter akunDebitAdapter = new AkunAdapter(c, result.akunDebit);
			AkunAdapter akunKreditAdapter = new AkunAdapter(c, result.akunKredit);
			this.akunDebitSpinner.setAdapter(akunDebitAdapter);
			this.akunKreditSpinner.setAdapter(akunKreditAdapter);
		}
		this.containerView.setVisibility(View.INVISIBLE);
		Animation anim = AnimationUtils.loadAnimation(c, R.anim.fade_in);
		anim.setFillAfter(true);
		this.containerView.startAnimation(anim);
	}

}
