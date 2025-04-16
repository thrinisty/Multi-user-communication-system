package com.npu.qqclient.service;

import com.npu.qqcommon.Message;
import com.npu.qqcommon.MessageType;

import java.io.*;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {
    private Socket socket;

    //构造器创建的时候接收一个套接字
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为持续通信持续循环
        while (true) {
            try {
                //System.out.println("客户端线程，等待读取服务器数据");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();//没有发送对象 线程发送阻塞
                //其余处理,后续需要使用
                //判断message类型，处理后续内容

                //当读到用户列表请求
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表然后返回
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("======当前在线用户======");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //将消息显示出来
                    System.out.println("\n收到来自 " + message.getSender() + " 的消息，内容如下：");
                    System.out.println(message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    System.out.println("\n收到来自 " + message.getSender() + " 群发的消息，内容如下：");
                    System.out.println(message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    System.out.println("\n收到来自 " + message.getSender() + " 的文件");
                    FileOutputStream fos = new FileOutputStream(message.getDest());
                    fos.write(message.getFileBytes());
                    fos.close();
                    System.out.println("文件保存在目录 " + message.getDest());
                }
                else {
                    System.out.println("暂时不处理");
                }


            } catch (Exception e) {

            }
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
