CREATE TABLE IF NOT EXISTS flexibility_reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_id UUID NOT NULL,
    market_id UUID NOT NULL,
    positive_bid_id UUID,
    negative_bid_id UUID,
    positive_value DECIMAL(15,3) NOT NULL,
    positive_capacity_price DECIMAL(10,6),
    positive_energy_price DECIMAL(10,6),
    negative_value DECIMAL(15,3) NOT NULL,
    negative_capacity_price DECIMAL(10,6),
    negative_energy_price DECIMAL(10,6),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);