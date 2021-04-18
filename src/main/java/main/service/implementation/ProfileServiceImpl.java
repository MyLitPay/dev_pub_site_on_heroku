package main.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.api.request.ProfileRequest;
import main.api.response.ResultResponse;
import main.config.SecurityConfig;
import main.exception.UploadImageException;
import main.model.User;
import main.repo.UserRepository;
import main.security.UserDetailsServiceImpl;
import main.service.ProfileService;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileServiceImpl implements ProfileService {
    final UserDetailsServiceImpl userDetailsService;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_SIZE_PHOTO = 5242880;

    public ProfileServiceImpl(UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = SecurityConfig.passwordEncoder();
    }

    @Override
    public ResultResponse editProfile(MultipartFile photo,
                                      String name,
                                      String email,
                                      String password,
                                      String removePhoto,
                                      String request) throws IOException {

        User user = userDetailsService.getUserFromContextHolder();
        Map<String, String> errors = new HashMap<>();

        if (request != null) {
            ProfileRequest jsonRequest = new ObjectMapper().readValue(request, ProfileRequest.class);
            removePhoto = jsonRequest.getRemovePhoto();
            name = jsonRequest.getName();
            email = jsonRequest.getEmail();
            password = jsonRequest.getPassword();
        }

        if (name != null) {
            if (!name.matches("\\w+")) {
                errors.put("name", "Имя указано неверно");
            } else {
                user.setName(name);
            }
        }

        if (email != null && !email.equals(user.getEmail())) {
            if (userRepository.findByEmail(email).isPresent()) {
                errors.put("email", "Этот e-mail уже зарегистрирован");
            } else {
                user.setEmail(email);
            }
        }

        if (password != null) {
            if (password.length() < MIN_PASSWORD_LENGTH) {
                errors.put("password", "Пароль короче 6-ти символов");
            } else {
                user.setPassword(passwordEncoder.encode(password));
            }
        }

        if (removePhoto != null && removePhoto.equals("1")) {
            user.setPhoto(null);
        }

        if (photo != null) {
            if (photo.getSize() > MAX_SIZE_PHOTO) {
                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
            }

            BufferedImage img = ImageIO.read(photo.getInputStream());
            BufferedImage rszImg = Scalr.resize(img,36, 36);

            Path rootFolder = createRandomPathFolders();
            File filePhoto = new File(rootFolder + File.separator + photo.getOriginalFilename());
            String[] photoName = Objects.requireNonNull(photo.getOriginalFilename()).split("\\.");
            String formatName = photoName[photoName.length - 1];
            ImageIO.write(rszImg, formatName, filePhoto);

            String shortPath = getShortPathOfImage(filePhoto.getPath());
            user.setPhoto(shortPath);
        }

        ResultResponse response = new ResultResponse();

        if (errors.isEmpty()) {
            userRepository.saveAndFlush(user);
            response.setResult(true);
        } else {
            response.setResult(false);
            response.setErrors(errors);
        }

        return response;
    }

    @Override
    public String uploadImage(MultipartFile image)  {
        if (image.getSize() > MAX_SIZE_PHOTO) {
            Map<String, String> error = Map.of("image", "Размер файла превышает допустимый размер");
            throw new UploadImageException(error);
        }

        String s = File.separator;
        Path rootImageDir;
        try {
            rootImageDir = createRandomPathFolders();
        } catch (IOException ex) {
            Map<String, String> error = Map.of("image", "Ошибка сохранения файла");
            throw new UploadImageException(error);
        }

        String imageDir = rootImageDir + s + image.getOriginalFilename();
        try {
            image.transferTo(Paths.get(imageDir));
        } catch (IOException ex) {
            Map<String, String> error = Map.of("image", "Файл не выбран");
            throw new UploadImageException(error);
        }


        return getShortPathOfImage(imageDir);
    }

    private Path createRandomPathFolders() throws IOException {
        String randomString = RandomStringUtils.randomAlphabetic(9);
        String s = File.separator;

        String dir = "upload" + s +
                randomString.substring(0, 3) + s +
                randomString.substring(3, 6) + s +
                randomString.substring(6) + s;

        return Files.createDirectories(Paths.get(dir));
    }

    private String getShortPathOfImage(String pathOfImage) {
        String shortPath = "";
        Pattern pattern = Pattern.compile("/upload.+");
        Matcher matcher = pattern.matcher(pathOfImage);
        if (matcher.find()) {
            shortPath = matcher.group();
        }
        return shortPath;
    }
}
