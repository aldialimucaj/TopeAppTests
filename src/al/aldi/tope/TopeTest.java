package al.aldi.tope;

import al.aldi.tope.view.activities.ClientsListActivity;
import al.aldi.tope.view.activities.TopeSettingsAcitivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class al.aldi.tope.TopeTest \
 * al.aldi.tope.tests/android.test.InstrumentationTestRunner
 */
public class TopeTest extends ActivityInstrumentationTestCase2<Tope> {

    Activity activityUnderTest   = null;
    Menu     topeMenu            = null;
    MenuItem menuItemTopeServers = null;

    public TopeTest() {
        super(Tope.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activityUnderTest = getActivity();
    }

    public void testPreConditions() throws Exception {
    }

    public void testShowActivityTopeServer() throws Exception {
        // Adding monitor to listen to activity start
        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(ClientsListActivity.class.getName(), null, false);
        // Click the menu option
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        getInstrumentation().invokeMenuActionSync(activityUnderTest, R.id.action_clients, 0);
        // Check if the activity was started and close it afterwards
        Activity a = getInstrumentation().waitForMonitorWithTimeout(am, 1000);
        assertTrue(getInstrumentation().checkMonitorHit(am, 1));
        a.finish();
    }

    public void testShowActivitySettings() throws Exception {
        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(TopeSettingsAcitivity.class.getName(), null, false);

        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        getInstrumentation().invokeMenuActionSync(activityUnderTest, R.id.action_settings, 0);

        Activity settingsActivity = getInstrumentation().waitForMonitorWithTimeout(am, 1000);
        assertTrue(getInstrumentation().checkMonitorHit(am, 1));
        settingsActivity.finish();
    }
}
