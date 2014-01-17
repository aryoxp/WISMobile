package ap.mobile.wis.base;

import java.util.ArrayList;

public class JenisTransaksi {
	public String kode;
	public ArrayList<Akun> akunDebit;
	public ArrayList<Akun> akunKredit;
	
	public JenisTransaksi(String kode, ArrayList<Akun> akunDebit, ArrayList<Akun> akunKredit) {
		this.kode = kode;
		this.akunDebit = akunDebit;
		this.akunKredit = akunKredit;
	}
}
