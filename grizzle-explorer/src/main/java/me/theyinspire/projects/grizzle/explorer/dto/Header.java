package me.theyinspire.projects.grizzle.explorer.dto;

public class Header {

    private String name;
    private boolean isAssociation;

    public String getName() {
        return name;
    }

    public Header setName(final String name) {
        this.name = name;
        return this;
    }

    public boolean isAssociation() {
        return isAssociation;
    }

    public Header setAssociation(final boolean association) {
        isAssociation = association;
        return this;
    }
}
