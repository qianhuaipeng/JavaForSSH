package com.eastrobot.robotdev.websocket;

import com.eastrobot.robotdev.controller.LoginController;
import com.eastrobot.robotdev.entity.ConnectionInfo;
import com.eastrobot.robotdev.utils.task.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: alan.peng
 * @Description:
 * @Date: Create in 10:58 2017/9/20
 * @Modified By：
 */
//@ServerEndpoint("/websocket/{sessionId}")
public class WebsocketTest {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private static Logger logger = LoggerFactory.getLogger(WebsocketTest.class);
    private static CopyOnWriteArraySet<WebsocketTest> websocketSet = new CopyOnWriteArraySet<WebsocketTest>();
    private static ConcurrentMap<String,WebsocketTest> websocketMap = new ConcurrentHashMap<String, WebsocketTest>();
    private Session session;
    //private String sessionId;



    @OnOpen
    public void onOpen (@PathParam("sessionId") String sessionId, Session session){
        this.session = session;
        //this.sessionId = session.getId();
        setWebsocketMap(sessionId,this);
        addOnlineCount();
        logger.info("sessionId:" + sessionId);
        logger.info("有新连接加入，当前人数为" + getOnlineCount());
        ConnectionInfo connectionInfo = LoginController.map.get(sessionId);
        //if (connectionInfo != null) {
            ITask iTask = new TestTask("",this);
            TaskPool.getInstance().addTask(iTask,sessionId);
        //}

    }

    @OnClose
    public void onClose(@PathParam("sessionId") String sessionId){
        shutdowm(sessionId);
    }

    @OnMessage
    public void onMessage(@PathParam("sessionId") String sessionId, String message, Session session){
        logger.info(sessionId + "收到消息： " + message);
        TestTask iTask =   (TestTask)TaskPool.getInstance().getThreadManager().get(sessionId);
        //System.out.println("clientTask" + clientTask.getId());
        iTask.setMessage(message);
        //sendMessage(message);
    }

    @OnError
    public void onError(@PathParam("sessionId") String sessionId, Session session, Throwable error){
        logger.error("发生错误");
        shutdowm(sessionId);
        error.printStackTrace();
    }


    public synchronized  void sendMessage(String message){
        try {
            System.out.println(this.session + "message:" + message);
            if (StringUtils.isNoneBlank(message)) {
                this.session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String id, String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void shutdowm(String sessionId){
        logger.info("shutdowm ......");
        getWebsocketMap().remove(this.session.getId());
        TaskPool.getInstance().shutDown(sessionId);
        subOnlineCount();

    }

    public void setWebsocketMap(String key, WebsocketTest websocket){
        websocketMap.put(key,websocket);
    }

    public ConcurrentMap<String,WebsocketTest> getWebsocketMap(){
        return websocketMap;
    }

    public synchronized void addOnlineCount(){
        WebsocketTest.onlineCount++;
    }

    public synchronized void subOnlineCount(){
        WebsocketTest.onlineCount--;
    }

    public synchronized int getOnlineCount(){
        return onlineCount;
    }



}
