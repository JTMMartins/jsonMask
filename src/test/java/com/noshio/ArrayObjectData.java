package com.noshio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrayObjectData {

    private String _id;
    private int index;
    private String guid;
    private boolean isActive;
    private String balance;
    private String picture;
    private int age;
    private String eyeColor;
    private String name;
    private String gender;
    private String company;
    private String email;
    private String phone;
    private String address;
    private String about;
    private String registered;
    private Double latitude;
    private Double longitude;
    private String tags;
    private List<Friend> friends;
    private String greeting;
    private String favoriteFruit;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Friend {
        private int id;
        private String name;

    }
}
