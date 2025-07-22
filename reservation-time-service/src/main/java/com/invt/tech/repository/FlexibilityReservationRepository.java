package com.invt.tech.repository;

import com.invt.tech.entity.FlexibilityReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlexibilityReservationRepository extends JpaRepository<FlexibilityReservation, Long> {

    // We need to get list of Flexibility Reservation using method parameters assetId and marketId

    List<FlexibilityReservation> findByAssetIdAndMarketId(UUID assetId, UUID marketId);
}
