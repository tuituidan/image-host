package com.tuituidan.oss.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * IpUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/11/6
 */
@UtilityClass
@Slf4j
public class IpUtils {

    /**
     * 获取本地IP.
     *
     * @return IP
     */
    public static String getIpAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    // 排除loopback类型地址
                    if (inetAddr.isLoopbackAddress()) {
                        continue;
                    }
                    if (inetAddr.isSiteLocalAddress()) {
                        return inetAddr.getHostAddress();
                    }
                    if (candidateAddress == null) {
                        candidateAddress = inetAddr;
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("InetAddress.getLocalHost获取失败.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception ex) {
            log.error("获取本机IP失败", ex);
        }
        return "";
    }
}
