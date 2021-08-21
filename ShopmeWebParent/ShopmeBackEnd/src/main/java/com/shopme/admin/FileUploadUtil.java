package com.shopme.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

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
            Path filePath = uploadPath.resolve(fileName);

            // REPLACE_EXISTING – replace a file if it exists
            // Files.copy(source, target, ...)
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Could not save file: " + fileName, e);
        }

    }

}
