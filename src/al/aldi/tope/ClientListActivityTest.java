package al.aldi.tope;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.view.activities.ClientsListActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;

/**
 * User: Aldi Alimucaj
 * Date: 15.11.13
 * Time: 23:46
 * <p/>
 * [Add Description]
 */
public class ClientListActivityTest extends ActivityInstrumentationTestCase2<ClientsListActivity> {
    private Solo     solo              = null;
    private Activity activityUnderTest = null;

    public ClientListActivityTest() {
        super(ClientsListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activityUnderTest = getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
//        solo.finishOpenedActivities();
    }

    public void testAddNewServer() throws Exception {
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected ClientListActivity activity", activityName);
        solo.sleep(2000);
        int initialCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();

        // opening add and edit activity
        solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_clients_add));
        String serverName = AldiStringUtils.random(5);
        solo.enterText(0, serverName);
        solo.enterText(1, "192.168.178.50");
        solo.clickOnButton(activityUnderTest.getString(R.string.title_button_ok));
        assert (solo.waitForActivity(activityUnderTest.getClass(), 1000));
        // waiting for response to come
        assert (solo.waitForText(activityUnderTest.getString(R.string.task_executed_sccuessfully), 1, 10000));
        // waiting for response to be processed
        solo.sleep(3000);

        // it should appear in the list
        int secondCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        assert (secondCount == initialCount + 1);

        // checking if new server appears in list
        assert (solo.searchText(serverName, true));
        // cleaning up the added server
        solo.clickLongOnText(serverName);
        solo.clickOnText(activityUnderTest.getString(R.string.client_edit_delete));
        solo.clickOnText(activityUnderTest.getString(R.string.yes));
        solo.sleep(2000);
        int thirdCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        assert (initialCount == thirdCount);
    }
}
