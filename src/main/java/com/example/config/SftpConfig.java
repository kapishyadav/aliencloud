package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Configuration
@ConfigurationProperties(prefix = "sftp")
public class SftpConfig {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remoteDir}")
    private String remoteDir;

    @Bean
    public JSch jsch() {
        return new JSch();
    }

    @Bean
public ChannelSftp channelSftp() throws Exception {
    System.out.println("Connecting to SFTP: " + username + "@" + host + ":" + port);
    JSch jsch = new JSch();
    Session session = jsch.getSession(username, host, port);
    session.setPassword(password);
    session.setConfig("StrictHostKeyChecking", "no");
    session.connect(10000); // 10 sec timeout
    System.out.println("SFTP Session connected.");

    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
    channelSftp.connect(5000);
    System.out.println("SFTP Channel connected.");
    return channelSftp;
}


    // Required getters & setters for Spring to bind properties
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }
}
