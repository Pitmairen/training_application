/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.hials.trainingapp.routing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author pitmairen
 */
public class Validators {
    
    
    public static class DateValidator implements FormInput.FormInputValidator {

        private final boolean mAllowDateInPast;

        public DateValidator(boolean allowDateInPast) {
            mAllowDateInPast = allowDateInPast;
        }

        @Override
        public void validateInput(FormInput form, String input) {

            String value = form.getValue(input, "");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(value);

                if (dateFormat.format(date).equals(value)) {
                    if(!mAllowDateInPast && checkDateInPast(date)){
                        form.addValidationError("Date is in the past");
                    }
                    return;
                }

            } catch (ParseException e) {
                ;
            }

            form.addValidationError("Date should be in the format yyyy-mm-dd");

        }

        private boolean checkDateInPast(Date date) {
            
            Calendar c = Calendar.getInstance();
            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date today = c.getTime();

            return date.compareTo(today) < 0;
    
        }

    }
    
}
