package me.theyinspire.projects.grizzle.importer.web.dto;

import java.util.Objects;

public class RunRequest {

    private Boolean rerun;

    public Boolean getRerun() {
        return rerun;
    }

    public void setRerun(final Boolean rerun) {
        this.rerun = rerun;
    }

    @Override
    public String toString() {
        return "RunRequest{" +
                "rerun=" + rerun +
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
        final RunRequest that = (RunRequest) o;
        return Objects.equals(rerun, that.rerun);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rerun);
    }

}
