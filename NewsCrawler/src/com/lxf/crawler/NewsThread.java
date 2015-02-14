package com.lxf.crawler;

/**
 * ִ��ѭ����ȡ���߳�
 * 
 * @author �����
 * 
 */
public class NewsThread implements Runnable {

	private static final long TIME_OUT = 1000 * 60 * 5;// ���5������ȡһ��
	private static final NewsThread NEWS_THREAD = new NewsThread();//����
	private volatile static boolean isStart = true;//���������shared variable���ź�,Ĭ�������߳�

	/**
	 * ˽�л�����������������ģʽ
	 */
	private NewsThread() {
		// ���ⲻС��������ڲ����ù�����
		//throw new AssertionError();
	}

	/**
	 * ��ȡ����
	 * 
	 * @return NewsThread��������
	 */
	public static NewsThread getInstance() {
		return NEWS_THREAD;
	}

	/**
	 * ʹ�ù��������shared variable�������ź�
	 * 
	 * @param status
	 *            �����߳�����true,ֹͣ�߳�����false
	 */
	public static void changeStatus(boolean status) {
		isStart = status;
	}

	@Override
	public void run() {
		int i = 0;// ִ�д���
		while (isStart) {
			System.out.println("************************��ʼ��" + ++i
					+ "��ץȡ************************");
			RollNews.getNews();
			System.out.println("************************������" + i
					+ "��ץȡ************************");
			try {
				Thread.sleep(TIME_OUT);
			} catch (InterruptedException e) {
				System.out.println("sleep interrupted");
			}

		}
	}

}
