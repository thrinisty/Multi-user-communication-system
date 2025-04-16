package com.npu.qqserver.server;

import com.npu.qqcommon.Message;
import com.npu.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

//该类对该类的对象和客户端保持连接，拥有一个socket
public class ServerConnectClientThread extends Thread {
    Socket socket = null;
    private String userId = "";//连接到服务器的用户id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

   @Override
    public void run() {
        while (true) {
            try{
                System.out.println("用户id " + userId);
                System.out.println("正在尝试读取客户端数据");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //等待使用的数据
                //更具Message类型处理后续
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户端要求返回在线用户列表
                    System.out.println(message.getSender() + " 需要在线列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    //拿到了信息构造一个Message
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    //写入Message
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                    System.out.println("已发送在线用户列表给 " + userId);

                } else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + " 准备退出");
                    //将客户端的线程从集合中删除
                    ManageClientThreads.removeClientThread(userId);
                    socket.close();
                    //退出while循环
                    break;
                } else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //根据收到的消息中获取发送的对象进行转发
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread
                            .getSocket().getOutputStream());
                    oos.writeObject(message);//发送，如果客户不在线可以保存到数据库中
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    //需要遍历线程的管理集合逐个发送
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> interator = hm.keySet().iterator();
                    while (interator.hasNext()) {
                        //取出在线用户ID
                        String onLineUserId = interator.next().toString();
                        if(!onLineUserId.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get
                                    (onLineUserId).getSocket().getOutputStream());
                                oos.writeObject(message);
                        }
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    //根据getterid选取集合中的对应线程得到socket用以转发文件消息
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread
                            .getSocket().getOutputStream());
                    oos.writeObject(message);

                }
                else {
                    System.out.println("收到服务器消息，暂不处理");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
