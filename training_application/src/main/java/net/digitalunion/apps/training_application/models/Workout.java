package net.digitalunion.apps.training_application.models;

/**
 *
 * @author hasirak
 */
public class Workout {

    private int workoutID;
    private int trainingProgramID;
    private String workoutName;
    private String description;

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
     * @return the trainingProgramID
     */
    public int getTrainingProgramID() {
        return trainingProgramID;
    }

    /**
     * @param trainingProgramID the trainingProgramID to set
     */
    public void setTrainingProgramID(int trainingProgramID) {
        this.trainingProgramID = trainingProgramID;
    }

    /**
     * @return the workoutName
     */
    public String getWorkoutName() {
        return workoutName;
    }

    /**
     * @param workoutName the workoutName to set
     */
    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
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
