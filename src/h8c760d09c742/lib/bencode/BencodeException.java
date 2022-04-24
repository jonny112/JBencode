package h8c760d09c742.lib.bencode;

import java.io.IOException;

/**
 * Indication of Bencode processing errors.
 */
public class BencodeException extends IOException {

	private static final long serialVersionUID = 1L;

	public BencodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BencodeException(String message) {
		super(message);
	}

	public BencodeException(Throwable cause) {
		super(cause);
	}

}
