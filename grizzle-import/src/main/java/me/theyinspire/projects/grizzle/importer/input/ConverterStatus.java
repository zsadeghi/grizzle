package me.theyinspire.projects.grizzle.importer.input;

public enum ConverterStatus {

    PREPARING(true),
    CONVERTING_ARTISTS(true),
    CONVERTING_TRACKS(true),
    CONVERTING_LYRICS(true),
    IDLE(false),
    DONE(false),
    ERROR(false);

    private final boolean working;

    ConverterStatus(final boolean working) {
        this.working = working;
    }

    public boolean isWorking() {
        return working;
    }
}
