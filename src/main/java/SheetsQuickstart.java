import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;
@NoArgsConstructor
public class SheetsQuickstart {
    private String rootWord;
    private final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final String CREDENTIALS_FILE_PATH = "credentials.json";

    public SheetsQuickstart(String rootWord) {
        this.rootWord = rootWord;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public UpdateValuesResponse write(ArrayList<Result> parseResult) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1CM8m8RDm5VA1nPM_c8dEINYr0Mxx3GKBzM-V6AmQj_w";

        final String range = "verbs!A1:AV";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> currentValues = response.getValues();
        if (isAlreadyExist(parseResult, currentValues)) {
            System.out.println("Word already exist");
            return null;
        }

        int sizeOfExistingTable = currentValues.size();
        final String rangeSet = "verbs!A" + (sizeOfExistingTable + 1) + ":BV";

        List<List<Object>> values = getValuesFromParseResult(parseResult);

        ValueRange body = new ValueRange()
                .setValues(values);
        UpdateValuesResponse result =
                service.spreadsheets().values().update(spreadsheetId, rangeSet, body)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
        return result;
    }

    private boolean isAlreadyExist(ArrayList<Result> parseResult, List<List<Object>> currentValues) {
        String wordToCheck = parseResult.get(0).getInfinitiv();
        return currentValues.stream()
                .anyMatch(l -> l.stream().filter(o -> o instanceof String)
                        .anyMatch(obj -> Objects.equals(wordToCheck, obj.toString())));
    }

    private List<List<Object>> getValuesFromParseResult(ArrayList<Result> parseResult) {

        Map<Binyan, Result> collect = parseResult.stream().filter(result -> Objects.nonNull(result.getBinyan()))
                .collect(Collectors.toMap(r -> Binyan.fromString(r.getBinyan()), r -> r));

        ArrayList<Object> strings = new ArrayList<>();
        strings.add(Optional.ofNullable(rootWord).orElse(""));
        strings = addBinyanToResultList(Binyan.PAAL, collect, strings);
        strings = addBinyanToResultList(Binyan.PIEL, collect, strings);
        strings = addBinyanToResultList(Binyan.PUAL, collect, strings);
        strings = addBinyanToResultList(Binyan.HITPAEL, collect, strings);
        strings = addBinyanToResultList(Binyan.NIFAL, collect, strings);
        strings = addBinyanToResultList(Binyan.HIFIL, collect, strings);
        strings = addBinyanToResultList(Binyan.HUFAL, collect, strings);


        return Arrays.asList(strings);
                // Additional rows ...
    }

    private ArrayList<Object> addBinyanToResultList(Binyan binyan, Map<Binyan, Result> collect, ArrayList<Object> strings) {
        if (collect.keySet().contains(binyan)) {
            Result result = collect.get(binyan);
            strings.add(Optional.ofNullable(result.getPresent2()).orElse(""));
            strings.add(Optional.ofNullable(result.getPresent1()).orElse(""));
            strings.add(Optional.ofNullable(result.getPast2()).orElse(""));
            strings.add(Optional.ofNullable(result.getPast1()).orElse(""));
            strings.add(Optional.ofNullable(result.getFuture2()).orElse(""));
            strings.add(Optional.ofNullable(result.getFuture1()).orElse(""));
            strings.add(Optional.ofNullable(result.getInfinitiv()).orElse(""));
            strings.add(Optional.ofNullable(result.getTranslation()).orElse(""));
        } else {
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
            strings.add("");
        }

        return strings;
    }
}