package wang.ulane.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.ddr.poi.html.HtmlRenderPolicy;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import wang.ulane.file.ConvertUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class WordHtmlUtil {

    public static void wirteWithHtml(String toPath, String content) throws IOException {
        HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy();
        Configure configure = Configure.builder()
                // 注册html解析插件
                .bind("content", htmlRenderPolicy)
                // .bind("content2", htmlRenderPolicy)
                .build();

//        String content = "<h1>标题头</h1><h2>第二个标题</h2><a href=\"https://www.baidu.com\">百度搜索</a>";
        StringBuffer sbf = new StringBuffer();
        sbf.append("<html><body>");
        sbf.append(content);
        sbf.append("</body></html>");
        Map<String, Object> map = new HashMap<>();
        map.put("content", sbf.toString());

        //模板
        XWPFDocument document = new XWPFDocument();
        document.createStyles();
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageSz pgsz = sectPr.addNewPgSz();
        pgsz.setW(BigInteger.valueOf(11906));
        pgsz.setH(BigInteger.valueOf(16838));

        CTPageMar pgmr = sectPr.addNewPgMar();
        pgmr.setTop(BigInteger.valueOf(1440));
        pgmr.setRight(BigInteger.valueOf(1800));
        pgmr.setBottom(BigInteger.valueOf(1440));
        pgmr.setLeft(BigInteger.valueOf(1800));

//        XWPFParagraph title = document.createParagraph();
////		title.setAlignment(ParagraphAlignment.LEFT);
////		title.setSpacingBetween(1.5);
//        XWPFRun titleRun = title.createRun();
////		contRun.setFontSize(14);
//        titleRun.setText("名称");
//        titleRun.addBreak();
//        titleRun.setText("第二行");

        //一段一个html, 不然render的时候会放到段落最后面
        document.createParagraph().createRun().setText("{{content}}");

//        document.createParagraph().createRun().setText("第三段");

//		XWPFTemplate temp = XWPFTemplate.compile("C:\\Users\\Administrator\\Desktop\\temp\\html2wordtemplate.docx", configure).render(map);

//		FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\temp\\temp.doc");
//		document.write(fos);
//		XWPFTemplate temp = XWPFTemplate.compile(new File("C:\\Users\\Administrator\\Desktop\\temp\\temp.doc"), configure).render(map);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.write(baos);
        XWPFTemplate temp = XWPFTemplate.compile(ConvertUtil.parse(baos), configure).render(map);

        FileOutputStream ostream = new FileOutputStream(toPath);
        temp.write(ostream);
        temp.close();
        ostream.close();
    }

}
