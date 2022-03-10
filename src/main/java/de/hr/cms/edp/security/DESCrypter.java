package de.hr.cms.edp.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.jboss.logging.Logger;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

/**
 * Generiert bzw. entschlüsselt einen mit DES symmetrisch verschlüsselten
 * String, für einfache Sicherheitsansprüche.<br>
 * Generieren: Aufruf als Standalone-Klasse mit dem zu verschlüsselnden String
 * als Argument
 * 
 * @author hwrage
 *
 */
public class DESCrypter {

	protected final Logger logger = Logger.getLogger(DESCrypter.class);

	private static Cipher ecipher;
	private static Cipher dcipher;
	private static SecretKey key;
	private static File keyFile;
	private static final String DES = "DES";
	private static final String INIT_KEYFILE_PATH = System
			.getProperty("java.io.tmpdir") + File.separator + "edpdes.key";

	/**
	 * 
	 * @param keyFile
	 */
	public DESCrypter(File keyFile) {

		DESCrypter.keyFile = keyFile;
	}

	/**
	 * initialisiert anhand der übergebenen Datei, die den per "main" erzeugten
	 * DES-Key enthalten muss
	 */
	private void init() {

		try {
			FileInputStream fis = new FileInputStream(keyFile);
			byte[] encodedKey = new byte[(int) keyFile.length()];
			fis.read(encodedKey);
			fis.close();

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			DESKeySpec spec = new DESKeySpec(encodedKey);
			key = keyFactory.generateSecret(spec);
		} catch (Exception e) {
			logger.error("Fehler beim Lesen des DES-Keys: " + e);
		}

		try {
			ecipher = Cipher.getInstance(DES);
			dcipher = Cipher.getInstance(DES);
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception e) {
			logger.error("Fehler beim Initialisieren der Verschluesselung: "
					+ e);
		}
	}

	private static void generateKey() {

		File keyfile = new File(INIT_KEYFILE_PATH);
		try {
			key = KeyGenerator.getInstance(DES).generateKey();
			FileOutputStream fos = new FileOutputStream(keyfile);
			fos.write(key.getEncoded());
			fos.close();
		} catch (Exception e) {
			System.out.println("Fehler beim Erzeugen/Speichern des Keys: " + e);
		}
	}

	private String encrypt(String str) {

		init();
		try {
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8); // verschlüsseln
			enc = BASE64EncoderStream.encode(enc);
			return new String(enc);
		} catch (Exception e) {
			logger.error("Fehler beim Verschluesseln: " + e);
		}
		return null;
	}

	public String decrypt(String str) {

		init();
		try {
			byte[] dec = BASE64DecoderStream.decode(str.getBytes());
			byte[] utf8 = dcipher.doFinal(dec); // entschlüsseln
			return new String(utf8, "UTF8");
		} catch (Exception e) {
			logger.error("Fehler beim Entschluesseln: " + e);
		}
		return null;
	}

	/**
	 * wird zur initialen Keygenerierung verwendet.
	 * 
	 * @param args
	 *            Passwort
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out
					.println("Bitte den zu verschluesselnden String als Argument angeben!");
			System.exit(1);
		}

		File newKeyFile = new File(INIT_KEYFILE_PATH);
		DESCrypter dc = new DESCrypter(newKeyFile);
		if (!newKeyFile.exists()) {
			System.out.println("Speichere Key nach " + INIT_KEYFILE_PATH
					+ ". Bitte nach <jboss.server.config.dir> legen.");
			generateKey();
		} else {
			System.out
					.println("Im Zielverzeichnis existiert schon ein gleichnamiger Key: "
							+ INIT_KEYFILE_PATH
							+ "\nBitte diesen erst entfernen!");
			System.exit(1);
		}

		String result = dc.encrypt(args[0]);
		System.out.println("DES-verschluesseltes Resultat (bitte in JVM-Config bereitstellen): "
				+ result);
		// System.out.println("wieder entschluesselt: " + dc.decrypt(result));
	}
}