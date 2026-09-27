package com.ascrackers.backend.config;

import com.ascrackers.backend.model.Category;
import com.ascrackers.backend.model.Product;
import com.ascrackers.backend.repository.CategoryRepository;
import com.ascrackers.backend.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * Loads the full 2026 price list into the database the first time the app
 * starts against an empty catalog, so the storefront (which reads /api/products
 * and /api/categories straight from this database) always has every item from
 * "FINAL_ANISH_CRACKERS_PRICE_LIST_2026_AUG.pdf" without anyone having to
 * re-type 219 products by hand in the admin panel.
 *
 * Safe to deploy repeatedly: it only inserts when the corresponding table is
 * completely empty, so it will never duplicate rows or overwrite anything an
 * admin has already added, edited, or removed.
 */
@Component
public class CatalogSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogSeeder.class);

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CatalogSeeder(CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) {
        seedCategories();
        seedProducts();
    }

    private void seedCategories() {
        if (categoryRepo.count() > 0) {
            log.info("[CatalogSeeder] categories table already has data — skipping category seed.");
            return;
        }
        try (InputStream in = new ClassPathResource("seed/categories.json").getInputStream()) {
            List<Category> categories = objectMapper.readValue(
                    in, objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));
            categoryRepo.saveAll(categories);
            log.info("[CatalogSeeder] Seeded {} categories from the 2026 price list.", categories.size());
        } catch (Exception e) {
            log.error("[CatalogSeeder] Failed to seed categories", e);
        }
    }

    private void seedProducts() {
        if (productRepo.count() > 0) {
            log.info("[CatalogSeeder] products table already has data — skipping product seed.");
            return;
        }
        try (InputStream in = new ClassPathResource("seed/products.json").getInputStream()) {
            List<Product> products = objectMapper.readValue(
                    in, objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));
            productRepo.saveAll(products);
            log.info("[CatalogSeeder] Seeded {} products from the 2026 price list.", products.size());
        } catch (Exception e) {
            log.error("[CatalogSeeder] Failed to seed products", e);
        }
    }
}
