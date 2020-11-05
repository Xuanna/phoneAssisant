package com.cici.cicimobileassistant.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static FileUtils fileUtils = new FileUtils();

    private FileUtils() {

    }

    //外部存储的根目录
    public static final String SD_ROOT_DIR = getSDCardBaseDir() + "/";

    public static String SD_PACKAGE_CACHE_DIR;


    public static FileUtils init(Context context) {
        if (isSDCardMounted()) {
            SD_PACKAGE_CACHE_DIR = context.getExternalCacheDir().getAbsolutePath() + "/";
        }

        return fileUtils;
    }

    /**
     * 获取SD根目录
     */
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }


    // 判断SD卡是否被挂载
    public static boolean isSDCardMounted() {
        // return Environment.getExternalStorageState().equals("mounted");
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 在内存卡根目录创建一个文件
     *
     * @param path
     */
    public static File createSDRootFile(String path) throws IOException {
        File file = new File(SD_ROOT_DIR + path);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    /**
     * 创建sd卡中包文件中的文件，随着应用的卸载而删除
     */
    public static File createSDPackageFile(String path) throws IOException {
        File file = new File(SD_PACKAGE_CACHE_DIR + path);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 在内存卡根目录创建文件夹，再创建文件
     *
     * @param path
     */
    public static File createSDRootFile(String folderName, String path) throws IOException {

        if (isSDCardMounted()){
            File file = new File(SD_ROOT_DIR + folderName);
            if (!file.exists()) {
                file.mkdirs();//根据文件路径，可以创建多个文件夹
            }
            File file1 = new File(file, path);
            if (file.exists()) {
                if (!file1.exists()) {
                    file1.createNewFile();//再创建文件
                }
            }

            return file1;
        }

        return null;
    }


    public static File createSDPackageFile(String folderName, String path) throws IOException {
        if (isSDCardMounted()) {
            File file = new File(SD_PACKAGE_CACHE_DIR + folderName);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(file, path);
            if (file.exists()) {
                if (!file1.exists()) {
                    file1.createNewFile();//再创建文件
                }
            }
            return file1;
        }
        return null;
    }


    /**
     * 读取文件
     *
     * @param file
     */
    public static String read(File file) {
        StringBuilder builder = new StringBuilder();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            byte[] bytes = new byte[1024];

            int len;
            if ((len = fis.read(bytes)) != 0) {
                builder.append(new String(bytes, 0, len));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return builder.toString();


    }

}
