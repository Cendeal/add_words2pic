# 图片添加水印，生成邀请函（Java)

#### 用途说明：
- 1.可以用于生成自定义的水印
- 2.可以动态生成邀请函，指定特定对象的姓名、内容
- 3.可以生成手写书信（这个下载对应的手写字体，安装到电脑就可用了）
#### 源码的功能：
- 1.提供自定义字体属性（样式，大小，颜色）
- 2.可以实现自动换行
- 3.可以旋转文字
- 4.可以保存多种格式，支持jpeg、jpg、png等
#### 样例展示：
![邀请函](https://github.com/Cendeal/add_words2pic/blob/master/example/output.jpg)
![水印水平方向](https://github.com/Cendeal/add_words2pic/blob/master/example/shuiyin.jpg)
![水印旋转](https://github.com/Cendeal/add_words2pic/blob/master/example/shuiyin_rota.jpg)
#### 源码分析：

- 1.AddContent2Picture类的设计使用了构造者模式，方便初始化对象，同时提高了易读性：

> 构造模式部分代码：
```
//内部类Builder
public static class Builder {
		String outImgPath;// 输出路径
		String srcpath;
		int x;
		int y;
		int border;// 边界
		int lineHeight;// 行距
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
	//构造方法定义为私有，不可以直接外部调用
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
```
>实例化对象时可以如下使用
```
//可以这样快速的构造对象
AddContent2Picture test = new AddContent2Picture.Builder()
				.srcpath(srcImg)//背景图
				.outImagPath(tarImgPath)//输出
				.border(0)//边界
				.lineHeight(0)//文字行间距
				.contentPosition(10, 10)//文字内容开始位置
		.create();
```
- 2.自动换行方法，需要考虑ASCII码和汉字，以及粗体问题
 (1)粗体问题方案：
 
 ```
 	private int getTheFontWidth(Font font) {
		int width_of_font;
		@SuppressWarnings("restriction")
		FontMetrics tf = sun.font.FontDesignMetrics.getMetrics(font);
		if (font.isBold()) {
			width_of_font = tf.stringWidth("aa");
		} else {
			width_of_font = tf.stringWidth("口");
		}
		return width_of_font;
	}
 ```
  (2)ASCII码和汉字问题格式化方案：
  
```
private void fonmatConten2(Font font, String msg, String footer, int startx, int starty, Graphics2D g) {
		float linelen = srcImgWidth - border - startx;
		starty += font.getSize();
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
				//这里是判断是ASCII码还是其他字符
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
```
####  演示使用代码：
```
Font font = new Font("宋体", Font.PLAIN, 10);
Color fontcolor = new Color(0, 0, 0);//字体颜色
String srcImg = "";//文件背景图
String tarImgPath = "";//出输出路径
String msg = "亲爱的某某：\n  您好！\n  我们诚心邀请您出席我们组织于2018.6.8在某某广场举行的庆功会。\n  我们期待您的到临！";
		String footer = "某某组织\n2018年5月19";
		/*
		 * 图片生成
		 */
AddContent2Picture test = new AddContent2Picture.Builder()
				.srcpath(srcImg)//背景图
				.outImagPath(tarImgPath)//输出
				.border(0)//边界
				.lineHeight(0)//文字行间距
				.contentPosition(10, 10)//文字内容开始位置
		.create();
test.addMsg(msg, footer, fontcolor, font);
test.outPutFile("jpg", "outputfilename");
```
### 项目地址：
[地址](https://github.com/Cendeal/add_words2pic/)



