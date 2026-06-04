package com.learning.pScanWithUi;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
	
	

	

}
