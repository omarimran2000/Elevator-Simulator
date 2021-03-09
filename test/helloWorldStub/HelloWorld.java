package helloWorldStub;

import java.io.Serializable;
import java.util.Objects;

public class HelloWorld implements Serializable {
    private final String string;

    public HelloWorld(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HelloWorld that = (HelloWorld) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}