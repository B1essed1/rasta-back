package uz.rasta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.rasta.dto.ProductDto;
import uz.rasta.dto.ShopDto;
import uz.rasta.entity.Shop;
import uz.rasta.repository.ShopRepository;
import uz.rasta.service.ProductService;
import uz.rasta.service.ShopService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final ShopService shopService;
    private final ShopRepository shopRepository;
    private final ProductService productService;

    @GetMapping("/discover")
    public ResponseEntity<List<ShopDto.Response>> discover(
            @RequestParam(required = false) Shop.ShopType type,
            @RequestParam(required = false) String location) {
        List<ShopDto.Response> shops = shopService.listAll();

        if (type != null) {
            shops = shops.stream()
                    .filter(s -> s.type() == type)
                    .toList();
        }

        if (location != null && !location.isBlank()) {
            String loc = location.toLowerCase();
            shops = shops.stream()
                    .filter(s -> s.location() != null && s.location().toLowerCase().contains(loc))
                    .toList();
        }

        return ResponseEntity.ok(shops);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShopDto.Response>> search(@RequestParam String q) {
        if (q == null || q.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        String query = q.toLowerCase();
        List<ShopDto.Response> results = shopService.listAll().stream()
                .filter(shop -> {
                    boolean nameMatch = shop.name().toLowerCase().contains(query);
                    boolean handleMatch = shop.handle().toLowerCase().contains(query);
                    boolean locationMatch = shop.location() != null
                            && shop.location().toLowerCase().contains(query);
                    return nameMatch || handleMatch || locationMatch;
                })
                .toList();

        return ResponseEntity.ok(results);
    }

    @GetMapping("/shops/{handle}/storefront")
    public ResponseEntity<Map<String, Object>> storefront(@PathVariable String handle) {
        ShopDto.Response shop = shopService.getByHandle(handle);
        ShopDto.ConfigResponse config = null;
        try {
            config = shopService.getConfig(shop.id());
        } catch (IllegalArgumentException ignored) {
            // Config may not exist
        }

        List<ProductDto.Response> products = productService.listByShop(shop.id(), true);

        return ResponseEntity.ok(Map.of(
                "shop", shop,
                "config", config != null ? config : Map.of(),
                "products", products
        ));
    }
}
