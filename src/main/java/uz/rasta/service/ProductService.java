package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.config.ApiException;
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
    private final ProductImageRepository imageRepository;
    private final ShopRepository shopRepository;
    private final StockMovementRepository stockMovementRepository;

    public List<ProductDto.Response> listByShop(UUID shopId, boolean publicView) {
        List<Product> products = publicView
                ? productRepository.findByShopIdAndVisibleTrueOrderBySortOrderAsc(shopId)
                : productRepository.findByShopIdOrderBySortOrderAsc(shopId);

        return products.stream()
                .map(p -> {
                    List<ProductVariant> variants = variantRepository.findByProductId(p.getId());
                    List<ProductImage> images = imageRepository.findByProductIdOrderBySortOrder(p.getId());
                    return ProductDto.Response.from(p, variants, images);
                })
                .toList();
    }

    public ProductDto.Response getById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ApiException.notFound("product.not.found"));
        List<ProductVariant> variants = variantRepository.findByProductId(productId);
        List<ProductImage> images = imageRepository.findByProductIdOrderBySortOrder(productId);
        return ProductDto.Response.from(product, variants, images);
    }

    @Transactional
    public ProductDto.Response create(UUID shopId, ProductDto.CreateRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
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

        List<ProductImage> images = imageRepository.findByProductIdOrderBySortOrder(product.getId());
        return ProductDto.Response.from(product, variants, images);
    }

    @Transactional
    public ProductDto.Response update(UUID shopId, UUID productId, ProductDto.UpdateRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ApiException.notFound("product.not.found"));

        if (!product.getShop().getId().equals(shopId)) {
            throw ApiException.badRequest("product.not.in.shop");
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

        List<ProductVariant> variants;
        if (request.variants() != null) {
            List<ProductVariant> existingVariants = variantRepository.findByProductId(productId);
            List<UUID> incomingIds = request.variants().stream()
                    .map(ProductDto.VariantRequest::id)
                    .filter(id -> id != null)
                    .toList();

            for (ProductVariant ev : existingVariants) {
                if (!incomingIds.contains(ev.getId())) {
                    variantRepository.delete(ev);
                }
            }

            variants = new ArrayList<>();
            for (ProductDto.VariantRequest vr : request.variants()) {
                if (vr.id() != null) {
                    ProductVariant existing = variantRepository.findById(vr.id())
                            .orElseThrow(() -> ApiException.notFound("variant.not.found"));
                    if (vr.optionsJson() != null) existing.setOptionsJson(vr.optionsJson());
                    if (vr.barcode() != null) existing.setBarcode(vr.barcode());
                    if (vr.qty() != null) existing.setQty(vr.qty());
                    if (vr.avgCost() != null) existing.setAvgCost(vr.avgCost());
                    if (vr.threshold() != null) existing.setThreshold(vr.threshold());
                    variants.add(variantRepository.save(existing));
                } else {
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

        List<ProductImage> images = imageRepository.findByProductIdOrderBySortOrder(productId);
        return ProductDto.Response.from(product, variants, images);
    }

    @Transactional
    public ProductDto.ImageResponse addImage(UUID shopId, UUID productId, String url, UUID variantId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));
        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ApiException.notFound("product.not.found"));
        if (!product.getShop().getId().equals(shopId)) {
            throw ApiException.badRequest("product.not.in.shop");
        }

        ProductVariant variant = null;
        if (variantId != null) {
            variant = variantRepository.findById(variantId)
                    .orElseThrow(() -> ApiException.notFound("variant.not.found"));
        }

        int nextOrder = imageRepository.findByProductIdOrderBySortOrder(productId).size();

        ProductImage image = ProductImage.builder()
                .product(product)
                .variant(variant)
                .url(url)
                .sortOrder(nextOrder)
                .build();

        image = imageRepository.save(image);
        return ProductDto.ImageResponse.from(image);
    }

    @Transactional
    public void removeImage(UUID shopId, UUID productId, UUID imageId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));
        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> ApiException.notFound("product.not.found"));

        imageRepository.delete(image);
    }

    @Transactional
    public void delete(UUID shopId, UUID productId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ApiException.notFound("product.not.found"));

        if (!product.getShop().getId().equals(shopId)) {
            throw ApiException.badRequest("product.not.in.shop");
        }

        imageRepository.deleteByProductId(productId);
        variantRepository.deleteByProductId(productId);
        productRepository.delete(product);
    }

    @Transactional
    public void restock(UUID shopId, ProductDto.RestockRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        ProductVariant variant = variantRepository.findById(request.variantId())
                .orElseThrow(() -> ApiException.notFound("variant.not.found"));

        variant.setQty(variant.getQty() + request.qty());

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
                .orElseThrow(() -> ApiException.notFound("shop.not.found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("shop.not.owner");
        }

        ProductVariant variant = variantRepository.findById(request.variantId())
                .orElseThrow(() -> ApiException.notFound("variant.not.found"));

        variant.setQty(variant.getQty() + request.delta());
        if (variant.getQty() < 0) {
            throw ApiException.badRequest("stock.negative");
        }
        variantRepository.save(variant);

        StockMovement.MovementReason reason;
        try {
            reason = StockMovement.MovementReason.valueOf(request.reason().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw ApiException.badRequest("stock.reason.invalid");
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
