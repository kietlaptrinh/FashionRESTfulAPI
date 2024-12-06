//package com.kietlaptrinh.shop_ecomerce.services;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@Service
//public class FileUploadService {
//    @Value("${FILE_ZONE}")
//    private String storageZone;
//
//    @Value("${FILE_UPLOAD_API_KEY}")
//    private String fileUploadKey;
//
//    @Value("${FILE_UPLOAD_HOST_URL}")
//    private String fileHostName;
//
//    public int uploadFile(MultipartFile file, String fileName){
//        try {
//            String urlString =  fileHostName+"/"+storageZone+"/"+fileName;
//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("PUT");
//            connection.setRequestProperty("AccessKey",fileUploadKey);
//            connection.setRequestProperty("Content-Type", "application/octet-stream");
//            connection.setDoOutput(true);
//
//
//            long fileSize = file.getSize();
//
//            try (BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream());
//                 BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream())) {
//
//                byte[] buffer = new byte[8192];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            int responseCode = connection.getResponseCode();
//            String responseMsg = connection.getResponseMessage();
//            return responseCode;
//        }
//        catch (Exception e){
//            return 500;
//        }
//    }
//
//
//}


package com.kietlaptrinh.shop_ecomerce.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;

    public FileUploadService(
            @Value("${CLOUDINARY_CLOUD_NAME}") String cloudName,
            @Value("${CLOUDINARY_API_KEY}") String apiKey,
            @Value("${CLOUDINARY_API_SECRET}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadFile(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );
            return uploadResult.get("secure_url").toString(); // Trả về URL của file đã upload
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to Cloudinary", e);
        }
    }
}

