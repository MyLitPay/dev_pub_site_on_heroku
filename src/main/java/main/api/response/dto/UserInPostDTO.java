package main.api.response.dto;

import main.model.User;

public class UserInPostDTO {

    private int id;
    private String name;

    public UserInPostDTO() {
    }

    public UserInPostDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

        public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
