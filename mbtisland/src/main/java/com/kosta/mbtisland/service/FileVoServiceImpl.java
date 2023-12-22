package com.kosta.mbtisland.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.mbtisland.entity.FileVo;
import com.kosta.mbtisland.repository.FileVoRepository;

@Service
public class FileVoServiceImpl implements FileVoService {
	@Autowired
	FileVoRepository fileVoRepository;
	
	// 파일 업로드 경로
	@Value("${upload.path}") // org.springframework.beans.factory.annotation.Value
	private String uploadPath;
	
	// 파일 저장
	@Override
	public String insertFile(String boardType, Integer postNo, List<MultipartFile> files) throws Exception {
		LocalDate currentDate = LocalDate.now();
		Timestamp writeDate = Timestamp.valueOf(currentDate.atStartOfDay());
		
		// 파일 저장 경로
//		String dir = "";
		// 파일 인덱스 목록
		String fileNums = "";
		
		if(files!=null && files.size()!=0) {
			for(MultipartFile file : files) {
				// Entity 생성 (new FileVo()로 해도 무방)
				FileVo fileVo = FileVo.builder()
						.filePath(uploadPath)
						.fileName(file.getOriginalFilename())
						.fileType(file.getContentType())
//						.fileSize((int) file.getSize())
						.fileSize((long) file.getSize())
						.uploadDate(writeDate)
						.postNo(postNo)
						.boardType(boardType).build();

				// Repository에 업로드할 파일을 save
				fileVoRepository.save(fileVo);
				
				// upload 폴더에 파일 업로드
				File uploadFile = new File(uploadPath + fileVo.getFileIdx());
				file.transferTo(uploadFile);
				
				// file들의 번호를 구분자 , 로 나누어 목록으로 만듬
				// 빈 문자열이 아닐 때
				// 빈 문자열 상태인 0번째 인덱스를 제외하고, 1번째 인덱스부터 값 앞에 구분자 ,를 붙여줌
				if(!fileNums.equals(""))
					fileNums += ",";
				fileNums += fileVo.getFileIdx();
			}
		}
		
		// 파일 인덱스 목록 반환
		return fileNums;
	}
	
	// 이미지 출력
	@Override
	public void readImage(Integer fileIdx, OutputStream out) throws Exception {
		// 경로 설정
		String dir = "c:/upload/";
		FileInputStream fis = new FileInputStream(dir + fileIdx);
		FileCopyUtils.copy(fis, out);
		fis.close();
	}
}
