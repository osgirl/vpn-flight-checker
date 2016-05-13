package com.ae.vpn.servers.common.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ae on 7-5-16.
 */

@Component
public class DockerService {
    @Value("${dockerUrl}")
    private String dockerUrl;

    public DockerClient getNewDockerClient() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                                                      .withDockerTlsVerify(false)
                                                      .withDockerHost(dockerUrl)
                                                      .build();
        DockerClient client = DockerClientBuilder.getInstance(config).build();
        return client;
    }
}
