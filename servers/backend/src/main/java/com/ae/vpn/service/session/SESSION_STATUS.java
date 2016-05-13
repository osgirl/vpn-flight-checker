package com.ae.vpn.service.session;

/**
 * Created by ae on 10-5-16.
 */
public enum SESSION_STATUS {
    FAILDED, // Internet connection was interupped for any reason
    UP, // Working :-)
    STARTING, // Not UP yet but, getting there :-)
    DOWN, // Destroyed don't access
}
