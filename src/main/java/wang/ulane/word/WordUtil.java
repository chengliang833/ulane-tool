package wang.ulane.word;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class WordUtil {

    private static final String docTableLineSplit = Character.toString((char)7);

    public static String readDocxToString(File file) throws IOException {
        return readDocxToString(file, "\n", true);
    }
    public static String readDocxToString(File file, String paraSplit, boolean maybeDoc) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean swapType = false;
        try (InputStream fis = new FileInputStream(file)) {
            XWPFDocument xwpfDocument = new XWPFDocument(fis);
            Iterator<IBodyElement> its = xwpfDocument.getBodyElementsIterator();
            while(its.hasNext()){
                IBodyElement ele = its.next();
                if(ele instanceof XWPFParagraph){
                    sb.append(((XWPFParagraph)ele).getText()).append(paraSplit);
                }else if(ele instanceof XWPFTable){
                    sb.append(((XWPFTable)ele).getText()).append(paraSplit);
                }else if(ele instanceof XWPFSDT){
                    sb.append(((XWPFSDT)ele).getContent().getText()).append(paraSplit);
                }
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
            boolean lastInTable = false;
            for (int i = 0; i < range.numParagraphs(); i++) {
                Paragraph para = range.getParagraph(i);
                String strTemp = para.text();
                if(para.isInTable()){
                    lastInTable = true;
                    if(docTableLineSplit.equals(strTemp)) {
                        if(para.isTableRowEnd()){
                            if(sb.charAt(sb.length()-1) == '\t'){
                                sb.replace(sb.length()-1, sb.length(), paraSplit);
                            }else{
                                sb.append(paraSplit);
                            }
                        }else{
                            sb.append("\t");
                        }
                    }else{
                        sb.append(strTemp.replaceAll(docTableLineSplit, "\t").replaceAll("\\r", "\t"));
                    }
                }else{
                    if(lastInTable){
                        sb.append(paraSplit);
                        lastInTable = false;
                    }
                    sb.append(strTemp.replaceAll("\\r*$", "")).append(paraSplit);
                }
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
