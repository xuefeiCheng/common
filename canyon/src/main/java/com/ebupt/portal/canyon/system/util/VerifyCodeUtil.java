package com.ebupt.portal.canyon.system.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码生产工具类
 *
 * @author chy
 * @date 2019-03-10 13:33
 */
@Slf4j
public class VerifyCodeUtil {

	private static final String VERIFY_CODE = "23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	private static final Integer RGB_LEN = 3;
	private static final Integer MAX_RGB = 255;
	private static final Integer MAX_LINE = 20;

	/**
	 * 使用默认源生成指定长度验证码
	 *
	 * @param verifySize
	 *                      验证码长度
	 * @return
	 *                      生成的验证码
	 */
	public static String generateVerifyCode(int verifySize) {
		return generateVerifyCode(verifySize, VERIFY_CODE);
	}

	/**
	 * 使用指定源生成指定长度验证码
	 *
	 * @param verifySize
	 *                      验证码长度
	 * @param sourceCode
	 *                      源
	 * @return
	 *                      生成的验证码
	 */
	public static String generateVerifyCode(int verifySize, String sourceCode) {
		if (StringUtils.isEmpty(sourceCode)) {
			sourceCode = VERIFY_CODE;
		}

		// 生成验证码
		int sourceCodeLen = sourceCode.length();
		Random random = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(verifySize);
		for (int i = 0; i < verifySize; i++) {
			verifyCode.append(sourceCode.charAt(random.nextInt(sourceCodeLen - 1)));
		}
		return verifyCode.toString();
	}

	/**
	 * 输出指定验证码
	 *
	 * @param width
	 *                  验证码图片宽度
	 * @param height
	 *                  验证码图片高度
	 * @param os
	 *                  输出流
	 * @param verifyCode
	 *                  验证码
	 */
	public static void outputImage(int width, int height, OutputStream os, String verifyCode) {
		int verifyCodeLen = verifyCode.length();

		// 定义图像buffer
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random random = new Random();
		Graphics2D graphics2D = image.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 对指定的矩形区域填充颜色
		graphics2D.setColor(Color.GRAY);
		graphics2D.fillRect(0, 0, width, height);
		Color color = getRandomColor(200, 250);
		graphics2D.setColor(color);
		graphics2D.fillRect(0, 2, width, height - 4);

		// 绘制干扰线
		graphics2D.setColor(getRandomColor(160, 200));
		for (int i = 0; i < MAX_LINE; i++) {
			int x1 = random.nextInt(width - 1);
			int y1 = random.nextInt(height - 1);
			int x2 = random.nextInt(6) + 1;
			int y2 = random.nextInt(12) + 1;
			graphics2D.drawLine(x1, y1, x1 + x2 + 40, y1 + y2 + 20);
		}

		// 添加噪点
		float yawpRate = 0.05f;
		int area = (int) (yawpRate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}

		// 扭曲图片
		shear(graphics2D, width, height, color);

		// 设置字体
		graphics2D.setColor(getRandomColor(100, 160));
		int fontSize = height - 4;
		Font font = new Font("Algerian", Font.ITALIC, fontSize);
		graphics2D.setFont(font);

		// 设置文本
		char[] chars = verifyCode.toCharArray();
		for (int i = 0; i < verifyCodeLen; i++) {
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4.0 * random.nextDouble() * (random.nextBoolean() ? 1: -1),
					(width / verifyCodeLen) * i + fontSize / 2.0, height / 2.0);
			graphics2D.setTransform(affine);
			graphics2D.drawChars(chars, i, 1, ((width - 10) / verifyCodeLen) * i + 5,
					height / 2 + fontSize / 2 - 5);
		}

		graphics2D.dispose();
		try {
			ImageIO.write(image, "jpg", os);
		} catch (IOException e) {
			log.error("验证码输出失败: {}", e.toString());
		}
	}

	/**
	 * 扭曲图片
	 *
	 * @param graphics2D
	 *                      图像对象
	 * @param width
	 *                      宽度
	 * @param height
	 *                      高度
	 * @param color
	 *                      颜色
	 */
	private static void shear(Graphics2D graphics2D, int width, int height, Color color) {
		shearX(graphics2D, width, height, color);
		shearY(graphics2D, width, height, color);
	}

	/**
	 * 扭曲x轴
	 *
	 * @param graphics2D
	 *                      图像对象
	 * @param width
	 *                      宽度
	 * @param height
	 *                      高度
	 * @param color
	 *                      颜色
	 */
	private static void shearX(Graphics2D graphics2D, int width, int height, Color color) {
		Random random = new Random();
		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < height; i++) {
			double d = (period >> 1) * Math.sin((double) i / (double) period
					+ (6.2831853071795862D * (double) phase) / (double) frames);
			graphics2D.copyArea(0, i, width, 1, (int) d, 0);
			if (borderGap) {
				graphics2D.setColor(color);
				graphics2D.drawLine((int) d, i, 0, i);
				graphics2D.drawLine((int) d + width, i, width, i);
			}
		}
	}

	/**
	 * 扭曲y轴
	 *
	 * @param graphics2D
	 *                      图像对象
	 * @param width
	 *                      宽度
	 * @param height
	 *                      高度
	 * @param color
	 *                      颜色
	 */
	private static void shearY(Graphics2D graphics2D, int width, int height, Color color) {
		Random random = new Random();
		int period = random.nextInt(40) + 10;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;

		for (int i = 0; i < width; i++) {
			double d = (period >> 1) * Math.sin((double) i / (double) period
					+ (6.2831853071795862D * (double) phase) / (double) frames);
			graphics2D.copyArea(i, 0, 1, height, 0, (int) d);
			if (borderGap) {
				graphics2D.setColor(color);
				graphics2D.drawLine(i, (int) d, i, 0);
				graphics2D.drawLine(i, (int) d + height, i, height);
			}
		}
	}

	/**
	 * 获取随机生成的RGB颜色int数值
	 *
	 * @return
	 *          RGB颜色int数值
	 */
	private static int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c: rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	/**
	 * 生成RGB数组
	 *
	 * @return
	 *              RGB数组
	 */
	private static int[] getRandomRgb() {
		Random random = new Random();
		int[] rgb = new int[3];
		for (int i = 0; i < RGB_LEN; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	/**
	 * 生成随机颜色
	 *
	 * @param fc
	 *              fc
	 * @param bc
	 *              bc
	 * @return
	 *              颜色对象
	 */
	private static Color getRandomColor(int fc, int bc) {
		if (fc > MAX_RGB) {
			fc = 255;
		}
		if (bc > MAX_RGB) {
			bc = 255;
		}

		Random random = new Random();
		int r = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}


}
