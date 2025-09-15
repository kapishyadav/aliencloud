package com.example.model;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.example.config.SftpConfig;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class SftpService {

    private final SftpConfig config;

    public SftpService(SftpConfig config) {
        this.config = config;
    }

    private Session createSession() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(config.getUsername(), config.getHost(), config.getPort());
        session.setPassword(config.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return session;
    }

    public void uploadFile(String username, String filename, InputStream fileStream) throws Exception {
        Session session = createSession();
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();

        String userDir = config.getRemoteDir() + "/" + username;

        try {
            // ensure user folder exists
            sftpChannel.stat(userDir);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                sftpChannel.mkdir(userDir);

                // chmod 700 to lock down permissions
                setPermissions(session, userDir, "700");
            } else {
                throw e;
            }
        }

        // upload file into user’s folder
        String remotePath = userDir + "/" + filename;
        sftpChannel.put(fileStream, remotePath);

        sftpChannel.disconnect();
        session.disconnect();
    }

    private void setPermissions(Session session, String path, String mode) throws Exception {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand("chmod " + mode + " " + path);
        channelExec.connect();
        channelExec.disconnect();
    }


}
