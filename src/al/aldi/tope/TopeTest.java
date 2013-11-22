package al.aldi.tope;

import al.aldi.tope.view.activities.ClientsListActivity;
import al.aldi.tope.view.activities.TopeSettingsAcitivity;
import android.app.Activity;
import android.app.Instrumentation;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
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
    private Solo            solo              = null;

    public TopeTest() {
        super(Tope.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activityUnderTest = getActivity();
        mInstrumentation = getInstrumentation();
        solo = new Solo(getInstrumentation(), getActivity());

        mViewPager = (ViewPager) activityUnderTest.findViewById(R.id.pager);
    }

    public void testPreConditions() throws Exception {
        assertNotNull(mViewPager);
    }

    public void testShowActivityTopeServer() throws Exception {
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected ClientListActivity activity", activityName);
        solo.sleep(2000);
        // opening menu and clicking scan network
        solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_clients));
        solo.sleep(2000);
        // this is enough to prove that the activity does not crash
        assertTrue(solo.waitForActivity(ClientsListActivity.class, 3000));
        solo.finishOpenedActivities();
    }

    public void testShowActivitySettings() throws Exception {
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected TopeSettingsActivity activity", activityName);
        solo.sleep(2000);
        // opening menu and clicking scan network
        solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_settings));
        solo.sleep(2000);
        // this is enough to prove that the activity does not crash
        assertTrue(solo.waitForActivity(TopeSettingsAcitivity.class, 3000));
        solo.finishOpenedActivities();
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
