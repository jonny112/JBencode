package h8c760d09c742.lib.bencode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Abstraction of a Bencode entity.
 * <br>Which may be:
 * <br> - a byte string (byte array)
 * <br> - a long integer value
 * <br> - a list of other Bencode objects
 * <br> - a dictionary mapping strings to Bencode objects
 */
public abstract class BencodeObject implements Iterable<BencodeObject> {

	BencodeObject() { }
	
	abstract void accept(BencodeVisitor visitor) throws BencodeException;
	
	/**
	 * Determines if this represents an integer value.
	 * @return Whether this is a Bencode integer-object.
	 */
	public boolean isInteger() {
		return false;
	}
	
	/**
	 * Determines if this represents a string.
	 * A Bencode string is a byte array which may or may not be interpretable as a character string.
	 * @return Whether this is a Bencode string-object.
	 */
	public boolean isString() {
		return false;
	}
	
	/**
	 * Determines if this represents a List (of Bencode objects).
	 * @return Whether this is a Bencode list-object.
	 */
	public boolean isList() {
		return false;
	}
	
	/**
	 * Determines if this represents a Dictionary (map of strings to Bencode objects).
	 * @return Whether this is a Bencode dictionary-object.
	 */
	public boolean isDictionary() {
		return false;
	}
	
	/**
	 * Returns the integer value of an integer-object.
	 * @return A 64-bit signed integer.
	 */
	public long getValue() {
		throw new IllegalStateException("Not an integer object.");
	}
	
	/**
	 * Returns the byte array of a string-object.
	 * @return A byte array.
	 */
	public byte[] getData() {
		throw new IllegalStateException("Not a string object.");
	}
	
	/**
	 * Returns the Unicode interpretation of a string-object.
	 * @return A Unicode string.
	 */
	public String getString() {
		try {
			return new String(getData(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	protected ArrayList<BencodeObject> getList() {
		throw new IllegalStateException("Not a list object.");
	}
	
	protected TreeMap<String, BencodeObject> getDictionary() {
		throw new IllegalStateException("Not a dictionary object.");
	}
	
	/**
	 * Retrieves an element at the specified position in a list-object.
	 * @param index Zero-based index of the element.
	 * @return A Bendode object.
	 */
	public BencodeObject get(int index) {
		return getList().get(index);
	}
	
	/**
	 * Sets the element at the specified position in a list-object.
	 * A null-value will remove the element at that position.
	 * @param index Zero-based index of the element.
	 * @param value A Bencode object of any kind or {@code null}.
	 * @return This Bencode object.
	 */
	public BencodeObject set(int index, BencodeObject value) {
		if (value != null) getList().set(index, value);
		else getList().remove(index);
		return this;
	}
	
	/**
	 * Adds an element after the specified position in a list-object.
	 * If a null-value is passed the list remains unchanged.
	 * @param index Zero-based index of the preceding element.
	 * @param value A Bencode object of any kind or {@code null}.
	 * @return This Bencode object.
	 */
	public BencodeObject insert(int index, BencodeObject value) {
		if (value != null) getList().add(index, value);
		return this;
	}
	
	/**
	 * Adds an element at the end of a list-object.
	 * If a null-value is passed the list remains unchanged.
	 * @param value A Bencode object of any kind or {@code null}.
	 * @return This Bencode object.
	 */
	public BencodeObject append(BencodeObject value) {
		if (value != null) getList().add(value);
		return this;
	}
	
	/**
	 * Returns the value for the specified key in a dictionary-object.
	 * If the key does not exist {@code null} is returned.
	 * @param key String to which the object is mapped.
	 * @return The corresponding Bencode object or null.
	 */
	public BencodeObject get(String key) {
		return getDictionary().get(key);
	}
	
	/**
	 * Sets the value for the specified key in a dictionary-object.
	 * Passing a null-value removes the key.
	 * @param key String to which the object is to be mapped.
	 * @param value Beencode object of any kind or {@code null}.
	 * @return This Bencode object.
	 */
	public BencodeObject set(String key, BencodeObject value) {
		if (value != null) getDictionary().put(key, value);
		else getDictionary().remove(key);
		return this;
	}
	
	/**
	 * Determines the number of elements in a list- or dictionary-object.
	 * @return The number of elements.
	 */
	public int getCount() {
		if (isList()) {
			return getList().size();
		} else if (isDictionary()) {
			return getDictionary().size();
		} else {
			throw new IllegalStateException("Neither a list nor a dictionary");
		}
	}
	
	/**
	 * Checks if the specified key exists in a dictionary-object.
	 * @param key String naming the key.
	 * @return Whether the key exists and it's value is not {@code null}.
	 */
	public boolean contains(String key) {
		return getDictionary().get(key) != null;
	}
	
	/**
	 * Iterate over the elements of a list-object.
	 */
	@Override
	public Iterator<BencodeObject> iterator() {
		return getList().iterator();
	}
	
	/**
	 * Gets the key-set of a dictionary-object.
	 * @return The set of keys in the dictionary.
	 */
	public Set<String> getKeys() {
		return getDictionary().keySet();
	}
	
	/**
	 * Gets the entry-set of a dictionary-object.
	 * @return The set of entries in the dictionary.
	 */
	public Set<Map.Entry<String, BencodeObject>> getEntries() {
		return getDictionary().entrySet();
	}
	
	@Override
	public String toString() {
		return toString(null);
	}
	
	/**
	 * @see BencodeObject#toString(String, String, boolean, BencodeIO)
	 */
	public String toString(BencodeIO io) {
		return toString("", "  ", false, io);
	}
	
	/**
	 * @see BencodeObject#toString(String, String, boolean, BencodeIO)
	 */
	public String toString(boolean dumpStrings) {
		return toString(dumpStrings, null);
	}
	
	/**
	 * @see BencodeObject#toString(String, String, boolean, BencodeIO)
	 */
	public String toString(boolean dumpStrings, BencodeIO io) {
		return toString("", "  ", dumpStrings, io);
	}
	
	/**
	 * @see BencodeObject#toString(String, String, boolean, BencodeIO)
	 */
	public String toString(String prefix, String indent, boolean dumpStrings) {
		return toString(prefix, indent, dumpStrings, null);
	}
	
	/**
	 * Generates a printable representation from a Bencode object structure recursively.
	 * @return The string representation of this Bencode object, possibly including sub-objects.
	 * @see BencodeStringifier#BencodeStringifier(String, String, boolean, BencodeIO)
	 */
	public String toString(String prefix, String indent, boolean dumpStrings, BencodeIO io) {
		return new BencodeStringifier(prefix, indent, dumpStrings, io).visit(this).toString();
	}

}
