//package com.kietlaptrinh.shop_ecomerce.controllers;
//
//import com.kietlaptrinh.shop_ecomerce.services.FileUploadService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/file")
//@CrossOrigin
//public class FileUploadController {
//
//    @Autowired
//    FileUploadService fileUploadService;
//
//    @PostMapping
//    public ResponseEntity<?> fileUpload(@RequestParam(value = "file",required = true) MultipartFile file
//            , @RequestParam(value = "fileName",required = true) String fileName){
//
//        int statusCode = fileUploadService.uploadFile(file,fileName);
//        return new ResponseEntity<>(statusCode == 201 ? HttpStatus.CREATED: HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}

package com.kietlaptrinh.shop_ecomerce.controllers;

import com.kietlaptrinh.shop_ecomerce.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String uploadedUrl = fileUploadService.uploadFile(file);
            return ResponseEntity.ok(uploadedUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}

