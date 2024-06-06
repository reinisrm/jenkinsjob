package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.services.LendingService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryReportServiceImpl {

    private final Logger log = LoggerFactory.getLogger(InventoryReportServiceImpl.class);

    private final LendingService lendingService;

    @Autowired
    public InventoryReportServiceImpl(LendingService lendingService) {
        this.lendingService = lendingService;
    }

    public byte[] generateInventoryReport() throws IOException {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.getMonthValue() >= 9 ? LocalDate.of(now.getYear(), 9, 1) : LocalDate.of(now.getYear() - 1, 9, 1);
        LocalDate endDate = now.getMonthValue() >= 9 ? LocalDate.of(now.getYear() + 1, 8, 31) : LocalDate.of(now.getYear(), 8, 31);

        List<Lending> lendings = lendingService.findLendingsByDateRange(startDate, endDate);

        log.info("Generating inventory report for lendings between {} and {}", startDate, endDate);
        log.info("Number of lendings found: {}", lendings.size());

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Inventory Report");

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Uzsk.Nom.Kods", "Uzsk.Nom.Nosaukums", "Uzsk.Inv.Numurs", "Uzsk.Part.Numurs",
                    "Uzsk.Nom.Mērv.Kod", "Uzsk.Daudzums", "Uzsk.Summa,EUR", "Lietošanas stāvoklis",
                    "Uzsk.InvNolietVērtības samazinājums, EUR", "Uzsk.AP.Kods", "Uzsk.AP.Nosaukums",
                    "Uzsk.Inv.AtrVPer.Spēkā no", "Uzsk.Inv.AtrVPer.InvAtrV.Kods",
                    "Uzsk.Inv.AtrVPer.InvAtrV.Nosaukums", "Uzsk.Part.Proj.Kods"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Lending lending : lendings) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue(lending.getInventory().getDevice());
                row.createCell(2).setCellValue(lending.getInventory().getInventoryNumber());
                row.createCell(3).setCellValue("");
                row.createCell(4).setCellValue("gab.");
                row.createCell(5).setCellValue("1.");
                row.createCell(6).setCellValue("0.00");
                row.createCell(7).setCellValue("Izsniegts");
                row.createCell(8).setCellValue("");
                row.createCell(9).setCellValue(getLenderInitials(lending.getLender()));
                row.createCell(10).setCellValue(lending.getLender().getName() + " " + lending.getLender().getSurname());
                row.createCell(11).setCellValue("");
                row.createCell(12).setCellValue(lending.getInventory().getRoom());
                row.createCell(13).setCellValue(getRoomDescription(lending.getInventory().getRoom()));
                row.createCell(14).setCellValue("");
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                log.info("Inventory report generated successfully.");
                return outputStream.toByteArray();
            }
        }
    }

    private String getLenderInitials(Person lender) {
        if (lender == null) return "";
        String initials = lender.getName().substring(0, 1) + lender.getSurname().substring(0, 1) + "_INV";
        return initials.toUpperCase();
    }

    private String getRoomDescription(String room) {
        if (room == null) return "";
        switch (room.charAt(0)) {
            case 'A': return "A korpuss " + room;
            case 'B': return "B korpuss, " + room;
            case 'C': return "C korpuss, " + room;
            case 'D': return "D korpuss, " + room;
            case 'E': return "E korpuss, " + room;
            default: return room;
        }
    }
}
