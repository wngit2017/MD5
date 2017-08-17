
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class test {
	private static final String ALGOGRITHM = "RSA";
    private static final String PUBLIC_KEY_PATH = "public.key";
    private static final String PRIVATE_KEY_PATH = "private.key";
    public static void testGenerate() throws Exception {
        //KeyPairGenerator引擎类用于产生密钥对，JDK(7)默认支持的算法有，DiffieHellman、DSA、RSA、EC
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGOGRITHM);
        //产生密钥对
        KeyPair keyPair = generator.generateKeyPair();
        //获取公钥
        PublicKey publicKey = keyPair.getPublic();
        //获取私钥
        PrivateKey privateKey = keyPair.getPrivate();
        writeKey(PUBLIC_KEY_PATH, publicKey);
        writeKey(PRIVATE_KEY_PATH, privateKey);
        
    }
    
    public static void writeKey(String path, Key key) throws Exception {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
        oos.close();
    }
     
    public static Key readKey(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream bis = new ObjectInputStream(fis);
        Object object = bis.readObject();
        bis.close();
        return (Key) object;
    }
    
    
public static void main(String[] args) throws Exception {
	//testGenerate();
		Cipher cipher = Cipher.getInstance(ALGOGRITHM);
	    //读取公钥，进行加密
	    PublicKey publicKey = (PublicKey) readKey(PUBLIC_KEY_PATH);
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	    //加密
	    String sendInfo = "0,bwdz1303,20160923101616100,66.00,2016-09-23";
	    byte[] results = cipher.doFinal(sendInfo.getBytes());
	    //进行base64加密
	     String dd = new String(new BASE64Encoder().encode(results));
	     System.out.println(dd);
	     
//	     dd="Jl5/HTr7GltWbmD6rHFHzTU1P5h0TqADnPjlCQcffaaNH/9r2ATGTwcOPXP90+oeMhG74nrQ2jPNSwG4sT3J1GCit2v7xJOzh/M/6WZPAJxyaX2Uiv7a+nDou7b4H5ueai1HmU7CRSGNCIKg8aYJ7u8XAqZleBah2pJWALUkgx4=";
	     byte[] sd = Base64.decode(dd);
	    //读取私钥，进行解密
	    PrivateKey privateKey = (PrivateKey) readKey(PRIVATE_KEY_PATH);
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    //解密
	    byte[] deciphered = cipher.doFinal(sd);
	    //得到明文
	    String recvInfo = new String(deciphered);
	    System.out.println(recvInfo);
}
}
