package model;

import java.io.Serializable;
import java.util.List;


/**
 * Representation of a function call to be use in stub.
 *
 * @param functionNumber The number assigned to the function in order for the server to tell but function is being called.
 * @param arguments      The function arguments.
 */
public record StubRequestMessage(int functionNumber,
                                 List<Serializable> arguments) implements Serializable {

}
