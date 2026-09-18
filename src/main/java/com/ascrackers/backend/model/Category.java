package com.ascrackers.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    private String id; // e.g. "single-sound"

    private String nameEn;
    private String nameTa;
    private String image;

    public Category() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getNameTa() { return nameTa; }
    public void setNameTa(String nameTa) { this.nameTa = nameTa; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
