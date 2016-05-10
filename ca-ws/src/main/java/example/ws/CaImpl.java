package example.ws;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.jws.WebService;

@WebService(endpointInterface = "example.ws.Ca")
public class CaImpl implements Ca {
	
	final static String BROKER = "UpaBroker";
	final static String TRANSPORTER1 = "UpaTransporter1";
	final static String TRANSPORTER2 = "UpaTransporter2";

	
	public String sayHello(String name) {
		return "Hello " + name + "!";
	}
	
	public byte[] getCertificates(String name) throws Exception {
		final String KEY_FILE = "keys/";

		Certificate certificate = null;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		
		certificate = readCertificateFile(KEY_FILE + name + "/" + name + ".cer");
		System.out.println(certificate);
		out = new ObjectOutputStream(bos);   
		out.writeObject(certificate);
		byte[] yourBytes = bos.toByteArray();
		return yourBytes;
		
	}
	
	/**
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;
			// It is possible to print the content of the certificate file:
			// System.out.println(cert.toString());
		}
		bis.close();
		fis.close();
		return null;
	}

}
