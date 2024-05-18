package world.tsmk.customjukebox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Tool {
	private static final String IV = "0102030405060708";

	public static InputStream toOgg(InputStream songInputStream) {
		try {
			File tempFile = File.createTempFile("mcmodcustomjukebox", ".tmp");
			IOUtils.copy(songInputStream, new FileOutputStream(tempFile));
			File target = File.createTempFile("mcmodcustomjukebox", ".tmp");
			//Audio Attributes
			AudioAttributes audio = new AudioAttributes();

			//Encoding attributes
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setOutputFormat("ogg");
			attrs.setAudioAttributes(audio);

			//Encode
			Encoder encoder = new Encoder();
			encoder.encode(new MultimediaObject(tempFile), target, attrs);
			try {
				tempFile.delete();
			} catch (Exception ignored) {
			}
			return new FileInputStream(target);
		} catch (IOException | EncoderException e) {
			return null;
		}
	}

	public static URL getNetMusic(String id) {
		try {
			String params = getEnc("{\"ids\": \"[%s]\", \"level\": \"standard\", \"encodeType\": \"ogg\", \"csrf_token\": \"\"}".formatted(id));
			String encSecKey = "bb20ee9409e57057e4d1b55e4d77c94bff4d8cbf181c467bbd3fa156e3419665c6c1e643621d5d82c128251fb85f0cb34d4f08c88407b4148924ffa818f59a64b3814784e7e3837bad4f6f9690cb2cf721d9ea1af12c16a32a9df00be710b70ee8ed32036cc6a465b28ef43f4382cbcb4595b3121be75ecba9171876b611b8fc";
			params = URLEncoder.encode(params, StandardCharsets.UTF_8);
			encSecKey = URLEncoder.encode(encSecKey, StandardCharsets.UTF_8);
			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString("params=%s&encSecKey=%s".formatted(params, encSecKey)))
					.uri(URI.create("https://music.163.com/weapi/song/enhance/player/url/v1?csrf_token="))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
					.build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
			JsonObject data = new Gson().fromJson(response.body(), JsonObject.class).getAsJsonArray("data").get(0).getAsJsonObject();
			return new URL(data.get("url").getAsString());
		} catch (Exception e) {
			return null;
		}
	}

	public static String to16(String data) {
		int len1 = 16 - (data.length() % 16);
		char paddingChar = (char) len1;
		StringBuilder paddedData = new StringBuilder(data);
		for (int i = 0; i < len1; i++) {
			paddedData.append(paddingChar);
		}
		return paddedData.toString();
	}

	public static String encryption(String data, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

		String data1 = to16(data);
		byte[] encryptedBytes = cipher.doFinal(data1.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String getEnc(String data) throws Exception {
		String param4 = "0CoJUm6Qyw8W8jud";
		String enc = "g4PXsCuqYE6icH3R";

		String firstEncryption = encryption(data, param4);
		return encryption(firstEncryption, enc);
	}

}
