import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;

public class HebrewParser {
    private final HashSet<String> binyansSet;
    private final String root;
    private ArrayList<Result> results = new ArrayList<>();

    public HebrewParser(String root) {
        this.root = root;
        binyansSet = new HashSet<>();
        binyansSet.add("ПААЛЬ");
        binyansSet.add("ПИЭЛЬ");
        binyansSet.add("НИФЪАЛЬ");
        binyansSet.add("hИФЪИЛЬ");
        binyansSet.add("hИТПАЭЛЬ");
        binyansSet.add("ПУАЛЬ");
        binyansSet.add("hУФЪАЛЬ");
    }

    public ArrayList<Result> parse() throws IOException {

        String url = "https://www.pealim.com/ru/search/?from-nav=1&q=" + URLEncoder.encode(root, "UTF-8");
//        String url = "https://www.pealim.com/ru/dict/?pos=verb&num-radicals=all&rf=%D7%94&rf=%D7%99&r2=%D7%A0&r1=%D7%A4" + URLEncoder.encode(root, "UTF-8");
        Document doc = Jsoup.connect(url).get();
        for (Element e : doc.getElementsByClass("verb-search-result")) {
            boolean isBinyan = false;
            for (Element el : e.getElementsByClass("verb-search-binyan")) {
                boolean res = binyansSet.stream().anyMatch(binyan -> el.text().contains(binyan));
                if (res) {
                    isBinyan = true;
                    break;
                }
            }
            if (isBinyan) {
                String href = e.getElementsByClass("btn btn-primary").first().attributes().get("href");
                String binyanUrl = "https://www.pealim.com" + href;
                Document docInt = Jsoup.connect(binyanUrl).get();
                Result result = new Result();
                result.setTranslation(docInt.getElementsByClass("lead").first().text());
                result.setBinyan(docInt.getElementsByClass("container").get(1).select("b").first().text());
                Element element1 = docInt.select("table").get(0).select("tbody").get(0);
                result.setInfinitiv(element1.getElementById("INF-L").getElementsByClass("menukad").text());
                result.setPresent1(element1.getElementById("AP-ms").getElementsByClass("menukad").text());
                result.setPresent2(element1.getElementById("AP-fs").getElementsByClass("menukad").text());
                result.setPast1(element1.getElementById("PERF-1s").getElementsByClass("menukad").text());
                result.setPast2(element1.getElementById("PERF-3ms").getElementsByClass("menukad").text());
                result.setFuture1(element1.getElementById("IMPF-1s").getElementsByClass("menukad").text());
                result.setFuture2(element1.getElementById("IMPF-3ms").getElementsByClass("menukad").text());
                results.add(result);
            }
        }
        return results;
    }
}
