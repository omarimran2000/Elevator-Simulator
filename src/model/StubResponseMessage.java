package model;

import java.io.Serializable;
import java.util.Objects;

public class StubResponseMessage implements Serializable {

    private final int functionNumber;
    private final Serializable output;

    public StubResponseMessage(int functionNumber, Serializable output) {
        this.functionNumber = functionNumber;
        this.output = output;
    }

    public int getFunctionNumber() {
        return functionNumber;
    }

    public Serializable getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "StubResponseMessage{" +
                "functionNumber=" + functionNumber +
                ", output=" + output +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StubResponseMessage that = (StubResponseMessage) o;
        return functionNumber == that.functionNumber && Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionNumber, output);
    }
}
