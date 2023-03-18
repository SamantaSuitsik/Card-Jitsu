import java.util.Objects;

public class Kaart implements Comparable<Kaart>{
    private int tugevus;
    private String element;

    public Kaart(int tugevus, String element) {
        this.tugevus = tugevus;
        this.element = element;
    }

    public void kaartString(){
        System.out.println("┌───────────┐");
        System.out.println("│"+" "+tugevusValja()+" ".repeat(8)+"│");
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
                "│" + " " + tugevusValja() + " ".repeat(8) + "│",
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
                    " *  *  * ",
                    "  \\ | /  ",
                    "*===*===*",
                    "  / | \\  ",
                    " *  *  * ",
            };
        }
        return new String[]{"a", "b"};
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

    public int getTugevus() {
        return tugevus;
    }

    public void setTugevus(int tugevus) {
        this.tugevus = tugevus;
    }

    public String getElement() {
        return element;
    }

    public void setType(String element) {
        this.element = element;
    }


}
