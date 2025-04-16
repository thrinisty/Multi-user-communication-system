package com.npu.qqclient.service;

import com.npu.qqcommon.Message;
import com.npu.qqcommon.MessageType;
import com.npu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class UserClientService {
    private User u = new User();//因为要在其他地方使用属性
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    //用以判断是否登录正确，消息来自于服务端按
    public boolean checkUser(String userId, String password) {
        boolean b = false;
        u.setUserId(userId);
        u.setPassword(password);
        //连接到服务器，发送u对象
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象
            
            //读取收到的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){
                //创建一个和服务器端保持通信的线程->创建线程类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                //为了方便管理，将线程放入一个集合中ManageClientConnectServerThread
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                b = true;
            } else {
                //登陆失败不启动线程，关闭没有用到的socket
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    //向服务器端请求在线列表
    public void onlineFriendList() {
        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        ms.setSender(u.getUserId());//
        //发送给服务器，先得到当前线程的socket对应的输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(ms);//发送请求
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //退出客户端，给服务端发送一个退出的消息
    public void logout() {
        Message ms = new Message();
        ms.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        ms.setSender(u.getUserId());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(ms);//发送请求
            System.out.println("退出系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
