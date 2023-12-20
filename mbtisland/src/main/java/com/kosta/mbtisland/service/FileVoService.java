package com.kosta.mbtisland.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileVoService {
	public String insertFile(Integer no, List<MultipartFile> files) throws Exception;
}
