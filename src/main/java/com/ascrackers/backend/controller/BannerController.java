package com.ascrackers.backend.controller;

import com.ascrackers.backend.dto.BannerDto;
import com.ascrackers.backend.dto.BannerRequest;
import com.ascrackers.backend.model.Banner;
import com.ascrackers.backend.repository.BannerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Homepage hero banner slides.
 * GET is public (storefront reads it on every page load).
 * PUT is protected by AdminAuthFilter (needs the admin Bearer token) and
 * replaces the whole ordered list in one call — matching how the admin
 * Banner tab saves (reorder / add / remove, then Save banner).
 */
@RestController
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerRepository repo;

    public BannerController(BannerRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<BannerDto> getAll() {
        return repo.findAllByOrderBySortOrderAsc()
                .stream()
                .map(BannerDto::from)
                .toList();
    }

    @PutMapping
    @Transactional
    public List<BannerDto> replaceAll(@Valid @RequestBody List<BannerRequest> banners) {
        repo.deleteAllInBatch();

        List<Banner> saved = new java.util.ArrayList<>();
        int order = 0;
        for (BannerRequest request : banners) {
            Banner banner = new Banner();
            banner.setUrl(request.getUrl());
            banner.setSortOrder(order++);
            saved.add(repo.save(banner));
        }

        return saved.stream().map(BannerDto::from).toList();
    }
}
