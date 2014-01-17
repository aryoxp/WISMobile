package ap.mobile.wis;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import ap.mobile.wis.adapters.TransaksiAdapter;
import ap.mobile.wis.base.Transaksi;
import ap.mobile.wis.interfaces.TransaksiDeleteInterface;
import ap.mobile.wis.interfaces.TransaksiLoadingInterface;
import ap.mobile.wis.listeners.DeleteItemTransaksiAnimationListener;
import ap.mobile.wis.tasks.TransaksiDeleteTask;
import ap.mobile.wis.tasks.TransaksiPengeluaranTask;

public class PengeluaranFragment extends Fragment 
	implements View.OnClickListener, TransaksiLoadingInterface,
	OnScrollListener, TransaksiDeleteInterface, OnClickListener {

	private int page = 1;
	private TransaksiPengeluaranTask transaksiPengeluaranTask;
	private TransaksiDeleteTask transaksiDeleteTask;
	private ListView listContainerView;
	private TransaksiAdapter adapter; 
	private ArrayList<Transaksi> listTransaksi;
	
	private Transaksi tDelete;
	
	public PengeluaranFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_pengeluaran, container, false);
		
		this.listTransaksi = new ArrayList<Transaksi>();
		this.adapter = new TransaksiAdapter(getActivity(), this.listTransaksi, this, this);
		this.listContainerView = (ListView) view.findViewById(R.id.listPengeluaran);
		this.listContainerView.addFooterView(inflater.inflate(R.layout.view_loading_footer, null, false));
		this.listContainerView.setAdapter(this.adapter);
		this.listContainerView.setOnScrollListener(this);
		//this.listContainerView.setOnItemClickListener(this);
		
		transaksiPengeluaranTask = new TransaksiPengeluaranTask(getActivity(), this, this.listContainerView);
		transaksiPengeluaranTask.execute(page);
		
		ImageButton btRefresh = (ImageButton) view.findViewById(R.id.btRefresh);
		btRefresh.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(final View v) {
		switch(v.getId()) {
		case R.id.btRefresh:  
			if(this.transaksiPengeluaranTask.getStatus() == Status.RUNNING) {
				Toast.makeText(getActivity(), "Service is loading...", Toast.LENGTH_SHORT).show();
				return;
			}
			this.page = 1;
			this.listTransaksi.clear();
			this.listContainerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out));
			this.transaksiPengeluaranTask = new TransaksiPengeluaranTask(getActivity(), this, listContainerView);
			this.transaksiPengeluaranTask.execute(page);
			break;
		case R.id.itemTransaksiContainer:
			Transaksi tDetail = (Transaksi) v.getTag();
			TransaksiDetailDialogFragment fragment = 
					TransaksiDetailDialogFragment.NewInstance(tDetail);
			fragment.show(getActivity().getFragmentManager(), "detail");
			break;
		case R.id.buttonDeleteItemTransaksi:
			this.tDelete = (Transaksi) v.getTag();
			new AlertDialog.Builder(getActivity())
			.setTitle("Konfirmasi")
			.setMessage("Hapus transaksi?\n"+
			tDelete.getTanggal()+"\n"+
			tDelete.namaAkunPengeluaran+"\n"+tDelete.keterangan+"\n"+tDelete.textNominal+"")
			.setPositiveButton(android.R.string.yes, this)
			.setNegativeButton(android.R.string.no, null)
			.show();
			
			
			//this.listTransaksi.remove(tDelete);
			//this.adapter.notifyDataSetChanged();
			
			//
			//this.listTransaksi.remove(position);
			//this.adapter.notifyDataSetChanged();
			//TransaksiDeleteTask deleteTask = 
			//		new TransaksiDeleteTask(getActivity(), 
			//				this.listTransaksi, this.adapter, position, this);
			//deleteTask.setListView(this.listContainerView);
			//deleteTask.execute(this.listTransaksi.get(position).no);
			break;
		}
	}

	@Override
	public void OnLoadingComplete(ArrayList<Transaksi> listTransaksi) {
		this.adapter.addItems(listTransaksi);
		this.adapter.notifyDataSetChanged();
		if(page==1) {
			this.listContainerView.setSelection(0);
			this.listContainerView.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
			anim.setFillAfter(true);
			this.listContainerView.startAnimation(anim);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int lastInScreen = firstVisibleItem + visibleItemCount;
		if(this.transaksiPengeluaranTask != null) {
			AsyncTask.Status status = this.transaksiPengeluaranTask.getStatus();
			if(lastInScreen == totalItemCount && status != Status.RUNNING) {
				Log.d("onScroll", "loading started...");
				this.transaksiPengeluaranTask = new TransaksiPengeluaranTask(getActivity(), this, listContainerView);
				this.transaksiPengeluaranTask.execute(this.page + 1);
				this.page++;
			}
			
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void OnDeleteComplete(Boolean result, Integer position, String error) {
		//Transaksi t = this.listTransaksi.get(position);			
		//this.listTransaksi.remove(position);
		//this.adapter.notifyDataSetChanged();
		//Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
		//animation.setAnimationListener(new DeleteItemTransaksiAnimationListener(this.adapter));
		//View itemView = this.listContainerView.getChildAt(position);
		//itemView.startAnimation(animation);
		//Toast.makeText(getActivity(), "delete: ID: " + t.no, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(this.transaksiDeleteTask != null && this.transaksiDeleteTask.getStatus() == Status.RUNNING) {
			Toast.makeText(getActivity(), "Delete in progress...", Toast.LENGTH_SHORT).show();
		} else {
			if(tDelete != null) {
				this.transaksiDeleteTask = new TransaksiDeleteTask(getActivity(), this.listTransaksi, this.adapter, this.tDelete);
				this.transaksiDeleteTask.setListView(listContainerView);
				this.transaksiDeleteTask.execute(tDelete.no);
				Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
				animation.setFillAfter(true);
				this.listContainerView.startAnimation(animation);
			}
		}
	}
	
}
