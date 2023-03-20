import java.util.Objects;

public class Kaart implements Comparable<Kaart>{
    private int tugevus;
    private String element;
    private String eriline = null;


    public Kaart(int tugevus, String element) {
        this.tugevus = tugevus;
        this.element = element;
    }

    public Kaart(int tugevus, String element, String eriline) {
        this.tugevus = tugevus;
        this.element = element;
        this.eriline = eriline;
    }

    public void kaartString(){
        System.out.println("┌───────────┐");
        System.out.println("│"+" "+tugevusValja()+" ".repeat(2)+erilineValja()+" "+"│");
        System.out.println("│"+" "+elementvalja()[0] +" "+"│");
        System.out.println("│"+" "+elementvalja()[1] +" "+"│");
        System.out.println("│"+" "+elementvalja()[2] +" "+"│");
        System.out.println("│"+" "+elementvalja()[3] +" "+"│");
        System.out.println("│"+" "+elementvalja()[4] +" "+"│");
        System.out.println("└───────────┘");

    }
    private String tugevusValja(){
        if (this.tugevus < 10)
            return (this.tugevus+" ");
        else
            return String.valueOf(this.tugevus);
    }
    public String[] kaartReturn(){
        return new String[]{
                "┌───────────┐",
                "│" + " " + tugevusValja() + " ".repeat(2)+erilineValja()+" " + "│",
                "│" + " " + elementvalja()[0] + " " + "│",
                "│" + " " + elementvalja()[1] + " " + "│",
                "│" + " " + elementvalja()[2] + " " + "│",
                "│" + " " + elementvalja()[3] + " " + "│",
                "│" + " " + elementvalja()[4] + " " + "│",
                "└───────────┘",

        };
    }

    private String[] elementvalja() {
        if (Objects.equals(this.element, "tuli")) {
            return new String[]{
                    "   |     ",
                    "| ||  |  ",
                    "|  ||  | ",
                    "|||||||| ",
                    " ||||||  ",
            };
        }
        if (Objects.equals(this.element, "vesi")){
            return new String[]{
                    "   /\\    ",
                    "  /  \\   ",
                    " /    \\  ",
                    "|      | ",
                    " \\____/  ",
            };
        }
        if (Objects.equals(this.element, "lumi")){
            return new String[]{
                    "  *   *  ",
                    "   \\ /   ",
                    "*───*───*",
                    "   / \\   ",
                    "  *   *  ",
            };
        }
        return new String[]{"a", "b"};
    }

    public String erilineValja(){

        String xemoji = "❌";
        String blokemoji = "\uD83D\uDEAB";
        String noolemoji = "➡";
        String vahetusemoji = "\uD83D\uDD01";
        String bommemoji = "\uD83D\uDCA3";
        String tuleemoji = "\uD83D\uDD25";
        String veeemoji = "\uD83D\uDCA7";
        String lumeemoji = "❄";
        String kitsas = "\u2009";
        String teinekitsas = "\u202F";

        if (this.eriline == null)
            return "     ";
        switch (this.eriline){
            case "+2" -> {
                return "   +2";
            }
            case "-2" -> {
                return "   -2";
            }
            case "vahetus" -> {
                return kitsas+"1"+vahetusemoji+"9"+kitsas;
            }
            case "eemalda tuli" -> {
                return " " + bommemoji + tuleemoji;
            }
            case "eemalda vesi" -> {
                return kitsas+kitsas + bommemoji + veeemoji+kitsas;
            }
            case "eemalda lumi" -> {
                return " " + bommemoji + lumeemoji + kitsas;
            }
            case "muuda tuli" -> {
                return tuleemoji + noolemoji + lumeemoji;
            }
            case "muuda vesi" -> {
                return veeemoji + noolemoji + tuleemoji;
            }
            case "muuda lumi" -> {
                return teinekitsas+ lumeemoji + noolemoji + veeemoji;
            }
            case "blokeeri tuli" -> {
                return teinekitsas+blokemoji + tuleemoji + kitsas;
            }
            case "blokeeri vesi" -> {
                return teinekitsas+kitsas+blokemoji + veeemoji + kitsas;
            }
            case "blokeeri lumi" -> {
                return teinekitsas+kitsas+blokemoji + lumeemoji + kitsas;
            }
            default -> {
                return "   ";
            }
        }
    }

    @Override
    public int compareTo(Kaart o) {
        if (Objects.equals(this.element, o.element)) {
            return this.tugevus - o.tugevus;
        }
        else {
            switch (this.element) {
                case "tuli" -> {
                    if (Objects.equals(o.element, "lumi"))
                        return 1;
                    else
                        return -1;
                }
                case "vesi" -> {
                    if (Objects.equals(o.element, "tuli"))
                        return 1;
                    else
                        return -1;
                }
                case "lumi" -> {
                    if (Objects.equals(o.element, "vesi"))
                        return 1;
                    else
                        return -1;
                }
                default -> {
                    return 0;
                }
            }
        }
    }

    public String getEriline() {
        return eriline;
    }

    public void setEriline(String eriline) {
        this.eriline = eriline;
    }

    public int getTugevus() {
        return tugevus;
    }

    public void setTugevus(int tugevus) {
        this.tugevus = tugevus;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }


}
