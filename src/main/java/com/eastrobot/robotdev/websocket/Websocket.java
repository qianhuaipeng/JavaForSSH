package com.eastrobot.robotdev.websocket;

import com.eastrobot.robotdev.utils.task.ITask;
import com.eastrobot.robotdev.utils.task.SSHClientTask;
import com.eastrobot.robotdev.utils.task.TaskPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
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
@ServerEndpoint("/websocket")
public class Websocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private static Logger logger = LoggerFactory.getLogger(Websocket.class);
    private static CopyOnWriteArraySet<Websocket> websocketSet = new CopyOnWriteArraySet<Websocket>();
    private static ConcurrentMap<String,Websocket> websocketMap = new ConcurrentHashMap<String, Websocket>();
    private Session session;
    private String id;
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        this.id = session.getId();
        setWebsocketMap(id,this);
        addOnlineCount();
        logger.info("sessionId:" + id);
        logger.info("有新连接加入，当前人数为" + getOnlineCount());
        ITask iTask = new SSHClientTask(id,"", this);
        TaskPool.getInstance().addTask(iTask,id);
    }

    @OnClose
    public void onClose(){
        shutdowm();
    }

    @OnMessage
    public void onMessage(String message, Session session){
        String id = session.getId();
        System.out.println("来自客户端：" + id +"的消息"+ message);
        //this.sendMessage(message);
        SSHClientTask clientTask = (SSHClientTask) TaskPool.getInstance().getThreadManager().get(id);
        System.out.println("clientTask" + clientTask.getId());
        clientTask.setMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable error){
        logger.error("发生错误");
        shutdowm();
        error.printStackTrace();
    }


    public void sendMessage(String message){
        try {
            this.session.getBasicRemote().sendText(message);
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


    public void shutdowm(){
        logger.info("shutdowm ......");
        getWebsocketMap().remove(this.session.getId());
        TaskPool.getInstance().shutDown(this.id);
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
