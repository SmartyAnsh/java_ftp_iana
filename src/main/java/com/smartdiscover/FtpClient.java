package com.smartdiscover;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FtpClient {

    private static final int TIMEOUT_100_SECONDS = 100000;
    private static final String FTP_SERVER = "ftp.iana.org";
    private static final String USERNAME = "anonymous";
    private static final String PASSWORD = "anonymous";
    private static final String DIRECTORY = "tz//releases";


    public static void main(String[] args) {
        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(FTP_SERVER);
            ftp.setConnectTimeout(TIMEOUT_100_SECONDS);
            ftp.setDataTimeout(TIMEOUT_100_SECONDS);
            ftp.setControlKeepAliveTimeout(TIMEOUT_100_SECONDS);
            ftp.setControlKeepAliveReplyTimeout(TIMEOUT_100_SECONDS);

            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }

            ftp.login(USERNAME, PASSWORD);
            FTPFile[] files = ftp.listFiles(DIRECTORY);

            if (files.length > 0) {
                Arrays.sort(files, (i1, i2) -> i2.getTimestamp().compareTo(i1.getTimestamp()));
                FTPFile latestFile = files[0];
                System.out.println(String.format("Latest distribution [%s] on the [%s]", latestFile.getName(), latestFile.getTimestamp().getTime()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
