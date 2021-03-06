package com.sytz.aerialroad;

import android.os.Environment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

public class TZFileTool {
    public static String appRootDir;

    public static void writeObjToJson(JSONObject obj, String filePath) {
        try {
            Writer output;
            File file = new File(filePath);
            output = new BufferedWriter(new FileWriter(file));
            output.write(obj.toString());
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJsonToObj(String filePath) {
        JSONObject jsonObject = null;
        try {
            FileInputStream in = new FileInputStream(filePath);
            String result = stringFromInputStream(in);
            jsonObject = new JSONObject(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static String stringFromInputStream(InputStream in) {
        String result = null;

        try {
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //sdcard not have?
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//get root dir
        }
        return sdDir.toString();

    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            File oldfile = new File(oldPath);
            if (oldfile.isDirectory()) { //??????????????????
                copyFolder(oldPath, newPath);
            }
            else {
                int bytesum = 0;
                int byteread = 0;
                if (oldfile.exists()) { //???????????????
                    File newfile = new File(newPath);
                    String strTmp = newPath;
                    if (newfile.isDirectory()) {
                        strTmp = newPath + oldfile.getName();
                    }

                    FileInputStream inStream = new FileInputStream(oldPath); //???????????????
                    FileOutputStream fs = new FileOutputStream(strTmp);
                    byte[] buffer = new byte[1444];
                    while ( (byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //????????? ????????????
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                    fs.close();
                }
            }
        }
        catch (Exception e) {
            System.out.println("??????????????????????????????");
            e.printStackTrace();
        }
    }
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // ????????????????????????????????????????????????????????????????????????????????????
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("??????????????????" + fileName + "?????????");
                return true;
            } else {
                System.out.println("??????????????????" + fileName + "?????????");
                return false;
            }
        } else {
            System.out.println("???????????????????????????" + fileName + "????????????");
            return false;
        }
    }
    public static boolean deleteDirectory(String dir) {
        // ??????dir?????????????????????????????????????????????????????????
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // ??????dir???????????????????????????????????????????????????????????????
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("?????????????????????" + dir + "????????????");
            return false;
        }
        boolean flag = true;
        // ????????????????????????????????????????????????
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // ???????????????
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // ???????????????
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("?????????????????????");
            return false;
        }
        // ??????????????????
        if (dirFile.delete()) {
            System.out.println("????????????" + dir + "?????????");
            return true;
        } else {
            return false;
        }
    }

    public static boolean copyFolder(String oldPath, String newPath) {
        boolean isok = true;
        try {
            (new File(newPath)).mkdirs(); //???????????????????????? ?????????????????????
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)) {
                    temp=new File(oldPath+file[i]);
                }
                else
                {
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()) {//?????????????????????
                    copyFolder(oldPath + file[i], newPath + file[i]);
                }
            }
        }
        catch (Exception e) {
            isok = false;
        }
        return isok;
    }
}
