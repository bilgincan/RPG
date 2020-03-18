public class TankArmor extends Armor{
    private final int price = 150;

    public TankArmor(){
        super();
        super.plusSpeed = -3;
        super.plusDodge = -3;
        super.plusDefence = 5;
    }

    public int getPrice(){
        return this.price;
    }
}
