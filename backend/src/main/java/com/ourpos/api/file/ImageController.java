package com.ourpos.api.file;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final FileStore fileStore;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }
}
