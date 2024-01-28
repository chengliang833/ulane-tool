package wang.ulane.word;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class WordUtil {

    public static String readDocxToString(File file) throws IOException {
        return readDocxToString(file, "\n", true);
    }
    public static String readDocxToString(File file, String paraSplit, boolean maybeDoc) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean swapType = false;
        try (InputStream fis = new FileInputStream(file)) {
            XWPFDocument xwpfDocument = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                sb.append(para.getText()).append(paraSplit);
            }
        } catch (IllegalArgumentException e){
            if(!maybeDoc){
                throw e;
            }
            swapType = true;
        }
        if(swapType){
            return readDocToString(file, paraSplit, false);
        }
        return sb.toString();
    }

    public static String readDocToString(File file) throws IOException {
        return readDocToString(file, "\n", true);
    }
    public static String readDocToString(File file, String paraSplit, boolean maybeDocx) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean swapType = false;
        try (InputStream fis = new FileInputStream(file)) {
            HWPFDocument hwpf = new HWPFDocument(fis);
            Range range = hwpf.getRange();
            for (int i = 0; i < range.numParagraphs(); i++) {
                sb.append(range.getParagraph(i).text().replaceAll("\\r*$", "")).append(paraSplit);
            }
        } catch (IllegalArgumentException e){
            if(!maybeDocx){
                throw e;
            }
            swapType = true;
        }
        if(swapType){
            return readDocxToString(file, paraSplit, false);
        }
        return sb.toString();
    }



}
