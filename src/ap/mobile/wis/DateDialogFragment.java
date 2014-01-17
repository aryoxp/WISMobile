package ap.mobile.wis;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import ap.mobile.wis.interfaces.DateInterface;

public class DateDialogFragment extends DialogFragment 
	implements OnClickListener {
	
	private int year;
	private int month;
	private int day;
	
	private DatePicker dp;
	private DateInterface dateInterfaceCallback;
	
	public DateDialogFragment(){
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setDateDialogInterfaceCallback(DateInterface dateInterface) {
		this.dateInterfaceCallback = dateInterface;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d = super.onCreateDialog(savedInstanceState);
		d.setTitle("Tanggal Transaksi");
		return d;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_datepicker, container, false);
		View buttonOK = v.findViewById(R.id.buttonDatePickerOK);
		
		
		this.dp = (DatePicker) v.findViewById(R.id.datePicker);
		this.dp.init(year, month, day, null);
		
		buttonOK.setOnClickListener(this);
		
		return v;
	}

	public String getDateString() {
		String dateString = String.valueOf(year) + "-";
		if(month < 10)
			dateString += "0"+ String.valueOf(month+1) + "-";
		else dateString += String.valueOf(month+1) + "-";
		if(day < 10)
			dateString += "0"+String.valueOf(day);
		else dateString += String.valueOf(day);
		return dateString;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonDatePickerOK) {
			this.year = this.dp.getYear();
			this.month = this.dp.getMonth();
			this.day = this.dp.getDayOfMonth();
			if(this.dateInterfaceCallback != null)
				this.dateInterfaceCallback.OnDateSelected(year, month, day);
			this.dismiss(); 
		}
	}
	
	
}
