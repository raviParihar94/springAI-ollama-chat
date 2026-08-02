package com.beelive.ollama.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.LoggerFactory;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import java.util.regex.Pattern;

public class SopLangChainDocumentLoader {
    private static final Logger logger = LoggerFactory.getLogger(SopLangChainDocumentLoader.class);

    private final Tesseract tesseract;

    // Common noise patterns in SOPs
    private static final Pattern SCREENSHOT_CAPTION_PATTERN = Pattern.compile(
            "(?i)^(figure|fig|image|screenshot|snapshot|img|pic|illustration)\\s*\\d*[:.-]?.*"
    );
    private static final Pattern PAGE_NUMBER_PATTERN = Pattern.compile(
            "(?i)^\\s*(page\\s*\\d+\\s*(of\\s*\\d+)?|\\d+\\s*/\\s*\\d+|\\d+)\\s*$"
    );

    public SopLangChainDocumentLoader(String tessDataPath) {
        this.tesseract = new Tesseract();
        this.tesseract.setDatapath(tessDataPath);
        this.tesseract.setLanguage("eng");
    }

    /**
     * Loads a PDF file, strips headers/footers, runs OCR on screenshots, and returns
     * a LangChain4j Document.
     */
    public Document loadAndCleanSop(File pdfFile, float topMarginRatio, float bottomMarginRatio) throws IOException {
        StringBuilder fullCleanText = new StringBuilder();

        try (PDDocument pdfDocument = Loader.loadPDF(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            HeaderFooterStripper stripper = new HeaderFooterStripper(topMarginRatio, bottomMarginRatio);

            for (int i = 0; i < pdfDocument.getNumberOfPages(); i++) {
                int pageNum = i + 1;
                stripper.setStartPage(pageNum);
                stripper.setEndPage(pageNum);

                // 1. Extract raw digital text ignoring headers/footers
                String digitalText = stripper.getText(pdfDocument);

                // 2. Render page to image and perform OCR (captures screenshots & embedded UI text)
                BufferedImage pageImage = renderer.renderImageWithDPI(i, 200); // 200 DPI for good OCR quality
                String ocrText = "";
                try {
                    ocrText = tesseract.doOCR(pageImage);
                } catch (Exception e) {
                    // Handle or log individual page OCR failures
                    logger.info("Exception occurred during OCR {} ",ocrText );
                }

                // 3. Clean and merge text
                String cleanedPage = cleanContent(digitalText + "\n" + ocrText);

                if (!cleanedPage.isBlank()) {
                    fullCleanText.append("--- Page ").append(pageNum).append(" ---\n");
                    fullCleanText.append(cleanedPage).append("\n\n");
                }
            }
        }

        // Create LangChain4j Document with Metadata
        Metadata metadata = Metadata.metadata("file_name", pdfFile.getName()).put("document_type", "SOP");
        return Document.from(fullCleanText.toString().trim(), metadata);
    }

    private String cleanContent(String text) {
        String[] lines = text.split("\\r?\\n");
        List<String> cleanLines = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) continue;
            if (PAGE_NUMBER_PATTERN.matcher(trimmed).matches()) continue;
            if (SCREENSHOT_CAPTION_PATTERN.matcher(trimmed).matches()) continue;
            if (trimmed.matches("^[\\-_=*#]{3,}$")) continue; // Repeated dividers

            cleanLines.add(trimmed.replaceAll("\\s+", " "));
        }

        return String.join("\n", cleanLines);
    }

    /**
     * Layout-aware text stripper to exclude Y-axis margins (Header & Footer).
     */
    private static class HeaderFooterStripper extends PDFTextStripper {
        private final float topMarginRatio;
        private final float bottomMarginRatio;

        public HeaderFooterStripper(float topMarginRatio, float bottomMarginRatio) throws IOException {
            super();
            this.topMarginRatio = topMarginRatio;
            this.bottomMarginRatio = bottomMarginRatio;
            setSortByPosition(true);
        }

        @Override
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
            if (textPositions == null || textPositions.isEmpty()) return;

            float pageHeight = getCurrentPage().getMediaBox().getHeight();
            float topYLimit = pageHeight * topMarginRatio;
            float bottomYLimit = pageHeight * (1 - bottomMarginRatio);

            float textY = textPositions.get(0).getYDirAdj();

            if (textY >= topYLimit && textY <= bottomYLimit) {
                super.writeString(text, textPositions);
            }
        }
    }
}


