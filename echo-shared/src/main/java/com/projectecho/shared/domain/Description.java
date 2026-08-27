package com.projectecho.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

/** Simple value object representing a textual description. */
@Embeddable
public class Description {

    @Column(nullable = false, length = 2000)
    private String text;

    protected Description() {}

    public Description(final String text) {
        this.text = Objects.requireNonNull(text, "Description text cannot be null");
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Description{" + "text='" + text + '\'' + '}';
    }

    @Override
    public boolean equals(final Object o) {
        boolean result = false;
        if (this == o) {
            result = true;
        } else if (o instanceof Description) {
            final Description that = (Description) o;
            result = Objects.equals(text, that.text);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
