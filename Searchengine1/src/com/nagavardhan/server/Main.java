package com.nagavardhan.server;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public class Main {

	public static void main(String[] args) {

		ExecutorService es = Executors.newFixedThreadPool(1);
		@SuppressWarnings("rawtypes")
		List<Future> futures = new ArrayList<Future>();

		futures.add(es.submit(new Callable<Object>() {
			public Object call() throws Exception {
				com.nagavardhan.webcrawling.Main.initializeFromDB();
				return null;
			}
		}));

		for (Future future : futures)

			try {

				System.out.println("Please Wait until it loads");
				future.get();
				System.out.println("Now Carry On Your Search");
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}

		ContextHandlerCollection contexts = new ContextHandlerCollection();

		contexts.setHandlers(new Handler[] { new ApplicationContextBuilder().buildWebAppContext() });

		final JettyServer jettyServer = new JettyServer();
		jettyServer.setHandler(contexts);
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				new ServerRunner(jettyServer);
			}
		};
		EventQueue.invokeLater(runner);
	}
}