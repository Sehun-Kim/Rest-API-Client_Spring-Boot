package com.example.restClient.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.example.restClient.dto.RequestDto;

@WebListener
public class ThreadListener implements ServletContextListener // 서버가 구동됨과 동시에 Thread들을 시작시키기 위해 ServletContextListener를 구현했다.
{

	// thread들이 담길 threadPool
	private ThreadPoolExecutor threadPool;

	// Client의 요청을 처리할 RestApi의 url이 담긴 array
	private String urlArr[] = {"http://localhost:8181/restApi/csv/record", "http://localhost:8282/restApi/csv/record", "http://localhost:8383/restApi/csv/record"};

	/** Client의 요청이 담길 Queue
	 * 클라이언트의 요청은 여러 곳에서 다수로 들어온다고 가정하고
	 * 요청에 대한 처리는 다른 요청에 문제되지 않게 '비동기'적으로 처리되어야한다.
	 * 클라이언트 서버는 요청의 처리를 각각 다른 API 서버에 보내어 동시에 처리해야한다.
	 * 때문에 요청을 순서대로 Queue에 담아두고 쓰레드들이 Queue에 접근해야 한다.
	 * 쓰레드가 큐에 접근할때는 다른 쓰레드들은 접근하지 못하게 하기 위해 ConcurrentLinkedQueue를 사용하였다. 
	 *   */
	public static final ConcurrentLinkedQueue<RequestDto> queue = new ConcurrentLinkedQueue<>();


	/** ThreadPool에 쓰레드를 생성후 쓰레드를 실행한다.
	 * @throws InterruptedException */
	public void startThread() throws InterruptedException {
		for(int i=0; i<this.urlArr.length; i++) {
			threadPool.execute(new APICallThread(urlArr[i]));
		}
	}

	/** 컨텍스트 초기화 시
	 * 1. ThreadPool을 초기화한다.
	 * 2. 쓰레드들을 생성하고 시작시키는 메소드를 호출한다.
	 *  */
	@Override
	public void contextInitialized (ServletContextEvent event) {
		// threadpool 초기화
		this.threadPool = new ThreadPoolExecutor( 0, 4, 60, TimeUnit.SECONDS, new SynchronousQueue  <Runnable>() );

		// thread 생성 및 시작 메소드 호출
		try {
			startThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** 컨텍스트 종료 시 thread를 종료시킨다 */
	public void contextDestroyed (ServletContextEvent event) {
		System.out.println ("== DaemonListener.contextDestroyed has been called. ==");
		threadPool.shutdown();
	}
}