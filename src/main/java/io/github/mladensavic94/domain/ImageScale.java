package io.github.mladensavic94.domain;

public enum ImageScale {

    SMALL(200),
    MEDIUM(480),
    LARGE(800),
    CUSTOM();

    private int size;

    ImageScale(int size) {
        this.size = size;
    }

    ImageScale() {
    }

    public int getSize() {
        return size;
    }
}
