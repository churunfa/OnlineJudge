package OnlineJudge.web.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 验证码
 */
@WebServlet("/checkCode")
public class CheckCodeServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		//服务器通知浏览器不要缓存
		response.setHeader("pragma","no-cache");
		response.setHeader("cache-control","no-cache");
		response.setHeader("expires","0");

		//在内存中创建一个长80，宽30的验证码
		//参数一：长
		//参数二：宽
		//参数三：颜色
		int width = 200;
		int height = 69;
		BufferedImage verifyImg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

//生成对应宽高的初始图片

		String randomText = VerifyCode.drawRandomText(width,height,verifyImg);
		request.getSession().setAttribute("CHECKCODE_SERVER",randomText);
		//将内存中的图片输出到浏览器
		//参数一：图片对象
		//参数二：图片的格式，如PNG,JPG,GIF
		//参数三：图片输出到哪里去
		ImageIO.write(verifyImg,"PNG",response.getOutputStream());
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request,response);
	}
}



