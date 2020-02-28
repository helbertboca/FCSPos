package com.fcs.fcspos.model;

public class Net {

    private String ssid;
    private String password;
    private String ip;
    private int port;

    public Net(String ssid, String password, String ip, int port) {
        this.ssid = ssid;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
