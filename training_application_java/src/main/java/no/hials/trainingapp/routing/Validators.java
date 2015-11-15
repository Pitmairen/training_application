/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.hials.trainingapp.routing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author pitmairen
 */
public class Validators {
    
    
    /**
     * Checks that a number is int he min-max range
     */
    public static class IntegerRange implements FormInput.FormInputValidator {

        private final int mMin;
        private final int mMax;
        public IntegerRange(int min, int max){
            mMin = min;
            mMax = max;
        }
        
        @Override
        public void validateInput(FormInput form, String input) {


            int value;
            try{
                value = Integer.parseInt(form.getValue(input, ""));
            }catch(NumberFormatException e){
                form.addValidationError(input + ": Must be an integer");
                return;
            }
            
            
            if(value < mMin){
                form.addValidationError(input + ": Must be greater than " + mMin);
            }
            else if(value > mMax){
                form.addValidationError(input + ": Must be less than " + mMax);
            }
        }
    
        
    }
    
    /**
     * Checks that the value in in the set of allowed values.
     */
    public static class ValidValues implements FormInput.FormInputValidator {

        private final HashSet<String> mValidValues;
        
        public ValidValues(String ...values){
            mValidValues = new HashSet<>(Arrays.asList(values));
        }
        
        @Override
        public void validateInput(FormInput form, String input) {

            String value = form.getValue(input, "");
            
            if(!mValidValues.contains(value)){
                form.addValidationError(input + ": Invalid value");
            }
        }
    }
    
    
    /**
     * Checks that all the specified inputs have the same value
     */
    public static class MatchingInputs implements FormInput.FormValidator {

        private final String[] mInputs;
        
        public MatchingInputs(String ...inputs){
            mInputs = inputs;
        }
        
        @Override
        public void validateInput(FormInput form) {

            if(mInputs.length == 0){
                return;
            }
            
            String value = form.getValue(mInputs[0], "");
            
            for(String input : mInputs){
                if(!value.equals(form.getValue(input))){
                    form.addValidationError("The inputs " + Arrays.toString(mInputs) + " must match");
                    return;
                }
                
            }
        }
    }
    
    
    /**
     * Checks that the value is a valid date
     */
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
