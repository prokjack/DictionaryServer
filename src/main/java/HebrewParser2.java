import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

public class HebrewParser2 {
    private final HashSet<Binyan> binyansSet;
    private final String root;
    private ArrayList<Result> results = new ArrayList<>();

    public HebrewParser2(String root) {
        this.root = root;
        binyansSet = new HashSet<>();
        binyansSet.addAll(Arrays.asList(Binyan.values()));
//        binyansSet.add("ПИЭЛЬ");
//        binyansSet.add("НИФЪАЛЬ");
//        binyansSet.add("hИФЪИЛЬ");
//        binyansSet.add("hИТПАЭЛЬ");
//        binyansSet.add("ПУАЛЬ");
//        binyansSet.add("hУФЪАЛЬ");
    }

    public ArrayList<Result> parse() throws IOException {
        if (root.length() != 3) {
            System.out.println("Root can be only from 3 letters for now");
            return null;
        }

//        String url = "https://www.pealim.com/ru/search/?from-nav=1&q=" + URLEncoder.encode(root, "UTF-8");
        String url = "https://www.pealim.com/ru/dict/?pos=verb&num-radicals=all&rf=" + URLEncoder.encode(root.charAt(2) + "", "UTF-8") + "&r2=" + URLEncoder.encode(root.charAt(1) + "", "UTF-8") + "&r1=" + URLEncoder.encode(root.charAt(0) + "", "UTF-8");
        Document doc = Jsoup.connect(url).get();
        Elements trElementsTable = doc.getElementsByClass("table table-hover dict-table-t").select("tr");
        for (int i = 1; i < trElementsTable.size(); i++) {
            Element element = trElementsTable.get(i);
            String href = element.select("a").first().attributes().get("href");
            String binyanUrl = "https://www.pealim.com" + href;
            Document docInt = Jsoup.connect(binyanUrl).get();
            Result result = new Result();
            result.setTranslation(docInt.getElementsByClass("lead").first().text());
            String text = docInt.getElementsByClass("container").get(1).select("b").first().text();

            result.setBinyan(text);
            Element element1 = docInt.select("table").get(0).select("tbody").get(0);
            result.setInfinitiv(Optional.ofNullable(element1.getElementById("INF-L")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPresent1(Optional.ofNullable(element1.getElementById("AP-ms")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPresent2(Optional.ofNullable(element1.getElementById("AP-fs")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPast1(Optional.ofNullable(element1.getElementById("PERF-1s")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setPast2(Optional.ofNullable(element1.getElementById("PERF-3ms")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setFuture1(Optional.ofNullable(element1.getElementById("IMPF-1s")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            result.setFuture2(Optional.ofNullable(element1.getElementById("IMPF-3ms")).map(r -> r.getElementsByClass("menukad")).map(Elements::text).orElse(null));
            results.add(result);
        }
        return results;
    }
}
