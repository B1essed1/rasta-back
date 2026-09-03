package uz.rasta.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.rasta.dto.ShopDto;
import uz.rasta.entity.Shop;
import uz.rasta.entity.ShopConfig;
import uz.rasta.entity.User;
import uz.rasta.repository.ShopConfigRepository;
import uz.rasta.repository.ShopRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopConfigRepository shopConfigRepository;

    public List<ShopDto.Response> listAll() {
        return shopRepository.findByStatus(Shop.ShopStatus.LIVE)
                .stream()
                .map(ShopDto.Response::from)
                .toList();
    }

    public ShopDto.Response getByHandle(String handle) {
        Shop shop = shopRepository.findByHandle(handle)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found: " + handle));
        return ShopDto.Response.from(shop);
    }

    public ShopDto.Response getById(UUID id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));
        return ShopDto.Response.from(shop);
    }

    @Transactional
    public ShopDto.Response create(ShopDto.CreateRequest request, User owner) {
        if (shopRepository.existsByHandle(request.handle())) {
            throw new IllegalArgumentException("Handle already taken: " + request.handle());
        }

        Shop shop = Shop.builder()
                .handle(request.handle())
                .name(request.name())
                .tagline(request.tagline())
                .location(request.location())
                .type(request.type() != null ? request.type() : Shop.ShopType.OTHER)
                .owner(owner)
                .initials(extractInitials(request.name()))
                .coverColor(request.coverColor())
                .instagram(request.instagram())
                .telegram(request.telegram())
                .phone(request.phone())
                .build();

        shop = shopRepository.save(shop);

        // Create default config
        ShopConfig config = ShopConfig.builder()
                .shop(shop)
                .build();
        shopConfigRepository.save(config);

        return ShopDto.Response.from(shop);
    }

    @Transactional
    public ShopDto.Response update(UUID shopId, ShopDto.UpdateRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        if (request.name() != null) shop.setName(request.name());
        if (request.tagline() != null) shop.setTagline(request.tagline());
        if (request.location() != null) shop.setLocation(request.location());
        if (request.type() != null) shop.setType(request.type());
        if (request.status() != null) shop.setStatus(request.status());
        if (request.coverColor() != null) shop.setCoverColor(request.coverColor());
        if (request.instagram() != null) shop.setInstagram(request.instagram());
        if (request.telegram() != null) shop.setTelegram(request.telegram());
        if (request.phone() != null) shop.setPhone(request.phone());
        if (request.plan() != null) shop.setPlan(request.plan());

        shop = shopRepository.save(shop);
        return ShopDto.Response.from(shop);
    }

    @Transactional
    public void delete(UUID shopId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        shopConfigRepository.findByShopId(shopId).ifPresent(shopConfigRepository::delete);
        shopRepository.delete(shop);
    }

    public ShopDto.ConfigResponse getConfig(UUID shopId) {
        ShopConfig config = shopConfigRepository.findByShopId(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop config not found"));
        return ShopDto.ConfigResponse.from(config);
    }

    @Transactional
    public ShopDto.ConfigResponse updateConfig(UUID shopId, ShopDto.ConfigRequest request, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found"));

        if (!shop.getOwner().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not the owner of this shop");
        }

        ShopConfig config = shopConfigRepository.findByShopId(shopId)
                .orElseGet(() -> ShopConfig.builder().shop(shop).build());

        if (request.theme() != null) config.setTheme(request.theme());
        if (request.palette() != null) config.setPalette(request.palette());
        if (request.layout() != null) config.setLayout(request.layout());
        if (request.font() != null) config.setFont(request.font());
        if (request.customPaletteJson() != null) config.setCustomPaletteJson(request.customPaletteJson());

        config = shopConfigRepository.save(config);
        return ShopDto.ConfigResponse.from(config);
    }

    private String extractInitials(String name) {
        if (name == null || name.isBlank()) return "";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
    }
}
