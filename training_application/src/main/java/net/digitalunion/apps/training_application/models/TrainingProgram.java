package net.digitalunion.apps.training_application.models;

/**
 *
 * @author hasirak
 */
public class TrainingProgram {

    private int trainingProgramID;
    private int TrainerID;

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
     * @return the TrainerID
     */
    public int getTrainerID() {
        return TrainerID;
    }

    /**
     * @param TrainerID the TrainerID to set
     */
    public void setTrainerID(int TrainerID) {
        this.TrainerID = TrainerID;
    }
}
