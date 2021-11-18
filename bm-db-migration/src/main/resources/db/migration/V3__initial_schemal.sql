ALTER TABLE `product`
    ADD COLUMN `image_byte` BLOB NULL AFTER `sub_category_id`;

ALTER TABLE `product_detail`
    ADD COLUMN `image_byte` BLOB NULL AFTER `product_id`;
