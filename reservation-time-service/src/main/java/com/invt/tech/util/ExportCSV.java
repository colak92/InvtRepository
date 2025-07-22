package com.invt.tech.util;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class ExportCSV {

    // Here, we are exporting data to CSV file and return CSV file as response

    public static void exportToCSV(List<FlexibilityReservationDTO> reservations, boolean isTotal, HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=reservations.csv");

        writeReservationsToCSV(reservations, isTotal, new OutputStreamWriter(response.getOutputStream()));
    }

    // Here, we are saving CSV file in folder

    public static void saveCSVFile(List<FlexibilityReservationDTO> reservations, boolean isTotal, String filePath) throws IOException {
        writeReservationsToCSV(reservations, isTotal, new FileWriter(filePath));
    }

    // Helper method to reduce boiler code, also converting positive and negative values KiloWatts to MegaWatts by this formula (Kw / 1000) = MW
    // If total parameter true we will have 5 header columns, else we will have all header columns

    private static void writeReservationsToCSV(List<FlexibilityReservationDTO> reservations, boolean isTotal, Writer outputWriter) throws IOException {
        try (CSVWriter writer = new CSVWriter(outputWriter)) {
            if (isTotal) {
                writer.writeNext(new String[]{"timestamp", "assetId", "marketId", "positiveValue", "negativeValue"});
                for (var r : reservations) {
                    writer.writeNext(new String[]{
                            r.getTimestamp().toString(),
                            r.getAssetId().toString(),
                            r.getMarketId().toString(),
                            String.valueOf(r.getPositiveValue() / 1000),
                            String.valueOf(r.getNegativeValue() / 1000)
                    });
                }
            } else {
                writer.writeNext(new String[]{
                        "assetId", "marketId",
                        "positiveBidId", "negativeBidId",
                        "positiveValue", "positiveCapacityPrice", "positiveEnergyPrice",
                        "negativeValue", "negativeCapacityPrice", "negativeEnergyPrice",
                        "timestamp", "updatedAt"
                });
                for (var r : reservations) {
                    writer.writeNext(new String[]{
                            r.getAssetId().toString(),
                            r.getMarketId().toString(),
                            r.getPositiveBidId() != null ? r.getPositiveBidId().toString() : "",
                            r.getNegativeBidId() != null ? r.getNegativeBidId().toString() : "",
                            String.valueOf(r.getPositiveValue() / 1000),
                            String.valueOf(r.getPositiveCapacityPrice()),
                            String.valueOf(r.getPositiveEnergyPrice()),
                            String.valueOf(r.getNegativeValue() / 1000),
                            String.valueOf(r.getNegativeCapacityPrice()),
                            String.valueOf(r.getNegativeEnergyPrice()),
                            r.getTimestamp().toString(),
                            r.getUpdatedAt().toString()
                    });
                }
            }
        }
    }
}
