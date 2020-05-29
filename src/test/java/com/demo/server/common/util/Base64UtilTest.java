package com.demo.server.common.util;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Base64UtilTest {

	String plainStrForCheck = " {\"exp\":1590656498,\"userId\":\"10029291d承担\",\"iat\":1590656496}";
	String base64StrForCheck = "IHsiZXhwIjoxNTkwNjU2NDk4LCJ1c2VySWQiOiIxMDAyOTI5MWTmib/mi4UiLCJpYXQiOjE1OTA2NTY0OTZ9";
	String base64UrlStrForCheck = "IHsiZXhwIjoxNTkwNjU2NDk4LCJ1c2VySWQiOiIxMDAyOTI5MWTmib_mi4UiLCJpYXQiOjE1OTA2NTY0OTZ9";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBase64Encode() throws UnsupportedEncodingException {
		String base64Str = Base64Util.base64Encode(plainStrForCheck);
		assertTrue(base64Str.equals(base64StrForCheck));
	}

	@Test
	public void testBase64Decode() throws UnsupportedEncodingException {
		String plainStr = Base64Util.base64Decode(base64StrForCheck);
		assertTrue(plainStr.equals(plainStrForCheck));
	}

	@Test
	public void testBase64UrlEncode() throws UnsupportedEncodingException {
		String base64UrlStr = Base64Util.base64UrlEncode(plainStrForCheck);
		assertTrue(base64UrlStr.equals(base64UrlStrForCheck));
	}

	@Test
	public void testBase64UrlDecode() throws UnsupportedEncodingException {
		String plainStr = Base64Util.base64UrlDecode(base64UrlStrForCheck);
		assertTrue(plainStr.equals(plainStrForCheck));
	}

}
