package com.xcl.core.utils;

import com.google.common.base.Charsets;
import com.xcl.core.conf.HadoopConfig;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author :xiaochanglu
 * @Description :文件操作工具类 创建目录 文件写入 读取文件 拷贝文件和目录 删除文件和目录 从URL转换 基于统配和过滤查看文件和目录 比较文件内容 文件的更新时间 检查校验码
 * @date :2019/1/9 13:38
 */
@Slf4j
public class FileUtils {

    @Value("${hdfs.path: }")
    private String path;

    private FileSystem fileSystem;

    {
        try {
            fileSystem = HadoopConfig.getFileSystem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String uploadType = "1";

    private static String downloadType = "hdfs";

    /**
     * @param : [filepath]
     * @return : java.io.File
     * @Description ：判断是否存在目录或文件. 不存在则创建
     * @author : xcl
     * @date : 2019/1/9 9:52
     */
    public static File isChartPathExist(String filepath) {
        File file = new File(filepath);
        try {
            if (file.exists()) {
                if (file.isDirectory()) {
                    //目录
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                } else {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                }
            } else {

            }

        } catch (Exception e) {

        }
        return file;
    }

    public static boolean writer(File file, String string) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            writer.write(string);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static String read(File file) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    /**
     * @Description  ：从文件中按行读取内容
     * @author       : xcl
     * @param        : [file]
     * @return       : java.lang.String
     * @date         : 2019/3/18 15:54
     */
    public static List<String> lineRead(File file){
        List<String> lines = null;
        try{
            lines = com.google.common.io.Files.readLines(file, Charsets.UTF_8);
        }catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }

    private void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File MultipartFileToFile(MultipartFile file) {
        File f = null;
        try {

            if (file.equals("") || file.getSize() <= 0) {
                file = null;
            } else {
                InputStream ins = file.getInputStream();
                f = new File(file.getOriginalFilename());
                inputStreamToFile(ins, f);
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        }
    }

    /**
     * @param : [file, filePath]
     * @return : void
     * @Description ：本地文件上传
     * @author : xcl
     * @date : 2019/4/3 15:05
     */
    private void upLoadNIO(MultipartFile file, String filePath) {
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        InputStream is = null;
        try {
            is = file.getInputStream();
            //java NIO包提供了无缓冲情况下在两个通道之间直接传输字节的可能
            //为了读来自URL的文件，需从URL流创建 ReadableByteChannel
            readableByteChannel =
                Channels.newChannel(is);
            //从 ReadableByteChannel 读取字节将被传输至 FileChannel
            fileOutputStream = new FileOutputStream(filePath);
            fileChannel = fileOutputStream.getChannel();
            //然后使用transferFrom方法，从ReadableByteChannel 类下载来自URL的字节传输到FileChannel
            //transferTo() 和 transferFrom() 方法比简单使用缓存从流中读更有效
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                readableByteChannel.close();
                fileOutputStream.close();
                fileChannel.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param : [file, fileSaveFullPath]
     * @return : void
     * @Description ：hadoop服务文件上传
     * @author : xcl
     * @date : 2019/4/3 15:04
     */
    private void hdfs(MultipartFile file, String fileSaveFullPath) {
        System.out.println("*************" + fileSaveFullPath);
        Path pathDst = new Path(path + fileSaveFullPath);

        OutputStream out = null;
        InputStream is = null;
        try {
            System.out.println("=========" + fileSystem);
            is = file.getInputStream();
            out = fileSystem.create(pathDst);
            IOUtils.copyBytes(is, out, 4096, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out == null) {
                    out.close();
                }
                if (is == null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    static public void copyfileToHdfs(Configuration config, String url, File file) {
        try {
            FileSystem fs = FileSystem.get(URI.create(url), config);
            Path src = new Path(file.getPath());
            // 要上传到hdfs的目标路径
            Path dst = new Path(url);
            fs.copyFromLocalFile(src, dst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void upLoadIO1(MultipartFile file, String filePath) {
        try {
            BufferedInputStream in = new BufferedInputStream(file.getInputStream());
            //当从InputStream读取文件时，强烈建议使用BufferedInputStream去包装InputStream，用于提升性能
            //使用缓存可以提升性能。read方法每次读一个字节，每次方法调用意味着系统调用底层的文件系统。
            // 当JVM调用read()方法时，程序执行上下文将从用户模式切换到内核模式并返回
            //从性能的角度来看，这种上下文切换非常昂贵
            //为了读取URL的字节并写至本地文件，需要使用FileOutputStream 类的write方法
            //使用BufferedInputStream，read方法按照我们设置缓冲器大小读取文件。
            // 示例中我们设置一次读取1024字节，所以 BufferedInputStream 是必要的

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void upLoadIO2(MultipartFile file, String filePath) {
        //上述示例代码冗长，幸运的是在Java7中Files类包含处理IO操作的助手方法。
        // 可以使用File.copy()方法从InputStream中读取所有字节
        try {
            Files.copy(
                file.getInputStream(),
                Paths.get(filePath),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void upload(MultipartFile file, String filePath) {
        System.out.println("======================" + uploadType);
        if ("0".equals(uploadType)) {
            log.info("NFS fileupload...");
            upLoadNIO(file, filePath);
        }
        if ("1".equals(uploadType)) {
            log.info("HDFS fileupload...");
            hdfs(file, filePath);
        }
        if ("2".equals(uploadType)) {
            log.info("HDFS and NFS fileupload...");
            upLoadNIO(file, filePath);
            hdfs(file, filePath);
        }
    }

    public InputStream download(String fileSaveFullPath) {
        InputStream back = null;
        if ("hdfs".equals(downloadType)) {
            log.info("======hdfs下载......");
            back = hdfsDownLoad(fileSaveFullPath);
        } else {
            back = nfsDownLoad(fileSaveFullPath);
        }
        return back;
    }

    private InputStream hdfsDownLoad(String fileSaveFullPath) {
        //        FileSystem fs = HdfsUtils.getHdfsFileSystem();
        Path pathDst = new Path(path + fileSaveFullPath);
        InputStream is = null;
        try {
            is = fileSystem.open(pathDst);
        } catch (IOException e) {
            log.info(pathDst.toString() + " hdfs file not found...");
            e.printStackTrace();
        }
        return is;
    }

    private static InputStream nfsDownLoad(String fileSaveFullPath) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(fileSaveFullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    //    public static void main(String[] args) {
    //        System.out.println(writer(isChartPathExist("D:/test.txt"),"1111111111111111111111111111111"));
    //        System.out.println(read(isChartPathExist("D:/test.txt")));
    //        upLoadIO1(new File("D:/WebChat-master.zip"),"D:/test/test.zip");
    //        isChartPathExist("D:/filedata/nmyx/xcyz/20190330");
    //        String str = "D:\\filedata\\nmyx\\xcyz\\20190330";
    //        new File(str).mkdirs();
    //    }


}
