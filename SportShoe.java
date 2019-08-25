public class SportShoe extends Shoe{
    private final int price = 75;

    public SportShoe(){
        super();
        super.plusSpeed = 1.5;
        super.plusDodge = 2;
    }

    public int getPrice(){
        return this.price;
    }

}
