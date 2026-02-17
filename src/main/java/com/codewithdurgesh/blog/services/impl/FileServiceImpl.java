package com.codewithdurgesh.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codewithdurgesh.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename(); // abc.jpg / abc.png

        String randomId = UUID.randomUUID().toString();
        String fileName =
                randomId.concat(originalName.substring(originalName.lastIndexOf(".")));

        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(path + File.separator + fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName)
            throws FileNotFoundException {

        String fullPath = path + File.separator + fileName;
        return new FileInputStream(fullPath);
    }
}
