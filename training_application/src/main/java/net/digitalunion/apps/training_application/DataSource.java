
package net.digitalunion.apps.training_application;

import net.digitalunion.apps.training_application.models.Trainer;

/**
 *
 * @author pitmairen
 */
public interface DataSource {
 
    public Trainer createNewTrainer(String firstName, String lastName, 
                                    String email, String password);
    
    
    public Trainer getTrainerById(int id);
    
    
    
}
