package ap.mobile.wis.listeners;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;

public class DeleteItemTransaksiAnimationListener implements AnimationListener{

	private BaseAdapter adapter;
	
	public DeleteItemTransaksiAnimationListener(BaseAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onAnimationStart(Animation animation) {}

}
