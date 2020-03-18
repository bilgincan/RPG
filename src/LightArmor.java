public class LightArmor extends Armor{
    private final int price = 50;

    public LightArmor(){
        super();
        super.plusSpeed = 1.5;
        super.plusDodge = 1;
        super.plusDefence = 0.25;
    }

    public int getPrice(){
        return this.price;
    }
}
