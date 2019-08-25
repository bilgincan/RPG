public abstract class Armor implements Item{
    protected double plusDefence;
    protected double plusSpeed;
    protected double plusDodge;

    public double[] getPlusEffect(){
        double[] returned = new double[9];
        returned[2] = plusDefence;
        returned[3] = plusSpeed;
        returned[4] = plusDodge;
        return returned;
    }

    public abstract int getPrice();
}
