package com.shopme.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir,
                                String fileName,
                                MultipartFile multipartFile) throws IOException {

        // file name
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            // create files
            Files.createDirectories(uploadPath);
        }

        // get input stream from multipartFile
        try (InputStream inputStream = multipartFile.getInputStream()){
            // call resolve() to create resolved Path
            // path: đường dẫn

            // filePath = uploadPath\fileName
            Path filePath = uploadPath.resolve(fileName);


            // REPLACE_EXISTING – replace a file if it exists
            // Files.copy(inputStream, Path, CopyOptions)
            // used to copy bytes from inputStream to file(filePath)
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Could not save file: " + fileName, e);
        }

    }

    // clear directory
    public static void clearDir(String dir){

        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                // isisDirectory
                // Check if the specified path
                // is a directory or not
                if(!Files.isDirectory(file)){
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete file: " + file);
                        // System.out.println("Could not delete file: " + file);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory: " + dirPath);
            // System.out.println("Could not list directory: " + dirPath);
        }

    }


}
