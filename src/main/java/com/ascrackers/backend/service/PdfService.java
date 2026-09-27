package com.ascrackers.backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    public interface Line {
        String getName();
        Integer getQty();
        Double getUnitPrice();
    }

    public byte[] generateDocument(String title, Long docId, String customerName, String customerPhone,
                                    String customerAddress, java.time.LocalDateTime date,
                                    List<? extends Line> lines, Double total) {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);
            Font boldFont = new Font(Font.HELVETICA, 11, Font.BOLD);

            Paragraph heading = new Paragraph("Anish Crackers", titleFont);
            document.add(heading);
            document.add(new Paragraph("Sivakasi Wholesale & Retail - Fancy Crackers, Sparklers & Gift Boxes", normalFont));
            document.add(new Paragraph("3/149-1 Sivakamipuram, Sattur Main Road, Sivakasi, Virudhunagar, Tamil Nadu - 626189", normalFont));
            document.add(new Paragraph("Dharma: 7448981688  |  Mobile: 9787503426", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph(title + " #" + docId, boldFont));
            if (date != null) {
                document.add(new Paragraph("Date: " + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), normalFont));
            }
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Customer: " + (customerName != null ? customerName : "-"), normalFont));
            if (customerPhone != null) document.add(new Paragraph("Phone: " + customerPhone, normalFont));
            if (customerAddress != null) document.add(new Paragraph("Address: " + customerAddress, normalFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 1.5f, 1.5f});

            table.addCell(new Phrase("Item", boldFont));
            table.addCell(new Phrase("Qty", boldFont));
            table.addCell(new Phrase("Unit Price", boldFont));
            table.addCell(new Phrase("Amount", boldFont));

            for (Line line : lines) {
                double amount = line.getQty() * line.getUnitPrice();
                table.addCell(new Phrase(line.getName(), normalFont));
                table.addCell(new Phrase(String.valueOf(line.getQty()), normalFont));
                table.addCell(new Phrase(String.format("Rs. %.2f", line.getUnitPrice()), normalFont));
                table.addCell(new Phrase(String.format("Rs. %.2f", amount), normalFont));
            }
            document.add(table);

            document.add(new Paragraph(" "));
            Paragraph totalPara = new Paragraph(String.format("Total: Rs. %.2f", total), boldFont);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPara);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for choosing Anish Crackers - Best Quality & Price, the Sivakasi Way.", normalFont));

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
