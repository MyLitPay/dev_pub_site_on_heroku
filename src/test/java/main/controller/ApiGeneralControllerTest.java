package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.api.response.dto.TagDTO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ApiGeneralControllerTest extends AbstractIntegrationTest {

    @Test
    public void init() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/init"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("DevPub"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subtitle").value("Рассказы разработчиков"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+7 922 669-63-95"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("paytonklon@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copyright").value("Mike Loginov"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.copyrightFrom").value("2021"));
    }

    @Test
    public void getSettings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/settings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.MULTIUSER_MODE").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.POST_PREMODERATION").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.STATISTICS_IS_PUBLIC").value(false));

    }

    @Test
    public void getTags() throws Exception {
        List<TagDTO> tagDTOList = List.of(
                new TagDTO("Maths", 1),
                new TagDTO("Robots", 1),
                new TagDTO("Physics", 1));
        String response = mapper.writeValueAsString(new TagResponse(tagDTOList));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tag")
                .queryParam("query", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void getCalendar() throws Exception {
        List<Integer> years = List.of(2021);
        Map<String, Integer> posts = Map.of("2021-04-06", 1);
        String response = mapper.writeValueAsString(new CalendarResponse(years, posts));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/calendar"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void uploadImage() throws Exception {
        login("fredo@m.com", "111111");

        File myImg = new File("upload/No_face.jpeg");
        byte[] myImgBytes = Files.readAllBytes(myImg.toPath());

        MockMultipartFile multipartFile = new MockMultipartFile("image", "No_face.jpeg",
                MediaType.MULTIPART_FORM_DATA_VALUE, myImgBytes);
        String response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/image")
                .file(multipartFile))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        File actual = new File(response.substring(1));
        assertEquals(myImg.getName(), actual.getName());
    }

    @Test
    public void addComment() throws Exception {
        login("fredo@m.com", "111111");
        CommentRequest request = new CommentRequest();
        request.setParentId(null);
        request.setPostId(2);
        request.setText("Test comment");
        String content = mapper.writeValueAsString(request);
        String response = mapper.writeValueAsString(new CommentResponse(4));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void moderate() throws Exception {
        login("moder@m.com", "000000");
        String content = mapper.writeValueAsString(new ModerationRequest(1, "accept"));
        String response = mapper.writeValueAsString(new ResultResponse(true));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/moderation")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void editProfile() throws Exception {
        login("fredo@m.com", "111111");

        File myImg = new File("upload/No_face.jpeg");
        byte[] myImgBytes = Files.readAllBytes(myImg.toPath());
        MockMultipartFile multipartImg = new MockMultipartFile("image", "No_face.jpeg",
                MediaType.MULTIPART_FORM_DATA_VALUE, myImgBytes);
        String response = mapper.writeValueAsString(new ResultResponse(true));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/profile/my")
                .file(multipartImg)
                .param("name", "Mike").param("email", "mike@m.com")
                .param("removePhoto", "0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void getMyStatistics() throws Exception {
        login("willi@m.com", "111111");
        String response = mapper.writeValueAsString(new StatisticsResponse(
                1, 1, 1, 11, 1617733320
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/statistics/my"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    public void getAllStatistics() throws Exception {
        login("moder@m.com", "000000");
        String response = mapper.writeValueAsString(new StatisticsResponse(
                1, 1, 1, 11, 1617733320
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/statistics/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void updateGlobalSettings() throws Exception {
        login("moder@m.com", "000000");
        String request = mapper.writeValueAsString(new SettingsRequest(true, true, true));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/settings")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(request))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/settings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.MULTIUSER_MODE").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.POST_PREMODERATION").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.STATISTICS_IS_PUBLIC").value(true));
    }
}