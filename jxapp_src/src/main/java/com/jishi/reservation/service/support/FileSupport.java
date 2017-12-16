package com.jishi.reservation.service.support;

import com.jishi.reservation.util.Constant;
import com.jishi.reservation.util.FileCheckUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sloan on 2017/11/7.
 */
public class FileSupport {



    public static boolean checkImageFile(String fileName, MultipartFile multipartFile) throws IOException {

        File file = new File(multipartFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        String fileItType = FileCheckUtil.getFileItType(file);

        String suffix = returnSuffix(fileName);

        if (fileItType != null) {
            return FileCheckUtil.getFileSize(file) < Constant.MAX_FILE_SIZE && (fileItType.equals("jpg") ||
                    fileItType.equals("bmp") || fileItType.equals("png") ||
                    suffix.equals("JPG") || suffix.equals("BMP")  ||
                            suffix.equals("PNG") || suffix.equals("jpg") || suffix.equals("bmp"));
        } else {
            return false;
        }
    }

    private static String returnSuffix(String fileName) {

        int index = fileName.lastIndexOf(".");
        char[] ch = fileName.toCharArray();

//根据 copyValueOf(char[] data, int offset, int count) 取得最后一个字符串
        String lastString = String.copyValueOf(ch, index + 1, ch.length - index - 1);
        System.out.println("获取到的后缀名："+lastString);
        return lastString;
    }
}
