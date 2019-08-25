public class Cloak extends Wizardish{
    private final int price = 125;

    public Cloak(){
        super();
        super.plusHeal = 0.5;
        super.plusPersuasive = 0.5;
        super.plusInvestigation = 0.5;
        super.plusSneak = 0.5;
    }

    public int getPrice(){
        return this.price;
    }
}
