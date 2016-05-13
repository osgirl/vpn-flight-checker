FROM debian:latest
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true \
  | debconf-set-selections  \
  && echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" \
  | tee /etc/apt/sources.list.d/webupd8team-java.list \
  &&  echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" \
  | tee -a /etc/apt/sources.list.d/webupd8team-java.list \
  && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 \
  && apt-get -qq update \
  && apt-get -qqy install \
      oracle-java8-installer \
      oracle-java8-unlimited-jce-policy \
      curl \
  && rm -rf /var/lib/apt/lists/* \
  && rm -rf /var/cache/oracle-jdk8-installer \
  && apt-get -qq clean && apt-get -qq autoremove \
  && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* \
  && mkdir -p /root/.docker \
  && curl -L https://github.com/docker/machine/releases/download/v0.6.0/docker-machine-`uname -s`-`uname -m` > /bin/docker-machine \
  && chmod +x /bin/docker-machine \
  && curl -L https://get.docker.com/builds/Linux/x86_64/docker-1.10.0 > /bin/docker \
  && chmod +x /bin/docker
ADD backend.jar /