package com.peer39.service.impl;

import com.peer39.helpers.SSLHelper;
import com.peer39.service.DownloaderConverterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DownloaderConverterServiceImpl implements DownloaderConverterService {
    @Value("${app.downloader.followRedirects}")
    private boolean followRedirects;

    @Override
    public String download(String url) throws IOException {
             return SSLHelper
                     .getConnection(url)
                     .followRedirects(followRedirects)
                     .referrer(url)
                     .get()
                     .text();
    }
}