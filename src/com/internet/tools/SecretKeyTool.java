package com.internet.tools;

import android.text.TextUtils;

public class SecretKeyTool {

	private static int[] secretKeys = new int[] { 1973, 6260, 6285, 8095, 9008,
			4502, 6463, 8434, 8203, 3529, 5996, 3129, 7504, 5472, 9048, 5901,
			2064, 4825, 3565, 5468, 3705, 5857, 3481, 9826, 6213, 6093, 2331,
			4279, 3821, 7380, 3423, 8074, 4174, 3889, 5532, 8839, 6067, 9571,
			7461, 8190, 2222, 3972, 2705, 6830, 2455, 5063, 7898, 5139, 5608,
			6678, 9665, 5806, 4771, 1459, 8008, 1299, 3396, 1728, 9384, 2045,
			1142, 4554, 4172, 5046, 4406, 7378, 3324, 2263, 3799, 7960, 8617,
			8529, 7021, 2083, 7054, 9919, 8927, 9403, 1675, 1493, 3862, 4068,
			6208, 7070, 6724, 4241, 1773, 8225, 7577, 7482, 8105, 1010, 2057,
			7978, 2572, 2004, 5267, 8034, 2597, 9165

	};

	public static boolean isSecretKeyIn(String secretKey) {
		if (!TextUtils.isEmpty(secretKey)) {
			for (int item : secretKeys) {
				if (TextUtils.equals(secretKey, item + "")) {
					return true;
				}
			}
		}
		return false;
	}

	public static void dddd() {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 100; i++) {
			int aaa = (int) (Math.random() * 9000 + 1000);
			sb.append(aaa).append(",");

		}
		System.out.println(sb.toString());

	}
}
