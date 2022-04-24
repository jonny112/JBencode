package h8c760d09c742.lib.bencode;

/**
 * Numeric value in Bencoding.
 */
public class BencodeInteger extends BencodeObject {

	private final long value;
	
	/**
	 * Create a Bencode object wrapping a long-integer value.
	 * @param value The 64-bit signed integer ({@link Long}) to be wrapped.
	 */
	public BencodeInteger(long value) {
		this.value = value;
	}

	@Override
	public boolean isInteger() {
		return true;
	}

	@Override
	public long getValue() {
		return value;
	}

	@Override
	void accept(BencodeVisitor visitor) throws BencodeException {
		visitor.visit(this);
	}

}
