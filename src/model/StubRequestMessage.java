package model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class StubRequestMessage implements Serializable {
    private final int functionNumber;
    private final List<Serializable> arguments;

    public StubRequestMessage(int functionNumber, List<Serializable> arguments) {
        this.functionNumber = functionNumber;
        this.arguments = arguments;
    }


    public int getFunctionNumber() {
        return functionNumber;
    }

    public List<Serializable> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "StubRequestMessage{" +
                "functionNumber=" + functionNumber +
                ", arguments=" + arguments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StubRequestMessage that = (StubRequestMessage) o;
        return functionNumber == that.functionNumber && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionNumber, arguments);
    }
}
