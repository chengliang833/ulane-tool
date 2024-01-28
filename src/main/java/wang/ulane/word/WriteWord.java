package wang.ulane.word;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.*;
import org.ddr.poi.html.HtmlRenderPolicy;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import wang.ulane.file.ConvertUtil;

public class WriteWord {
    public static void main(String[] args) throws IOException {
//        writeText();
//        String content = "<h1>标题头</h1><h2>第二个标题</h2><a href=\"https://www.baidu.com\">百度搜索</a>";
        String content = ConvertUtil.parseString(new FileInputStream("C:\\Users\\Administrator\\Desktop\\temp\\temp.txt"));
        WordHtmlUtil.wirteWithHtml("C:\\Users\\Administrator\\Desktop\\temp\\temp.docx", content);
        System.out.println("finish...");
    }

    public static void writeText() throws IOException {
        XWPFDocument document = new XWPFDocument();
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        title.setSpacingBetween(1.5);
        XWPFRun titleRun = title.createRun();
        titleRun.setFontSize(14);
        titleRun.setText("名称");

        XWPFParagraph cont = document.createParagraph();
        cont.setAlignment(ParagraphAlignment.LEFT);
        cont.setSpacingBetween(1.5);
        XWPFRun contRun = cont.createRun();
        contRun.setFontSize(14);
        contRun.setText("内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
        contRun.addBreak();
        contRun.setText("2内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");

        FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\temp\\temp.doc");
        document.write(fos);
    }

}