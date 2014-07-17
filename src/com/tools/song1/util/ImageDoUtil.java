/**   
 * @Title: ImaageDoUtil.java 
 * @Package com.base.util 
 * @Description: TODO
 * @author Jeckey.Liu   
 * @date 2014年7月1日 上午9:34:33 
 * @version V1  
 */
package com.tools.song1.util;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

/**
 * @ClassName: ImaageDoUtil
 * @Description: TODO
 * @author Jeckey.Liu
 * @date 2014年7月1日 上午9:34:33
 * 
 */
public class ImageDoUtil {
	private static final int targetWidth = 300;
	private static final int targetHeight = 300;
	private static final String fileSuffixStr = ".temp";

	// ///
	public static void main(String[] args) throws Exception {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		String dateStr = formate.format(date);
		// //
		String outPath = "D:/Koala_bak" + dateStr + ".jpg";
		File outFile = new File(outPath);
		FileOutputStream out = new FileOutputStream(outFile);
		// ///
		String path = "D:/Koala.jpg";
		File file = new File(path);
		if (file.exists()) {
			FileInputStream input = new FileInputStream(file);
			BufferedImage image = ImageIO.read(input);
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			System.out.println("Image width:::" + imageWidth);
			System.out.println("Image height:::" + imageHeight);
			double sx = (double) targetWidth / imageWidth;
			double sy = (double) targetHeight / imageHeight;
			// image = resize(image, targetWidth, targetHeight, sx, sy);
			image = resize(image, targetWidth, targetHeight, 1, 1);
			// image = resizeCut(image, targetWidth, targetHeight, sx, sy);
			image = resizeFixedSizeCut(image, targetWidth, targetHeight, sx, sy);
			ImageIO.write(image, "jpg", out);
			System.out.println("out over..............");
		} else {
			System.out.println("file not found...........");
		}

	}

	/**
	 * 
	 * 图片截取
	 * 
	 * @param tempPath
	 * @param suffix
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String cutImage(File file) {
		if (file == null) {
			return null;
		}
		if (file.exists()) {
			FileInputStream input = null;
			FileOutputStream out = null;
			try {
				String path = file.getAbsolutePath();
				if (!ImageFileUtil.isImageFile(path)) {
					return null;
				}
				String tempPath = new String(path.substring(0, path.lastIndexOf(".")) + "(300x300).jpg");
				input = new FileInputStream(file);
				BufferedImage image = ImageIO.read(input);
				int imageWidth = image.getWidth();
				int imageHeight = image.getHeight();
				System.out.println("Image width:::" + imageWidth);
				System.out.println("Image height:::" + imageHeight);
				double sx = (double) targetWidth / imageWidth;
				double sy = (double) targetHeight / imageHeight;
				image = resize(image, targetWidth, targetHeight, sx, sy);

				File outFile = new File(tempPath);
				out = new FileOutputStream(outFile);
				ImageIO.write(image, "jpg", out);
				System.out.println("out over..............");
				return tempPath;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (input != null)
						input.close();
					if (out != null)
						out.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					input = null;
					out = null;
				}
			}

		} else {
			System.out.println("file not found...........");
		}
		return null;
	}

	/**
	 * 实现图像的等比缩放
	 * 
	 * @param source
	 * @param targetW
	 * @param targetH
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source, int targetW, int targetH, double sx, double sy) {
		if (sx < sy) {
			sy = sx;
			targetW = (int) (sy * source.getWidth());
			targetH = (int) (sy * source.getHeight());
		} else {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
			targetH = (int) (sx * source.getHeight());
		}
		return baseImage(source, targetW, targetH, sx, sy);
	}

	public static BufferedImage baseImage(BufferedImage source, int targetW, int targetH, double sx, double sy) {
		int type = source.getType();
		BufferedImage target = null;
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else
			target = new BufferedImage(targetW, targetH, type);
		Graphics2D g = target.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	/**
	 * 按固定比例裁剪
	 * 
	 * @param srcImage
	 * @param targetW
	 * @param targetH
	 * @return
	 */
	public static BufferedImage resizeCut(BufferedImage srcImage, int width, int hight, double sx, double sy) {
		if (width > 0 || hight > 0) {
			// 原图的大小
			int sw = srcImage.getWidth();
			int sh = srcImage.getHeight();
			// 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
			if (sw < width && sh < hight) {
				return srcImage;
			} else {
				if (sw > sh) {
					srcImage = baseImage(srcImage, (int) (sy * srcImage.getWidth()), (int) (sy * srcImage.getHeight()), sy, sy);
				} else {
					srcImage = baseImage(srcImage, (int) (sx * srcImage.getWidth()), (int) (sx * srcImage.getHeight()), sx, sx);
				}
			}
		}
		// 缩放后的图像的宽和高
		int w = srcImage.getWidth();
		int h = srcImage.getHeight();
		if (w == width) {// 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取
			// 计算X轴坐标
			int x = 0;
			int y = h / 2 - hight / 2;
			return subImage(srcImage, new Rectangle(x, y, width, hight));
		} else { // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取
			// 计算X轴坐标
			int x = w / 2 - width / 2;
			int y = 0;
			return subImage(srcImage, new Rectangle(x, y, width, hight));
		}
	}

	public static BufferedImage resizeFixedSizeCut(BufferedImage srcImage, int width, int hight, double sx, double sy) {
		if (width > 0 || hight > 0) {
			// 原图的大小
			int sw = srcImage.getWidth();
			int sh = srcImage.getHeight();
			// 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
			if (sw < width && sh < hight) {
				return srcImage;
			} else {
				srcImage = baseImage(srcImage, 300, 300, sx, sy);
			}
		}
		// 缩放后的图像的宽和高
		int w = srcImage.getWidth();
		int h = srcImage.getHeight();
		if (w == width) {// 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取
			// 计算X轴坐标
			int x = 0;
			int y = h / 2 - hight / 2;
			return subImage(srcImage, new Rectangle(x, y, width, hight));
		} else { // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取
			// 计算X轴坐标
			int x = w / 2 - width / 2;
			int y = 0;
			return subImage(srcImage, new Rectangle(x, y, width, hight));
		}
	}

	public static BufferedImage subImage(BufferedImage image, Rectangle subImageBounds) {
		if (subImageBounds.x < 0 || subImageBounds.y < 0 || subImageBounds.width - subImageBounds.x > image.getWidth() || subImageBounds.height - subImageBounds.y > image.getHeight()) {
			return image;
		}
		BufferedImage subImage = image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height);
		return subImage;
	}

}
