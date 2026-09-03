package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.dto.ProductDto;
import uz.rasta.entity.*;
import uz.rasta.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ShopRepository shopRepository;
    private final StockMovementRepository stockMovementRepository;

    public List<ProductDto.Response> listByShop(UUID shopId, boolean publicView) {
        List<Product> products = publicView
                ? productRepository.findByShopIdAndVisibleTrueOrderBySortOrderAsc(shopId)
                : productRepository.findByShopIdOrderBySortOrderAsc(shopId);

        return products.stream()
                .map(p -> {
                    List<ProductVariant> variants = variantRepository.findByProductId(p.getId());
                    return ProductDto.Response.from(p, variants);
                })
                .toList();
    }

    public ProductDto.Response getById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        List<ProductVariant> variants = variantRepository.findByProductId(productId);
        return ProductDto.Response.from(product, variants);
    }

    @Transactional
    public ProductDto.Response create(UUID shopId, ProductDto.CreateRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        Product product = Product.builder()
                .shop(shop)
                .catId(request.catId())
                .nameEn(request.nameEn())
                .nameRu(request.nameRu())
                .nameUz(request.nameUz())
                .descEn(request.descEn())
                .descRu(request.descRu())
                .descUz(request.descUz())
                .price(request.price())
                .visible(request.visible() != null ? request.visible() : true)
                .sortOrder(request.sortOrder() != null ? request.sortOrder() : 0)
                .tone(request.tone())
                .build();

        product = productRepository.save(product);

        List<ProductVariant> variants = new ArrayList<>();
        if (request.variants() != null) {
            for (ProductDto.VariantRequest vr : request.variants()) {
                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .optionsJson(vr.optionsJson())
                        .barcode(vr.barcode())
                        .qty(vr.qty() != null ? vr.qty() : 0)
                        .avgCost(vr.avgCost())
                        .threshold(vr.threshold() != null ? vr.threshold() : 5)
                        .build();
                variants.add(variantRepository.save(variant));
            }
        }

        return ProductDto.Response.from(product, variants);
    }

    @Transactional
    public ProductDto.Response update(UUID shopId, UUID productId, ProductDto.UpdateRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.getShop().getId().equals(shopId)) {
            throw new IllegalArgumentException("Product does not belong to this shop");
        }

        if (request.catId() != null) product.setCatId(request.catId());
        if (request.nameEn() != null) product.setNameEn(request.nameEn());
        if (request.nameRu() != null) product.setNameRu(request.nameRu());
        if (request.nameUz() != null) product.setNameUz(request.nameUz());
        if (request.descEn() != null) product.setDescEn(request.descEn());
        if (request.descRu() != null) product.setDescRu(request.descRu());
        if (request.descUz() != null) product.setDescUz(request.descUz());
        if (request.price() != null) product.setPrice(request.price());
        if (request.visible() != null) product.setVisible(request.visible());
        if (request.sortOrder() != null) product.setSortOrder(request.sortOrder());
        if (request.tone() != null) product.setTone(request.tone());

        product = productRepository.save(product);

        // Update variants if provided
        List<ProductVariant> variants;
        if (request.variants() != null) {
            // Get existing variant IDs
            List<ProductVariant> existingVariants = variantRepository.findByProductId(productId);
            List<UUID> incomingIds = request.variants().stream()
                    .map(ProductDto.VariantRequest::id)
                    .filter(id -> id != null)
                    .toList();

            // Delete variants not in the incoming list
            for (ProductVariant ev : existingVariants) {
                if (!incomingIds.contains(ev.getId())) {
                    variantRepository.delete(ev);
                }
            }

            variants = new ArrayList<>();
            for (ProductDto.VariantRequest vr : request.variants()) {
                if (vr.id() != null) {
                    // Update existing
                    ProductVariant existing = variantRepository.findById(vr.id())
                            .orElseThrow(() -> new IllegalArgumentException("Variant not found: " + vr.id()));
                    if (vr.optionsJson() != null) existing.setOptionsJson(vr.optionsJson());
                    if (vr.barcode() != null) existing.setBarcode(vr.barcode());
                    if (vr.qty() != null) existing.setQty(vr.qty());
                    if (vr.avgCost() != null) existing.setAvgCost(vr.avgCost());
                    if (vr.threshold() != null) existing.setThreshold(vr.threshold());
                    variants.add(variantRepository.save(existing));
                } else {
                    // Create new variant
                    ProductVariant variant = ProductVariant.builder()
                            .product(product)
                            .optionsJson(vr.optionsJson())
                            .barcode(vr.barcode())
                            .qty(vr.qty() != null ? vr.qty() : 0)
                            .avgCost(vr.avgCost())
                            .threshold(vr.threshold() != null ? vr.threshold() : 5)
                            .build();
                    variants.add(variantRepository.save(variant));
                }
            }
        } else {
            variants = variantRepository.findByProductId(productId);
        }

        return ProductDto.Response.from(product, variants);
    }

    @Transactional
    public void delete(UUID shopId, UUID productId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.getShop().getId().equals(shopId)) {
            throw new IllegalArgumentException("Product does not belong to this shop");
        }

        variantRepository.deleteByProductId(productId);
        productRepository.delete(product);
    }

    @Transactional
    public void restock(UUID shopId, ProductDto.RestockRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        ProductVariant variant = variantRepository.findById(request.variantId())
                .orElseThrow(() -> new IllegalArgumentException("Variant not found"));

        variant.setQty(variant.getQty() + request.qty());

        // Recalculate weighted average cost
        if (request.unitCost() != null && variant.getAvgCost() != null) {
            int oldQty = variant.getQty() - request.qty();
            var oldTotal = variant.getAvgCost().multiply(java.math.BigDecimal.valueOf(oldQty));
            var newTotal = request.unitCost().multiply(java.math.BigDecimal.valueOf(request.qty()));
            variant.setAvgCost(oldTotal.add(newTotal).divide(
                    java.math.BigDecimal.valueOf(variant.getQty()), 2, java.math.RoundingMode.HALF_UP));
        } else if (request.unitCost() != null) {
            variant.setAvgCost(request.unitCost());
        }

        variantRepository.save(variant);

        StockMovement movement = StockMovement.builder()
                .shop(shop)
                .variantId(request.variantId())
                .productId(request.productId())
                .delta(request.qty())
                .reason(StockMovement.MovementReason.RESTOCK)
                .unitCost(request.unitCost())
                .note(request.note())
                .build();

        stockMovementRepository.save(movement);
    }

    @Transactional
    public void adjust(UUID shopId, ProductDto.AdjustRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        ProductVariant variant = variantRepository.findById(request.variantId())
                .orElseThrow(() -> new IllegalArgumentException("Variant not found"));

        variant.setQty(variant.getQty() + request.delta());
        if (variant.getQty() < 0) {
            throw new IllegalArgumentException("Stock cannot go negative");
        }
        variantRepository.save(variant);

        StockMovement.MovementReason reason;
        try {
            reason = StockMovement.MovementReason.valueOf(request.reason().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reason: " + request.reason());
        }

        StockMovement movement = StockMovement.builder()
                .shop(shop)
                .variantId(request.variantId())
                .productId(request.productId())
                .delta(request.delta())
                .reason(reason)
                .note(request.note())
                .build();

        stockMovementRepository.save(movement);
    }
}
