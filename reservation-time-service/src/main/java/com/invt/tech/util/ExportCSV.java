package com.invt.tech.util;

import com.invt.tech.dto.FlexibilityReservationDTO;
import com.invt.tech.handler.CSVExportException;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    // Here, we are saving CSV file in folder

    /**
     * Saves a list of flexibility reservations as a CSV file at the specified file path.
     *
     * @param reservations the list of flexibility reservation DTOs to save
     * @param isTotal      if true, saves aggregated data with fewer columns; otherwise saves full details
     * @param filePath     the file system path where the CSV file will be saved
     * @throws CSVExportException if an I/O error occurs during file writing
     */
    public static void saveCSVFile(List<FlexibilityReservationDTO> reservations, boolean isTotal, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            writeReservationsToCSV(reservations, isTotal, fileWriter);
        } catch (IOException e) {
            throw new CSVExportException("Failed to save CSV file at " + filePath, e);
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
        } catch (IOException e) {
            throw new CSVExportException("Error while writing CSV data", e);
        }
    }
}
