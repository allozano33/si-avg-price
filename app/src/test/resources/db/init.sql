CREATE TABLE average_price (
                               id BIGINT(20) NOT NULL AUTO_INCREMENT,
                               sku VARCHAR(20) NOT NULL,
                               cnpj VARCHAR(14)  NOT NULL,
                               cost  DECIMAL(10,2) NOT NULL,
                               stock bigint(20)  NOT NULL,
                               created_at  datetime  NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at  datetime
) ENGINE=InnoDB;