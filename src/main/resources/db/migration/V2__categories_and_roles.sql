-- Add role to users
ALTER TABLE users ADD COLUMN role VARCHAR(10) NOT NULL DEFAULT 'SELLER';

-- Categories table (for shop types and product categories)
CREATE TABLE categories (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    kind       VARCHAR(20)  NOT NULL,
    slug       VARCHAR(50)  NOT NULL,
    name_en    VARCHAR(100) NOT NULL,
    name_uz    VARCHAR(100),
    name_ru    VARCHAR(100),
    icon       VARCHAR(10),
    sort_order INTEGER      NOT NULL DEFAULT 0,
    active     BOOLEAN      NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    UNIQUE (kind, slug)
);

CREATE INDEX idx_categories_kind ON categories(kind);

-- Seed shop types
INSERT INTO categories (kind, slug, name_en, name_uz, name_ru, icon, sort_order) VALUES
('SHOP_TYPE', 'fashion',     'Fashion',     'Moda',         'Мода',          '👗', 1),
('SHOP_TYPE', 'food',        'Food',        'Oziq-ovqat',   'Еда',           '🍜', 2),
('SHOP_TYPE', 'beauty',      'Beauty',      'Go''zallik',   'Красота',       '💄', 3),
('SHOP_TYPE', 'home',        'Home',        'Uy-joy',       'Дом',           '🏠', 4),
('SHOP_TYPE', 'gifts',       'Gifts',       'Sovg''alar',   'Подарки',       '🎁', 5),
('SHOP_TYPE', 'electronics', 'Electronics', 'Elektronika',  'Электроника',   '📱', 6),
('SHOP_TYPE', 'sports',      'Sports',      'Sport',        'Спорт',         '⚽', 7),
('SHOP_TYPE', 'books',       'Books',       'Kitoblar',     'Книги',         '📚', 8),
('SHOP_TYPE', 'toys',        'Toys',        'O''yinchoqlar','Игрушки',       '🧸', 9),
('SHOP_TYPE', 'other',       'Other',       'Boshqa',       'Другое',        '📦', 10);

-- Seed product categories
INSERT INTO categories (kind, slug, name_en, name_uz, name_ru, icon, sort_order) VALUES
('PRODUCT_CATEGORY', 'clothing',    'Clothing',    'Kiyim-kechak',  'Одежда',        '👕', 1),
('PRODUCT_CATEGORY', 'shoes',       'Shoes',       'Poyabzal',      'Обувь',         '👟', 2),
('PRODUCT_CATEGORY', 'accessories', 'Accessories', 'Aksessuarlar',  'Аксессуары',    '👜', 3),
('PRODUCT_CATEGORY', 'food',        'Food',        'Oziq-ovqat',    'Еда',           '🍜', 4),
('PRODUCT_CATEGORY', 'drinks',      'Drinks',      'Ichimliklar',   'Напитки',       '🥤', 5),
('PRODUCT_CATEGORY', 'cosmetics',   'Cosmetics',   'Kosmetika',     'Косметика',     '💄', 6),
('PRODUCT_CATEGORY', 'furniture',   'Furniture',   'Mebel',         'Мебель',        '🪑', 7),
('PRODUCT_CATEGORY', 'electronics', 'Electronics', 'Elektronika',   'Электроника',   '📱', 8),
('PRODUCT_CATEGORY', 'other',       'Other',       'Boshqa',        'Другое',        '📦', 9);

-- Normalize existing shop type values to lowercase slugs
UPDATE shops SET type = LOWER(type);
