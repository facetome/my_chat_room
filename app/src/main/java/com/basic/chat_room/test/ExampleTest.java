package com.basic.chat_room.test;


import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.basic.chat_room.R;
import com.basic.chat_room.ui.LoginActivity;


/**
 * 单元测试类.
 */
public class ExampleTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Activity mActivity;
    private EditText mUsername;


    public ExampleTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mUsername = (EditText) mActivity.findViewById(R.id.value_username);

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testActivityTestCaseSetUpProperly() {
        assertNotNull("wangliangsen", mActivity);
        String password = mUsername.getEditableText().toString();
        assertEquals("wangliangsen", password);
    }


//    public void testLogin() {
//        int[] num = {1, 2, 3};
//        for (int i = 0; i <= num.length; i++) {
//        }
//    }
//
//    public void testLogin2() {
//        int[] num = {1, 2, 3};
//        for (int i = 0; i <= num.length; i++) {
//        }
//    }
}
