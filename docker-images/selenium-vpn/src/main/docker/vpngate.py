#!/usr/bin/env python

#
# Inspired by https://gist.github.com/Lazza/bbc15561b65c16db8ca8
#

import os, sys, requests, base64, tempfile, subprocess, time, urllib2

def connectToVpn(server):
    print 'Testing: ' + server[0]
    _, path = tempfile.mkstemp()
    f = open(path, 'w')
    f.write(base64.b64decode(server[-1]))
    f.close()

    process = subprocess.Popen(['sudo', 'openvpn', '--config', path],
                               stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT)
    while True:
        line = process.stdout.readline()
        print line
        if "TLS Error: TLS handshake failed" in line: return None
        if "Initialization Sequence Completed" in line: return process
        if "Connection timed out" in line: return None
        if "failed, will try again in 5 seconds: No route to host" in line: return None
    pass # end of while

    return None
pass # end of def

def internetOn():
    try:
        response=urllib2.urlopen('http://www.google.com',timeout=5)
        return True
    except urllib2.URLError as err:
        pass
    pass # end of try
    return False
pass # end of def


def main():
    print 'Starting vpn stuff ..'
    if len(sys.argv) != 2:
        sys.stdout.write('usage: ' + sys.argv[0] + ' [country code]\n')
        exit(1)
    pass # end of if

    countryCode = sys.argv[1]

    if len(countryCode) != 2:
        sys.stdout.write('Not a valid country code\n')
        exit(1)
    pass # end of if

    countryIndex = 6;
    try:
        print 'Fetching VPN list'
        vpnData = requests.get('http://www.vpngate.net/api/iphone/').text.replace('\r','')
        servers = [line.split(',') for line in vpnData.split('\n')]
        labels = servers[1]
        labels[0] = labels[0][1:]
        servers = [s for s in servers[2:] if len(s) > 1]
    except:
        sys.stdout.write('Cannot get VPN servers data')
        exit(1)
    pass # end of try

    desired = [s for s in servers if countryCode.lower() in s[countryIndex].lower()]
    found = len(desired)
    sys.stdout.write('Found ' + str(found) + ' servers for country ' + countryCode + '\n')

    if found == 0:
        sys.stdout.write('ERROR: SETUP OF VPN FAILED ..')
        exit(1)
    pass # end of if

    supported = [s for s in desired if len(s[-1]) > 0]
    sys.stdout.write(str(len(supported)) + ' of these servers support OpenVPN\n')
    # We pick the best servers by score
    servers = sorted(supported, key=lambda s: s[2], reverse=True)
    connected = None
    for server in servers:
        connected = connectToVpn(server)
        if connected != None: break
    pass # end of while

    if connected == None:
        sys.stdout.write('ERROR: SETUP OF VPN FAILED ..')
        exit(1)
    pass # end of if
    sys.stdout.write('SUCCESS: SETUP OF VPN SUCCEEDED ..')

    try:
        while True:
            time.sleep(5000)
            if not internetOn():
                sys.stdout.write('ERROR: VPN CONNECTION INTERRUPTED ..')
            else:
                sys.stdout.write('VPN UP ..')
            pass # end of if
        pass # end of while
    except:
        # termination with Ctrl+C
        try:
            connected.kill()
        except:
            pass
        pass # end of try
        while connected.poll() != 0:
            time.sleep(1)
        pass # end of while
        sys.stdout.write('\nTerminated')
    pass # end of try
pass # end of def

if __name__ == "__main__":
    main()
pass # end of if