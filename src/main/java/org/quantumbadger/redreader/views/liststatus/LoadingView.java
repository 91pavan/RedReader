/*******************************************************************************
 * This file is part of RedReader.
 *
 * RedReader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedReader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RedReader.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.quantumbadger.redreader.views.liststatus;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.TextView;

public final class LoadingView extends StatusListItemView {

	private final TextView textView;
	private final ProgressBar progressBarView;

	private static final int LOADING_INDETERMINATE = -1, LOADING_DONE = -2;

	private final Handler loadingHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(final Message msg) {

			textView.setText((String)msg.obj);

			if(msg.what == LOADING_INDETERMINATE) {
				progressBarView.setIndeterminate(true);

			} else if(msg.what == LOADING_DONE) {
				progressBarView.setIndeterminate(false);
				progressBarView.setProgress(100);
				hide(500);

			} else {
				progressBarView.setIndeterminate(false);
				progressBarView.setProgress(msg.what);
			}
		}
	};

	public void setIndeterminate(final int textRes) {
		sendMessage(getContext().getString(textRes), LOADING_INDETERMINATE);
	}

	public void setProgress(final int textRes, final float fraction) {
		sendMessage(getContext().getString(textRes), Math.round(fraction * 100));
	}

	public void setDone(final int textRes) {
		sendMessage(getContext().getString(textRes), LOADING_DONE);
	}

	public void setIndeterminate(final String text) {
		sendMessage(text, LOADING_INDETERMINATE);
	}

	public void setProgress(final String text, final float fraction) {
		sendMessage(text, Math.round(fraction * 100));
	}

	public void setDone(final String text) {
		sendMessage(text, LOADING_DONE);
	}

	private void sendMessage(final String text, final int what) {
		final Message msg = new Message();
		msg.obj = text;
		msg.what = what;
		loadingHandler.sendMessage(msg);
	}

	public LoadingView(final Context context, final int initialTextRes, final boolean progressBarEnabled, final boolean indeterminate) {
		this(context, context.getString(initialTextRes), progressBarEnabled, indeterminate);
	}

	// TODO make XML
	public LoadingView(final Context context, final String initialText, final boolean progressBarEnabled, final boolean indeterminate) {

		super(context);

		textView = new TextView(context);
		textView.setText(initialText);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(15.0f);
		textView.setPadding((int)(15 * dpScale), (int)(10 * dpScale), (int)(10 * dpScale), (int)(4 * dpScale));
		textView.setSingleLine(true);
		textView.setEllipsize(TextUtils.TruncateAt.END);

		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(textView);

		if(progressBarEnabled) {
			progressBarView = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
			progressBarView.setMax(100);
			progressBarView.setProgress(0);
			progressBarView.setIndeterminate(indeterminate);
			progressBarView.setPadding((int)(10 * dpScale), 0, (int)(10 * dpScale), (int)(2 * dpScale));
			layout.addView(progressBarView);

		} else {
			progressBarView = null;
		}

		setContents(layout);

		setBackgroundColor(Color.rgb(0x00, 0x99, 0xCC));
	}
}
