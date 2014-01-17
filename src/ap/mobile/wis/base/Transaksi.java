package ap.mobile.wis.base;

/*
 * {"no":"58495","tanggal":"2014-01-13","jam":"01:01:01",
 * "pengeluaran":"Biaya Promosi",
 * "dana":"Tunai - Kas Kecil",
 * "keterangan":"bayar kekurangan edufair Mojokerto","nominal":"101150",
 * "tnominal":"Rp. 101,150","kodeak":"102","kodead":"509"}
 */
public class Transaksi {
	
	public static final String[] Bulan = {
		"","Januari","Februari","Maret","April","Mei","Juni",
		"Juli","Agustus","September","Oktober","November",
		"Desember" 
		};
	
	public String no;
	public String tanggal;
	public String jam;
	public String namaAkunPengeluaran;
	public String namaAkunDana;
	public String keterangan;
	public String nominal;
	public String textNominal;
	public String kodeAkunKredit;
	public String kodeAkunDebit;
	public String kodeJenisTransaksi;
	
	public String user = "donna";
	
	
	public String getTanggalJam() {
		String[] t = tanggal.split("\\-");
		return t[2] + "/" + t[1] + "/" + t[0] + ", " + 
				jam.substring(0, 5);
	}
	
	public String getTanggal() {
		String[] t = tanggal.split("\\-");
		return t[2] + "/" + t[1] + "/" + t[0];
	}
	
	
}
