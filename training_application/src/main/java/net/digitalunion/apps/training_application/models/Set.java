package net.digitalunion.apps.training_application.models;

/**
 *
 * @author hasirak
 */
public class Set {

    private int workoutID;
    private int exerciseID;
    private int load;
    private int repititions;
    private int repititionsPerformed;
    private String comment;

    /**
     * @return the workoutID
     */
    public int getWorkoutID() {
        return workoutID;
    }

    /**
     * @param workoutID the workoutID to set
     */
    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    /**
     * @return the exerciseID
     */
    public int getExerciseID() {
        return exerciseID;
    }

    /**
     * @param exerciseID the exerciseID to set
     */
    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    /**
     * @return the load
     */
    public int getLoad() {
        return load;
    }

    /**
     * @param load the load to set
     */
    public void setLoad(int load) {
        this.load = load;
    }

    /**
     * @return the repititions
     */
    public int getRepititions() {
        return repititions;
    }

    /**
     * @param repititions the repititions to set
     */
    public void setRepititions(int repititions) {
        this.repititions = repititions;
    }

    /**
     * @return the repititionsPerformed
     */
    public int getRepititionsPerformed() {
        return repititionsPerformed;
    }

    /**
     * @param repititionsPerformed the repititionsPerformed to set
     */
    public void setRepititionsPerformed(int repititionsPerformed) {
        this.repititionsPerformed = repititionsPerformed;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
