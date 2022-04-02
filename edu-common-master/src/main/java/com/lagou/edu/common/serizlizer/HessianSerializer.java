package com.lagou.edu.common.serizlizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
 * hessian序列化实现
 * @Author cobra
 * @Date 2014-10-29 下午4:09:07
 */
public class HessianSerializer implements Serializer{
	
	private static HessianSerializer serializer = new HessianSerializer();
	
	public static Serializer getInstance(){
		return serializer;
	}

	@Override
	public <T extends Serializable> byte[] serialize(T value) throws IOException {
		if(value == null) {
			throw new NullPointerException();  
		}
	    ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    HessianOutput ho = new HessianOutput(os);  
	    ho.writeObject(value);  
	    return os.toByteArray(); 
	}

	@SuppressWarnings("unchecked")
    @Override
	public <T extends Serializable> T deserialize(byte[] value) throws IOException {
		if(value == null){ 
			throw new NullPointerException();  
		}
	    ByteArrayInputStream is = new ByteArrayInputStream(value);  
	    HessianInput hi = new HessianInput(is);  
	    return (T) hi.readObject();  
	}
	
}
