/**
 * 
 */
package com.despegar.p13n.hestia.utils;

import java.net.InetAddress;

public class IPUtils {

    public static boolean isValidPublicIpAdress(InetAddress ipAddress) {
        return ipAddress != null && !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress();
    }


}
