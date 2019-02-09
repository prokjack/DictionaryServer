import javax.swing.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        String rootWord = "עבד";
        HebrewParser2 hebrewParser = new HebrewParser2(rootWord);
        ArrayList<Result> parseResult = hebrewParser.parse();
        String collect = parseResult.stream().map(Result::toString).collect(Collectors.joining("\n"));
        //Custom button text
        JFrame frame = new JFrame();
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(frame, collect, "Title on Box", dialogButton);
        if (dialogResult == 0) {
            System.out.println("Yes option");
//            XLSWorker xlsWorker = new XLSWorker("C:\\Users\\j2ck\\Documents\\dict.xlsx");
//            xlsWorker.writeXLSXFile(parseResult);
            SheetsQuickstart sheetsQuickstart = new SheetsQuickstart(rootWord);
            sheetsQuickstart.write(parseResult);
        } else {
            System.out.println("No Option");
        }
    }
}
