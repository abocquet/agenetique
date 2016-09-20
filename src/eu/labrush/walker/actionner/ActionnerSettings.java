package eu.labrush.walker.actionner;

public class ActionnerSettings {
    public double freq ;
    public double maxAngle ;
    public double minAngle ;
    public double phase;

    public ActionnerSettings addMaxAngle(double angle){
        this.maxAngle += angle ;
        return this ;
    }
}
