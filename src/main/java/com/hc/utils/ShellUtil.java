package com.hc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by GOPENEDD on 2019/5/21
 */
// 用来向linux输入命令
@SuppressWarnings({"CatchMayIgnoreException", "UnusedAssignment", "JavaDoc", "InfiniteLoopStatement"})
public class ShellUtil {

    /**
     * 执行命令
     * @param shell
     * @param isWait
     * @throws Exception
     */
    public static String runShell(String shell, boolean isWait) throws Exception {
        int port = getPortNotUsed("0.0.0.0");
        if (port < 0)
            throw new RuntimeException("当前无可用端口");
        String cmd = shell + " " + port + " " + (port + 1);
        Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
        if (isWait)
            process.waitFor();
        System.out.println("相关信息 -----> " + " 开启端口: " + port + (port + 1));
        return cmd;
    }

    @SuppressWarnings("ConstantConditions")
    private static int getPortNotUsed(String host) {
        int port = 8081;
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            if (addr != null) {
                while (true) {
                    Socket socket = new Socket(addr, port);
                    if (port < 65535)
                        port += 1;
                    socket.close();
                }
            } else throw new RuntimeException("Error: Address 不可用");
        } catch (IOException e) {
            // 端口未被占用
            return port;
        }
    }

    /**
     * 获取Linux进程的PID
     * @param command
     * @return
     */
    @SuppressWarnings("Duplicates")
    public static String getPID(String command) {
        BufferedReader reader = null;
        try {
            //显示所有进程
            Process process = Runtime.getRuntime().exec("ps -ef");
            process.waitFor();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(command)) {
                    // 读下一行才是
                    String[] strs = reader.readLine().split("\\s+");
                    System.out.println("相关信息 -----> " + " 关闭端口进程 " + strs[1] + "cmd :" + command);
                    return strs[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }
    /**
     * 关闭Linux进程 返回结果不为0， 则执行异常
     * @param Pid 进程的PID
     */
    public static int closeLinuxProcess(String Pid){
        Process process = null;
//        BufferedReader reader =null;
        try{
            //杀掉进程
            process = Runtime.getRuntime().exec("kill -9 "+Pid);
            return process.waitFor();
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line = null;
//            while((line = reader.readLine())!=null){
//                System.out.println("kill PID return info -----> "+line);
//            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }

//            if(reader!=null){
//                try {
//                    reader.close();
//                } catch (IOException e) {
//
//                }
//            }
        }
        return -1;
    }
}
