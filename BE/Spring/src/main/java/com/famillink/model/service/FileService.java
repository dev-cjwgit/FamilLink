package com.famillink.model.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {
    void init();

    void store(MultipartFile file);

    Path load(String filename);

    InputStreamResource loadAsResource(String filename) throws Exception;
}