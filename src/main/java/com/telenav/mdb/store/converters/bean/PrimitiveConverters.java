package com.telenav.mdb.store.converters.bean;

import java.io.IOException;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.telenav.mdb.store.converters.FieldConverter;

public class PrimitiveConverters {
	
	public static class CharConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeBytes(fieldNumber,
					ByteString.copyFromUtf8("" + (Character) value));
		}

		public Object read(CodedInputStream input) throws IOException {
			ByteString bs = input.readBytes();
			return bs.toStringUtf8().charAt(0);
		}
	}

	public static class StringConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeBytes(fieldNumber,
					ByteString.copyFromUtf8((String) value));
		}

		public Object read(CodedInputStream input) throws IOException {
			ByteString bs = input.readBytes();
			return bs.toStringUtf8();
		}
	}

	public static class ByteConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeBytes(fieldNumber,
					ByteString.copyFrom(new byte[] { (Byte) value }));
		}

		public Object read(CodedInputStream input) throws IOException {
			ByteString bs = input.readBytes();

			return bs.toByteArray()[0];
		}
	}

	public static class BytesConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeBytes(fieldNumber, ByteString.copyFrom((byte[]) value));
		}

		public Object read(CodedInputStream input) throws IOException {
			ByteString bs = input.readBytes();

			return bs.toByteArray();
		}
	}

	public static class BoolConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeBool(fieldNumber, (Boolean) value);
		}

		public Object read(CodedInputStream input) throws IOException {
			return input.readBool();
		}
	}

	public static class IntConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeInt32(fieldNumber, (Integer) value);
		}

		public Object read(CodedInputStream input) throws IOException {
			return input.readInt32();
		}
	}

	public static class LongConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeInt64(fieldNumber, (Long) value);
		}

		public Object read(CodedInputStream input) throws IOException {
			return input.readInt64();
		}
	}

	public static class FloatConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeFloat(fieldNumber, (Float) value);
		}

		public Object read(CodedInputStream input) throws IOException {
			return input.readFloat();
		}
	}

	public static class DoubleConverter implements FieldConverter {
		public void write(int fieldNumber, Object value,
				CodedOutputStream output) throws IOException {
			output.writeDouble(fieldNumber, (Double) value);
		}

		public Object read(CodedInputStream input) throws IOException {
			return input.readDouble();
		}
	}

}
