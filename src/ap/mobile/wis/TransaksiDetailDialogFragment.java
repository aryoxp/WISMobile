package ap.mobile.wis;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import ap.mobile.utils.TypefaceUtils;
import ap.mobile.wis.base.Transaksi;

public class TransaksiDetailDialogFragment extends DialogFragment 
	implements OnClickListener {
	
	private Transaksi t;
	
	public TransaksiDetailDialogFragment(){}
	public static TransaksiDetailDialogFragment NewInstance(Transaksi t) {
		TransaksiDetailDialogFragment detail = new TransaksiDetailDialogFragment();
		detail.setTransaksi(t);
		return detail;
	}
	
	private void setTransaksi(Transaksi t) {
		this.t = t;
	}
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog);

		// request a window without the title
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		//LayoutParams lp = dialog.getWindow().getAttributes();
		//DisplayMetrics dm = getResources().getDisplayMetrics();
		//lp.width = (int) (dm.widthPixels * 0.8);
		//lp.height = (int) (dm.heightPixels * 0.8);
		//dialog.getWindow().setLayout(1000, 1000);
		
		return dialog;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//int width = getActivity().getResources().getDimensionPixelSize((int) (getResources().getDisplayMetrics().widthPixels * 0.8));
		//int height = getResources().getDimensionPixelSize((int) (getResources().getDisplayMetrics().heightPixels *0.8));        
		//getDialog().getWindow().setLayout(700, 800);
		LayoutParams lp = getDialog().getWindow().getAttributes();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		lp.width = (int) (dm.widthPixels * 0.9);
		getDialog().getWindow().setAttributes(lp);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_transaksi_detail, container, false);
		View buttonOK = v.findViewById(R.id.buttonOKTransaksiDetail);
		
		TextView noTransaksi = (TextView) v.findViewById(R.id.noTransaksiDetailText);
		TextView tanggalTransaksi = (TextView) v.findViewById(R.id.tanggalTransaksiDetailText);
		TextView nominalTransaksi = (TextView) v.findViewById(R.id.nominalTransaksiDetailText);
		TextView namaAkunDebitTransaksi = (TextView) v.findViewById(R.id.namaAkunDebitTransaksiDetailText);
		TextView namaAkunKreditTransaksi = (TextView) v.findViewById(R.id.namaAkunKreditTransaksiDetailText);
		TextView keteranganTransaksi = (TextView) v.findViewById(R.id.keteranganTransaksiDetailText);
		TextView namaUserTransaksi = (TextView) v.findViewById(R.id.namaUserTransaksiDetailText);
		
		buttonOK.setOnClickListener(this);
		
		noTransaksi.setText("Transaksi #" + this.t.no);
		noTransaksi.setTypeface(TypefaceUtils.NewInstance(getActivity()).normalCondensed());
		tanggalTransaksi.setText(t.getTanggal());
		nominalTransaksi.setText(t.textNominal);
		namaAkunDebitTransaksi.setText(t.namaAkunPengeluaran);
		namaAkunKreditTransaksi.setText(t.namaAkunDana);
		keteranganTransaksi.setText(t.keterangan);
		namaUserTransaksi.setText(t.user);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonOKTransaksiDetail) {
			this.dismiss(); 
		}
	}
	
	
}
