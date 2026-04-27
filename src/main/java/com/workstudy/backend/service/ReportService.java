package com.workstudy.backend.service;

import com.workstudy.backend.model.WorkHour;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    public byte[] generateFinancialReport(List<WorkHour> hours) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Global Financial Ledger Report");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 710);
                contentStream.showText("System Output: WorkStudy Administration");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Total Records: " + hours.size());
                contentStream.endText();

                int yPosition = 650;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Student Name");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Job Title");
                contentStream.newLineAtOffset(150, 0);
                contentStream.showText("Hours");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Status");
                contentStream.endText();

                yPosition -= 20;

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                for (WorkHour wh : hours) {
                    if (yPosition < 50) {
                        // Create a new page if we run out of space
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        newStream.setFont(PDType1Font.HELVETICA, 10);
                        yPosition = 750;
                        newStream.beginText();
                        newStream.newLineAtOffset(50, yPosition);
                        // continue...
                        // Just a simple single-page logic or skip pagination for mock
                    }
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText(wh.getStudent() != null ? wh.getStudent().getName() : "Unknown");
                    contentStream.newLineAtOffset(150, 0);
                    
                    String jobTitle = wh.getJob() != null ? wh.getJob().getTitle() : "N/A";
                    if(jobTitle.length() > 20) jobTitle = jobTitle.substring(0, 20) + "...";
                    contentStream.showText(jobTitle);
                    
                    contentStream.newLineAtOffset(150, 0);
                    contentStream.showText(String.valueOf(wh.getHours()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(wh.getStatus() != null ? wh.getStatus() : "PENDING");
                    contentStream.endText();
                    
                    yPosition -= 20;
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF document", e);
        }
    }
}
