/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.android.car.settings.system;

import static android.os.UserManager.DISALLOW_FACTORY_RESET;

import static com.android.car.settings.common.PreferenceController.AVAILABLE;
import static com.android.car.settings.common.PreferenceController.DISABLED_FOR_USER;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.when;

import android.car.userlib.CarUserManagerHelper;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;

import androidx.preference.Preference;

import com.android.car.settings.common.PreferenceControllerTestHelper;
import com.android.car.settings.testutils.ShadowCarUserManagerHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowUserManager;

/** Unit test for {@link MasterClearEntryPreferenceController}. */
@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowCarUserManagerHelper.class})
public class MasterClearEntryPreferenceControllerTest {

    private Context mContext;
    private MasterClearEntryPreferenceController mController;
    @Mock
    private CarUserManagerHelper mCarUserManagerHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ShadowCarUserManagerHelper.setMockInstance(mCarUserManagerHelper);
        mContext = RuntimeEnvironment.application;

        mController = new PreferenceControllerTestHelper<>(mContext,
                MasterClearEntryPreferenceController.class,
                new Preference(mContext)).getController();
    }

    @After
    public void tearDown() {
        Settings.Global.putInt(mContext.getContentResolver(),
                Settings.Global.DEVICE_DEMO_MODE, 0);
        ShadowCarUserManagerHelper.reset();
    }

    @Test
    public void getAvailabilityStatus_nonAdminUser_disabledForUser() {
        setCurrentUserWithFlags(/* flags= */ 0);

        assertThat(mController.getAvailabilityStatus()).isEqualTo(DISABLED_FOR_USER);
    }

    @Test
    public void getAvailabilityStatus_adminUser_available() {
        setCurrentUserWithFlags(UserInfo.FLAG_ADMIN);

        assertThat(mController.getAvailabilityStatus()).isEqualTo(AVAILABLE);
    }

    @Test
    public void getAvailabilityStatus_adminUser_restricted_disabledForUser() {
        setCurrentUserWithFlags(UserInfo.FLAG_ADMIN);
        when(mCarUserManagerHelper.isCurrentProcessUserHasRestriction(
                DISALLOW_FACTORY_RESET)).thenReturn(true);

        assertThat(mController.getAvailabilityStatus()).isEqualTo(DISABLED_FOR_USER);
    }

    @Test
    public void getAvailabilityStatus_demoMode_demoUser_available() {
        setCurrentUserWithFlags(UserInfo.FLAG_DEMO);
        Settings.Global.putInt(mContext.getContentResolver(),
                Settings.Global.DEVICE_DEMO_MODE, 1);

        assertThat(mController.getAvailabilityStatus()).isEqualTo(AVAILABLE);
    }

    @Test
    public void getAvailabilityStatus_demoMode_demoUser_restricted_disabledForUser() {
        setCurrentUserWithFlags(UserInfo.FLAG_DEMO);
        when(mCarUserManagerHelper.isCurrentProcessUserHasRestriction(
                DISALLOW_FACTORY_RESET)).thenReturn(true);
        Settings.Global.putInt(mContext.getContentResolver(),
                Settings.Global.DEVICE_DEMO_MODE, 1);

        assertThat(mController.getAvailabilityStatus()).isEqualTo(DISABLED_FOR_USER);
    }

    private void setCurrentUserWithFlags(int flags) {
        UserInfo userInfo = new UserInfo(UserHandle.myUserId(), null, flags);
        when(mCarUserManagerHelper.isCurrentProcessAdminUser())
                .thenReturn(UserInfo.FLAG_ADMIN == (flags & UserInfo.FLAG_ADMIN));
        getShadowUserManager().addUser(userInfo.id, userInfo.name, userInfo.flags);
    }

    private ShadowUserManager getShadowUserManager() {
        return Shadows.shadowOf(UserManager.get(mContext));
    }
}
