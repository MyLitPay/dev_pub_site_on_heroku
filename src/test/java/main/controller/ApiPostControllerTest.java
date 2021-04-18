package main.controller;

import main.api.request.PostRequest;
import main.api.request.VoteRequest;
import main.api.response.PostResponse;
import main.api.response.ResultResponse;
import main.api.response.dto.*;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ApiPostControllerTest extends AbstractIntegrationTest {

    final PostResponse postResponse;

    {
        postResponse = new PostResponse();
        postResponse.setPosts(List.of(new PostDTO(
                2, 1617733320, new UserInPostDTO(3, "William"),
                "Двумерные тестовые функции для оптимизации",
                " Двумерные функции принимают два входных значения (x и y) и выводят единожды вычисленное на основе входа значение. Эти функции — одни из самых простых...",
                1, 1, 3, 11)));
        postResponse.setCount(1);
    }

    @Test
    public void getPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post")
                .param("offset", "0").param("limit", "10")
                .param("mode", "recent"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(postResponse)));
    }

    @Test
    public void getPostsByQuery() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/search")
                .param("offset", "0").param("limit", "10")
                .param("query", "Тестовые функции"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(postResponse)));
    }

    @Test
    public void getPostsByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/byDate")
                .param("offset", "0").param("limit", "10")
                .param("date", "2021-04-06"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(postResponse)));
    }

    @Test
    public void getPostsByTag() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/byTag")
                .param("offset", "0").param("limit", "10")
                .param("tag", "Maths"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(postResponse)));
    }

    @Test
    public void getPostById() throws Exception {
        login("fredo@m.com", "111111");
        PostByIdDTO post = new PostByIdDTO(
                2, 1617733320, true, new UserInPostDTO(3, "William"),
                "Двумерные тестовые функции для оптимизации",
                "<span style=\"color: rgb(51, 51, 51); font-family: -apple-system, system-ui, &quot;Segoe UI&quot;, Arial, sans-serif; font-size: 16px; background-color: rgb(255, 255, 255);\">Двумерные функции принимают два входных значения (x и y) и выводят единожды вычисленное на основе входа значение. Эти функции — одни из самых простых для изучения оптимизации. Их преимущество в том, что они могут визуализироваться в виде контурного графика или графика поверхности, показывающего топографию проблемной области с оптимумом и уникальными элементами, которые отмечены точками. В этом туториале вы ознакомитесь со стандартными двумерными тестовыми функциями, которые можно использовать при изучении оптимизации функций. Давайте начнём.<img src=\"/upload/FaX/eWl/Txv/No_face.jpeg\"></span>",
                1, 1, 12, List.of(
                new CommentDTO(1, 1617906120, "Nice post", new UserInCommentDTO(2, "Fredo", "/upload/EVn/RjG/wVS/Panda.jpeg"), null),
                new CommentDTO(2, 1617913320, "Thank you", new UserInCommentDTO(3, "William", "/upload/fgb/MOC/Zij/fox.jpeg"), 1),
                new CommentDTO(3, 1617992520, "Some comment", new UserInCommentDTO(1, "Moderator", "/upload/AeA/Cjm/WQt/moder.jpeg"), null)),
                Set.of("Maths", "Robots", "Physics"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(post)));
    }

    @Test
    public void getMyPosts() throws Exception {
        login("willi@m.com", "111111");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/my")
                .param("offset", "0").param("limit", "10")
                .param("status", "published"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(postResponse)));
    }

    @Test
    public void getModerate() throws Exception {
        login("moder@m.com", "000000");
        PostResponse post = new PostResponse();
        post.setPosts(List.of(new PostDTO(
                1, 1617297737, new UserInPostDTO(2, "Fredo"),
                "Атаки на компьютерное зрение",
                " Компьютерное зрение — направление в области анализа данных. Системы, которые оснащаются этой технологией, могут отвечать за очень важные процессы. Дл...",
                0, 0, 0, 0)));
        post.setCount(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/moderation")
                .param("offset", "0").param("limit", "10")
                .param("status", "new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(post)));
    }

    @Test
    public void createPost() throws Exception {
        login("fredo@m.com", "111111");
        PostRequest postRequest = new PostRequest(
                new Date().getTime(), (byte) 1, "Test using", Set.of("Spring"),
                "For the standard profile, the application will have a standalone MySQL database configuration, which requires having the MySQL server installed and running, with a proper user and database set up."
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(postRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(new ResultResponse(true))));
    }

    @Test
    public void updatePost() throws Exception {
        login("willi@m.com", "111111");
        PostRequest postRequest = new PostRequest(
                new Date().getTime(), (byte) 0, "Test using", Set.of("Spring", "Java"),
                "For the standard profile, the application will have a standalone MySQL database configuration, which requires having the MySQL server installed and running, with a proper user and database set up."
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/api/post/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(postRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(new ResultResponse(true))));
    }

    @Test
    public void like() throws Exception {
        login("fredo@m.com", "111111");
        VoteRequest voteRequest = new VoteRequest(2);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/like")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(voteRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(new ResultResponse(true))));
    }

    @Test
    public void dislike() throws Exception {
        login("moder@m.com", "000000");
        VoteRequest voteRequest = new VoteRequest(2);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post/dislike")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(voteRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(new ResultResponse(true))));
    }
}