package me.theyinspire.projects.grizzle.importer.input.reader;

public class Record<E> {

    private final E data;
    private final long recordNumber;

    public Record(final long recordNumber, final E data) {
        this.data = data;
        this.recordNumber = recordNumber;
    }

    public E getData() {
        return data;
    }

    public long getRecordNumber() {
        return recordNumber;
    }

    @Override
    public String toString() {
        return "#" + recordNumber + ": " + data;
    }

}
