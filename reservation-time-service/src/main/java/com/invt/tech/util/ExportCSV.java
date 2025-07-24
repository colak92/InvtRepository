package com.invt.tech.util;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.handler.CSVExportException;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

/**
 * Utility class for exporting flexibility reservation data to CSV format.
 * Provides methods to write CSV data directly to an HTTP response or save it as a file.
 *
 * <p>Handles conversion of power values from kilowatts (kW) to megawatts (MW) by dividing by 1000 during export.</p>
 */
public class ExportCSV {

    // Here, we are exporting data to CSV file and return CSV file as response

    /**
     * Exports a list of flexibility reservations as a CSV file and writes it to the HTTP response output stream.
     * Sets the content type to "text/csv" and configures the response for file download with a fixed filename "reservations.csv".
     *
     * @param reservations the list of flexibility reservation DTOs to export
     * @param isTotal      if true, exports aggregated data with fewer columns; otherwise exports full details
     * @param response     the HttpServletResponse to write the CSV data to
     * @throws CSVExportException if an I/O error occurs during writing to the response output stream
     */
    public static void exportToCSV(List<FlexibilityReservationDTO> reservations, boolean isTotal, HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=reservations.csv");

        try (OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream())) {
            writeReservationsToCSV(reservations, isTotal, writer);
        } catch (IOException e) {
            throw new CSVExportException("Failed to export CSV to response output stream", e);
        }
    }

    // Helper method to reduce boiler code, also converting positive and negative values KiloWatts to MegaWatts by this formula (Kw / 1000) = MW
    // If total parameter true we will have 5 header columns, else we will have all header columns

    /**
     * Helper method that writes reservation data to a CSV format using the provided Writer.
     * Converts positive and negative power values from kilowatts (kW) to megawatts (MW) by dividing by 1000.
     * Writes either aggregated or full detail columns based on the isTotal flag.
     *
     * @param reservations the list of flexibility reservation DTOs to write
     * @param isTotal      if true, writes aggregated data columns; otherwise writes full detail columns
     * @param outputWriter the Writer to write CSV data to
     * @throws CSVExportException if an I/O error occurs while writing CSV data
     */
    private static void writeReservationsToCSV(List<FlexibilityReservationDTO> reservations, boolean isTotal, Writer outputWriter) {
        try (CSVWriter writer = new CSVWriter(outputWriter)) {
            if (isTotal) {
                writer.writeNext(new String[]{"timestamp", "assetId", "marketId", "positiveValue", "negativeValue"});
                for (var r : reservations) {
                    writer.writeNext(new String[]{
                            r.getTimestamp() != null ? r.getTimestamp().toString() : "",
                            r.getAssetId() != null ? r.getAssetId().toString() : "",
                            r.getMarketId() != null ? r.getMarketId().toString() : "",
                            r.getPositiveValue() != null ? r.getPositiveValue().divide(BigDecimal.valueOf(1000)).toPlainString() : "",
                            r.getNegativeValue() != null ? r.getNegativeValue().divide(BigDecimal.valueOf(1000)).toPlainString() : ""
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
                            r.getAssetId() != null ? r.getAssetId().toString() : "",
                            r.getMarketId() != null ? r.getMarketId().toString() : "",
                            r.getPositiveBidId() != null ? r.getPositiveBidId().toString() : "",
                            r.getNegativeBidId() != null ? r.getNegativeBidId().toString() : "",
                            r.getPositiveValue() != null ? r.getPositiveValue().divide(BigDecimal.valueOf(1000)).toPlainString() : "",
                            r.getPositiveCapacityPrice() != null ? r.getPositiveCapacityPrice().toPlainString() : "",
                            r.getPositiveEnergyPrice() != null ? r.getPositiveEnergyPrice().toPlainString() : "",
                            r.getNegativeValue() != null ? r.getNegativeValue().divide(BigDecimal.valueOf(1000)).toPlainString() : "",
                            r.getNegativeCapacityPrice() != null ? r.getNegativeCapacityPrice().toPlainString() : "",
                            r.getNegativeEnergyPrice() != null ? r.getNegativeEnergyPrice().toPlainString() : "",
                            r.getTimestamp() != null ? r.getTimestamp().toString() : "",
                            r.getUpdatedAt() != null ? r.getUpdatedAt().toString() : ""
                    });
                }
            }
        } catch (IOException e) {
            throw new CSVExportException("Error while writing CSV data", e);
        }
    }
}
