package me.theyinspire.projects.grizzle.importer.input;

public class InputConversionAbortedException extends RuntimeException {

    public InputConversionAbortedException() {
        super("Input conversion was manually aborted.");
    }
}
