CREATE TABLE IF NOT EXISTS organisations_schema.shipments
(
    id                      UUID                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_id                UUID                     NOT NULL,
    merchant_shipment_id    VARCHAR                  NOT NULL,
    courier                 VARCHAR                  NOT NULL,
    tracking_id             VARCHAR                  NOT NULL,
    shipped_amount          DECIMAL(11, 2)           NOT NULL,
    shipped_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (order_id, merchant_shipment_id),
    FOREIGN KEY (order_id) REFERENCES organisations_schema.orders(id)
);

CREATE INDEX idx_shipments_order_id ON organisations_schema.shipments(order_id);