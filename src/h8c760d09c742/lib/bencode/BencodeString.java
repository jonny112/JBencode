package h8c760d09c742.lib.bencode;

import java.nio.charset.StandardCharsets;

/**
 * String of bytes in Bencoding.
 * A String may contain UTF-8 encoded text or arbitrary data.<br>
 * The Bencode format itself does not indicate the format of the data a String contains. 
 */
public class BencodeString extends BencodeObject {

	private final byte[] data;
	
	private static byte[] stringToBytes(String str) {
		if (str == null) throw new IllegalArgumentException("String must not be null.");
		return str.getBytes(StandardCharsets.UTF_8);
	}
	
	/**
	 * Create a Bencode object representing a binary string.
	 * @param str The byte array to be wrapped.
	 */
	public BencodeString(byte[] str) {
		if (str == null) throw new IllegalArgumentException("String content cannot not be null.");
		this.data = str;
	}
	
	/**
	 * Create a Bencode string-object from a Unicode string.
	 * @param str The string to be wrapped.
	 */
	public BencodeString(String str) {
		this(stringToBytes(str));
	}
	
	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	void accept(BencodeVisitor visitor) throws BencodeException {
		visitor.visit(this);
	}

}
