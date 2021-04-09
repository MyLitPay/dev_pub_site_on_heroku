package main.service;

import main.api.response.ResultResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    ResultResponse editProfile(MultipartFile photo,
                               String name,
                               String email,
                               String password,
                               String removePhoto,
                               String request) throws IOException;

    String uploadImage(MultipartFile image);
}
