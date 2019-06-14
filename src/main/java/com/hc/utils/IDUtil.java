package com.hc.utils;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by GOPENEDD on 2019/5/25
 */
public class IDUtil {

    public static String CmdID() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

}
