package com.learning.pScanWithUi;

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
	public List<ScanResult> scan(
			@RequestParam String host,
			@RequestParam int startPort,
			@RequestParam int endPort,
			@RequestParam(defaultValue = "20") int threads,
			@RequestParam(defaultValue = "500") int timeoutMs
	) throws InterruptedException, ExecutionException {
		
		return PortScanner.scanRangeParallel(host, startPort, endPort, threads, timeoutMs);
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
}
