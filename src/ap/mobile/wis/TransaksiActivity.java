package ap.mobile.wis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ap.mobile.utils.TypefaceUtils;
import ap.mobile.wis.base.Akun;
import ap.mobile.wis.base.Transaksi;
import ap.mobile.wis.interfaces.DateInterface;
import ap.mobile.wis.interfaces.TransaksiSaveInterface;
import ap.mobile.wis.tasks.AkunDebitKreditTransaksiTask;
import ap.mobile.wis.tasks.TransaksiSaveTask;

public class TransaksiActivity extends Activity 
	implements OnClickListener, DateInterface, TransaksiSaveInterface {
	
	private String kodeTransaksi;
	private TextView titleText;
	private TextView tanggalText;
	private TextView loadingText;
	private EditText nominalText;
	private EditText keteranganText;
	private Spinner akunDebitSpinner;
	private Spinner akunKreditSpinner;
	private View formContainer;
	private Typeface typeface;
	
	private int day, month, year;
	
	private DateDialogFragment dateDialogFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.kodeTransaksi = this.getIntent().getExtras().getString("kode");
		
		this.setContentView(R.layout.activity_transaksi);
		this.typeface = TypefaceUtils.NewInstance(this).normalCondensed();
		
		this.akunDebitSpinner = (Spinner) this.findViewById(R.id.spinnerAkunDebit);
		this.akunKreditSpinner = (Spinner) this.findViewById(R.id.spinnerAkunKredit);
		this.nominalText = (EditText) this.findViewById(R.id.nominal);
		this.keteranganText = (EditText) this.findViewById(R.id.keterangan);
		this.loadingText = (TextView) this.findViewById(R.id.loadingTransaksiStatusText);
		this.formContainer = (View) this.findViewById(R.id.formContainer);
		TextView titleText = (TextView) this.findViewById(R.id.titleTransaksi);
		titleText.setTypeface(typeface);
		
		this.tanggalText = (TextView) this.findViewById(R.id.tanggalTransaksiText);
		
		final Calendar c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);
 
		updateTanggalTextView();
		
		AkunDebitKreditTransaksiTask task = new AkunDebitKreditTransaksiTask(this, this.kodeTransaksi);
		task.setAkunView(akunDebitSpinner, akunKreditSpinner);
		task.setContainerView(formContainer);
		task.execute();
		
		Button buttonCancel = (Button) this.findViewById(R.id.buttonCancel);
		Button buttonSave = (Button) this.findViewById(R.id.buttonSave);
		
		buttonCancel.setTypeface(typeface);
		buttonSave.setTypeface(typeface);
		
		buttonCancel.setOnClickListener(this);
		buttonSave.setOnClickListener(this);
		tanggalText.setOnClickListener(this);
		this.findViewById(R.id.buttonResetDate).setOnClickListener(this);
		
		this.dateDialogFragment = new DateDialogFragment();
	    this.dateDialogFragment.setDateDialogInterfaceCallback(this);
		
	}

	private void updateTanggalTextView() {
		// set current date into textview
		this.tanggalText.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(day).append(" ")
			.append(Transaksi.Bulan[month + 1]).append(" ")
			.append(year));
	}
	
	public void setTitle(String title) {
		titleText.setText(title);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tanggalTransaksiText:

		    this.dateDialogFragment.show(getFragmentManager(), "dialog");
		    
			break;
		case R.id.buttonSave:
			
			Transaksi transaksi = new Transaksi();
			
			transaksi.tanggal = this.dateDialogFragment.getDateString();
			transaksi.nominal = this.nominalText.getText().toString();
			transaksi.keterangan = this.keteranganText.getText().toString();
			
			Akun akunDebit = (Akun) this.akunDebitSpinner.getSelectedItem();
			Akun akunKredit = (Akun) this.akunKreditSpinner.getSelectedItem();
			
			transaksi.kodeAkunDebit = akunDebit.kode;
			transaksi.kodeAkunKredit = akunKredit.kode;
			transaksi.kodeJenisTransaksi = this.kodeTransaksi;
			
			DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.US);			
			transaksi.jam = df.format(Calendar.getInstance().getTime());
			
			TransaksiSaveTask saveTask = new TransaksiSaveTask(this.getApplication(), this);
			saveTask.execute(transaksi);
			
			Log.d("Start save", this.dateDialogFragment.getDateString());
			Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.fade_out);
			animation.setFillAfter(true);
			this.loadingText.setText("Saving data transaksi\nPlease wait...");
			this.formContainer.startAnimation(animation);
			
			break;
		case R.id.buttonCancel:
			TransaksiActivity.this.finish();
			break;
		case R.id.buttonResetDate:
			final Calendar c = Calendar.getInstance();
			this.year = c.get(Calendar.YEAR);
			this.month = c.get(Calendar.MONTH);
			this.day = c.get(Calendar.DAY_OF_MONTH);
			this.updateTanggalTextView();
			break;
		}
	}

	@Override
	public void OnDateSelected(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.updateTanggalTextView();
	}

	@Override
	public void OnSaveComplete(Boolean result, String error) {
		this.loadingText.setText("");
		Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.fade_in);
		animation.setFillAfter(true);
		this.formContainer.startAnimation(animation);
		if(result) {
			Toast.makeText(getApplication(), "Data saved successfully. ", Toast.LENGTH_SHORT)
			.show();
		} else {
			Toast.makeText(getApplication(), "Unable to save data. " + error, Toast.LENGTH_SHORT)
			.show();
		}
		
	}
	
}
