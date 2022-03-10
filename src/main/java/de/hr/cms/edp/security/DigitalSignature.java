package de.hr.cms.edp.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DigitalSignature
{
	private final String ALGORITHM_DSA = "DSA";

	KeyPairGenerator keyGen;
	SecureRandom random;
	MessageDigest sha256;
	KeyFactory keyFactory;
	Signature dsa;

	public void init()
	{
		try
		{
			Provider provider = Security.getProvider("SUN");
			keyFactory = KeyFactory.getInstance(ALGORITHM_DSA);
			keyGen = KeyPairGenerator.getInstance(ALGORITHM_DSA, provider);
			random = SecureRandom.getInstance("SHA1PRNG", provider);
			sha256 = MessageDigest.getInstance("SHA-256");
			dsa = Signature.getInstance("SHA1withDSA", provider);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("DigitalSignature kann nicht initialisiert werden.");
		}
	}

	public KeyPair generateKeyPair() throws InvalidParameterException
	{
		keyGen.initialize(1024, random);
		KeyPair pair = keyGen.generateKeyPair();
		return pair;
	}

	public void savePrivateKey(PrivateKey privateKey, String path) throws IOException
	{
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(pkcs8EncodedKeySpec.getEncoded());
		fos.close();
	}

	public void savePublicKey(PublicKey publicKey, String path) throws IOException
	{
		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(x509EncodedKeySpec.getEncoded());
		fos.close();
	}

	/**
	 * Signiert ein Objekt mit dem definierten PrivateKey. Es wird nur der Hashwert (SHA-256) des serialisiertem Objekt
	 * signiert.
	 * 
	 * @param data
	 *            - zu signierende Daten als Liste von Objekten
	 * @param privkey
	 *            - Pfad zum privaten Schlüssel
	 * @return Die Signatur als Byte-Array
	 * @throws IOException
	 *             Fehler bei der Serialisierung der Daten, Schlüssel kann nicht geladen werden
	 * @throws GeneralSecurityException
	 *             Schlüssel fehlerhaft
	 */
	public byte[] signData(Object data, String privkeyPath) throws IOException, GeneralSecurityException
	{
		List<Object> dataList = new ArrayList<Object>();
		dataList.add(data);

		return signData(getUniqueByteArray(dataList), privkeyPath);
	}

	/**
	 * Signiert Liste von Objekten mit dem definierten PrivateKey. Es wird nur der Hashwert (SHA-256) der Daten
	 * signiert.
	 * 
	 * @param data
	 *            - zu signierende Daten als Liste von Objekten
	 * @param privkey
	 *            - Pfad zum privaten Schlüssel
	 * @return Die Signatur als Byte-Array
	 * 
	 */
	public byte[] signData(List<Object> data, String privkeyPath)
	{
		//TODO Exception werfen und beim Aufrufer behandeln 
		try
		{
			return signData(getUniqueByteArray(data), privkeyPath);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Signiert die übergebenen Daten mit dem definierten PrivateKey. Es wird nur der Hashwert (SHA-256) der Daten
	 * signiert.
	 * 
	 * @param data
	 *            - zu signierende Daten
	 * @param privkey
	 *            - Pfad zum privaten Schlüssel
	 * @return Die Signatur als Byte-Array
	 * @throws IOException
	 *             Schlüssel kann nicht geladen werden
	 * @throws GeneralSecurityException
	 *             Schlüssel fehlerhaft
	 */
	public byte[] signData(byte[] data, String privkeyPath) throws IOException, GeneralSecurityException
	{
		return signData(data, loadPrivateKey(privkeyPath));
	}

	/**
	 * Signiert die übergebenen Daten mit dem definierten PrivateKey. Es wird nur der Hashwert (SHA-256) der Daten
	 * signiert.
	 * 
	 * @param data
	 *            - zu signierende Daten
	 * @param privkey
	 *            - privater Schlüssel
	 * @return Die Signatur als Byte-Array
	 * @throws GeneralSecurityException
	 *             Schlüssel fehlerhaft
	 */
	public byte[] signData(byte[] data, PrivateKey privkey) throws GeneralSecurityException
	{
		if (data == null || privkey == null)
		{
			throw new NullPointerException();
		}

		dsa.initSign(privkey);
		dsa.update(getSHA256Hash(data).getBytes());
		return dsa.sign();
	}

	/**
	 * Die signierten Daten (nur der Hashwert der Daten) werden mittels Signatur und dem öffentlichen Schlüssel
	 * verifiziert.
	 * 
	 * @param obj
	 *            - Objekt, das zur Verifizierung herangezogen werden
	 * @param signature
	 * @param pubKeyPath
	 *            - Pfad zum PublicKey
	 * @return <code>true</code> bei güliger Signatur
	 * @throws IOException
	 *             Fehler beim Lesen des Schlüssels
	 * @throws GeneralSecurityException
	 *             Fehlerhafter Schlüssel
	 */
	public boolean verifySignature(Object obj, byte[] signature, String pubKeyPath) throws IOException,
	        GeneralSecurityException
	{
		List<Object> dataList = new ArrayList<Object>();
		dataList.add(obj);

		return verifySignature(getUniqueByteArray(dataList), signature, loadPublicKey(pubKeyPath));
	}

	/**
	 * Die signierten Daten (nur der Hashwert der Daten) werden mittels Signatur und dem öffentlichen Schlüssel
	 * verifiziert.
	 * 
	 * @param dataList
	 *            - Liste von Objekten die zur Verifizierung herangezogen werden
	 * @param signature
	 * @param pubKeyPath
	 *            - Pfad zum PublicKey
	 * @return <code>true</code> bei güliger Signatur
	 */
	public boolean verifySignature(List<Object> dataList, byte[] signature, String pubKeyPath)
	{
		//TODO Exception werfen und beim Aufrufer behandeln 
		try
		{
			return verifySignature(getUniqueByteArray(dataList), signature, loadPublicKey(pubKeyPath));
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Die signierten Daten (nur der Hashwert der Daten) werden mittels Signatur und dem öffentlichen Schlüssel
	 * verifiziert.
	 * 
	 * @param data
	 *            - Daten
	 * @param signature
	 *            - Signatur
	 * @param pubKey
	 *            - öffentlicher Schlüssel
	 * @return <code>true</code> bei güliger Signatur
	 * @throws GeneralSecurityException
	 *             Fehlerhafter Schlüssel
	 */
	public boolean verifySignature(byte[] data, byte[] signature, PublicKey pubKey) throws GeneralSecurityException
	{
		if (data == null || pubKey == null || signature == null)
		{
			throw new NullPointerException();
		}

		dsa.initVerify(pubKey);
		dsa.update(getSHA256Hash(data).getBytes());
		return dsa.verify(signature);
	}

	/**
	 * Öffentlichen Schlüssel laden.
	 * 
	 * @param path
	 *            Pfad zum Schlussel
	 * @return Schlüsselobjekt
	 * @throws IOException
	 *             Fehler beim Lesen
	 * @throws InvalidKeySpecException
	 *             Ungültiger Schlüssel
	 */
	PublicKey loadPublicKey(String path) throws IOException, InvalidKeySpecException
	{
		// Read Public Key.
		File filePublicKey = new File(path);
		FileInputStream fis = new FileInputStream(filePublicKey);
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}

	/**
	 * Privaten Schlüssel laden.
	 * 
	 * @param path
	 *            Pfad zum Schlussel
	 * @return Schlüsselobjekt
	 * @throws IOException
	 *             Fehler beim Lesen
	 * @throws InvalidKeySpecException
	 *             Ungültiger Schlüssel
	 */
	PrivateKey loadPrivateKey(String path) throws IOException, InvalidKeySpecException
	{
		// Read Private Key.
		File filePrivateKey = new File(path);
		FileInputStream fis = new FileInputStream(filePrivateKey);
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return privateKey;
	}

	/**
	 * Für jedes Objekt der Liste wird ein Hashwert erzeugt und in einer Liste gespeichert. Der Hashwert ist die
	 * String-Repräsentation einer Hexadezimalen Zahl. Die mit Hashwerten gefüllte Liste wird anschließend alphabetisch
	 * sortiert. Die sortierten Hashwerte werden zum Schluss zu einem String zusammengebaut und als byte[]
	 * zurückgeliefert.
	 * 
	 * @param list
	 *            Liste der Objekte
	 * @return Hashwert
	 * @throws IOException
	 *             Fehler während der Serialisierung der Objekte
	 */
	public byte[] getUniqueByteArray(List<Object> list) throws IOException
	{
		List<String> hashList = new ArrayList<String>();

		//Hash für jeden Eintrag erstellen
		for (Object obj : list)
		{
			byte[] bytes = toByteArray(obj);
			String hashEntry = getSHA256Hash(bytes);
			hashList.add(hashEntry);
		}

		//Liste von Hash-Werten sortieren
		Collections.sort(hashList, new Comparator<String>()
		{
			@Override
			public int compare(String s1, String s2)
			{
				return s1.compareTo(s2);
			}
		});

		//Alle Hash-Werte verketten
		StringBuilder sb = new StringBuilder();
		for (String s : hashList)
		{
			sb.append(s);
		}

		return sb.toString().getBytes();
	}

	/**
	 * Serialisiert ein Java Objekt und schreibt es in ein Byte-Array.
	 * 
	 * @param obj
	 *            das zu serialisierende Objekt
	 * @return Byte-Darstellung edes Objekts
	 * @throws IOException
	 *             Fehler während der Serialisierung
	 */
	byte[] toByteArray(Object obj) throws IOException
	{
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try
		{
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		}
		finally
		{
			if (oos != null)
			{
				oos.close();
			}
			if (bos != null)
			{
				bos.close();
			}
		}
		return bytes;
	}

	/**
	 * Erzeugt einen Hash-Wert für das übergebene Byte-Array. Der Hash-Wert ist ein String, der eine hexadezimale Zahl
	 * repräsentiert,
	 * 
	 * @param input
	 *            Byte-Array, über das der Hash-Wert gebildet werden soll
	 * @return Hexadezimalzahl als String
	 */
	String getSHA256Hash(byte[] input)
	{
		sha256.reset();
		sha256.update(input);
		byte[] digest = sha256.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < digest.length; i++)
		{
			hexString.append(Integer.toHexString(0xFF & digest[i]));
		}
		return hexString.toString();
	}
}
