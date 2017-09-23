package com.eastrobot.robotdev.utils.task;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.eastrobot.robotdev.websocket.WebsocketTest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author alan.peng
 * @Date 2017-09-22 10:55
 */
public class TestTask implements ITask {

    private String message;
    private WebsocketTest websocketTest;
    private Connection connection;
    private Session session;
    private BufferedReader stdout;
    private PrintWriter printWriter;
    private BufferedReader stderr;
    boolean flag = true;
    private ExecutorService service = Executors.newFixedThreadPool(3);
    private static final Logger logger = LoggerFactory.getLogger(TestTask.class);
    public TestTask(String message, WebsocketTest websocketTest) {
        this.message = message;
        this.websocketTest = websocketTest;
    }
    public void initSession(String hostName, int port, String userName, String passwd) throws IOException {
        connection = new Connection(hostName, port);
        connection.connect();

        boolean authenticateWithPassword = connection.authenticateWithPassword(userName, passwd);
        if (!authenticateWithPassword) {
            flag = false;
            connection.close();
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
    public void threadShutDown() {

    }

    @Override
    public void run() {
        try {
            initSession("172.16.1.147",12598,"u_dhwx","u_dhwx123");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*while (flag) {
            System.out.println("run ....");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNoneBlank(message)) {
                websocketTest.sendMessage(message);
                message = null;
            }

        }*/
        while (flag) {
            System.out.println("run.....");
            try {
                execCommand();
                String line;
                while ((line = stdout.readLine()) != null) {
                    System.out.println("" + line);
                    websocketTest.sendMessage(line);
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
                    if (StringUtils.isNoneBlank(message)) {
                        printWriter.write(message + "\r\n");
                        printWriter.flush();
                        message = null;
                    }

                }

            }
        });

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
