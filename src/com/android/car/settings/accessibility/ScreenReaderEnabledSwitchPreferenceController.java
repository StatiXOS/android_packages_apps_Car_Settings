/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.car.settings.accessibility;

import android.car.drivingstate.CarUxRestrictions;
import android.content.Context;

import com.android.car.settings.R;
import com.android.car.settings.common.ColoredSwitchPreference;
import com.android.car.settings.common.FragmentController;
import com.android.car.settings.common.PreferenceController;

/**
 * Switch for enabling the default screen reader service.
 */
public class ScreenReaderEnabledSwitchPreferenceController extends
        PreferenceController<ColoredSwitchPreference> {

    public ScreenReaderEnabledSwitchPreferenceController(Context context, String preferenceKey,
            FragmentController fragmentController, CarUxRestrictions uxRestrictions) {
        super(context, preferenceKey, fragmentController, uxRestrictions);
    }

    @Override
    protected Class<ColoredSwitchPreference> getPreferenceType() {
        return ColoredSwitchPreference.class;
    }

    @Override
    protected void updateState(ColoredSwitchPreference preference) {
        getPreference().setTitle(getContext().getString(R.string.enable_screen_reader_toggle_title,
                ScreenReaderUtils.getScreenReaderName(getContext())));
        getPreference().setChecked(ScreenReaderUtils.isScreenReaderEnabled(getContext()));
    }

    @Override
    protected boolean handlePreferenceChanged(ColoredSwitchPreference preference,
            Object newValue) {
        boolean enableScreenReader = (Boolean) newValue;
        ScreenReaderUtils.setScreenReaderEnabled(getContext(), enableScreenReader);
        return true;
    }
}

