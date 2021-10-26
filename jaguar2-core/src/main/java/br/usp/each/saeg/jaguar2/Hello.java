package br.usp.each.saeg.jaguar2;

public class Hello {

    private final String name;

    public Hello(final String name) {
        this.name = name;
    }

    public String greetings() {
        return String.format("Hello, %s", name);
    }

}
