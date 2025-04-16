package com.npu.qqclient.view;

import com.npu.qqclient.service.FileClientService;
import com.npu.qqclient.service.MessageClientService;
import com.npu.qqclient.service.UserClientService;

import java.util.Scanner;

public class QQview {
    //显示主菜单
    private boolean loop = true;
    private String key = "";
    Scanner scanner = new Scanner(System.in);
    private UserClientService userClientService = new UserClientService();//用于登录服务器/注册用户
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        QQview qqview = new QQview();
        qqview.mainMenu();
        System.out.println("退出聊天系统");
    }

    public void mainMenu() {
        while (loop) {
            System.out.println("==========欢迎来到网络登录系统==========");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入1-9： ");
            key = scanner.next();
            switch (key) {
                case "1":
                    System.out.println("登陆系统");
                    System.out.println("请输入用户号码： ");
                    String userId = scanner.next();
                    System.out.println("请输入用户密码： ");
                    String password = scanner.next();
                    //将用户密码和用户名称发送到服务端
                    //编写一个用户验证的类服务UserClientService
                    if (userClientService.checkUser((userId), password)) {
                        System.out.println("==========登录成功==========");
                        while (loop) {
                            System.out.println("==========网络通讯系统==========");
                            System.out.println("欢迎用户" + userId);
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入1-9 ");
                            key = scanner.next();
                            switch (key) {
                                case "1":
                                    System.out.println("显示在线用户列表");
                                    //写一个方法处理显示用户列表
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入群发的话，内容将被发给所有的在线用户");
                                    String s = scanner.next();
                                    //调用方法将字符串发送给服务器进行广播
                                    messageClientService.sendMessageToAll(s, userId);
                                    break;
                                case "3":
                                    System.out.print("请输入要聊天的用户: ");
                                    String getterId = scanner.next();
                                    System.out.print("请输入想说的话");
                                    String content = scanner.next();
                                    //编写一个方法完成发送
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    break;
                                case "4":
                                    System.out.print("请输入发送对象 ");
                                    String target = scanner.next();
                                    System.out.print("请输入要发送的文件路径 ");
                                    //e:\\1.jpg  e:\\2.jpg
                                    String filePath = scanner.next();
                                    System.out.print("请输入保存至对方电脑的文件路径 ");
                                    String savePath = scanner.next();
                                    fileClientService.sendFileToOne(filePath, savePath, userId, target);
                                    break;
                                case "9":
                                    //调用方法无异常退出，发送一个message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }

                        }
                    } else {
                        System.out.println("==========登陆失败==========");
                    }
                    break;
                case "9":
                    System.out.println("==========登陆退出==========");
                    loop = false;
                    break;
            }

        }
    }
}
