package h8c760d09c742.lib.bencode;

import java.io.EOFException;
import java.io.IOException;

/**
 * Deserialization of Bencode objects.
 */
public abstract class BencodeReader extends BencodeIO {
	
	protected abstract byte readByte() throws IOException;
	protected abstract void read(byte[] data) throws IOException;
	
	private boolean atEOF = false;
	private final BencodeObject ending = new BencodeTerminator();
	
	/**
	 * Reads the next Bencode object from the input.
	 * In case of a list or dictionary, sub-elements are deserialized recursively.<br>
	 * Usually a Bencoded document has a dictionary as the top level element.
	 * For a file/blob containing a single document only one call to this function will be necessary to read all of it.
	 * @return Deserialized object (including its sub-structure) or {@code null} if the end of input has been reached.
	 * @throws BencodeException In case of a parsing error or read failure. Numbers prefixed with '@' indicate byte offsets in the input.
	 * @throws EOFException If this function is called again after a previous call returned {@code null}.
	 * @throws IOException If the reader has already been closed.
	 */
	public BencodeObject read() throws IOException {
		if (closed) throw new IOException("Reader is closed");
		
		BencodeObject o;
		int posStart = getPosition();
		
		try {
			char start = (char)readByte();
			if (start == 'i') o = new BencodeInteger(readNumber(new StringBuilder(), true));
			else if (start == 'l') o = readList(new BencodeList());
			else if (start == 'd') o = readDictionary(new BencodeDictionary());
			else if (start == 'e') o = ending;
			else if (start >= 0x30 && start < 0x3A) o = new BencodeString(readString(start));
			else throw new IOException(String.format("Unknown start of object 0x%02X", start & 0xFF));
		} catch (BencodeException e) {
			throw new BencodeException(e.getMessage() + " part of object @" + posStart, e.getCause());
		} catch (EOFException e) {
			if (atEOF) throw e;
			atEOF = true;
			return null;
		} catch (IOException e) {
			throw new BencodeException("Parsing error @" + getPosition() + " in object starting @" + posStart, e);
		}
		
		objRef.put(o, new Range(posStart, getPosition()));
		return o;
	}
	
	private BencodeList readList(BencodeList list) throws IOException {
		BencodeObject entry;
		do {
			entry = read();
			if (entry == null) throw new IOException("Unexpected end of list");
			if (entry != ending) list.append(entry);
		} while (entry != ending);
		return list;
	}
	
	private BencodeDictionary readDictionary(BencodeDictionary dict) throws IOException {
		BencodeObject key, value;
		do {
			key = read();
			if (key == null) throw new IOException("Unexpected end of dictionary");
			if (!(key.isString() || key == ending)) throw new IOException("Illegal dictionary key");
			if (key != ending) {
				value = read();
				if (value == null) throw new IOException("End of input where dictionary value was expected");
				if (value == ending) throw new IOException("Missing value in dictionary");
				dict.set(key.getString(), value);
			}
		} while (key != ending);
		return dict;
	}
	
	private byte[] readString(char start) throws IOException {
		long strLen = (int)readNumber(new StringBuilder().append(start), false);
		if (strLen > Integer.MAX_VALUE) throw new IOException("Maximum string size exceeded");
		byte[] data = new byte[(int)strLen];
		read(data);
		return data;
	}
	
	private long readNumber(StringBuilder strVal, boolean expectEnd) throws IOException {
		byte chr;
		boolean more;
		do {
			more = false;
			try {
				chr = readByte();
			} catch (EOFException e) {
				throw new IOException("Unexpected end of number");
			}
			if (chr >= 0x30 && chr < 0x3A || chr == 0x2D) {
				strVal.append((char)chr);
				more = true;
			} else if (!(chr == ':' && !expectEnd || chr == 'e' && expectEnd)) {
				throw new IOException(String.format("Unexpected character 0x%02X in %s", chr & 0xFF, (expectEnd ? "value" : "length")));
			}
		} while (more);
		return Long.parseLong(strVal.toString());
	}

}
