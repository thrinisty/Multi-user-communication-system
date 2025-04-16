package com.npu.qqclient.service;

import com.npu.qqcommon.Message;
import com.npu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

//提供消息相关的服务方法
public class MessageClientService {
    public void sendMessageToOne(String content, String senderId, String getterId) {
        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_COMM_MES);//设置为普通消息类型
        ms.setSender(senderId);
        ms.setGetter(getterId);
        ms.setContent(content);
        System.out.println(senderId + "对" + getterId + "私聊 内容为：" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAll(String content, String senderId) {
        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_TO_ALL_MES);//设置为普通消息类型
        ms.setSender(senderId);
        ms.setContent(content);
        System.out.println(senderId + "群发消息，内容为：" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
