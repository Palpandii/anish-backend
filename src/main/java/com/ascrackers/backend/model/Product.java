package com.ascrackers.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String nameEn;

    private String nameTa;

    private String qtyUnit;

    private Double mrp;

    private Double price;

    @Column(length = 1000)
    private String image;

    // YouTube video id or full link for this product's demo video
    private String youtubeId;

    // Video uploaded straight from the admin's phone/computer (Cloudinary URL).
    // A product can have this, a YouTube link, both, or neither.
    @Column(length = 1000)
    private String videoUrl;

    @Column(name = "in_stock", nullable = false)
    private Boolean inStock = true;

    public Product() {}

    // ---- getters and setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getNameTa() { return nameTa; }
    public void setNameTa(String nameTa) { this.nameTa = nameTa; }

    public String getQtyUnit() { return qtyUnit; }
    public void setQtyUnit(String qtyUnit) { this.qtyUnit = qtyUnit; }

    public Double getMrp() { return mrp; }
    public void setMrp(Double mrp) { this.mrp = mrp; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getYoutubeId() { return youtubeId; }
    public void setYoutubeId(String youtubeId) { this.youtubeId = youtubeId; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Boolean getInStock() { return inStock; }
    public void setInStock(Boolean inStock) { this.inStock = inStock; }
}