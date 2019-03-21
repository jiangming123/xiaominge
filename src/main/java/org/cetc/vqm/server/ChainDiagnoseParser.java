package org.cetc.vqm.server;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.cetc.vqm.agent.util.DateTimeTool;
import org.cetc.vqm.chainstate.pojo.Distantvideojson;
import org.cetc.vqm.chainstate.pojo.Localvideojson;
import org.cetc.vqm.chainstate.pojo.Networkstatejson;

public class ChainDiagnoseParser {

	public static ChainDiagnoseMessage parse(final String strbuf) {
		ChainDiagnoseMessage cdm = null;
		byte[] data;
		try {
			data = Hex.decodeHex(strbuf.toCharArray());
			cdm = parse(data);
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return cdm;
	}
	
	public static ChainDiagnoseMessage parse(final byte[] buf) {
	final ChainDiagnoseMessage info = new ChainDiagnoseMessage();
	
		final byte[] buf_byte = buf.clone();
		
		int len = 0;
		final int lead_len = 2;
		len += lead_len;
		
		final int chainnumber = Byte.toUnsignedInt(buf_byte[len]);
		info.setChainnumber(chainnumber);
		len += 1;
		
		final int detecttype = Byte.toUnsignedInt(buf_byte[len]);
		info.setDetecttype(detecttype);
		len += 1;
		
		final Localvideojson localvideojson = new Localvideojson();
		byte[] tmp = new byte[4];
		float value;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setOverall(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setBlur(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setOverdark(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setOverbright(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setColorcast(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setLowcontrast(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setMosaic(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setBlackscreen(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setFrozen(value);
		len += 4;
		
		System.arraycopy(buf_byte, len, tmp, 0, 4);
		value = bytes2Float(tmp);
		localvideojson.setPsnr(value);
		len += 4;
		
		info.setLocalvideojson(localvideojson);
		
		final Networkstatejson networkstatejson = new Networkstatejson();
		networkstatejson.setState(0);
		networkstatejson.setFault(0);
		networkstatejson.setReason("");
		info.setNetworkstatejson(networkstatejson);
		
		len += 10;
		
		byte[] str = new byte[14];
		System.arraycopy(buf_byte, len, str, 0, 14);
		String time  = new String (str);
		
		info.setTime(time);
		
		len += 14;
		
		final byte[] filePath= new byte[52];
		System.arraycopy(buf_byte, len, filePath, 0, 52);
		String path = new String(filePath);	// 将byte数组转为String
		if(path!=null){
			info.setPath(path.trim());
		}
			/*String[] split = path.split("\\\\");
			if(split.length>0){
				pickup.setFileName(split[split.length-1]);
				
			}*/
		
		
		
		
		
		if(detecttype == 1)
		{
			float psnr = localvideojson.getPsnr();
			info.setChainstate(psnr);
		}
		else if(detecttype == 2)
		{
			float score = localvideojson.getOverall();
			info.setChainstate(score);
		}
	
	return info;
}

	/*public static ChainDiagnoseMessage parse(final byte[] buf) {
		final ChainDiagnoseMessage info = new ChainDiagnoseMessage();
		
		final byte[] buf_byte = buf.clone();
		
		int len = 0;
		final int lead_len = 2;
		len += lead_len;
		
		final int chainnumber = Byte.toUnsignedInt(buf_byte[len]);
		info.setChainnumber(chainnumber);
		len += 1;
		
		final int detecttype = Byte.toUnsignedInt(buf_byte[len]);
		info.setDetecttype(detecttype);
		len += 1;
		
		final int chainstate = Byte.toUnsignedInt(buf_byte[len]);
		info.setChainstate(chainstate);
		len += 1;
		
		final Distantvideojson distantvideojson = new Distantvideojson();
		
		final int mosaic = Byte.toUnsignedInt(buf_byte[len]);
		distantvideojson.setMosaic(mosaic);
		len += 1;
		
		final int blackscreen = Byte.toUnsignedInt(buf_byte[len]);
		distantvideojson.setBlackscreen(blackscreen);
		len += 1;
		
		final int frozen = Byte.toUnsignedInt(buf_byte[len]);
		distantvideojson.setFrozen(frozen);
		len += 1;
		
		info.setDistantvideojson(distantvideojson);
		
		final Networkstatejson networkstatejson = new Networkstatejson();
		networkstatejson.setState(0);
		networkstatejson.setFault(0);
		networkstatejson.setReason("");
		
		info.setNetworkstatejson(networkstatejson);
		
		len += 10;
		
		final Localvideojson localvideojson = new Localvideojson();
		
		final int blur = Byte.toUnsignedInt(buf_byte[len]);
		localvideojson.setBlur(blur);
		len += 1;
		
		final int overdark = Byte.toUnsignedInt(buf_byte[len]);
		localvideojson.setOverdark(overdark);
		len += 1;
		
		final int overbright = Byte.toUnsignedInt(buf_byte[len]);
		localvideojson.setOverbright(overbright);
		len += 1;
		
		final int colorcast = Byte.toUnsignedInt(buf_byte[len]);
		localvideojson.setColorcast(colorcast);
		len += 1;
		
		final int lowcontrast = Byte.toUnsignedInt(buf_byte[len]);
		localvideojson.setLowcontrast(lowcontrast);
		len += 1;
		
		
		final int psnr = Byte.toUnsignedInt(buf_byte[len]);
		localvideojson.setPsnr(psnr);
		len += 1;
		
		//byte[] tmp = new byte[8];
		//System.arraycopy(buf_byte, len, tmp, 0, 8);
		//double psnr = bytes2Double(tmp);
		//localvideojson.setPsnr(psnr);
		//len += 8;
		
		info.setLocalvideojson(localvideojson);
		
		byte[] str = new byte[14];
		System.arraycopy(buf_byte, len, str, 0, 14);
		String time  = new String (str);
		
		info.setTime(time);
		
		return info;
	}*/
	
	public static double bytes2Double(byte[] arr) {
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value |= ((long) (arr[i] & 0xff)) << (8 * i);
		}
		return Double.longBitsToDouble(value);
	}
	
	public static float bytes2Float(byte[] arr) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			value |= ((long) (arr[i] & 0xff)) << (8 * i);
		}
		return Float.intBitsToFloat(value);
	}
	

}
