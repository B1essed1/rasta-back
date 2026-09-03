-- ============================================================
-- rasta.uz - Initial schema
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ---- Users ----
CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone       VARCHAR(20) NOT NULL UNIQUE,
    name        VARCHAR(100),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_login_at TIMESTAMPTZ
);

-- ---- Shops ----
CREATE TABLE shops (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    handle      VARCHAR(60)  NOT NULL UNIQUE,
    name        VARCHAR(120) NOT NULL,
    tagline     JSONB,
    location    VARCHAR(200),
    type        VARCHAR(20)  NOT NULL DEFAULT 'OTHER',
    status      VARCHAR(10)  NOT NULL DEFAULT 'DRAFT',
    owner_id    UUID         NOT NULL REFERENCES users(id),
    rating      DOUBLE PRECISION DEFAULT 0.0,
    initials    VARCHAR(4),
    plan        VARCHAR(10)  NOT NULL DEFAULT 'STARTER',
    cover_color VARCHAR(20),
    instagram   VARCHAR(100),
    telegram    VARCHAR(100),
    phone       VARCHAR(20),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_shops_handle ON shops(handle);
CREATE INDEX idx_shops_owner_id ON shops(owner_id);
CREATE INDEX idx_shops_status ON shops(status);

-- ---- Shop Configs ----
CREATE TABLE shop_configs (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id             UUID NOT NULL UNIQUE REFERENCES shops(id) ON DELETE CASCADE,
    theme               VARCHAR(20) NOT NULL DEFAULT 'MINIMAL',
    palette             VARCHAR(30),
    layout              VARCHAR(20) NOT NULL DEFAULT 'GRID',
    font                VARCHAR(50),
    custom_palette_json JSONB
);

-- ---- Products ----
CREATE TABLE products (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id     UUID          NOT NULL REFERENCES shops(id) ON DELETE CASCADE,
    cat_id      VARCHAR(50),
    name_en     VARCHAR(200),
    name_ru     VARCHAR(200),
    name_uz     VARCHAR(200),
    desc_en     TEXT,
    desc_ru     TEXT,
    desc_uz     TEXT,
    price       NUMERIC(12,2) NOT NULL,
    visible     BOOLEAN       NOT NULL DEFAULT true,
    sort_order  INTEGER       DEFAULT 0,
    tone        VARCHAR(30),
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_products_shop_id ON products(shop_id);

-- ---- Product Variants ----
CREATE TABLE product_variants (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id   UUID          NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    options_json JSONB,
    barcode      VARCHAR(50),
    qty          INTEGER       NOT NULL DEFAULT 0,
    avg_cost     NUMERIC(12,2),
    threshold    INTEGER       NOT NULL DEFAULT 5
);

CREATE INDEX idx_product_variants_product_id ON product_variants(product_id);

-- ---- Orders ----
CREATE TABLE orders (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id          UUID          NOT NULL REFERENCES shops(id),
    order_no         INTEGER       NOT NULL,
    status           VARCHAR(20)   NOT NULL DEFAULT 'NEW',
    customer_name    VARCHAR(120),
    customer_phone   VARCHAR(20),
    customer_address TEXT,
    delivery_method  VARCHAR(30),
    delivery_fee     NUMERIC(12,2) DEFAULT 0,
    pay_method       VARCHAR(30),
    note             TEXT,
    goods_total      NUMERIC(12,2) NOT NULL,
    total            NUMERIC(12,2) NOT NULL,
    cancel_reason    TEXT,
    cancelled_by     VARCHAR(30),
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_orders_shop_id ON orders(shop_id);

-- ---- Order Lines ----
CREATE TABLE order_lines (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    UUID          NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id  UUID          REFERENCES products(id),
    variant_id  UUID          REFERENCES product_variants(id),
    name        VARCHAR(200)  NOT NULL,
    label       VARCHAR(100),
    qty         INTEGER       NOT NULL,
    unit_price  NUMERIC(12,2) NOT NULL,
    dropped     BOOLEAN       NOT NULL DEFAULT false
);

CREATE INDEX idx_order_lines_order_id ON order_lines(order_id);

-- ---- Sales ----
CREATE TABLE sales (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id     UUID          NOT NULL REFERENCES shops(id),
    sale_no     INTEGER       NOT NULL,
    pay_method  VARCHAR(30),
    status      VARCHAR(20)   NOT NULL DEFAULT 'COMPLETED',
    order_id    UUID,
    total       NUMERIC(12,2) NOT NULL,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_sales_shop_id ON sales(shop_id);

-- ---- Sale Lines ----
CREATE TABLE sale_lines (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sale_id     UUID          NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
    variant_id  UUID          REFERENCES product_variants(id),
    product_id  UUID,
    name        VARCHAR(200)  NOT NULL,
    label       VARCHAR(100),
    qty         INTEGER       NOT NULL,
    unit_price  NUMERIC(12,2) NOT NULL,
    cost        NUMERIC(12,2),
    returned    INTEGER       NOT NULL DEFAULT 0
);

CREATE INDEX idx_sale_lines_sale_id ON sale_lines(sale_id);

-- ---- Reviews ----
CREATE TABLE reviews (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id     UUID        NOT NULL REFERENCES shops(id) ON DELETE CASCADE,
    product_id  UUID,
    rating      INTEGER     NOT NULL CHECK (rating >= 1 AND rating <= 5),
    name        VARCHAR(100),
    text        TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_reviews_shop_id ON reviews(shop_id);

-- ---- Stock Movements ----
CREATE TABLE stock_movements (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id     UUID          NOT NULL REFERENCES shops(id),
    variant_id  UUID          NOT NULL,
    product_id  UUID          NOT NULL,
    delta       INTEGER       NOT NULL,
    reason      VARCHAR(20)   NOT NULL,
    unit_cost   NUMERIC(12,2),
    note        TEXT,
    sale_id     UUID,
    order_id    UUID,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_stock_movements_shop_id ON stock_movements(shop_id);
CREATE INDEX idx_stock_movements_variant_id ON stock_movements(variant_id);
