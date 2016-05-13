package com.ae.vpn.service.session;

import com.ae.vpn.servers.common.model.Portal;
import com.github.dockerjava.api.command.CreateContainerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ae on 7-5-16.
 */
public class Session {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private CreateContainerResponse container;

    private SessionCallBack sessionCallBack;

    private String ip;

    private Integer port;

    private Integer vncPort;

    private Portal portal;

    private SESSION_STATUS status;

    public synchronized SESSION_STATUS getStatus() {
        return status;
    }

    protected synchronized void setStatus(SESSION_STATUS status) {
        this.status = status;
    }

    protected void setContainer(CreateContainerResponse container) {
        this.container = container;
    }

    protected void setSessionCallBack(SessionCallBack sessionCallBack) {
        this.sessionCallBack = sessionCallBack;
    }

    protected void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Portal getPortal() {
        return portal;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    protected CreateContainerResponse getContainer() {
        return container;
    }

    public Integer getVncPort() {
        return vncPort;
    }

    public void setVncPort(Integer vncPort) {
        this.vncPort = vncPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (log != null ? !log.equals(session.log) : session.log != null) return false;
        if (container != null ? !container.equals(session.container) : session.container != null) return false;
        if (sessionCallBack != null ? !sessionCallBack.equals(session.sessionCallBack) : session.sessionCallBack != null)
            return false;
        if (ip != null ? !ip.equals(session.ip) : session.ip != null) return false;
        if (port != null ? !port.equals(session.port) : session.port != null) return false;
        if (vncPort != null ? !vncPort.equals(session.vncPort) : session.vncPort != null) return false;
        if (portal != session.portal) return false;
        return status == session.status;

    }

    @Override
    public int hashCode() {
        int result = log != null ? log.hashCode() : 0;
        result = 31 * result + (container != null ? container.hashCode() : 0);
        result = 31 * result + (sessionCallBack != null ? sessionCallBack.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (vncPort != null ? vncPort.hashCode() : 0);
        result = 31 * result + (portal != null ? portal.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "container=" + container +
                ", sessionCallBack=" + sessionCallBack +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", vncPort=" + vncPort +
                ", portal=" + portal +
                ", status=" + status +
                '}';
    }
}
