package com.ebupt.portal.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

public class VerifyCodeUtil {

    private static final String VERIFY_CODE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random random = new Random();

    /**
     * 使用默认源生成指定长度验证码
     *
     * @param verifySize
     *                      验证码长度
     * @return
     *                      指定长度验证码
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
     *                      指定长度验证码
     */
    public static String generateVerifyCode(int verifySize, String sourceCode) {
        if (sourceCode == null || sourceCode.length() == 0)
            sourceCode = VERIFY_CODE;

        int sourceCodeLen = sourceCode.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++)
            verifyCode.append(sourceCode.charAt(rand.nextInt(sourceCodeLen - 1)));

        return verifyCode.toString();
    }

    /**
     * 输出指定验证码图片流
     *
     * @param width
     *                  验证码图片宽度
     * @param height
     *                  验证码图片高度
     * @param outputStream
     *                  输出流
     * @param verifyCode
     *                  验证码
     */
    public static void outputImage(int width, int height, OutputStream outputStream, String verifyCode)
            throws IOException {
        int verifyLen = verifyCode.length();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[] {Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY,
                Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW};
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);

        graphics2D.setColor(Color.GRAY); // 设置边框颜色
        graphics2D.fillRect(0, 0, width, height);

        Color color = getRandColor(200, 250);
        graphics2D.setColor(color); // 设置背景色
        graphics2D.fillRect(0, 2, width, height - 4);

        // 绘制干扰线
        Random random = new Random();
        graphics2D.setColor(getRandColor(160, 200)); // 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int x1 = random.nextInt(6) + 1;
            int y1 = random.nextInt(12) + 1;
            graphics2D.drawLine(x, y, x + x1 + 40, y + y1 + 20);
        }

        // 添加噪点
        float yawpRate = 0.05f; // 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        shear(graphics2D, width, height, color); // 使图片扭曲

        graphics2D.setColor(getRandColor(100, 160));
        int fontSize = height - 4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        graphics2D.setFont(font);
        char[] chars = verifyCode.toCharArray();
        for (int i = 0; i < verifyLen; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1: -1),
                    (width / verifyLen) * i + fontSize /2, height /2);
            graphics2D.setTransform(affine);
            graphics2D.drawChars(chars, i, 1, ((width - 10) / verifyLen) * i + 5,
                    height / 2 + fontSize / 2 - 10);
        }

        graphics2D.dispose();
        ImageIO.write(image, "jpg", outputStream);
    }

    private static void shear(Graphics2D graphics2D, int width, int height, Color color) {
        shearX(graphics2D, width, height, color);
        shearY(graphics2D, width, height, color);
    }

    private static void shearX(Graphics2D graphics2D, int width, int height, Color color) {
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

    private static void shearY(Graphics2D graphics2D, int width, int height, Color color) {
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

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c :
                rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i <3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;

        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}