CREATE TABLE flexibility_reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_id UUID NOT NULL,
    market_id UUID NOT NULL,
    positive_bid_id UUID,
    negative_bid_id UUID,
    positive_value DOUBLE,
    positive_capacity_price DOUBLE,
    positive_energy_price DOUBLE,
    negative_value DOUBLE,
    negative_capacity_price DOUBLE,
    negative_energy_price DOUBLE,
    timestamp TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);