package com.ominext.image.entity;

import lombok.Data;

@Data
public class Image {
    private Long id;
    private String title;
    private String img;
    private byte[] picByte1;
    private String picByte;
}
