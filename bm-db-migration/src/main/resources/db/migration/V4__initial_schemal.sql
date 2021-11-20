ALTER TABLE `product_detail`
    CHANGE COLUMN `image_byte` `image_byte` LONGBLOB NULL DEFAULT NULL ;
ALTER TABLE `product`
    CHANGE COLUMN `image_byte` `image_byte` LONGBLOB NULL DEFAULT NULL ;
