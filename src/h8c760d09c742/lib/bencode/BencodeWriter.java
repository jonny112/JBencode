package h8c760d09c742.lib.bencode;

import java.io.IOException;
import java.util.Map;

/**
 * Serialization of Bencode objects.
 */
public abstract class BencodeWriter extends BencodeIO {

	protected abstract void write(byte data) throws IOException;
	protected abstract void write(byte[] data) throws IOException;
	
	private class Serializer implements BencodeVisitor {
		
		private void writeObject(BencodeObject obj) throws BencodeException {
			int posStart = getPosition();
			obj.accept(this);
			objRef.put(obj, new Range(posStart, getPosition()));
		}
		
		private void writeString(byte[] str) throws IOException {
			write((str.length + ":").getBytes());
			write(str);
		}

		@Override
		public void visit(BencodeInteger obj) throws BencodeException {
			try {
				write(("i" + obj.getValue() + "e").getBytes());
			} catch (IOException e) {
				new BencodeException(e);
			}
		}

		@Override
		public void visit(BencodeString str) throws BencodeException {
			try {
				writeString(str.getData());
			} catch (IOException e) {
				new BencodeException(e);
			}
		}

		@Override
		public void visit(BencodeList list) throws BencodeException {
			try {
				write((byte)'l');
				for (BencodeObject entry : list) writeObject(entry);
				write((byte)'e');
			} catch (IOException e) {
				new BencodeException(e);
			}
		}

		@Override
		public void visit(BencodeDictionary dict) throws BencodeException {
			try {
				write((byte)'d');
				for (Map.Entry<String, BencodeObject> entry : dict.getEntries()) {
					writeString(entry.getKey().getBytes());
					writeObject(entry.getValue());
				}
				write((byte)'e');
			} catch (IOException e) {
				new BencodeException(e);
			}
		}
		
	}
	
	private Serializer serializer = new Serializer();
	
	/**
	 * Writes the next Bencode object to the output.
	 * Lists and dictionaries are serialized recursively.
	 * @param obj Object to serialize.
	 * @return This writer object.
	 * @throws IOException If a write error occurs.
	 */
	public BencodeWriter write(BencodeObject obj) throws IOException {
		if (closed) throw new IOException("Writer is closed");
		try {
			serializer.writeObject(obj);
		} catch (BencodeException e) {
			throw (IOException)e.getCause();
		}
		return this;
	}

}
