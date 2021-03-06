package com.eastrobot.robotdev.websocket;

import com.eastrobot.robotdev.controller.LoginController;
import com.eastrobot.robotdev.entity.ConnectionInfo;
import com.eastrobot.robotdev.utils.task.ITask;
import com.eastrobot.robotdev.utils.task.SSHClientTask;
import com.eastrobot.robotdev.utils.task.TaskPool;
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
@ServerEndpoint("/websocket/{sessionId}")
public class Websocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private static Logger logger = LoggerFactory.getLogger(Websocket.class);
    private static CopyOnWriteArraySet<Websocket> websocketSet = new CopyOnWriteArraySet<Websocket>();
    private static ConcurrentMap<String,Websocket> websocketMap = new ConcurrentHashMap<String, Websocket>();
    private Session session;

    @OnOpen
    public void onOpen(@PathParam("sessionId") String sessionId, Session session){
        this.session = session;
        //this.id = session.getId();
        setWebsocketMap(sessionId,this);
        addOnlineCount();
        logger.info("sessionId:" + sessionId);
        logger.info("有新连接加入，当前人数为" + getOnlineCount());
        ConnectionInfo connectionInfo = LoginController.map.get(sessionId);
        if (connectionInfo != null) {
            ITask iTask = new SSHClientTask(sessionId,"", this ,connectionInfo);
            TaskPool.getInstance().addTask(iTask,sessionId);
        }

    }

    @OnClose
    public void onClose(@PathParam("sessionId") String sessionId){
        logger.info("主动关闭.....");
        shutdowm(sessionId);
    }

    @OnMessage
    public void onMessage(@PathParam("sessionId") String sessionId, String message, Session session){
        logger.info("收到消息： " + message);
        SSHClientTask clientTask = (SSHClientTask) TaskPool.getInstance().getThreadManager().get(sessionId);
        //System.out.println("clientTask" + clientTask.getId());
        clientTask.setMessage(message);
    }

    @OnError
    public void onError(@PathParam("sessionId") String sessionId, Session session, Throwable error){
        logger.error("发生错误" + sessionId);
        shutdowm(sessionId);
        error.printStackTrace();
    }


    public void sendMessage(String message){
        try {
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
        getWebsocketMap().remove(sessionId);
        TaskPool.getInstance().shutDown(sessionId);
        subOnlineCount();

    }

    public void setWebsocketMap(String key, Websocket websocket){
        websocketMap.put(key,websocket);
    }

    public ConcurrentMap<String,Websocket> getWebsocketMap(){
        return websocketMap;
    }

    public synchronized void addOnlineCount(){
        Websocket.onlineCount++;
    }

    public synchronized void subOnlineCount(){
        Websocket.onlineCount--;
    }

    public synchronized int getOnlineCount(){
        return onlineCount;
    }

}
