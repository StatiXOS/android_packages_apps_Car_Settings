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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;

import java.util.List;

/**
 * TODO(b/208511815): copied from Settings "as-is"; ideally should be move to SettingsLib, but if
 * not, we should copy the unit tests as well.
 */
public abstract class InstalledAppCounter extends AppCounter {

    /**
     * Count all installed packages, irrespective of install reason.
     */
    public static final int IGNORE_INSTALL_REASON = -1;

    private final int mInstallReason;

    public InstalledAppCounter(Context context, int installReason,
            PackageManager packageManager) {
        super(context, packageManager);
        mInstallReason = installReason;
    }

    @Override
    protected boolean includeInCount(ApplicationInfo info) {
        return includeInCount(mInstallReason, mPm, info);
    }

    /**
     * TODO(b/208511815): add javadoc if it's not moved to SettingsLib.
     */
    public static boolean includeInCount(int installReason, PackageManager pm,
            ApplicationInfo info) {
        final int userId = UserHandle.getUserId(info.uid);
        if (installReason != IGNORE_INSTALL_REASON
                && pm.getInstallReason(info.packageName,
                        new UserHandle(userId)) != installReason) {
            return false;
        }
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        }
        if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        Intent launchIntent = new Intent(Intent.ACTION_MAIN, null)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setPackage(info.packageName);
        List<ResolveInfo> intents = pm.queryIntentActivitiesAsUser(
                launchIntent,
                PackageManager.GET_DISABLED_COMPONENTS
                        | PackageManager.MATCH_DIRECT_BOOT_AWARE
                        | PackageManager.MATCH_DIRECT_BOOT_UNAWARE,
                userId);
        return intents != null && intents.size() != 0;
    }
}
