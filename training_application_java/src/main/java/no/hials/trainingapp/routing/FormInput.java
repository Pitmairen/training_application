/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.hials.trainingapp.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;

/**
 * Helper class for form input processing
 *
 * The usage is simple:
 *
 * FormInput input = new FormInput(request);
 *
 * input.addRequiredInputs("input1", "input2");
 * input.addOptionalInputs("input3", "input4");
 *
 * if (input.validateInput()) { // Input is valid }else{
 * input.getValidationErrors() // Contains a list of validation errors }
 *
 */
public class FormInput {

    /**
     * A validator can be used to validate a input value in the form. Objects
     * implementing this interface can be added to the form input object to
     * validate a single input value.
     */
    public interface FormInputValidator {

        void validateInput(FormInput form, String input);
    }

    /**
     * A validator can be used to validate the form. Objects implementing this
     * interface can be added to the form input object to validate the complete
     * form.
     */
    public interface FormValidator {

        void validateInput(FormInput form);
    }

    private final List<String> mAllInputs;
    private final List<String> mRequiredInputs;
    private final List<String> mValidationErrors;
    private final Request mRequest;
    private final Map<String, List<FormInputValidator>> mInputValidators;
    private final List<FormValidator> mGlobalValidators;

    /**
     * Creates a new form input processor for the given request
     *
     * @param request the request object
     */
    public FormInput(Request request) {
        mRequest = request;
        mAllInputs = new ArrayList<>();
        mRequiredInputs = new ArrayList<>();
        mValidationErrors = new ArrayList<>();
        mInputValidators = new HashMap<>();
        mGlobalValidators = new ArrayList<>();
    }

    /**
     * Add inputs that the user is required to provide.
     *
     * If one of these are missing or contains an empty string it will cause a
     * validation error.
     *
     * @param inputs a list of required inputs
     */
    public void addRequiredInputs(String... inputs) {
        mRequiredInputs.addAll(Arrays.asList(inputs));
        addInputs(inputs);
    }

    /**
     * Add inputs that are optional
     *
     * @param inputs
     */
    public void addOptionalInputs(String... inputs) {
        addInputs(inputs);
    }

    /**
     * Add a input validator to the specified input
     *
     * @param input the input to validate
     * @param validator the validator
     */
    public void addValidator(String input, FormInputValidator validator) {

        if (!mInputValidators.containsKey(input)) {
            mInputValidators.put(input, new ArrayList<>());
        }

        mInputValidators.get(input).add(validator);
    }

    /**
     * Add a global input validator to the form processing
     *
     * @param validator the validator
     */
    public void addValidator(FormValidator validator) {
        mGlobalValidators.add(validator);
    }

    /**
     * Returns the form value for the given input name
     *
     * @param name the input name
     * @return the value or null if it does not exist
     */
    public String getValue(String name) {
        return mRequest.queryParams(name);
    }

    /**
     * Returns the form value for the given input name
     *
     * If the input name does not exists return the default value
     *
     * @param name the input name
     * @param defaultValue the value to return if the input does not exist
     * @return the value if it exists else defaultValue
     */
    public String getValue(String name, String defaultValue) {
        String value = mRequest.queryParams(name);
        if(value == null){
            value = defaultValue;
        }
        return value;
    }

    /**
     * Checks if the form has been posted and that the input is valid
     *
     * A form is considered posted if the request method is POST
     *
     * @return true form was posted and all input was good
     */
    public boolean postedAndValid() {
        return mRequest.requestMethod().equals("POST") && validateInput();
    }

    /**
     * Validates the form input
     *
     * @return true if all input was good
     */
    public boolean validateInput() {

        checkRequiredInput();
        runValidators();

        return !hasValidationErrors();

    }

    /**
     * Returns true if there was any validation errors
     *
     * @return true if there was any validation errors
     */
    public boolean hasValidationErrors() {
        return !mValidationErrors.isEmpty();
    }

    /**
     * Add a validation error to the errors list
     *
     * @param message the message to add
     */
    public void addValidationError(String message) {
        mValidationErrors.add(message);
    }

    /**
     * Returns a list of validation errors
     *
     * @return a list of validation errors
     */
    public List<String> getValidationErrors() {
        return new ArrayList<>(mValidationErrors);
    }

    private void addInputs(String... inputs) {
        mAllInputs.addAll(Arrays.asList(inputs));
    }

    private void checkRequiredInput() {
        for (String input : mRequiredInputs) {
            String value = mRequest.queryParams(input);
            if (value == null || value.isEmpty()) {
                addValidationError(input + " is required");
            }
        }
    }

    private void runValidators() {

        for (String input : mAllInputs) {
            if (!mInputValidators.containsKey(input)) {
                continue;
            }

            List<FormInputValidator> validators = mInputValidators.get(input);
            for (FormInputValidator validator : validators) {
                validator.validateInput(this, input);
            }
        }

        for (FormValidator validator : mGlobalValidators) {

            validator.validateInput(this);
        }
    }
}
