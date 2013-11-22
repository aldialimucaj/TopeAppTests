package al.aldi.tope;

import al.aldi.libjaldi.string.AldiStringUtils;
import al.aldi.tope.utils.TopeUtils;
import al.aldi.tope.view.activities.ClientsListActivity;
import al.aldi.tope.view.activities.ScanServersActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;

/**
 * User: Aldi Alimucaj
 * Date: 15.11.13
 * Time: 23:46
 * <p/>
 * Tests for Client List Activity. All tests require at least one server running and listening to default port.
 */
public class ClientListActivityTest extends ActivityInstrumentationTestCase2<ClientsListActivity> {
    public static final String   TEST_SERVER       = "192.168.178.50";
    private             Solo     solo              = null;
    private             Activity activityUnderTest = null;

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
        solo.finishOpenedActivities();
    }

    public void testAddNewServer() throws Exception {
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected ClientListActivity activity", activityName);
        solo.sleep(2000);
        int initialCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        if (0 == initialCount) {
            solo.waitForText(activityUnderTest.getString(R.string.client_list_empty), 1, 1000);
            solo.clickOnButton(activityUnderTest.getString(R.string.no));
        }

        // opening add and edit activity
        solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_clients_add));

        String serverName = AldiStringUtils.random(5);
        solo.enterText(0, serverName);
        solo.enterText(1, TEST_SERVER);
        solo.clickOnButton(activityUnderTest.getString(R.string.title_button_ok));
        assertTrue(solo.waitForActivity(activityUnderTest.getClass(), 1000));
        // waiting for response to come
        solo.waitForText("(?i).*?successfully", 1, 10000);
        // waiting for response to be processed
        solo.sleep(3000);

        // it should appear in the list
        ListView lv = ((ListActivity) activityUnderTest).getListView();
        int secondCount = lv.getCount();
        //assertTrue(secondCount == initialCount + 1);

        // checking if new server appears in list
        assertTrue(solo.searchText(serverName, true));
        // cleaning up the added server
        solo.clickLongOnText(serverName);
        solo.clickOnText(activityUnderTest.getString(R.string.client_edit_delete));
        solo.clickOnText(activityUnderTest.getString(R.string.yes));
        solo.sleep(2000);
        int thirdCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        assertTrue(initialCount == thirdCount);
    }

    /**
     * Testing synchronize Client. It requires a server to be available
     *
     * @throws Exception
     */
    public void testSynchronizeServer() throws Exception {
        boolean newItemAdded = false;
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected ClientListActivity activity", activityName);
        solo.sleep(2000);
        // there should be at least one item
        int listCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        // if not add one to synchronize
        if (listCount < 1) {
            solo.waitForText(activityUnderTest.getString(R.string.client_list_empty), 1, 1000);
            solo.clickOnButton(activityUnderTest.getString(R.string.no));
            // opening add and edit activity
            solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_clients_add));
            String serverName = AldiStringUtils.random(5);
            solo.enterText(0, serverName);
            solo.enterText(1, TEST_SERVER);
            solo.clickOnButton(activityUnderTest.getString(R.string.title_button_ok));
            assertTrue(solo.waitForActivity(activityUnderTest.getClass(), 1000));
            // waiting for response to come
            solo.waitForText(activityUnderTest.getString(R.string.task_executed_sccuessfully), 1, 10000);
            // waiting for response to be processed
            solo.sleep(3000);
            newItemAdded = true;
        }
        int secondCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        //assertTrue(secondCount == 1);
        solo.clickLongInList(1);
        solo.clickOnText(activityUnderTest.getString(R.string.client_edit_synchronize));
        solo.waitForText(activityUnderTest.getString(R.string.client_edit_synchronize_client), 1, 1000);
        solo.waitForText(activityUnderTest.getString(R.string.task_executed_sccuessfully), 1, 10000);
        solo.sleep(2000);

        if (newItemAdded) {
            solo.clickLongInList(1);
            solo.clickOnText(activityUnderTest.getString(R.string.client_edit_delete));
            solo.clickOnText(activityUnderTest.getString(R.string.yes));
            solo.sleep(2000);
        }
        int thirdCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        assertTrue(listCount == thirdCount);
    }


    public void testEditServer() throws Exception {
        boolean newItemAdded = false;
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected ClientListActivity activity", activityName);
        solo.sleep(2000);
        // there should be at least one item
        int listCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        // click no if list is empty
        if (listCount == 0) {
            solo.waitForText(activityUnderTest.getString(R.string.client_list_empty), 1, 1000);
            solo.clickOnButton(activityUnderTest.getString(R.string.no));
        }
        // opening add and edit activity
        solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_clients_add));
        String serverName = AldiStringUtils.random(5);
        solo.enterText(0, serverName);
        solo.enterText(1, TEST_SERVER);
        solo.clickOnButton(activityUnderTest.getString(R.string.title_button_ok));
        assertTrue(solo.waitForActivity(activityUnderTest.getClass(), 1000));
        // waiting for response to come
        solo.waitForText(activityUnderTest.getString(R.string.task_executed_sccuessfully), 1, 10000);
        // waiting for response to be processed
        solo.sleep(3000);
        newItemAdded = true;

        int secondCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        //assertTrue(secondCount == listCount + 1);
        solo.clickLongOnText(serverName);
        solo.clickOnText(activityUnderTest.getString(R.string.client_edit_edit));
        assertTrue(solo.searchText(serverName));
        assertTrue(solo.searchText(TEST_SERVER));
        assertTrue(solo.searchText(TopeUtils.TOPE_DEFAULT_PORT));
        solo.clickOnButton(activityUnderTest.getString(R.string.title_button_cancel));

        solo.clickLongOnText(serverName);
        solo.clickOnText(activityUnderTest.getString(R.string.client_edit_delete));
        solo.clickOnText(activityUnderTest.getString(R.string.yes));
        solo.sleep(2000);

        int thirdCount = ((ListActivity) activityUnderTest).getListView().getAdapter().getCount();
        assertTrue(listCount == thirdCount);
    }

    public void testScanNetwork() throws Exception {
        String activityName = activityUnderTest.getLocalClassName().substring(activityUnderTest.getLocalClassName().lastIndexOf(".") + 1);
        solo.assertCurrentActivity("Expected ClientListActivity activity", activityName);
        solo.sleep(2000);
        // opening menu and clicking scan network
        solo.clickOnMenuItem(activityUnderTest.getString(R.string.action_clients_scan_network));
        solo.sleep(2000);
        // this is enough to prove that the activity does not crash
        assertTrue(solo.waitForActivity(ScanServersActivity.class, 3000));
    }
}
