package com.example.model;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class SftpStorageService {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String remoteDir;
    private final JSch jsch;
    private static final Logger log = LoggerFactory.getLogger(SftpStorageService.class);


    public SftpStorageService(
            @Value("${sftp.host}") String host,
            @Value("${sftp.port}") int port,
            @Value("${sftp.username}") String username,
            @Value("${sftp.password}") String password,
            @Value("${sftp.remoteDir}") String remoteDir,
            JSch jsch) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.remoteDir = remoteDir;
        this.jsch = jsch;
    }

    public void uploadFile(String user, String filename, InputStream inputStream) throws Exception {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            // 🔑 Open SSH session
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // 🔑 Open SFTP channel
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // Create per-user dir if not exists

            String userDir = remoteDir + user;
            try {
                channelSftp.mkdir(userDir);
                channelSftp.chmod(0700, userDir);
            } catch (SftpException e) {
                // ignore if folder exists
            }

            channelSftp.cd(userDir);

            // Upload the file
            log.info("Uploading {} to remote path {}", filename, userDir);
            channelSftp.put(inputStream, filename);
            log.info("File uploaded successfully to {}/{}", userDir, filename);

        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
