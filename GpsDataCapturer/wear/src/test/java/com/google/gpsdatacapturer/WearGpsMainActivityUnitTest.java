package com.google.gpsdatacapturer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.sharedlibrary.service.GpsDataCaptureService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class WearGpsMainActivityUnitTest {
    private WearGpsMainActivity wearGpsMainActivity;
    private Button startAndStopButton;
    private RadioGroup apiRadioGroup;
    private RadioButton lmRadioButton;
    private RadioButton flpRadioButton;
    private TextView gpsDataTextView;
    private TextView gpsStatusTextView;

    @Before
    public void setUp() {
        wearGpsMainActivity = Robolectric.buildActivity(WearGpsMainActivity.class).create().get();
        startAndStopButton = wearGpsMainActivity.findViewById(R.id.button_start_stop);
        apiRadioGroup = wearGpsMainActivity.findViewById(R.id.radio_group_location_api);
        lmRadioButton = wearGpsMainActivity.findViewById(R.id.radio_button_LM);
        flpRadioButton = wearGpsMainActivity.findViewById(R.id.radio_button_FLP);
        gpsDataTextView = wearGpsMainActivity.findViewById(R.id.text_view_gps_data);
        gpsStatusTextView = wearGpsMainActivity.findViewById(R.id.text_view_gps_status);

    }

    @Test
    public void testWearGpsMainActivityNotNull() {
        assertNotNull(wearGpsMainActivity);
    }

    @Test
    public void testButtonsAndViewsNotNull() {
        assertNotNull(startAndStopButton);
        assertNotNull(apiRadioGroup);
        assertNotNull(lmRadioButton);
        assertNotNull(flpRadioButton);
        assertNotNull(gpsDataTextView);
        assertNotNull(gpsStatusTextView);
    }

    @Test
    public void testInitialUIShowingCorrect() {
        assertEquals(View.VISIBLE, apiRadioGroup.getVisibility());
        assertEquals(View.VISIBLE, startAndStopButton.getVisibility());
        assertEquals("START", startAndStopButton.getText());
        assertEquals(View.GONE, gpsDataTextView.getVisibility());
        assertEquals(View.GONE, gpsStatusTextView.getVisibility());
    }

    @Test
    public void testLmRadioButtonChecked() {
        lmRadioButton.performClick();
        assertEquals(R.id.radio_button_LM, apiRadioGroup.getCheckedRadioButtonId());
    }

    @Test
    public void testFlpRadioButtonChecked() {
        flpRadioButton.performClick();
        assertEquals(R.id.radio_button_FLP, apiRadioGroup.getCheckedRadioButtonId());
    }

    @Test
    public void testApiRadioGroupHideOnStartButtonClicked() {
        startAndStopButton.performClick();
        assertEquals(View.GONE, apiRadioGroup.getVisibility());
    }

    @Test
    public void testStartAndStopButtonShowCorrectText() {
        assertEquals("START", startAndStopButton.getText());
        startAndStopButton.performClick();
        assertEquals("STOP", startAndStopButton.getText());
    }

    @Test
    public void testStartButtonClickShouldStartNewIntentService() {
        lmRadioButton.performClick();
        startAndStopButton.performClick();

        Intent expectedIntent = new Intent(wearGpsMainActivity, GpsDataCaptureService.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(wearGpsMainActivity);
        Intent shadowIntent = shadowActivity.getNextStartedService();

        assertTrue(shadowIntent.filterEquals(expectedIntent));
        assertEquals(View.VISIBLE, gpsDataTextView.getVisibility());
        assertEquals(View.VISIBLE, gpsStatusTextView.getVisibility());
    }
}
