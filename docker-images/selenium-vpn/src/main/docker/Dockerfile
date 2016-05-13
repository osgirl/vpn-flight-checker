FROM selenium/standalone-firefox-debug:2.48.2
RUN x11vnc -storepasswd ttBsXoOvcqdoweDc /home/seluser/.vnc/passwd
RUN apt-get update \
    && apt-get install -y  openvpn python python-pip \
    && pip install requests
COPY vpngate.py /
COPY checkPublicIp.sh /