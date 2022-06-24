# JBencode
Java library for reading and writing data in Bencode format, as used in BitTorrent metadata files.
Original specification can be found at http://bittorrent.org/beps/bep_0003.html under "bencoding".

This implementation aims to provide 
- full serialization and deserialization support for all format features
- a clean and efficient API
- strict parsing with transparent error reporting to aid debugging

## Overview
**BencodeObject**
- super-class for all format specific types (see model below)
- provides utility functions for accessing and manipulating parts of a document without casting

Model classes
- **BencodeDictionary** (string-key -> value/object map)
- **BencodeList** (value/object array)
- **BencodeInteger** (numeric value)
- **BencodeString** (byte array, possibly characters)

Buffer I/O
- **BencodeArrayWriter**
- **BencodeBufferReader**
- **BencodeBufferWriter**

Stream I/O
- **BencodeStreamReader**
- **BencodeStreamWriter**

Utilities
- **BencodeStringifier**

See JavaDoc for full documentation.

## Examples
#### Serialization
```java
byte[] output;	
try (BencodeArrayWriter writer = new BencodeArrayWriter()) {
	writer.write(new BencodeDictionary()
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
	output = writer.toByteArray();
}
```
#### Deserialization
```java
BencodeReader reader = new BencodeBufferReader(
	ByteBuffer.wrap(
		"d7:entriesl7:Entry 1i1234ed1:xi100e1:yi200ee7:Entry 4e6:numberi56789e5:title12:Hello World!e".getBytes()
	)
);
BencodeObject document = reader.read();
System.out.println(document.get("title").getString());
System.out.println(document.get("number").getValue());
System.out.println(document.get("entries").get(2).get("x").getValue());
```
```
Hello World!
56789
100
```
#### Debugging
```java
System.out.println(document.toString());
```
```
Dictionary(3)
  [entries]: List(4)
    String(7)
    Integer(1234)
    Dictionary(2)
      [x]: Integer(100)
      [y]: Integer(200)
    String(7)
  [number]: Integer(56789)
  [title]: String(12)
```
## Package
In lack of a specific domain the top-level package name for this project is derived the following way (Python):
```python
"h" + hashlib.sha1("jonny112@github.com".encode()).hexdigest()[-12:]
```
