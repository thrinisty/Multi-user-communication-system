package com.npu.qqclient.service;

import com.npu.qqcommon.Message;
import com.npu.qqcommon.MessageType;
import com.sun.org.apache.bcel.internal.generic.ObjectType;

import java.io.*;

//文件相关传输的类
public class FileClientService {
    public void sendFileToOne(String src, String dest, String senderId, String getterId) {
        //构造message信息
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setDest(dest);
        message.setGetter(getterId);
        message.setSrc(src);
        //读取文件内容
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];

        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            //将字节数组放入message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("发送目录 " + message.getSrc() + "目录文件至 "
                + message.getGetter() + " 的目录 " + message.getDest());
    }
}
