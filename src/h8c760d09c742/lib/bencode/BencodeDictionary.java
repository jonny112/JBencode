
package h8c760d09c742.lib.bencode;

import java.util.TreeMap;

/**
 * String keys mapped to values, lists or other dictionaries in Bencoding.
 */
public class BencodeDictionary extends BencodeObject {

	private final TreeMap<String, BencodeObject> dict;
	
	/**
	 * Create an empty Bencode dictionary.
	 */
	public BencodeDictionary() {
		this.dict = new TreeMap<>();
	}
	
	@Override
	public boolean isDictionary() {
		return true;
	}
	
	@Override
	protected TreeMap<String, BencodeObject> getDictionary() {
		return dict;
	}
	
	@Override
	void accept(BencodeVisitor visitor) throws BencodeException {
		visitor.visit(this);
	}
	
	/**
	 * Puts an object in the dictionary.
	 * @param key Key-string under which to store the value.
	 * @param obj Bencode object of any kind, {@code null} removes the key.
	 * @return
	 */
	public BencodeDictionary put(String key, BencodeObject obj) {
		if (obj != null) dict.put(key, obj);
		else dict.remove(key);
		return this;
	}
	
	/**
	 * Puts a string value in the dictionary.
	 * @param key Key-string under which to store the value.
	 * @param value String to be wrapped in a {@link BencodeString}.
	 * @return This dictionary object.
	 */
	public BencodeDictionary put(String key, String value) {
		dict.put(key, new BencodeString(value));
		return this;
	}
	
	/**
	 * Puts a long-integer value in the dictionary.
	 * @param key Key-string under which to store the value.
	 * @param value Long-integer to be wrapped into a {@link BencodeInteger}.
	 * @return This dictionary object.
	 */
	public BencodeDictionary put(String key, long value) {
		dict.put(key, new BencodeInteger(value));
		return this;
	}

}
