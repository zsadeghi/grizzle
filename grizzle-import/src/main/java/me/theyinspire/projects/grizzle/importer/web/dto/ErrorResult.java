package me.theyinspire.projects.grizzle.importer.web.dto;

import java.util.Objects;

public class ErrorResult {

    private Object target;
    private Throwable error;

    public Object getTarget() {
        return target;
    }

    public void setTarget(final Object target) {
        this.target = target;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(final Throwable error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "target=" + target +
                ", error=" + error +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ErrorResult that = (ErrorResult) o;
        return Objects.equals(target, that.target) &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {

        return Objects.hash(target, error);
    }

}
