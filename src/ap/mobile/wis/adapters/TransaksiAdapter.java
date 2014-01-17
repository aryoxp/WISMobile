package ap.mobile.wis.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ap.mobile.wis.R;
import ap.mobile.wis.base.Transaksi;

public class TransaksiAdapter extends BaseAdapter {

	private Context c;
	private ArrayList<Transaksi> listTransaksi;
	private OnClickListener detailClickListener;
	private OnClickListener deleteClickListener;
	//private Typeface condensed;
	
	public TransaksiAdapter(Context c, ArrayList<Transaksi> listTransaksi, 
			OnClickListener detailClickListener, OnClickListener deleteClickListener) {
		this.c = c;
		this.listTransaksi = listTransaksi;
		this.detailClickListener = detailClickListener;
		this.deleteClickListener = deleteClickListener;
		//this.condensed = TypefaceUtils.NewInstance(c).normalCondensed();
	}
	
	public void addItems(ArrayList<Transaksi> items) {
		this.listTransaksi.addAll(items);
	}
	
	@Override
	public int getCount() {
		return this.listTransaksi.size();
	}

	@Override
	public Object getItem(int position) {
		return this.listTransaksi.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(this.listTransaksi.get(position).no);
	}

	private class ViewHolder {
		TextView namaAkunView;
		TextView keteranganAkunView;
		TextView textNominalView;
		TextView tanggalTransaksiView;
		TextView kodeAkunDebitView;
		TextView kodeAkunKreditView;
		View itemContainer;
		View buttonDelete;
		int position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = ((Activity)c).getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_transaksi, null);
			ViewHolder vh = new ViewHolder();
			vh.namaAkunView = (TextView) rowView.findViewById(R.id.namaAkun);
			vh.keteranganAkunView = (TextView) rowView.findViewById(R.id.keteranganAkun);
			vh.textNominalView = (TextView) rowView.findViewById(R.id.textNominal);
			vh.tanggalTransaksiView = (TextView) rowView.findViewById(R.id.tanggalTransaksi);
			vh.kodeAkunDebitView = (TextView) rowView.findViewById(R.id.akunDebit);
			vh.kodeAkunKreditView = (TextView) rowView.findViewById(R.id.akunKredit);
			vh.itemContainer = (View) rowView.findViewById(R.id.itemTransaksiContainer);
			vh.buttonDelete = (View) rowView.findViewById(R.id.buttonDeleteItemTransaksi);
			rowView.setTag(vh);
		}
		ViewHolder vh = (ViewHolder) rowView.getTag();
		Transaksi t = this.listTransaksi.get(position);
		vh.position = position;
		vh.namaAkunView.setText(t.namaAkunPengeluaran);
		//vh.namaAkunView.setText(t.tanggal.replace('-', '/') + " " + t.jam);
				//+ this.listTransaksi.get(position).kodeAkunDebit + " "
				//+ this.listTransaksi.get(position).namaAkunPengeluaran);
		vh.keteranganAkunView.setText(t.keterangan);
		vh.textNominalView.setText(t.textNominal);
		vh.tanggalTransaksiView.setText(t.getTanggal());
		vh.kodeAkunDebitView.setText(t.kodeAkunDebit);
		vh.kodeAkunKreditView.setText(t.kodeAkunKredit);
		vh.itemContainer.setTag(t);
		vh.itemContainer.setOnClickListener((OnClickListener) this.detailClickListener);
		vh.buttonDelete.setTag(t);
		vh.buttonDelete.setOnClickListener((OnClickListener) this.deleteClickListener);
		/*
		vh.tanggalTransaksiView.setTypeface(condensed);
		vh.namaAkunView.setTypeface(condensed);
		vh.keteranganAkunView.setTypeface(condensed);
		vh.textNominalView.setTypeface(condensed);
		*/
		return rowView;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return this.getView(position, convertView, parent);
	}

}
