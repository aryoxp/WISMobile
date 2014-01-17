package ap.mobile.wis.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import ap.mobile.wis.R;
import ap.mobile.wis.base.Akun;

public class AkunAdapter extends BaseAdapter 
	implements SpinnerAdapter {

	private Context c;
	private ArrayList<Akun> akunList;
	
	public AkunAdapter(Context c, ArrayList<Akun> akunList) {
		this.c = c;
		this.akunList = akunList;
	}
	
	@Override
	public int getCount() {
		return this.akunList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.akunList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(this.akunList.get(position).kode);
	}

	private class ViewHolder {
		TextView namaAkunView;
		TextView keteranganAkunView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_spinner_akun, null);
			ViewHolder vh = new ViewHolder();
			vh.namaAkunView = (TextView) rowView.findViewById(R.id.namaAkun);
			vh.keteranganAkunView = (TextView) rowView.findViewById(R.id.keteranganAkun);
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		vh.namaAkunView.setText(this.akunList.get(position).kode + " "
				+ this.akunList.get(position).nama);
		vh.keteranganAkunView.setText(this.akunList.get(position).keterangan);
		return rowView;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return this.getView(position, convertView, parent);
	}

}
