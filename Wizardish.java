public abstract class Wizardish implements Item{
    protected double plusHeal;
    protected double plusPersuasive;
    protected double plusInvestigation;
    protected double plusSneak;

    public double[] getPlusEffect(){
        double[] returned = new double[9];
        returned[8] = plusHeal;
        returned[5] = plusPersuasive;
        returned[6] = plusInvestigation;
        returned[7] = plusSneak;
        return returned;
    }

    public abstract int getPrice();

}
