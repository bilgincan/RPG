public class HeavyShoe extends Shoe{
    private final int price = 60;

    public HeavyShoe(){
        super();
        super.plusDefence = 1;
    }

    public int getPrice(){
        return this.price;
    }

}
