package com.npu.qqserver.server;

import com.npu.qqcommon.Message;
import com.npu.qqcommon.MessageType;
import com.npu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

//服务端监听9999端口，等待客户端连接，保持通信
public class QQServer {
    private ServerSocket ss = null;
    //创建一个集合存放多个用户，如果是这一些用户则合法
    private static HashMap<String, User> validUsers = new HashMap<>();
    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("100", "123456"));
        validUsers.put("300", new User("100", "123456"));
        validUsers.put("400", new User("100", "123456"));
        validUsers.put("李昊轩", new User("李昊轩", "123456"));
        validUsers.put("王凌", new User("王凌", "123456"));
    }

    public boolean checkUser(String userId, String password) {
        User user = validUsers.get(userId);
        if(user == null) {

            System.out.println("用户id错误，不存在用户" + userId);
            return false;
        }
        if(!password.equals(user.getPassword())) {
            System.out.println("用户密码错误");
            return false;
        }
        return true;
    }

    public QQServer() {
        try {
            System.out.println("服务端在9999监听...");
            ss = new ServerSocket(9999);
            while(true) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();
                //将数据进行验证，但是这没有数据库，模拟一个验证流程
                Message message = new Message();
                //验证用户是否有效
                if(checkUser(u.getUserId(), u.getPassword())) {
                    //登陆成功，向客户端发送连接信息
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);

                    //创建线程持有socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();

                    //将线程放入集合
                    ManageClientThreads.addClientThread(u.getUserId(),serverConnectClientThread);
                } else {
                    //登陆失败
                    System.out.println("用户 " + u.getUserId() + "登录无效");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果服务端退出循环，服务器端不再监听
            try{
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
