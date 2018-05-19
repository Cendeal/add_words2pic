import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AddContent2Picture {
	public static class Position {
		int x;
		int y;

		void setXY(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	private String outImgPath;// ���·��
	private Image srcImg;// Դ�ļ�
	private int srcImgWidth;// ��ȡͼƬ�Ŀ�
	private int srcImgHeight;// ��ȡͼƬ�ĸ�
	private BufferedImage bufImg;
	private Position contentP;// ���ݵĿ�ʼλ��
	private int border;// �߽�
	private int lineHeight;// �о�
	private int lastfontsize;// ��һ�е������С,Ĭ��0����ͨ��setLastLine�ı�ֵ
	private int rotarad;

	// ���÷�����

	public void addMsg(String msg, String footer, Color color, Font font) {
		try {
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			g.setColor(color);
			g.setFont(font);
			g.rotate(Math.toRadians(rotarad));
			fonmatConten2(font, msg, footer, contentP.x, contentP.y + lastfontsize, g);
			lastfontsize = font.getSize();
			g.dispose();
			bufImg.flush();
			srcImg = bufImg;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	// ������֧�ֶ�������
	private void addFooter(Font font, String footer, int starty, Graphics2D g) {
		String[] strings = footer.split("\n");
		int len = 0;
		int fontwidth = getTheFontWidth(font);
		
		for (String s : strings) {
			int hanzi = 0;
			int zimu = 0;
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) <= '~') {
					zimu++;
				} else {
					hanzi++;
				}
			}
			int tmp_len = hanzi + zimu / 2;
			if (zimu % 2 != 0)
				tmp_len += 1;
			len = (len > tmp_len ? len : tmp_len);
		}
		for (String s : strings) {
			int footx = srcImgWidth - border - len * fontwidth;
			g.drawString(s, footx, starty);
			starty += lineHeight + font.getSize();
		}
	}

	private int getTheFontWidth(Font font) {
		int width_of_font;
		@SuppressWarnings("restriction")
		FontMetrics tf = sun.font.FontDesignMetrics.getMetrics(font);
		if (font.isBold()) {
			width_of_font = tf.stringWidth("aa");
		} else {
			width_of_font = tf.stringWidth("��");
		}
		return width_of_font;
	}

	// ��ʽ������ǽ����ִ���Ϊ���ַ�
	private void fonmatConten2(Font font, String msg, String footer, int startx, int starty, Graphics2D g) {
		float linelen = srcImgWidth - border - startx;
		starty += font.getSize();
		// ����ÿһ�е���������Ϊ������������ͨ����Ĵ�С���������Բ������ִ���ʽ
		int num;
		int width_of_font = getTheFontWidth(font);
		num = (int) (linelen / width_of_font);
		int currword = 0;
		int lastword = 0;
		int temp = num;
		while (currword < msg.length()) {
			int s = 0;
			lastword = currword;
			for (int i = 0; i < num && currword < msg.length(); i++, currword++) {
				if (msg.charAt(currword) == '\n' && i != 0) {
					currword++;
					break;
				}
				if (msg.charAt(currword) <= '~') {
					s++;
					if (s == 2) {
						num++;
						s = 0;
					}
				}
			}

			g.drawString(msg.substring(lastword, currword), startx, starty);
			num = temp;
			starty += lineHeight + font.getSize();
		}
		if (footer != null)
			addFooter(font, footer, starty, g);
	}

	// ���
	public void outPutFile(String picfomat, String filename) {
		try {
			FileOutputStream outImgStream = new FileOutputStream(outImgPath + "\\" + filename + "." + picfomat);
			ImageIO.write(bufImg, picfomat, outImgStream);
			outImgStream.flush();
			outImgStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getOutImgPath() {
		return outImgPath;
	}

	public Image getSrcImg() {
		return srcImg;
	}

	public int getSrcImgWidth() {
		return srcImgWidth;
	}

	public int getSrcImgHeight() {
		return srcImgHeight;
	}

	public Position getContentP() {
		return contentP;
	}

	public int getBorder() {
		return border;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public int getLastfontsize() {
		return lastfontsize;
	}

	public void setOutImgPath(String outImgPath) {
		this.outImgPath = outImgPath;
	}

	public void setSrcImg(Image srcImg) {
		this.srcImg = srcImg;
	}

	public void setSrcImgWidth(int srcImgWidth) {
		this.srcImgWidth = srcImgWidth;
	}

	public void setSrcImgHeight(int srcImgHeight) {
		this.srcImgHeight = srcImgHeight;
	}

	public void setBufImg(BufferedImage bufImg) {
		this.bufImg = bufImg;
	}

	public void setContentP(Position contentP) {
		this.contentP = contentP;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	public void setLastfontzie(int size) {
		lastfontsize = size;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	public void setLastfontsize(int lastfontsize) {
		this.lastfontsize = lastfontsize;
	}

	public int getRotarad() {
		return rotarad;
	}

	public void setRotarad(int rotarad) {
		this.rotarad = rotarad;
	}

	public static class Builder {
		String outImgPath;// ���·��
		String srcpath;
		int x;
		int y;
		int border;// �߽�
		int lineHeight;// �о�
		int rotarad;

		public Builder srcpath(String srcpath) {
			this.srcpath = srcpath;
			return this;
		}

		public Builder outImagPath(String out) {
			outImgPath = out;
			return this;
		}

		public Builder contentPosition(int x, int y) {

			this.x = x;
			this.y = y;
			return this;
		}

		public Builder border(int border) {
			this.border = border;
			return this;
		}

		public Builder lineHeight(int lineHeight) {
			this.lineHeight = lineHeight;
			return this;

		}

		public Builder rotarad(int rotarad) {
			this.rotarad = rotarad;
			return this;
		}

		public AddContent2Picture create() {
			return new AddContent2Picture(this);
		}
	}

	// ���췽��
	private AddContent2Picture(Builder b) {
		this.border = b.border;
		this.lineHeight = b.lineHeight;
		this.contentP = new Position();
		contentP.x = b.x;
		contentP.y = b.y;
		this.lastfontsize = 0;
		this.outImgPath = b.outImgPath;
		this.rotarad=b.rotarad;
		File srcImgFile = new File(b.srcpath);
		try {
			this.srcImg = ImageIO.read(srcImgFile);
			srcImgWidth = srcImg.getWidth(null);
			srcImgHeight = srcImg.getHeight(null);
			bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
