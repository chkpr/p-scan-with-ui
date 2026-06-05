package com.learning.pScanWithUi;


import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;
import java.lang.*;

public class PortScanner {

	/**
	 * Teste si un port est ouvert sur un hôte donné.
	 * 
	 * @param host      adresse ip ou nom d'hôte cible
	 * @param port      numero de port à tester (1-65535)
	 * @param timeoutMS délai d'attente maximum en millisecondes
	 * @return true si le port est ouvert, false sinon
	 */
	
	private static ExecutorService executor;

	public static boolean isPortOpen(String host, int port, int timeoutMs) {
		try (Socket socket = new Socket()) {

			socket.connect(new InetSocketAddress(host, port), timeoutMs);

			return true; // connexion réussie, le port est ouvert
		} catch (SocketTimeoutException e) {
			return false; // pas de réponse dans le délai = port filtré
		} catch (IOException e) {
			return false; // si connexion refusée ou autre erreur -> port fermé
		}
	}

	public static String getServiceName(int port) {
		HashMap<Integer, String> service = new HashMap<>();

		service.put(21, "FTP");
		service.put(22, "SSH");
		service.put(25, "SMTP");
		service.put(53, "DNS");
		service.put(80, "HTTP");
		service.put(110, "POP3");
		service.put(143, "IMAP");
		service.put(443, "Https");
		service.put(3306, "MySQL");
		service.put(5432, "PostgreSQL");
		service.put(8080, "HTTP-Alt");
		return service.getOrDefault(port, "-");

	}

	public static void scanRange(String host, int startPort, int endPort, int timeoutMs) {
		System.out.printf("Scan de %s (ports %d à %d)...%n", host, startPort, endPort);
		long start = System.currentTimeMillis();
		int openCount = 0;
		long duration = System.currentTimeMillis() - start;

		for (int port = startPort; port <= endPort; port++) {
			// Afficher la progression
			long progressPercentage = (long) (port - startPort) * 100 / (endPort - startPort);
			// Calcul du temps restant estimé
			long elapsed = System.currentTimeMillis() - start;
			long portsScanned = port - startPort + 1;
			long totalPorts = endPort - startPort + 1;
			long timeLeft = (elapsed * totalPorts / portsScanned - elapsed) / 1000;

			if (isPortOpen(host, port, timeoutMs)) {
				String service = getServiceName(port);

				openCount++;
			}
		}

	}

	public static List<ScanResult> scanRangeParallel(String host, int startPort, int endPort, int threads, int timeoutMs)
			throws InterruptedException, ExecutionException {

		executor = Executors.newFixedThreadPool(threads);
		ConcurrentLinkedQueue<ScanResult> openPorts = new ConcurrentLinkedQueue<>();
		List<Future<?>> futures = new ArrayList<>();
		long startTime = System.currentTimeMillis();

		for (int port = startPort; port <= endPort; port++) {
			final int p = port;
			futures.add(executor.submit(() -> {
			    long before = System.currentTimeMillis();
			    if (isPortOpen(host, p, timeoutMs)) {
			        int responseTime = (int)(System.currentTimeMillis() - before);
			        openPorts.add(new ScanResult(p, getServiceName(p), responseTime));
			    }
			}));
		}

		for (Future<?> future : futures) {
			future.get(); // bloque jusqu'à ce que cette tâche soit finie
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);

		// Affichage des résultats triés
		List<ScanResult> sorted = new ArrayList<>(openPorts);
		sorted.sort(Comparator.comparingInt(ScanResult::getPort));
		{
			long duration = System.currentTimeMillis() - startTime;

			return new ArrayList<>(sorted);
		}
	}
	
	public static void stop() {
		if (executor != null ) {
			executor.shutdownNow();
		}
	}
}

