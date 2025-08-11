CREATE TABLE IF NOT EXISTS organisations_schema.orders
(
    id                UUID                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    merchant_id       UUID                     NOT NULL,
    buyer_id          UUID                     NOT NULL,
    merchant_order_id VARCHAR                  NOT NULL,
    total_amount      DECIMAL(11, 2)           NOT NULL,
    order_date        TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (merchant_id, merchant_order_id),
    FOREIGN KEY (merchant_id) REFERENCES organisations_schema.organisations(id),
    FOREIGN KEY (buyer_id) REFERENCES organisations_schema.organisations(id)
);