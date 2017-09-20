package com.eastrobot.robotdev.utils.task;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.eastrobot.robotdev.websocket.Websocket;
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
 * @Date: Create in 13:52 2017/9/20
 * @Modified By：
 */
public class SSHClientTask implements ITask {

    private String id;
    private String message;
    private Websocket websocket;
    private Connection connection;
    private Session session;
    private BufferedReader stdout;
    private PrintWriter printWriter;
    private BufferedReader stderr;
    private boolean flag = true;

    private static final Logger logger = LoggerFactory.getLogger(SSHClientTask.class);
    private ExecutorService service = Executors.newFixedThreadPool(3);
    private Scanner scanner = new Scanner(System.in);


    public SSHClientTask(String id, String message, Websocket websocket) {
        this.id = id;
        this.message = message;
        this.websocket = websocket;
    }

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

    @Override
    public void run() {
        try {
            initSession("172.16.1.147",12598,"u_dhwx","u_dhwx123");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (flag) {
            System.out.println("run.....");
            try {
                execCommand();
                String line;
                while ((line = stdout.readLine()) != null) {
                    System.out.println("" + line);
                    websocket.sendMessage(line);
                }
                //execCommand();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("", e);
            } catch (IOException e){
                logger.error("", e);
            }
        }
    }

    public void execCommand() throws IOException {
       /* service.submit(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    System.out.println("execCmd....." + stdout.readLine());

                    while ((line = stdout.readLine()) != null) {
                        System.out.println("" + line);
                        websocket.sendMessage(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });*/

        service.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (message != null) {
                        printWriter.write(message + "\r\n");
                        printWriter.flush();
                        message = null;
                    }

                }

            }
        });

    }

    @Override
    public void threadShutDown() {
        flag = Boolean.FALSE;
        TaskPool.getInstance().getThreadManager().remove(id);
        service.shutdown();
        close();
    }

    public void close() {
        IOUtils.closeQuietly(stdout);
        IOUtils.closeQuietly(stderr);
        IOUtils.closeQuietly(printWriter);
        //IOUtils.closeQuietly(scanner);
        session.close();
        connection.close();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Websocket getWebsocket() {
        return websocket;
    }

    public void setWebsocket(Websocket websocket) {
        this.websocket = websocket;
    }


    public static void main(String[] args) {
        ITask iTask = new SSHClientTask("0","", null);
        TaskPool.getInstance().addTask(iTask);

    }

}
