package eu.labrush.walker.actionner;

import eu.labrush.walker.walker.BipedBody;
import eu.labrush.walker.walker.SpringLeg;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepListener;
import org.dyn4j.dynamics.World;


public class BipedeBodyActionner {

    BipedBody walker;

    private ActionnerSettings hip_left ;
    private ActionnerSettings hip_right ;

    private ActionnerSettings knee_left ;
    private ActionnerSettings knee_right ;

    public BipedeBodyActionner(BipedBody walker){
        this.walker = walker;
    }

    public void listen(World world){
        addListener(world, walker.getLegs().get(0), hip_left, knee_left);
        addListener(world, walker.getLegs().get(1), hip_right, knee_right);
    }

    private void addListener(World world, SpringLeg leg, ActionnerSettings hip, ActionnerSettings knee){

        world.addListener(new StepListener(){

            private double hip_avg = (hip.maxAngle + hip.minAngle) / 2 ;
            private double hip_amplitude = Math.abs(hip.maxAngle - hip.minAngle) ; //Cas ou max > min, normalement à réparer directement dans l'adn...
            private double hip_pulse = hip.freq / (2 * Math.PI);

            private double knee_avg = (knee.maxAngle + knee.minAngle) / 2 ;
            private double knee_amplitude = knee.maxAngle - knee.minAngle ;
            private double knee_pulse = knee.freq / (2 * Math.PI);

            private double time = 0 ;

            @Override
            public void begin(Step step, World world) {

                time += step.getDeltaTime();
                leg.setHipAngle(hip_avg + hip_amplitude * Math.sin(hip_pulse * time + hip.phase));
                leg.setKneeAngle(-(knee_avg + knee_amplitude * Math.sin(knee_pulse * time + knee.phase)));

            }

            @Override
            public void updatePerformed(Step step, World world) {

            }

            @Override
            public void postSolve(Step step, World world) {

            }

            @Override
            public void end(Step step, World world) {

            }
        });
    }

    public ActionnerSettings getLeftHip() {
        return hip_left;
    }

    public ActionnerSettings getRightHip() {
        return hip_right;
    }

    public ActionnerSettings getLeftKnee() {
        return knee_left;
    }

    public ActionnerSettings getRighKnee() {
        return knee_right;
    }

    public void setLeftHip(ActionnerSettings hip) {
        this.hip_left = hip;
    }

    public void setRightHip(ActionnerSettings hip) {
        this.hip_right = hip;
    }

    public void setLeftKnee(ActionnerSettings knee) {
        this.knee_left = knee;
    }

    public void setRightKnee(ActionnerSettings knee) {
        this.knee_right = knee;
    }
}
