public class MediumArmor extends Armor{
    private final int price = 100;

    public MediumArmor(){
        super();
        super.plusSpeed = 1;
        super.plusDodge = 1;
        super.plusDefence = 1;
    }

    public int getPrice(){
        return this.price;
    }
}
