package net.digitalunion.apps.training_application.models;

/**
 *
 * @author hasirak
 */
public class Exercise {

    private int exerciseID;
    private String exerciseName;
    private String description;

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
     * @return the exerciseName
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * @param exerciseName the exerciseName to set
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
