package al.aldi.tope;

import al.aldi.tope.view.activities.ClientsListActivity;
import al.aldi.tope.view.activities.TopeSettingsAcitivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import com.jayway.android.robotium.solo.Solo;

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

    private Activity        activityUnderTest = null;
    private Instrumentation mInstrumentation  = null;
    private ViewPager       mViewPager        = null;

    public TopeTest() {
        super(Tope.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activityUnderTest = getActivity();
        mInstrumentation = getInstrumentation();

        mViewPager = (ViewPager) activityUnderTest.findViewById(R.id.pager);
    }

    public void testPreConditions() throws Exception {
        assertNotNull(mViewPager);
    }

    public void testShowActivityTopeServer() throws Exception {
        // Adding monitor to listen to activity start
        Instrumentation.ActivityMonitor am = mInstrumentation.addMonitor(ClientsListActivity.class.getName(), null, false);
        // Click the menu option
        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        mInstrumentation.invokeMenuActionSync(activityUnderTest, R.id.action_clients, 0);
        // Check if the activity was started and close it afterwards
        Activity a = mInstrumentation.waitForMonitorWithTimeout(am, 1000);
        assertTrue(mInstrumentation.checkMonitorHit(am, 1));
        a.finish();
    }

    public void testShowActivitySettings() throws Exception {
        Instrumentation.ActivityMonitor am = mInstrumentation.addMonitor(TopeSettingsAcitivity.class.getName(), null, false);

        mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        mInstrumentation.invokeMenuActionSync(activityUnderTest, R.id.action_settings, 0);

        Activity settingsActivity = mInstrumentation.waitForMonitorWithTimeout(am, 1000);
        assertTrue(mInstrumentation.checkMonitorHit(am, 1));
        settingsActivity.finish();
    }

    public void testClickThroughPagerTabStrip() throws Exception {
        swipe(Direction.Right);
        swipe(Direction.Right);
        swipe(Direction.Left);
        swipe(Direction.Left);
    }

    protected void swipe(final Direction direction) {
        activityUnderTest.runOnUiThread(new Runnable() {
            public void run() {
                int current = mViewPager.getCurrentItem();
                if (direction == Direction.Right) {
                    if (current > 0) {
                        mViewPager.setCurrentItem(current - 1, true);
                    }
                } else {
                    if (current < mViewPager.getChildCount()) {
                        mViewPager.setCurrentItem(current + 1, true);
                    }
                }
            }
        });
    }

    public enum Direction {
        Left, Right;
    }
}
