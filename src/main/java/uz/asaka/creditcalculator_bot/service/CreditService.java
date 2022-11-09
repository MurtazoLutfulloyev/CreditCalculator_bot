package uz.asaka.creditcalculator_bot.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import uz.asaka.creditcalculator_bot.model.Credit;
import uz.asaka.creditcalculator_bot.model.User;
import uz.asaka.creditcalculator_bot.repository.CreditRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    public final static String DEST = "D:\\IdeaProjects\\CreditCalculator_bot\\src\\main\\resources\\files";

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public String creditCalculator(Long totalAmount, Integer duration, double percentage, User user, String lang) throws DocumentException, IOException {

        double amount = ((totalAmount * Math.pow((1 + percentage/12), duration))/(Math.pow((1 + percentage/12),duration) - 1))/duration;

        return createPdf(duration, String.valueOf(amount), user, percentage, totalAmount, lang);
    }
    public String createPdf(Integer duration, String payment, User user, double percentage, Long totalAmount, String lang) throws IOException, DocumentException {

        System.out.println(lang);
        System.out.println(new Date());
        Document document = new Document();
        String path = DEST + "\\" + fileNameGenerator();
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[]{6, 6, 6});
        table.setWidthPercentage(100);
        table.addCell(createCell((lang.equals("uz"))? "Oy" : "Месяц", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell((lang.equals("uz"))? "Sana" : "Дата", 2, 1, Element.ALIGN_CENTER));
        table.addCell(createCell((lang.equals("uz"))? "To`lov summasi" : "Сумма к оплате", 2, 1, Element.ALIGN_CENTER));

        for (int i = 0; i < duration; i++) {
            LocalDate date = LocalDate.now();
            LocalDate date1 = date.plusMonths(i);
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            table.addCell(createCell(String.valueOf(i + 1), 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(date1.format(formatters), 1, 1, Element.ALIGN_CENTER));
            table.addCell(createCell(payment, 1, 1, Element.ALIGN_CENTER));
        }
        document.add(table);
        document.close();

        Credit credit = new Credit();
        credit.setDuration(duration);
        credit.setUser(user);
        credit.setPercentage(percentage);
        credit.setTotalAmount(totalAmount);
        credit.setFilePath(path);
        creditRepository.save(credit);
        return path;
    }

    public static PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) {
        Font font = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private static String fileNameGenerator() {
        return String.valueOf(new Random().nextInt(900000) + 100000) + ".pdf";
    }

}
