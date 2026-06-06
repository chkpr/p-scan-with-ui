package com.learning.pScanWithUi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/api")
public class ScanController {
	
	@GetMapping("/scan")
	public ResponseEntity<?> scan(
			@RequestParam String host,
			@RequestParam int startPort,
			@RequestParam int endPort,
			@RequestParam(defaultValue = "20") int threads,
			@RequestParam(defaultValue = "500") int timeoutMs
			
			) throws ExecutionException {
		if (startPort < 1 || startPort > 65535 || endPort < 1 || endPort > 65535) {
		    return ResponseEntity.status(400).body("Port range must be between 1 and 65535.");
		}
		if (startPort > endPort) {
		    return ResponseEntity.status(400).body("Start port must be lower than end port.");
		}
		if (threads < 1 || threads > 500) {
		    return ResponseEntity.status(400).body("Thread count must be between 1 and 500.");
		}
		try {return ResponseEntity.ok(PortScanner.scanRangeParallel(host, startPort, endPort, threads, timeoutMs));
	} catch (InterruptedException e) {
		Thread.currentThread().interrupt();
		return ResponseEntity.status(499).body("Scancancelled");
	} catch (IllegalArgumentException e) {
		return ResponseEntity.status(400).body(e.getMessage());
	}
	}
	
	

	@GetMapping("/stop")
	public void stopScanning() {
		PortScanner.stop();
	}
	
	@GetMapping("/progress")
	public Map<String, Integer> getProgress() {
		Map<String, Integer> result = new HashMap<>();
		result.put("progress", PortScanner.progress.get());
		result.put("total", PortScanner.totalPorts);
		return result;
	}
	
	@GetMapping("/status")
	public boolean getStatus() {
		return PortScanner.isRunning;
	}
}
