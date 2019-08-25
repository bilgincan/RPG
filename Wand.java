public class Wand extends Wizardish{
    private final int price = 100;

    public Wand(){
        super();
        super.plusHeal = 1;
    }

    public int getPrice(){
        return this.price;
    }
}
