package com.softwarewithpassion.nrgyinvoicr.backend.plans.control;

public class InvalidExpressionException extends Exception {

    public InvalidExpressionException() {
        super("Provided expression is invalid.");
    }
}
