package h8c760d09c742.lib.bencode.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import h8c760d09c742.lib.bencode.BencodeDictionary;
import h8c760d09c742.lib.bencode.BencodeException;
import h8c760d09c742.lib.bencode.BencodeList;
import h8c760d09c742.lib.bencode.BencodeObject;
import h8c760d09c742.lib.bencode.BencodeReader;
import h8c760d09c742.lib.bencode.io.BencodeArrayWriter;
import h8c760d09c742.lib.bencode.io.BencodeBufferReader;

@ExtendWith(TestBencode.TestMethodPrinter.class)
public class TestBencode {

	public static class TestMethodPrinter implements BeforeEachCallback {
		@Override
		public void beforeEach(ExtensionContext ext) throws Exception {
			ext.getTestMethod().ifPresent(m -> System.err.println("----- " + m.getName() + " -----"));
		}
	}
	
	@Test
	public void readJunk() throws IOException {
		BencodeReader rd = new BencodeBufferReader(ByteBuffer.wrap(new byte[] { 'd', 0x7f }));
		try {
			rd.read();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(e instanceof BencodeException);
		}
	}
	
	@Test
	public void readDictBadKey() {
		BencodeReader rd = new BencodeBufferReader(ByteBuffer.wrap("dddi1e".getBytes()));
		try {
			rd.read();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(e instanceof BencodeException);
		}
	}
	
	@Test
	public void readDictNoVal() {
		BencodeReader rd = new BencodeBufferReader(ByteBuffer.wrap("d1:a".getBytes()));
		try {
			rd.read();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(e instanceof BencodeException);
		}
	}
	
	@Test
	public void readDictNoEnd() {
		BencodeReader rd = new BencodeBufferReader(ByteBuffer.wrap("d1:ai1e".getBytes()));
		try {
			rd.read();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(e instanceof BencodeException);
		}
	}
	
	@Test
	public void testSerialize() throws IOException {
		byte[] out;
		
		try (BencodeArrayWriter wr = new BencodeArrayWriter()) {
			wr.write(new BencodeDictionary()
				.put("title", "Hello World!")
				.put("number", 56789)
				.put("entries", new BencodeList()
					.add("Entry 1")
					.add(1234)
					.add(new BencodeDictionary()
						.put("x", 100)
						.put("y", 200)
					)
					.add("Entry 4")
				)
			);
			out = wr.toByteArray();
		}
		System.out.println(new String(out));
		
		BencodeReader rd = new BencodeBufferReader(ByteBuffer.wrap(out));
		BencodeObject doc = rd.read();
		System.out.println(doc.get("title").getString());
		System.out.println(doc.get("number").getValue());
		System.out.println(doc.get("entries").get(2).get("x").getValue());
		System.out.println(doc.toString(true, rd));
	}
	
}
