package com.internet.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * @author sea File相关操作类
 */
public class FileUtil {

    /**
     * 创建文件
     * 
     * @param
     * @return
     * @throws IOException
     */
    public static File creatSDFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }

    /**
     * 创建文件及上级所有目录
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public static File createAbsFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            creatSDDir(path.substring(0, path.lastIndexOf("/")));
        }
        file.createNewFile();
        return file;
    }

    /**
     * 删除文件
     * 
     * @param path
     * @return 是否返回成功
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * 此方法用于删目录或文件
     * 
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    } else {
                        delFile[j].delete();// 删除文件
                    }
                }
                f.delete();
            }
        } else if (f.exists() && f.isFile()) {
            f.delete();
        }
    }

    /**
     * 循环删除空的文件夹
     * 
     * @param dir
     */
    public static void deleteEmptyDir(File dir) {
        if (dir.isDirectory()) {
            File[] fs = dir.listFiles();
            if (fs != null && fs.length > 0) {
                for (int i = 0; i < fs.length; i++) {
                    File tmpFile = fs[i];
                    if (tmpFile.isDirectory()) {
                        deleteEmptyDir(tmpFile);
                    }
                    if (tmpFile.isDirectory()
                            && tmpFile.listFiles().length <= 0) {
                        tmpFile.delete();
                    }
                }
            }
            if (dir.isDirectory() && dir.listFiles().length == 0) {
                dir.delete();
            }
        }
    }

    /**
     * 创建目录
     * 
     * @param dirName
     * @return
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();
        return dir;
    }

    /**
     * 获取文件是否存在
     * 
     * @param path
     * @return
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 将流保存为文件
     * 
     * @param path
     * @param input
     * @return
     */
    public static File write2SDFromInput(String path, InputStream input)
            throws IOException {
        File file = null;
        OutputStream output = null;
        int i;
        file = createAbsFile(path);
        output = new FileOutputStream(file);
        byte buffer[] = new byte[4 * 1024];
        while ((i = input.read(buffer)) != -1) {
            output.write(buffer, 0, i);
        }
        output.flush();
        output.close();
        System.out.println("文件大小为：" + file.length());
        return file;
    }

    /**
     * 将流部分保存为文件
     * 
     * @param path
     *            路径
     * @param input
     * @param filelength
     *            保存的文件大小
     * @return
     */
    public static File write2SDFromInputWithPercent(String path,
            InputStream input, int filelength) throws IOException {
        File file = null;
        OutputStream output = null;
        int i;
        int temp = 0;
        file = creatSDFile(path);
        output = new FileOutputStream(file);
        byte buffer[] = new byte[4 * 1024];
        while ((i = input.read(buffer)) != -1) {
            output.write(buffer, 0, i);
            temp += i;
            if (temp >= filelength) {
                break;
            }
        }
        output.flush();
        output.close();
        System.out.println("文件大小为：" + file.length());
        return file;
    }

    /**
     * 复制文件
     * 
     * @param srcFile
     *            源文件File
     * @param destDir
     *            目标目录File
     * @param newFileName
     *            新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    public static long copyFile(File srcFile, File destDir, String newFileName)
            throws Exception {
        long copySizes = 0;
        if (!srcFile.exists()) {
            System.out.println("源文件不存在");
            copySizes = -1;
        } else if (!destDir.exists()) {
            System.out.println("目标目录不存在");
            copySizes = -1;
        } else if (newFileName == null) {
            System.out.println("文件名为null");
            copySizes = -1;
        } else {
            BufferedInputStream bin = new BufferedInputStream(
                    new FileInputStream(srcFile));
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(new File(destDir, newFileName)));
            int b = 0, i = 0;
            long t1 = System.currentTimeMillis();
            while ((b = bin.read()) != -1) {
                bout.write(b);
                i++;
            }
            long t2 = System.currentTimeMillis();
            bout.flush();
            bin.close();
            bout.close();
            copySizes = i;
            long t = t2 - t1;
            System.out.println("复制了" + i + "个字节" + "时间" + t);
        }
        return copySizes;
    }

    /**
     * 复制文件(以超快的速度复制文件)
     * 
     * @param srcFile
     *            源文件File
     * @param destDir
     *            目标目录File
     * @param newFileName
     *            新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    public static long copyFileFast(File srcFile, File destDir,
            String newFileName) throws Exception {
        long copySizes = 0;
        if (!srcFile.exists()) {
            System.out.println("源文件不存在");
            copySizes = -1;
        } else if (!destDir.exists()) {
            System.out.println("目标目录不存在");
            copySizes = -1;
        } else if (newFileName == null) {
            System.out.println("文件名为null");
            copySizes = -1;
        } else {
            FileChannel fcin = new FileInputStream(srcFile).getChannel();
            FileChannel fcout = new FileOutputStream(new File(destDir,
                    newFileName)).getChannel();
            // ByteBuffer buff = ByteBuffer.allocate(1024);
            // int b = 0 ,i = 0;
            long t1 = System.currentTimeMillis();
            /*
             * while(fcin.read(buff) != -1){ buff.flip(); fcout.write(buff);
             * buff.clear(); i++; }
             */
            long size = fcin.size();
            fcin.transferTo(0, fcin.size(), fcout);
            // fcout.transferFrom(fcin,0,fcin.size());
            // 一定要分清哪个文件有数据，那个文件没有数据，数据只能从有数据的流向
            // 没有数据的文件
            long t2 = System.currentTimeMillis();
            fcin.close();
            fcout.close();
            copySizes = size;
            long t = t2 - t1;
            System.out.println("复制了" + size + "个字节" + "时间" + t);
        }
        return copySizes;
    }

    /**
     * 复制目录,目录最后不带分隔符
     * 
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws Exception {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {// 是文件直接复制
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                copyFileFast(sourceFile, new File(targetDir), file[i].getName());
            }
            if (file[i].isDirectory()) {// 是目录，递归调用
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 此方法适用于清空目录
     * 
     * @param filepath
     * @throws IOException
     */
    public static void clearDir(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        clearDir(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }

}
