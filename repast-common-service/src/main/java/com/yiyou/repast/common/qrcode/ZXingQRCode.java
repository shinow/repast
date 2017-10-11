package com.yiyou.repast.common.qrcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**  
 * @Description: (二维码)     
 */
public class ZXingQRCode {

	private static final String LOGO_PATH="/data/upload/fjj/logo.png";
	private static final int QRCOLOR = 0xFF000000; // 默认是黑色
	private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

	public static void main(String[] args) throws WriterException {
		try {
			getLogoQRCode("https://www.baidu.com/", null,100,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成带logo的二维码图片
	 *
	 * @param qrPic
	 * @param logoPic
	 */
	public static String getLogoQRCode(String qrUrl, String productName,int size,HttpServletResponse resp) {
		String filePath = LOGO_PATH; 
		String content = qrUrl;
		try {
			ZXingQRCode zp = new ZXingQRCode();
			BufferedImage bim = zp.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 400, 400,
					zp.getDecodeHintType());
			return zp.addLogo_QRCode(bim, new File(filePath), new LogoConfig(), productName,resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 给二维码图片添加Logo
	 *
	 * @param qrPic
	 * @param logoPic
	 */
	public String addLogo_QRCode(BufferedImage bim, File logoPic, LogoConfig logoConfig, String productName,HttpServletResponse resp) {
		try {
			BufferedImage image = bim;
			Graphics2D g = image.createGraphics();

			/**
			 * 读取Logo图片
			 */
			BufferedImage logo = ImageIO.read(logoPic);
			/**
			 * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
			 */
			int widthLogo = logo.getWidth(null) > image.getWidth() * 3 / 10 ? (image.getWidth() * 3 / 10)
					: logo.getWidth(null),
					heightLogo = logo.getHeight(null) > image.getHeight() * 3 / 10 ? (image.getHeight() * 3 / 10)
							: logo.getWidth(null);

			/**
			 * logo放在中心
			 */
			int x = (image.getWidth() - widthLogo) / 2;
			int y = (image.getHeight() - heightLogo) / 2;
			// 开始绘制图片
			g.drawImage(logo, x, y, widthLogo, heightLogo, null);
			g.dispose();
			// 把商品名称添加上去，商品名称不要太长哦，这里最多支持两行。太长就会自动截取啦
			if (productName != null && !productName.equals("")) {
				// 新的图片，把带logo的二维码下面加上文字
				BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D outg = outImage.createGraphics();
				// 画二维码到新的面板
				outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
				// 画文字到新的面板
				outg.setColor(Color.BLACK);
				outg.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
				int strWidth = outg.getFontMetrics().stringWidth(productName);
				if (strWidth > 399) {
					// 长度过长就换行
					String productName1 = productName.substring(0, productName.length() / 2);
					String productName2 = productName.substring(productName.length() / 2, productName.length());
					int strWidth1 = outg.getFontMetrics().stringWidth(productName1);
					int strWidth2 = outg.getFontMetrics().stringWidth(productName2);
					outg.drawString(productName1, 200 - strWidth1 / 2,
							image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12);
					BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
					Graphics2D outg2 = outImage2.createGraphics();
					outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
					outg2.setColor(Color.BLACK);
					outg2.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
					outg2.drawString(productName2, 200 - strWidth2 / 2,
							outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
					outg2.dispose();
					outImage2.flush();
					outImage = outImage2;
				} else {
					outg.drawString(productName, 200 - strWidth / 2,
							image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12); // 画文字
				}
				outg.dispose();
				outImage.flush();
				image = outImage;
			}
			logo.flush();
			image.flush();
			
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setDateHeader("Expires", 0);
            resp.setContentType("image/jpeg");
            // 将图像输出到Servlet输出流中。
            ServletOutputStream sos = resp.getOutputStream();
            ImageIO.write(image, "jpeg", sos);
            sos.close();
			/*ImageIO.write(image, "png",new File("F:/TDC-" + new Date().getTime() + "test.png")); // TODO
			String imageBase64QRCode = Base64.encodeBase64URLSafeString(baos.toByteArray());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建初始化二维码
	 *
	 * @param bm
	 * @return
	 */
	public BufferedImage fileToBufferedImage(BitMatrix bm) {
		BufferedImage image = null;
		try {
			int w = bm.getWidth(), h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 生成二维码bufferedImage图片
	 *
	 * @param content
	 *            编码内容
	 * @param barcodeFormat
	 *            编码类型
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param hints
	 *            设置参数
	 * @return
	 */
	public BufferedImage getQR_CODEBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height,
			Map<EncodeHintType, ?> hints) {
		MultiFormatWriter multiFormatWriter = null;
		BitMatrix bm = null;
		BufferedImage image = null;
		try {
			multiFormatWriter = new MultiFormatWriter();
			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
			int w = bm.getWidth();
			int h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
				}
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 设置二维码的格式参数
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Map<EncodeHintType, Object> getDecodeHintType() {
		// 用于设置QR二维码参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		hints.put(EncodeHintType.MAX_SIZE, 350);
		hints.put(EncodeHintType.MIN_SIZE, 100);

		return hints;
	}
}

class LogoConfig {
	// logo默认边框颜色
	public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
	// logo默认边框宽度
	public static final int DEFAULT_BORDER = 2;
	// logo大小默认为照片的1/5
	public static final int DEFAULT_LOGOPART = 5;

	private final int border = DEFAULT_BORDER;
	private final Color borderColor;
	private final int logoPart;

	/**
	 * Creates a default config with on color {@link #BLACK} and off color
	 * {@link #WHITE}, generating normal black-on-white barcodes.
	 */
	public LogoConfig() {
		this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
	}

	public LogoConfig(Color borderColor, int logoPart) {
		this.borderColor = borderColor;
		this.logoPart = logoPart;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getBorder() {
		return border;
	}

	public int getLogoPart() {
		return logoPart;
	}
}
