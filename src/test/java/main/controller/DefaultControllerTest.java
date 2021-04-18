package main.controller;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.PrintWriter;

import static org.junit.Assert.*;

public class DefaultControllerTest extends AbstractIntegrationTest {

    @Test
    public void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void redirectToIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/recent"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void getImage() throws Exception {
        FileSystemResource resource = new FileSystemResource(
                new File("upload/zzX/HrV/NAd/No_face.jpeg"));
        mockMvc.perform(MockMvcRequestBuilders.get("/upload/zzX/HrV/NAd/No_face.jpeg"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}