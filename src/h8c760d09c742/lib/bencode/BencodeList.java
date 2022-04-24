package h8c760d09c742.lib.bencode;

import java.util.ArrayList;

/**
 * List of values, dictionaries or other lists in Bencoding.
 */
public class BencodeList extends BencodeObject {

	private final ArrayList<BencodeObject> list;
	
	/**
	 * Create an empty Bencode list.
	 */
	public BencodeList() {
		this.list = new ArrayList<>();
	}
	
	@Override
	public boolean isList() {
		return true;
	}
	
	@Override
	protected ArrayList<BencodeObject> getList() {
		return list;
	}
	
	@Override
	void accept(BencodeVisitor visitor) throws BencodeException {
		visitor.visit(this);
	}
	
	/**
	 * Adds an object at the end of the list.
	 * @param obj A Bencode object of any kind, null-values are ignored.
	 * @return This list object.
	 */
	public BencodeList add(BencodeObject obj) {
		if (obj != null) list.add(obj);
		return this;
	}
	
	/**
	 * Adds a string value at the end of the list.
	 * @param value String to be wrapped in a {@link BencodeString}.
	 * @return This list object.
	 */
	public BencodeList add(String value) {
		list.add(new BencodeString(value));
		return this;
	}
	
	/**
	 * Adds an integer value at the end of the list.
	 * @param value Long-integer to be wrapped into a {@link BencodeInteger}.
	 * @return This list object.
	 */
	public BencodeList add(long value) {
		list.add(new BencodeInteger(value));
		return this;
	}

}
