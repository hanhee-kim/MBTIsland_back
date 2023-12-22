package com.kosta.mbtisland.service;

import java.io.OutputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileVoService {
	public String insertFile(String boardType, Integer no, List<MultipartFile> files) throws Exception; // 파일 저장
	public void readImage(Integer fileIdx, OutputStream out) throws Exception; // 이미지 출력
}
