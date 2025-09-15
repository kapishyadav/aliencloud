package com.example.model;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

public class RemoteFileStorageService {
    
    private final ChannelSftp channelSftp;

    @Value("${sftp.remoteDir}")
    private String remoteBaseDir;

    public RemoteFileStorageService(ChannelSftp channelSftp) {
        this.channelSftp = channelSftp;
    }

    public void uploadFile(String username, MultipartFile file) throws Exception {
        String userDir = remoteBaseDir + "/" + username;

        // Ensure user directory exists (create if missing)
        try {
            channelSftp.cd(userDir);
        } catch (SftpException e) {
            channelSftp.mkdir(userDir);
            channelSftp.cd(userDir);
        }

        try (InputStream inputStream = file.getInputStream()) {
            channelSftp.put(inputStream, file.getOriginalFilename());
        }
    }

}
