/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.herve.coordinator.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.handmark.pulltorefresh.library.BuildConfig;
import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
	private final String LOG_TAG = "ptrRecyclerView";

	public PullToRefreshRecyclerView(Context context) {
		super(context);
	}

	public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshRecyclerView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(isReadyForPullStart() || isReadyForPullEnd()){
				getRefreshableView().stopScroll();
			}
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
		RecyclerView scrollView;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			scrollView = new InternalRecyclerViewSDK9(context, attrs);
		} else {
			scrollView = new RecyclerView(context, attrs);
		}
		scrollView.setLayoutManager(new LinearLayoutManager(getContext()));
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return isFirstItemVisible();
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return isLastItemVisible();
	}

	private boolean isFirstItemVisible() {
		final RecyclerView.Adapter adapter = getRefreshableView().getAdapter();

		if (null == adapter || adapter.getItemCount() == 0) {
			if (BuildConfig.DEBUG) {
				Log.d(LOG_TAG, "isFirstItemVisible. Empty View.");
			}
			return true;

		} else {

			/**
			 * This check should really just be:
			 * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
			 * internally use a HeaderView which messes the positions up. For
			 * now we'll just add one to account for it and rely on the inner
			 * condition which checks getTop().
			 */
			if (((LinearLayoutManager) getRefreshableView().getLayoutManager()).findFirstVisibleItemPosition() <= 1) {
				final View firstVisibleChild = getRefreshableView().getChildAt(0);
				if (firstVisibleChild != null) {
					return firstVisibleChild.getTop() >= getRefreshableView().getTop();
				}
			}
		}

		return false;
	}

	private boolean isLastItemVisible() {
		final RecyclerView.Adapter adapter = getRefreshableView().getAdapter();

		if (null == adapter || adapter.getItemCount() == 0) {
			if (BuildConfig.DEBUG) {
				Log.d(LOG_TAG, "isLastItemVisible. Empty View.");
			}
			return true;
		} else {
			final int lastItemPosition = getRefreshableView().getLayoutManager().getItemCount() - 1;
			final int lastVisiblePosition = ((LinearLayoutManager) getRefreshableView().getLayoutManager()).findLastVisibleItemPosition();

			if (BuildConfig.DEBUG) {
				Log.d(LOG_TAG, "isLastItemVisible. Last Item Position: " + lastItemPosition + " Last Visible Pos: "
						+ lastVisiblePosition);
			}

			/**
			 * This check should really just be: lastVisiblePosition ==
			 * lastItemPosition, but PtRListView internally uses a FooterView
			 * which messes the positions up. For me we'll just subtract one to
			 * account for it and rely on the inner condition which checks
			 * getBottom().
			 */
			if (lastVisiblePosition >= lastItemPosition - 1) {
				final int childIndex = lastVisiblePosition - ((LinearLayoutManager) getRefreshableView().getLayoutManager()).findFirstVisibleItemPosition();
				final View lastVisibleChild = getRefreshableView().getChildAt(childIndex);
				if (lastVisibleChild != null) {
					return lastVisibleChild.getBottom()  <= getRefreshableView().getBottom();
				}
			}
		}

		return false;
	}

	@TargetApi(9)
	final class InternalRecyclerViewSDK9 extends RecyclerView {

		public InternalRecyclerViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshRecyclerView.this, deltaX, scrollX, deltaY, scrollY,
					getScrollRange(), isTouchEvent);

			return returnValue;
		}

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}

	public void setAdapter(RecyclerView.Adapter adapter){
		getRefreshableView().setAdapter(adapter);
	}
}
