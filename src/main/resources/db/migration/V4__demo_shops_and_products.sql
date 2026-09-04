-- Add image columns
ALTER TABLE shops ADD COLUMN logo_url VARCHAR(500);
ALTER TABLE shops ADD COLUMN cover_url VARCHAR(500);
ALTER TABLE products ADD COLUMN image_url VARCHAR(500);

-- Demo user (owner for all demo shops)
INSERT INTO users (id, phone, name, role) VALUES
('00000000-0000-0000-0000-000000000001', '+998900000001', 'Demo Seller', 'SELLER');

-- ============================================================
-- 1. Lola Atelier (fashion) — Tashkent
-- ============================================================
INSERT INTO shops (id, handle, name, location, type, status, owner_id, rating, initials, plan, cover_color, logo_url, cover_url, instagram, telegram)
VALUES (
  '10000000-0000-0000-0000-000000000001',
  'lola-atelier', 'Lola Atelier', 'Toshkent', 'fashion', 'LIVE',
  '00000000-0000-0000-0000-000000000001', 4.8, 'LA', 'PRO', '#c7b09a',
  'https://images.unsplash.com/photo-1441984904996-e0b6ba687e04?w=120&h=120&fit=crop',
  'https://images.unsplash.com/photo-1441984904996-e0b6ba687e04?w=1200&h=400&fit=crop',
  '@lolaatelier', '@lolaatelier'
);

INSERT INTO shop_configs (shop_id, theme, palette, layout, font) VALUES
('10000000-0000-0000-0000-000000000001', 'BOUTIQUE', 'ivory', 'GRID', 'instrument');

INSERT INTO products (id, shop_id, cat_id, name_en, name_uz, name_ru, desc_en, price, visible, sort_order, image_url) VALUES
('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'clothing', 'Silk Wrap Dress', 'Ipak ko''ylak', 'Шёлковое платье', 'Hand-stitched silk wrap dress with subtle gold threading', 590000, true, 1, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'clothing', 'Linen Blazer', 'Zig''ir blezer', 'Льняной блейзер', 'Relaxed-fit linen blazer in sand', 420000, true, 2, 'https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'accessories', 'Handwoven Scarf', 'Qo''lda to''qilgan sharf', 'Шарф ручной работы', 'Cashmere-blend handwoven scarf', 180000, true, 3, 'https://images.unsplash.com/photo-1601924921557-45e8e0964fc4?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000001', 'accessories', 'Leather Tote Bag', 'Charm sumka', 'Кожаная сумка-тоут', 'Full-grain leather tote, handmade in Tashkent', 650000, true, 4, 'https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000001', 'clothing', 'Cotton Palazzo Pants', 'Paxta shim', 'Хлопковые брюки палаццо', 'Wide-leg cotton palazzo in cream', 280000, true, 5, 'https://images.unsplash.com/photo-1506629082955-511b1aa562c8?w=600&h=600&fit=crop');

INSERT INTO product_variants (product_id, options_json, qty) VALUES
('20000000-0000-0000-0000-000000000001', '"S"', 8),
('20000000-0000-0000-0000-000000000001', '"M"', 12),
('20000000-0000-0000-0000-000000000001', '"L"', 6),
('20000000-0000-0000-0000-000000000002', '"S"', 5),
('20000000-0000-0000-0000-000000000002', '"M"', 10),
('20000000-0000-0000-0000-000000000002', '"L"', 7),
('20000000-0000-0000-0000-000000000003', '"One Size"', 20),
('20000000-0000-0000-0000-000000000004', '"One Size"', 15),
('20000000-0000-0000-0000-000000000005', '"S"', 6),
('20000000-0000-0000-0000-000000000005', '"M"', 10),
('20000000-0000-0000-0000-000000000005', '"L"', 8);

-- ============================================================
-- 2. Non & Co (food) — Samarqand
-- ============================================================
INSERT INTO shops (id, handle, name, location, type, status, owner_id, rating, initials, plan, cover_color, logo_url, cover_url, instagram, telegram)
VALUES (
  '10000000-0000-0000-0000-000000000002',
  'non-and-co', 'Non & Co', 'Samarqand', 'food', 'LIVE',
  '00000000-0000-0000-0000-000000000001', 4.9, 'N&', 'STARTER', '#d8bf94',
  'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=120&h=120&fit=crop',
  'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=1200&h=400&fit=crop',
  '@nonandco', '@nonandco'
);

INSERT INTO shop_configs (shop_id, theme, palette, layout, font) VALUES
('10000000-0000-0000-0000-000000000002', 'MINIMAL', 'sand', 'GRID', 'outfit');

INSERT INTO products (id, shop_id, cat_id, name_en, name_uz, name_ru, desc_en, price, visible, sort_order, image_url) VALUES
('20000000-0000-0000-0000-000000000010', '10000000-0000-0000-0000-000000000002', 'food', 'Samarkand Non', 'Samarqand noni', 'Самаркандская лепёшка', 'Traditional tandoor-baked round bread', 18000, true, 1, 'https://images.unsplash.com/photo-1549931319-a545753467c8?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000011', '10000000-0000-0000-0000-000000000002', 'food', 'Patir Non', 'Patir non', 'Патыр нон', 'Buttery layered flatbread', 25000, true, 2, 'https://images.unsplash.com/photo-1586444248902-2f64eddc13df?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000012', '10000000-0000-0000-0000-000000000002', 'food', 'Somsa (Beef)', 'Somsa (mol go''shti)', 'Самса (говядина)', 'Crispy pastry stuffed with spiced beef and onion', 12000, true, 3, 'https://images.unsplash.com/photo-1630409351241-e90e7f5e434d?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000013', '10000000-0000-0000-0000-000000000002', 'food', 'Honey Baklava', 'Asal baqlava', 'Медовая пахлава', 'Layered pastry with walnuts and wild honey', 45000, true, 4, 'https://images.unsplash.com/photo-1519676867240-f03562e64548?w=600&h=600&fit=crop');

INSERT INTO product_variants (product_id, options_json, qty) VALUES
('20000000-0000-0000-0000-000000000010', '"1 pc"', 50),
('20000000-0000-0000-0000-000000000010', '"5 pcs"', 20),
('20000000-0000-0000-0000-000000000011', '"1 pc"', 40),
('20000000-0000-0000-0000-000000000012', '"3 pcs"', 30),
('20000000-0000-0000-0000-000000000012', '"6 pcs"', 15),
('20000000-0000-0000-0000-000000000013', '"500g"', 25),
('20000000-0000-0000-0000-000000000013', '"1kg"', 10);

-- ============================================================
-- 3. Gulnoza Beauty (beauty) — Tashkent
-- ============================================================
INSERT INTO shops (id, handle, name, location, type, status, owner_id, rating, initials, plan, cover_color, logo_url, cover_url, instagram, telegram)
VALUES (
  '10000000-0000-0000-0000-000000000003',
  'gulnoza-beauty', 'Gulnoza Beauty', 'Toshkent', 'beauty', 'LIVE',
  '00000000-0000-0000-0000-000000000001', 4.7, 'GB', 'PRO', '#f3c9d6',
  'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=120&h=120&fit=crop',
  'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=1200&h=400&fit=crop',
  '@gulnozabeauty', '@gulnozabeauty'
);

INSERT INTO shop_configs (shop_id, theme, palette, layout, font) VALUES
('10000000-0000-0000-0000-000000000003', 'PLAYFUL', 'blush', 'GALLERY', 'newsreader');

INSERT INTO products (id, shop_id, cat_id, name_en, name_uz, name_ru, desc_en, price, visible, sort_order, image_url) VALUES
('20000000-0000-0000-0000-000000000020', '10000000-0000-0000-0000-000000000003', 'cosmetics', 'Rose Face Serum', 'Atirgul yuz serumi', 'Сыворотка для лица с розой', 'Hydrating face serum with Damask rose extract', 145000, true, 1, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000021', '10000000-0000-0000-0000-000000000003', 'cosmetics', 'Matte Lip Kit', 'Mat lab to''plami', 'Матовый набор для губ', 'Long-wear matte lipstick in 6 shades', 89000, true, 2, 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000022', '10000000-0000-0000-0000-000000000003', 'cosmetics', 'Natural Clay Mask', 'Tabiiy loy niqobi', 'Натуральная глиняная маска', 'Purifying clay mask with green tea', 72000, true, 3, 'https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000023', '10000000-0000-0000-0000-000000000003', 'cosmetics', 'Eye Palette — Sunset', 'Ko''z palitrasi — Quyosh', 'Палитра теней — Закат', '12-shade warm-tone eye palette', 195000, true, 4, 'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000024', '10000000-0000-0000-0000-000000000003', 'cosmetics', 'Argan Hair Oil', 'Argan soch moyi', 'Аргановое масло для волос', 'Cold-pressed argan oil for shine and repair', 120000, true, 5, 'https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=600&h=600&fit=crop');

INSERT INTO product_variants (product_id, options_json, qty) VALUES
('20000000-0000-0000-0000-000000000020', '"30ml"', 30),
('20000000-0000-0000-0000-000000000020', '"50ml"', 15),
('20000000-0000-0000-0000-000000000021', '"Nude"', 20),
('20000000-0000-0000-0000-000000000021', '"Berry"', 18),
('20000000-0000-0000-0000-000000000021', '"Red"', 25),
('20000000-0000-0000-0000-000000000022', '"100g"', 35),
('20000000-0000-0000-0000-000000000023', '"One Size"', 22),
('20000000-0000-0000-0000-000000000024', '"100ml"', 28);

-- ============================================================
-- 4. Silk Road Goods (home) — Buxoro
-- ============================================================
INSERT INTO shops (id, handle, name, location, type, status, owner_id, rating, initials, plan, cover_color, logo_url, cover_url, instagram, telegram)
VALUES (
  '10000000-0000-0000-0000-000000000004',
  'silk-road-goods', 'Silk Road Goods', 'Buxoro', 'home', 'LIVE',
  '00000000-0000-0000-0000-000000000001', 4.6, 'SR', 'PRO', '#bcae93',
  'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=120&h=120&fit=crop',
  'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=1200&h=400&fit=crop',
  '@silkroadgoods', '@silkroadgoods'
);

INSERT INTO shop_configs (shop_id, theme, palette, layout, font) VALUES
('10000000-0000-0000-0000-000000000004', 'MINIMAL', 'olive', 'MAGAZINE', 'hanken');

INSERT INTO products (id, shop_id, cat_id, name_en, name_uz, name_ru, desc_en, price, visible, sort_order, image_url) VALUES
('20000000-0000-0000-0000-000000000030', '10000000-0000-0000-0000-000000000004', 'furniture', 'Suzani Cushion Cover', 'Suzana yostiq qopi', 'Чехол с вышивкой сюзане', 'Hand-embroidered suzani cushion, 45x45cm', 180000, true, 1, 'https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000031', '10000000-0000-0000-0000-000000000004', 'furniture', 'Ceramic Tea Set', 'Sopol choy to''plami', 'Керамический чайный набор', 'Handmade Rishtan ceramic tea set for 6', 320000, true, 2, 'https://images.unsplash.com/photo-1530018607912-eff2daa1bac4?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000032', '10000000-0000-0000-0000-000000000004', 'furniture', 'Ikat Table Runner', 'Ikat dasturxon yo''lagi', 'Дорожка на стол икат', 'Silk ikat table runner, 200cm', 240000, true, 3, 'https://images.unsplash.com/photo-1616046229478-9901c5536a45?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000033', '10000000-0000-0000-0000-000000000004', 'furniture', 'Brass Lantern', 'Mis fonus', 'Латунный фонарь', 'Hammered brass lantern with geometric cutouts', 450000, true, 4, 'https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?w=600&h=600&fit=crop');

INSERT INTO product_variants (product_id, options_json, qty) VALUES
('20000000-0000-0000-0000-000000000030', '"Blue"', 12),
('20000000-0000-0000-0000-000000000030', '"Red"', 8),
('20000000-0000-0000-0000-000000000030', '"Green"', 10),
('20000000-0000-0000-0000-000000000031', '"One Set"', 7),
('20000000-0000-0000-0000-000000000032', '"One Size"', 15),
('20000000-0000-0000-0000-000000000033', '"Small"', 6),
('20000000-0000-0000-0000-000000000033', '"Large"', 4);

-- ============================================================
-- 5. Tech Zone (electronics) — Tashkent
-- ============================================================
INSERT INTO shops (id, handle, name, location, type, status, owner_id, rating, initials, plan, cover_color, logo_url, cover_url, instagram, telegram)
VALUES (
  '10000000-0000-0000-0000-000000000005',
  'tech-zone', 'Tech Zone', 'Toshkent', 'electronics', 'LIVE',
  '00000000-0000-0000-0000-000000000001', 4.5, 'TZ', 'STARTER', '#2d3436',
  'https://images.unsplash.com/photo-1518770660439-4636190af475?w=120&h=120&fit=crop',
  'https://images.unsplash.com/photo-1518770660439-4636190af475?w=1200&h=400&fit=crop',
  '@techzoneuz', '@techzoneuz'
);

INSERT INTO shop_configs (shop_id, theme, palette, layout, font) VALUES
('10000000-0000-0000-0000-000000000005', 'BOLD', 'midnight', 'GRID', 'space');

INSERT INTO products (id, shop_id, cat_id, name_en, name_uz, name_ru, desc_en, price, visible, sort_order, image_url) VALUES
('20000000-0000-0000-0000-000000000040', '10000000-0000-0000-0000-000000000005', 'electronics', 'Wireless Earbuds Pro', 'Simsiz quloqchinlar Pro', 'Беспроводные наушники Pro', 'Active noise cancellation, 30hr battery', 890000, true, 1, 'https://images.unsplash.com/photo-1590658268037-6bf12f032f55?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000041', '10000000-0000-0000-0000-000000000005', 'electronics', 'Portable Charger 20000mAh', 'Ko''chma quvvatlagich', 'Портативное зарядное устройство', 'Fast charge power bank, USB-C + USB-A', 250000, true, 2, 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000042', '10000000-0000-0000-0000-000000000005', 'electronics', 'Smart Watch Band', 'Aqlli soat tasmasi', 'Ремешок для смарт-часов', 'Silicone sport band, fits most models', 65000, true, 3, 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000043', '10000000-0000-0000-0000-000000000005', 'electronics', 'LED Desk Lamp', 'LED stol chiroqi', 'Настольная LED-лампа', 'Adjustable brightness, USB powered', 180000, true, 4, 'https://images.unsplash.com/photo-1507473885765-e6ed057ab6fe?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000044', '10000000-0000-0000-0000-000000000005', 'electronics', 'Bluetooth Speaker Mini', 'Bluetooth karnay mini', 'Мини Bluetooth-колонка', 'Waterproof portable speaker, 12hr playback', 320000, true, 5, 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600&h=600&fit=crop');

INSERT INTO product_variants (product_id, options_json, qty) VALUES
('20000000-0000-0000-0000-000000000040', '"Black"', 20),
('20000000-0000-0000-0000-000000000040', '"White"', 15),
('20000000-0000-0000-0000-000000000041', '"One Size"', 30),
('20000000-0000-0000-0000-000000000042', '"Black"', 40),
('20000000-0000-0000-0000-000000000042', '"Navy"', 25),
('20000000-0000-0000-0000-000000000042', '"Green"', 20),
('20000000-0000-0000-0000-000000000043', '"White"', 18),
('20000000-0000-0000-0000-000000000043', '"Black"', 12),
('20000000-0000-0000-0000-000000000044', '"Black"', 22),
('20000000-0000-0000-0000-000000000044', '"Blue"', 10);

-- ============================================================
-- 6. Bahor Flowers (gifts) — Namangan
-- ============================================================
INSERT INTO shops (id, handle, name, location, type, status, owner_id, rating, initials, plan, cover_color, logo_url, cover_url, instagram, telegram)
VALUES (
  '10000000-0000-0000-0000-000000000006',
  'bahor-flowers', 'Bahor Flowers', 'Namangan', 'gifts', 'LIVE',
  '00000000-0000-0000-0000-000000000001', 4.9, 'BF', 'STARTER', '#e8c2dd',
  'https://images.unsplash.com/photo-1487530811176-3780de880c2d?w=120&h=120&fit=crop',
  'https://images.unsplash.com/photo-1487530811176-3780de880c2d?w=1200&h=400&fit=crop',
  '@bahorflowers', '@bahorflowers'
);

INSERT INTO shop_configs (shop_id, theme, palette, layout, font) VALUES
('10000000-0000-0000-0000-000000000006', 'PLAYFUL', 'blush', 'GALLERY', 'newsreader');

INSERT INTO products (id, shop_id, cat_id, name_en, name_uz, name_ru, desc_en, price, visible, sort_order, image_url) VALUES
('20000000-0000-0000-0000-000000000050', '10000000-0000-0000-0000-000000000006', 'other', 'Rose Bouquet Classic', 'Atirgul guldastasi klassik', 'Букет роз Классика', '25 premium red roses with eucalyptus', 250000, true, 1, 'https://images.unsplash.com/photo-1494972688394-4cc796f9e4c5?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000051', '10000000-0000-0000-0000-000000000006', 'other', 'Spring Mix Bouquet', 'Bahorgi aralash guldasta', 'Весенний микс букет', 'Seasonal wildflowers in pastel tones', 180000, true, 2, 'https://images.unsplash.com/photo-1490750967868-88aa4f44baee?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000052', '10000000-0000-0000-0000-000000000006', 'other', 'Gift Box — Sweet Moments', 'Sovg''a qutisi — Shirin lahzalar', 'Подарочный набор — Сладкие моменты', 'Chocolates, dried fruits and a mini bouquet', 350000, true, 3, 'https://images.unsplash.com/photo-1549465220-1a8b9238f760?w=600&h=600&fit=crop'),
('20000000-0000-0000-0000-000000000053', '10000000-0000-0000-0000-000000000006', 'other', 'Indoor Plant — Monstera', 'Uy o''simligi — Monstera', 'Комнатное растение — Монстера', 'Healthy monstera in ceramic pot', 290000, true, 4, 'https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=600&h=600&fit=crop');

INSERT INTO product_variants (product_id, options_json, qty) VALUES
('20000000-0000-0000-0000-000000000050', '"Standard"', 15),
('20000000-0000-0000-0000-000000000050', '"Premium"', 8),
('20000000-0000-0000-0000-000000000051', '"Small"', 20),
('20000000-0000-0000-0000-000000000051', '"Large"', 10),
('20000000-0000-0000-0000-000000000052', '"One Size"', 12),
('20000000-0000-0000-0000-000000000053', '"Small"', 6),
('20000000-0000-0000-0000-000000000053', '"Medium"', 4);
