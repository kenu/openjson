package com.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DhLottoExample {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		for(int i = 1; i < 54; i++) {
			List<Integer> array = lottoParser(i);
			System.out.print(i + ": ");
			System.out.println(array);			
		}
	}

	private static List<Integer> lottoParser(int number) throws JsonMappingException, JsonProcessingException {
		String url = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo="
				+ number;
		RestTemplate restTemplate = new RestTemplate();
		String string = restTemplate.getForObject(url, String.class);
		JsonNode node = new ObjectMapper().readTree(string);
		Iterator<String> fields = node.fieldNames();
		List<Integer> list = new ArrayList<>();
		while (fields.hasNext()) {
			String field = fields.next();
			if (field.startsWith("drwtNo")) {
				list.add(node.get(field).intValue());
			}
			System.out.println(field + ": " + node.get(field));
		}
		list.sort(Comparator.naturalOrder());
		return list;
	}

}
