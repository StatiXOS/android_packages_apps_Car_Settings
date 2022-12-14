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
package com.android.car.settings.enterprise;

import static android.car.test.mocks.AndroidMockitoHelper.syncCallOnMainThread;

import static com.google.common.truth.Truth.assertWithMessage;

import androidx.fragment.app.Fragment;

import org.junit.Test;

public final class EnterprisePrivacySettingsActivityTest extends BaseEnterpriseTestCase {

    @Test
    public void testGetFragment() throws Exception {
        Fragment fragment = syncCallOnMainThread(() ->
                // EnterprisePrivacySettingsActivity must be created in the main thread
                new EnterprisePrivacySettingsActivity().getInitialFragment());
        assertWithMessage("initial fragment class").that(fragment)
                .isInstanceOf(EnterprisePrivacySettingsFragment.class);
    }
}
