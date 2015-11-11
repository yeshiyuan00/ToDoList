package com.ysy.conquer.otto;

import com.squareup.otto.Bus;

/**
 * User: ysy
 * Date: 2015/11/11
 * Time: 12:04
 */
public final class BusProvider {
    private static final Bus bus = new Bus();
    public static Bus getInstance(){
        return bus;
    }
    private BusProvider(){}
}
