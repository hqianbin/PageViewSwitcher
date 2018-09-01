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

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PullToRefreshCoordinatorLayout extends PullToRefreshBase<LinearLayout> {

	public PullToRefreshCoordinatorLayout(Context context) {
		super(context);
	}

	public PullToRefreshCoordinatorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshCoordinatorLayout(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshCoordinatorLayout(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
		LinearLayout linearLayout = new LinearLayout(context);
		return linearLayout;
	}

	@Override
	protected boolean isReadyForPullStart() {

		LinearLayout layout = getRefreshableView();
		if(layout.getChildCount() > 0
				&& layout.getChildAt(0) instanceof CoordinatorLayout){
			CoordinatorLayout coordinatorLayout = (CoordinatorLayout)layout.getChildAt(0);
			if(coordinatorLayout.getChildCount() > 0
					&& coordinatorLayout.getChildAt(0) instanceof AppBarLayout){
				AppBarLayout appBarLayout = (AppBarLayout) coordinatorLayout.getChildAt(0);
				return appBarLayout.getTop() >= 0;
			}
		}
		return false;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return false;
	}

}
