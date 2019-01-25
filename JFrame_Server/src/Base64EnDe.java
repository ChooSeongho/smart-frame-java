import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64EnDe {
	public static String fileToString(File file) {
		String fileString = new String();
		Encoder encoder = Base64.getEncoder();
		FileInputStream inputStream = null;
		ByteArrayOutputStream byteOutStream = null;

		try {
			inputStream = new FileInputStream(file);
			byteOutStream = new ByteArrayOutputStream();

			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf)) != -1) {
				byteOutStream.write(buf, 0, len);
			}

			byte[] fileArray = byteOutStream.toByteArray();
			fileString = encoder.encodeToString(fileArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				byteOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileString;
	}

	public static String bytesToString(byte[] bytes) {
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(bytes);
	}

	public static byte[] stringToBytes(String str) {
		Decoder decoder = Base64.getDecoder();
		byte[] decodedString = decoder.decode(str);
		return decodedString;
	}
}
