
package ch.epfl.alpano;

/**
 * Provides a check for illegal arguments
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface Preconditions {
	/**
	 * checks if the argument is valid
	 * 
	 * @param b
	 * @throws IllegalArgumentException
	 */
	public static void checkArgument(boolean b) {
		if (!b)
			throw new IllegalArgumentException();
	}

	/**
	 * checks if the argument is valid
	 * 
	 * @param b
	 * @param message
	 * @throws IllegalArgumentException
	 */
	public static void checkArgument(boolean b, String message) {
		if (!b)
			throw new IllegalArgumentException(message);
	}
}
