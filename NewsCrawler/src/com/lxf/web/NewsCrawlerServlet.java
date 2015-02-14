package com.lxf.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lxf.crawler.NewsThread;
/**
 * ��������/ֹͣ�����߳�
 * @author �����
 *
 */
public class NewsCrawlerServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = request.getParameter("op");
		NewsThread newsThread = NewsThread.getInstance();// ��ȡ�̵߳���
		Thread thread = new Thread(newsThread);// �����߳�
		if ("start".equals(op)) {
			NewsThread.changeStatus(true);// ������״̬����
			thread.start();
			op = "stop";
			System.out.println("�߳̿�ʼ������");
		} else if ("stop".equals(op)) {

			NewsThread.changeStatus(false);// ������״̬����
			thread.interrupt();
			op = "start";
			System.out.println("�߳��жϣ�����");
		}
		thread = null;
		request.getSession().setAttribute("op", op);
		response.sendRedirect("index.jsp");
	}

}
