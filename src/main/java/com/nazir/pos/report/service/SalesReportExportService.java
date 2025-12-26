package com.nazir.pos.report.service;

import com.nazir.pos.report.dto.SalesReportRequest;
import com.nazir.pos.report.dto.SalesSummaryResponse;
import com.nazir.pos.report.repository.InvoiceReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesReportExportService {

    private final InvoiceReportRepository repository;

    public byte[] exportExcel(SalesReportRequest request, Long storeId) {
        List<SalesSummaryResponse> data = repository.salesSummary(request, storeId).getContent();
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sales Report");
            /* ================= HEADER STYLE ================= */
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            /* ================= DATA STYLES ================= */
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("₹#,##0.00"));
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-MM-yyyy HH:mm"));
            /* ================= HEADER ROW ================= */
            String[] headers = {"Invoice No", "POS", "Cashier", "Payment Mode", "Sub Total", "GST", "Total", "Date"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            /* ================= DATA ROWS ================= */
            int rowIdx = 1;
            double totalSub = 0, totalGst = 0, grandTotal = 0;

            for (SalesSummaryResponse r : data) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(nvl(r.getInvoiceNo()));
                row.createCell(1).setCellValue(nvl(r.getPosName()));
                row.createCell(2).setCellValue(nvl(r.getCashierUsername()));
                row.createCell(3).setCellValue(
                        r.getPaymentMode() != null ? r.getPaymentMode().name() : ""
                );

                Cell sub = row.createCell(4);
                sub.setCellValue(nvl(r.getSubTotal()));
                sub.setCellStyle(currencyStyle);

                Cell gst = row.createCell(5);
                gst.setCellValue(nvl(r.getGstAmount()));
                gst.setCellStyle(currencyStyle);

                Cell total = row.createCell(6);
                total.setCellValue(nvl(r.getTotalAmount()));
                total.setCellStyle(currencyStyle);

                Cell date = row.createCell(7);
                if (r.getCreatedAt() != null) {
                    date.setCellValue(r.getCreatedAt());
                    date.setCellStyle(dateStyle);
                }

                totalSub += nvl(r.getSubTotal());
                totalGst += nvl(r.getGstAmount());
                grandTotal += nvl(r.getTotalAmount());
            }
            /* ================= TOTALS ROW ================= */
            Row totalRow = sheet.createRow(rowIdx);

            CellStyle totalStyle = workbook.createCellStyle();
            Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalStyle.setFont(totalFont);
            totalStyle.setDataFormat(workbook.createDataFormat().getFormat("₹#,##0.00"));

            totalRow.createCell(3).setCellValue("TOTAL");
            totalRow.getCell(3).setCellStyle(totalStyle);

            Cell subTotalCell = totalRow.createCell(4);
            subTotalCell.setCellValue(totalSub);
            subTotalCell.setCellStyle(totalStyle);

            Cell gstTotalCell = totalRow.createCell(5);
            gstTotalCell.setCellValue(totalGst);
            gstTotalCell.setCellStyle(totalStyle);

            Cell grandTotalCell = totalRow.createCell(6);
            grandTotalCell.setCellValue(grandTotal);
            grandTotalCell.setCellStyle(totalStyle);
            /* ================= AUTO SIZE ================= */
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel report", e);
        }
    }

    public byte[] exportCsv(SalesReportRequest request, Long storeId) {

        List<SalesSummaryResponse> data = repository.salesSummary(request, storeId).getContent();
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))
        ) {
            // ===== HEADER =====
            writer.println(String.join(",",
                    "Invoice No",
                    "POS",
                    "Cashier",
                    "Payment Mode",
                    "Sub Total",
                    "GST",
                    "Total Amount",
                    "Date"
            ));
            // ===== DATA =====
            for (SalesSummaryResponse r : data) {
                writer.println(String.join(",",
                        csv(r.getInvoiceNo()),
                        csv(r.getPosName()),
                        csv(r.getCashierUsername()),
                        csv(r.getPaymentMode() != null ? r.getPaymentMode().name() : ""),
                        csv(r.getSubTotal()),
                        csv(r.getGstAmount()),
                        csv(r.getTotalAmount()),
                        csv(r.getCreatedAt())
                ));
            }
            writer.flush();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to export sales report to CSV", e);
        }
    }

    /* =======================
       CSV SAFE VALUE
    ======================= */
    private String csv(Object value) {
        if (value == null) return "";
        String s = value.toString();
        // Escape quotes & commas
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

    private double nvl(Double value) {
        return value == null ? 0.0 : value;
    }
}
