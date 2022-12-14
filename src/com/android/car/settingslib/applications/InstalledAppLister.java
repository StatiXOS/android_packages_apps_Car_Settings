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

package com.android.car.settingslib.applications;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.UserManager;

/**
 * TODO(b/208511815): copied from Settings "as-is"; ideally should be move to SettingsLib, but if
 * not, we should copy the unit tests as well.
 */
public abstract class InstalledAppLister extends AppLister {

    public InstalledAppLister(PackageManager packageManager, UserManager userManager) {
        super(packageManager, userManager);
    }

    @Override
    protected boolean includeInCount(ApplicationInfo info) {
        return InstalledAppCounter.includeInCount(PackageManager.INSTALL_REASON_POLICY, mPm, info);
    }
}
