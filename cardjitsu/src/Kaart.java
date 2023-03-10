import java.util.Objects;

public class Kaart {
    private int tugevus;
    private String element;

    public Kaart(int tugevus, String element) {
        this.tugevus = tugevus;
        this.element = element;
    }

    public void KaartValja(){
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

    private String[] elementvalja() {
        if (Objects.equals(this.element, "tuli")) {
            String[] uus = {
                    "   |     ",
                    "| ||  |  ",
                    "|  ||  | ",
                    "|||||||| ",
                    " ||||||  ",
            };
            return uus;
        }
        if (Objects.equals(this.element, "vesi")){
            String[] uus = {
                    "   /\\    ",
                    "  /  \\   ",
                    " /    \\  ",
                    "|      | ",
                    " \\____/  ",
            };
            return uus;
        }
        if (Objects.equals(this.element, "ice")){
            String[] uus = {
                    " *  *  * ",
                    "  \\ | /  ",
                    "*===*===*",
                    "  / | \\  ",
                    " *  *  * ",
            };
            return uus;
        }
        return new String[]{"a", "b"};
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
