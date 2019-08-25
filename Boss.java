public class Boss extends Character{
    public Boss(){
        super(1000,100,9,9,5,3,2,10,0,0,0,"Boss",null,0);
    }

    public void wearItem(){
        System.out.println("Boss can't wear item");
    }

    public void removeItem(){
        System.out.println("Boss has no item");
    }
}
