package com.maumjido.generate.mybatis.source.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
  private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
  private static final int BUFFER_SIZE = 1024;

  /**
   * 텍스트 포맷의 파일을 줄단위로 읽어 배열로 반환한다.
   * 
   * @param filePath
   *          파일경로
   * @return String[]
   * @throws IOException
   */
  public static String[] readFileByLine(String filePath) throws IOException {

    ArrayList<String> resultList = new ArrayList<String>();
    String[] result = null;

    String line = "";
    BufferedReader in = null;

    try {
      in = new BufferedReader(new FileReader(filePath));

      while ((line = in.readLine()) != null) {
        if (!line.trim().equals("")) {
          resultList.add(line);
        }
      }

    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (in != null)
        in.close();
    }

    result = new String[resultList.size()];
    resultList.toArray(result);

    return result;
  }

  public static void fileWrite(String fileFullPath, String contents) throws UnsupportedEncodingException, IOException {
    File file = new File(fileFullPath);
    logger.debug("fileFullPath : {}", fileFullPath);
    file.createNewFile();
    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
    dos.write(contents.getBytes("UTF-8"));
    dos.close();
  }

  /**
   * 텍스트 포맷의 파일을 줄단위로 읽어 String 반환한다.
   * 
   * @param filePath
   *          파일경로
   * @return String[]
   * @throws IOException
   */
  public static String readFileByLineForString(String filePath) throws IOException {

    String result = "";

    String line = "";
    BufferedReader in = null;

    try {
      in = new BufferedReader(new FileReader(filePath));

      while ((line = in.readLine()) != null) {
        if (!line.trim().equals("")) {
          result += line + "\n";
        }
      }

    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (in != null)
        in.close();
    }

    return result;
  }

  /**
   * 디렉토리로부터 파일목록을 가져온다.
   * 
   * @param path
   *          파일을 가져올 디렉토리
   * @return File[]
   */
  public static File[] getFileListFromDir(String path) {

    File dir = new File(path);
    File[] list = null;

    if (dir.exists()) {
      if (dir.isDirectory()) {
        list = dir.listFiles();
      }
    }
    return list;
  }

  public static boolean existDirectory(String fullPath) {
    File file = new File(fullPath);
    return file.isDirectory();
  }

  /**
   * path에 해당되는 디렉터리 생성
   * 
   * @param path
   * @return
   */
  public static boolean makeDirectory(String path) {
    boolean success = true;
    File directory = new File(path);

    if (!directory.exists()) {
      success = directory.mkdirs();
    }

    return success;
  }

  /**
   * 디렉토리로부터 파일목록을 가져온다. (하위 디렉터리&파일 포함)
   * 
   * @param path
   *          파일을 가져올 디렉토리
   * @return File[]
   */
  public static List<File> getFileListFromDirWithSubFile(String path, FileFilter fileFilter) {
    File dir = new File(path);
    List<File> fileList = new ArrayList<File>();
    if (dir.exists()) {
      if (dir.isDirectory()) {
        for (File subFile : dir.listFiles(fileFilter)) {
          fileList.add(subFile);
          if (subFile.isDirectory())
            fileList.addAll(getFileListFromDirWithSubFile(subFile.getAbsolutePath(), fileFilter));
        }
      }
    }
    return fileList;
  }

  /**
   * 주어진 경로로 부터 하위의 모든 경로들을 ArrayList 에 넣는다.
   */
  public static void getAllSubDirs(String fromPath, ArrayList<String> pathArray) {
    File folder = new File(fromPath);
    File[] subFiles = folder.listFiles();

    for (int i = 0; i < subFiles.length; i++) {
      if (subFiles[i].isDirectory()) {
        pathArray.add(subFiles[i].getAbsolutePath());

        getAllSubDirs(subFiles[i].getAbsolutePath(), pathArray);
      }
    }
  }

  public static File copy(String src, String targetDir) throws Exception {

    File srcFile = new File(src);
    if (!srcFile.exists())
      return null;

    File tgtDir = new File(targetDir);
    if (!tgtDir.exists())
      throw new IOException(targetDir + " not exists");
    if (!tgtDir.isDirectory())
      throw new IOException(targetDir + " not dir");

    File copied = null;
    if (srcFile.isDirectory()) {
      copied = new File(targetDir + File.separator + srcFile.getName());
      copied.mkdirs();
      String[] subs = srcFile.list();
      for (int i = 0; i < subs.length; i++) {
        copy(src + File.separator + subs[i], copied.getAbsolutePath());
      }
    } else {
      copied = copyFile(srcFile, targetDir);
    }

    return copied;
  }

  public static File copyFile(File src, String targetDir) throws Exception {

    if (!src.exists())
      return null;

    File tDir = new File(targetDir);
    if (!tDir.exists()) {
      tDir.mkdirs();
    } else if (!tDir.isDirectory()) {
      throw new IOException(targetDir + " not dir");
    }

    String fileName = src.getName();

    if (fileName != null && !fileName.equals("") && targetDir != null && !targetDir.equals("")) {
      while (existFile(fileName, targetDir)) {
        fileName = renameFile(fileName);
      }
    }

    File tFile = new File(targetDir + File.separator + fileName);

    InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(tFile);

    int read = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    while ((read = in.read(buf)) != -1)
      out.write(buf, 0, read);

    in.close();
    out.flush();
    out.close();
    fileName = "";

    return tFile;
  }

  public static boolean existFile(String fileName, String targetDir) throws Exception {

    boolean result = false;
    File[] files = getFileListFromDir(targetDir);
    for (int i = 0; files != null && i < files.length; i++) {
      if (files[i].getName().equals(fileName)) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static String renameFile(String fileName) throws Exception {

    if (fileName == null || fileName.equals(""))
      return fileName;
    int lastIdx = fileName.lastIndexOf(".");

    if (lastIdx == -1) {
      fileName = fileName + "1";
    } else {
      fileName = fileName.substring(0, lastIdx) + "1" + fileName.substring(lastIdx);
    }

    return fileName;
  }

  /**
   * 하위의 모든 파일과 디렉토리를 삭제한다.
   * 
   * @param file
   *          삭제할 파일 또는 디렉토리
   */
  public static void removeDir(File file) {
    if (file.exists()) {
      if (file.isDirectory()) {
        File[] list = file.listFiles();
        for (File f : list) {
          removeDir(f);
        }
        file.delete();
      } else {
        file.delete();
      }
    } // end of if( file.isDirectory() )
  } // end of if( file.exists() )

  /**
   * 하위의 모든 파일과 디렉토리를 삭제한다.
   * 
   * @param fileStr
   */
  public static void removeDir(String fileStr) {
    removeDir(new File(fileStr));
  } // end of if( file.exists() )

  public static boolean existFile(String fileFullPath) {
    File file = new File(fileFullPath);
    return file.exists();
  }

  public static String getCurrentProjectPath() {
    File projectPath = new File(".");
    String currentProjectPath = projectPath.getAbsoluteFile().toString().replaceAll("\\.", "");
    return currentProjectPath;
  }
}
