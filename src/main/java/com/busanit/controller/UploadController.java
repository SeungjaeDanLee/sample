package com.busanit.controller;

import com.busanit.domain.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@Log4j2
public class UploadController {

    @Value("${com.busanit.upload.path}")    // application.properties의 변수
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            // 이미지 파일만 업로드 가능하도록
            if (uploadFile.getContentType().startsWith("image") == false) {
                log.warn("파일은 이미지만 업로드 가능합니다.");
                // 403 Forbidden
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(
                    originalName.lastIndexOf("\\") + 1);

            log.info("fileName: " + fileName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID를 이용하여 동일한 파일명이 나오지 않도록 처리
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath +
                    File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try {
                // 파일 저장(MultipartFile의 transferTo() 사용)
                uploadFile.transferTo(savePath);

                // 썸네일 생성
                String thumbnailSaveName = uploadPath + File.separator
                        + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                // 파일로 생성
                File thumbnalFile = new File(thumbnailSaveName);
                // 썸네일 이미지 생성(가로 100, 세로 100)
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnalFile,
                        100, 100);
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    private String makeFolder() {
        String str = LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        // 폴더 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName: " + srcFileName);
            File file = new File(uploadPath + File.separator +
                    srcFileName);
            log.info("file: " + file);
            HttpHeaders header = new HttpHeaders();

            // MIME타입 처리
            header.add("Content-Type",
                    Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
                    header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        log.info("@@@fileName: " + fileName);
        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            // 원본 파일 삭제
            File file = new File(uploadPath + File.separator
                                                        + srcFileName);
            boolean result = file.delete();

            // 썸네일 파일 삭제
            File thumbnail = new File(file.getParent(),
                                "s_" + file.getName());
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}










