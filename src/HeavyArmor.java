public class HeavyArmor extends Armor{
    private final int price = 150;

    public HeavyArmor(){
        super();
        super.plusDodge = -1;
        super.plusSpeed = -0.5;
        super.plusDefence = 2;
    }

    public int getPrice(){
        return this.price;
    }
}
