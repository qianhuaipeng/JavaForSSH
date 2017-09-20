package com.eastrobot.robotdev.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alan.peng
 * @Description:
 * @Date: Create in 16:10 2017/9/19
 * @Modified By：
 */
public class SSHClientUtil {

    private Logger log = LoggerFactory.getLogger(getClass());
    private Connection connection;
    private Session session;
    private BufferedReader stdout;
    private PrintWriter printWriter;
    private BufferedReader stderr;
    private ExecutorService service = Executors.newFixedThreadPool(3);
    private Scanner scanner = new Scanner(System.in);

    public void initSession(String hostName, int port, String userName, String passwd) throws IOException {
        connection = new Connection(hostName, port);
        connection.connect();

        boolean authenticateWithPassword = connection.authenticateWithPassword(userName, passwd);
        if (!authenticateWithPassword) {
            throw new RuntimeException("Authentication failed. Please check hostName, userName and passwd");
        }
        session = connection.openSession();
        session.requestDumbPTY();
        session.startShell();
        stdout = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStdout()), "UTF-8"));
        stderr = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStderr()), "UTF-8"));
        printWriter = new PrintWriter(session.getStdin());
    }

    public void execCommand() throws IOException {
        service.submit(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    while ((line = stdout.readLine()) != null) {
                        System.out.println("" + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("input:");
                    String nextLine = scanner.nextLine();
                    printWriter.write(nextLine + "\r\n");
                    printWriter.flush();
                }

            }
        });

    }

    public void close() {
        IOUtils.closeQuietly(stdout);
        IOUtils.closeQuietly(stderr);
        IOUtils.closeQuietly(printWriter);
        //IOUtils.closeQuietly(scanner);
        session.close();
        connection.close();
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SSHClientUtil sshAgent = new SSHClientUtil();
        sshAgent.initSession("172.16.1.147",12598, "u_dhwx", "u_dhwx123");

        sshAgent.execCommand();

        // sshAgent.close();

    }
}
