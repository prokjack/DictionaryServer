import sun.misc.Signal;

public enum Binyan {
    PAAL("ПААЛЬ"),PIEL("ПИЭЛЬ"),NIFAL("НИФЪАЛЬ"),HIFIL("hИФЪИЛЬ"),HITPAEL("hИТПАЭЛЬ"),PUAL("ПУАЛЬ"),HUFAL("hУФЪАЛЬ");

    private final String binyan;
    Binyan(String binyan) {
        this.binyan = binyan;
    }

    public static Binyan fromString(String text) {
        for (Binyan b : Binyan.values()) {
            if (b.binyan.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() { return binyan; }
}
