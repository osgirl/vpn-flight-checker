package com.ae.vpn.service.session;

import com.ae.vpn.servers.common.docker.DockerService;
import com.ae.vpn.servers.common.model.Portal;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ae on 8-5-16.
 */

@Component
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${dockerImageName}")
    private String dockerImageName;

    @Autowired
    private DockerService dockerService;

    private DockerClient mainClient;

    @PostConstruct
    private void init() {
        mainClient = dockerService.getNewDockerClient();
    }

    public Session createSession(Portal portal) {
        log.info("Creating a session for " + portal);
        Session session = new Session();
        session.setPortal(portal);
        session.setStatus(SESSION_STATUS.STARTING);

        //
        // Python needs to be run unbuffered in order to immediately print to
        // stdout in deamon mode:
        // https://github.com/docker/docker/issues/12447#issuecomment-94417192
        //
        String entryPointCommand = "(/opt/bin/entry_point.sh)";
        String portalCommand = "(python -u /vpngate.py " + portal.name() + " &)";
        String startCommand = String.format("%s; %s", portalCommand, entryPointCommand);

        ExposedPort tcp4444 = ExposedPort.tcp(4444);

        Ports portBindings = new Ports();
        portBindings.bind(tcp4444, Ports.binding(null));

        ExposedPort tcp5900 = ExposedPort.tcp(5900);
        portBindings.bind(tcp5900, Ports.binding(null));

        String[] command = new String[] { "sh", "-c", startCommand };
        CreateContainerCmd createContainerCmd;
        createContainerCmd = mainClient.createContainerCmd(dockerImageName)
                                       .withAttachStderr(false)
                                       .withAttachStdout(false)
                                       .withCmd(command)
                                       .withPortBindings(portBindings)
                                       .withPrivileged(true);
        CreateContainerResponse response = createContainerCmd.exec();
        session.setContainer(response);

        SessionCallBack sessionCallBack = new SessionCallBack(session);
        DockerClient asyncClient = dockerService.getNewDockerClient();
        asyncClient.attachContainerCmd(response.getId())
                   .withStdOut(true)
                   .withStdErr(true)
                   .withLogs(true)
                   .withFollowStream(true)
                   .exec(sessionCallBack);

        mainClient.startContainerCmd(response.getId()).exec();

        //
        // TODO inspect stuff
        //
        InspectContainerResponse inspectResponse;
        inspectResponse = mainClient.inspectContainerCmd(response.getId()).exec();

        log.info(inspectResponse.toString());
        Iterator<Map.Entry<ExposedPort, Ports.Binding[]>> it;
        it = inspectResponse.getNetworkSettings().getPorts()
                                                 .getBindings()
                                                 .entrySet()
                                                 .iterator();
        int seleniumPort = it.next().getValue()[0].getHostPort();
        log.info("Selenium port: " + seleniumPort);
        int vncPort = it.next().getValue()[0].getHostPort();
        log.info("vnc port: " + vncPort);
        session.setIp("localhost");
        session.setPort(seleniumPort);
        session.setVncPort(vncPort);

        log.info("Started session ..");
        return session;
    }

    public void destorySession(Session session) {
        mainClient.stopContainerCmd(session.getContainer().getId()).exec();
        mainClient.removeContainerCmd(session.getContainer().getId()).exec();
        session.setStatus(SESSION_STATUS.DOWN);
    }
}
