package com.npu.qqserver.server;

import java.util.HashMap;
import java.util.Iterator;

//该类用于管理与客户端通讯的线程
public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static void addClientThread(String userId, ServerConnectClientThread ServerConnectClientThread) {
        hm.put(userId, ServerConnectClientThread);
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static ServerConnectClientThread getClientThread(String userId) {
        return hm.get(userId);
    }

    public static void removeClientThread(String userId) {
        hm.remove(userId);
    }

    //编写方法返回用户列表
    public static String getOnlineUser() {
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUser = "";
        while(iterator.hasNext()) {
            onlineUser += iterator.next().toString() + " ";
        }
        return onlineUser;
    }
}
