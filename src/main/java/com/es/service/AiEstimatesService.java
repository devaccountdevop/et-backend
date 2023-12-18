package com.es.service;

import org.springframework.stereotype.Service;

@Service
public class AiEstimatesService {

	public double weightedAverage(int lowEstimates, int highEstimates, int realisticEstimates) {
		
		return lowEstimates + 4 * realisticEstimates + highEstimates ;
		
	}
public double standardDeviation(int lowEstimates, int highEstimates) {
		
		return highEstimates - lowEstimates ;
		
	}

}
