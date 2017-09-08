package com.yazhi1992.paddemo;

/**
 * Created by zengyazhi on 2017/9/8.
 */

public class StartFragmentEvent {
    String mName;

    public StartFragmentEvent(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
