package ap.mobile.wis.interfaces;

import java.util.ArrayList;

import ap.mobile.wis.base.Transaksi;

public interface TransaksiLoadingInterface {
	public void OnLoadingComplete(ArrayList<Transaksi> listTransaksi);
}
