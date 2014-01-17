package ap.mobile.wis.tasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;
import ap.mobile.utils.Rest;
import ap.mobile.wis.R;
import ap.mobile.wis.adapters.TransaksiAdapter;
import ap.mobile.wis.base.Transaksi;
import ap.mobile.wis.interfaces.TransaksiDeleteInterface;
import ap.mobile.wis.listeners.DeleteItemTransaksiAnimationListener;

public class TransaksiDeleteTask extends AsyncTask<String, Void, Boolean> {

	private String host;
	private TransaksiDeleteInterface deleteCompleteInterface;
	private String error;
	private int position;
	private ArrayList<Transaksi> listTransaksi;
	private TransaksiAdapter adapter;
	private ListView listContainerView;
	private Context c;
	private Transaksi tDelete;
	
	public TransaksiDeleteTask(Context c, ArrayList<Transaksi> listTransaksi, 
			TransaksiAdapter adapter, Transaksi tDelete) {
		this.c = c;
		this.adapter = adapter;
		this.listTransaksi = listTransaksi;
		this.tDelete = tDelete;
		//this.deleteCompleteInterface = callerObject;
		//this.position = position;
		this.host = PreferenceManager.getDefaultSharedPreferences(c).getString("prefIPAddress", null);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		String url = "http://" + this.host + "/ws/index.php/transaksi/delete";
		try {
			String noTransaksi = params[0];
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("no", noTransaksi));
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
		if(result) {
			this.listTransaksi.remove(this.tDelete);
			this.adapter.notifyDataSetChanged();
			Toast.makeText(c, "Data transaksi telah dihapus.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(c, "Data transaksi gagal dihapus. " + this.error, Toast.LENGTH_SHORT).show();
		}
		
		Animation animation = AnimationUtils.loadAnimation(this.c, R.anim.fade_in);
		animation.setFillAfter(true);
		this.listContainerView.startAnimation(animation);
		//this.deleteCompleteInterface.OnDeleteComplete(result, this.position, this.error);
	}

	public void setListView(ListView listContainerView) {
		this.listContainerView = listContainerView;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
