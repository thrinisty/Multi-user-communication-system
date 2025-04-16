package com.npu.qqclient.service;

import java.util.HashMap;

//管理客户端带服务端线程的一个类
public class ManageClientConnectServerThread {
    //把多个线程放入一个hashmap集合中，key是用户id， value是线程
    public static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    //将线程加入
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }
    //用id取出线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}
