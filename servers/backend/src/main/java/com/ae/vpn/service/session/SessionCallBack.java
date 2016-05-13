package com.ae.vpn.service.session;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by ae on 8-5-16.
 */
public class SessionCallBack implements ResultCallback<Frame> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Session session;

    public SessionCallBack(Session session) {
        this.session = session;
    }

    @Override
    public void onStart(Closeable closeable) {
        log.info("Starting ..");
    }

    @Override
    public void onNext(Frame object) {
        log.info(object.toString());
        String message = new String(object.getPayload());
        if (message.contains("ERROR: SETUP OF VPN FAILED ..")) {
            session.setStatus(SESSION_STATUS.FAILDED);
        } else if (message.contains("SUCCESS: SETUP OF VPN SUCCEEDED ..")) {
            session.setStatus(SESSION_STATUS.UP);
        } else if (message.contains("ERROR: VPN CONNECTION INTERRUPTED ..")) {
            session.setStatus(SESSION_STATUS.FAILDED);
        } else if (message.contains("VPN UP ..")) {
            session.setStatus(SESSION_STATUS.UP);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("Error: " + throwable + " ..");
    }

    @Override
    public void onComplete() {
        log.info("Completed ..");
    }

    @Override
    public void close() throws IOException {
        log.info("Closed ..");
    }
}
