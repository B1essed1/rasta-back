CREATE TABLE product_images (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID         NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    variant_id UUID         REFERENCES product_variants(id) ON DELETE SET NULL,
    url        VARCHAR(500) NOT NULL,
    sort_order INTEGER      NOT NULL DEFAULT 0
);

CREATE INDEX idx_product_images_product_id ON product_images(product_id);
CREATE INDEX idx_product_images_variant_id ON product_images(variant_id);

-- Migrate existing image_url data
INSERT INTO product_images (product_id, url, sort_order)
SELECT id, image_url, 0 FROM products WHERE image_url IS NOT NULL;

-- Drop old column
ALTER TABLE products DROP COLUMN image_url;
