package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.Lending;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AcceptanceActServiceImpl {

    private static final Map<Integer, String> MONTHS = new HashMap<>();

    static {
        MONTHS.put(1, "janvārī");
        MONTHS.put(2, "februārī");
        MONTHS.put(3, "martā");
        MONTHS.put(4, "aprīlī");
        MONTHS.put(5, "maijā");
        MONTHS.put(6, "jūnijā");
        MONTHS.put(7, "jūlijā");
        MONTHS.put(8, "augustā");
        MONTHS.put(9, "septembrī");
        MONTHS.put(10, "oktobrī");
        MONTHS.put(11, "novembrī");
        MONTHS.put(12, "decembrī");
    }

    public byte[] generateAcceptanceAct(Lending lending) throws IOException {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // date format
            LocalDate dateNow = LocalDate.now();
            String dateNowFormatted = formatDateToLatvian(dateNow);
            String estimatedReturnDateFormatted = lending.getEstimatedReturnDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // title
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(14);
            titleRun.setText("NODOŠANAS - PIENEMŠANAS AKTS");

            XWPFParagraph subtitleParagraph = document.createParagraph();
            subtitleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subtitleRun = subtitleParagraph.createRun();
            subtitleRun.setFontSize(12);
            subtitleRun.setText("Ventspilī");

            // date
            XWPFParagraph dateParagraph = document.createParagraph();
            dateParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun dateRun = dateParagraph.createRun();
            dateRun.setFontSize(12);
            dateRun.setText(dateNow.getYear() + ". gada " + dateNowFormatted);

            // main content
            XWPFParagraph contentParagraph = document.createParagraph();
            contentParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun contentRun = contentParagraph.createRun();
            contentRun.setFontSize(12);
            contentRun.setText("Ventspils Augstskolas Inženierzinātņu jaunākais laborants, Mārtiņš Leimants,");
            contentRun.addBreak();
            contentRun.setText("nodod lietošanā inventāru, ko saņem " + lending.getBorrower().getName() + " " + lending.getBorrower().getSurname() + " (turpmāk tekstā – „saņēmējs”).");
            contentRun.addBreak();

            // table
            XWPFTable table = document.createTable(4, 3);
            table.setWidth("100%");
            table.getRow(0).getCell(0).setText("Inventāra kods");
            table.getRow(0).getCell(1).setText("Piezīmes");
            table.getRow(0).getCell(2).setText("Skaits");

            table.getRow(1).getCell(0).setText(lending.getInventory().getInventoryNumber());
            table.getRow(1).getCell(1).setText("");
            table.getRow(1).getCell(2).setText("");

            for (int i = 2; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    table.getRow(i).getCell(j).setText("");
                }
            }

            XWPFParagraph afterTableParagraph = document.createParagraph();
            afterTableParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun afterTableRun = afterTableParagraph.createRun();
            afterTableRun.setFontSize(12);
            afterTableRun.setText("Saņēmējam jānodod inventārs izsniedzējam līdz: " + estimatedReturnDateFormatted);
            afterTableRun.addBreak();
            afterTableRun.setText("Inventāra saņēmējs apņemas nodot izsniegto inventāru tādā pašā tehniskajā stāvoklī kā saņēmis.");
            afterTableRun.addBreak();
            afterTableRun.setText("Pretējā gadījumā izsniedzējs ir tiesīgs pieprasīt saņēmējam kompensāciju nodarīto zaudējumu apmērā.");
            afterTableRun.addBreak();

            XWPFParagraph noteParagraph = document.createParagraph();
            noteParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun noteRun = noteParagraph.createRun();
            noteRun.setFontSize(12);
            noteRun.setText("Inventārs izsniegts saņēmējam: ");

            XWPFParagraph issuerParagraph = document.createParagraph();
            issuerParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun issuerRun = issuerParagraph.createRun();
            issuerRun.setFontSize(12);
            issuerRun.setText("Izsniedzējs:\nVentspils Augstskolas\nInženierzinātņu nodaļas laborants\nMārtiņš Leimants,\nTālr. +371 20255250,\nE-pasts: s21leimmart@venta.lv");
            issuerRun.addBreak();
            issuerRun.addBreak();
            issuerRun.setText("__________________________ (paraksts)");

            XWPFParagraph receiverParagraph = document.createParagraph();
            receiverParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun receiverRun = receiverParagraph.createRun();
            receiverRun.setFontSize(12);
            receiverRun.setText("Saņēmējs:\n" + lending.getBorrower().getName() + " " + lending.getBorrower().getSurname() + ",\nTālr. " + lending.getBorrower().getPhoneNumber());
            receiverRun.addBreak();
            receiverRun.addBreak();
            receiverRun.setText("__________________________ (paraksts)");

            XWPFParagraph returnNoteParagraph = document.createParagraph();
            returnNoteParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun returnNoteRun = returnNoteParagraph.createRun();
            returnNoteRun.setFontSize(12);
            returnNoteRun.setText("Inventārs nodots izsniedzējam: ");
            returnNoteRun.addBreak();
            returnNoteRun.setText("Izsniedzējs: __________________________ (paraksts)");
            returnNoteRun.addBreak();
            returnNoteRun.setText("Saņēmējs: __________________________ (paraksts)");

            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error while generating document", e);
        } finally {
            out.close();
            document.close();
        }

        return out.toByteArray();
    }

    private String formatDateToLatvian(LocalDate date) {
        String day = String.valueOf(date.getDayOfMonth());
        String month = MONTHS.get(date.getMonthValue());
        return day + ". " + month;
    }
}
