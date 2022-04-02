package com.lagou.edu.common.serizlizer;

import java.io.IOException;
import java.io.Serializable;

/**
 * 比特序列化接口
 * @Author cobra
 * @Date 2014-10-29 下午4:08:39
 */
public interface Serializer {
	
	public <T extends Serializable> byte[] serialize(T value) throws IOException;

	public <T extends Serializable> T deserialize(byte[] value) throws IOException;
	
}
