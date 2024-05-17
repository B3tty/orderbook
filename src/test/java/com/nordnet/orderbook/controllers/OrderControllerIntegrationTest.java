package com.nordnet.orderbook.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ResourceLoader resourceLoader;
  private static String completeOrderFileName = "order_correct.json";
  private static String emptySummary = "{\"averagePrice\":0.0,\"maxPrice\":0.0,\"minPrice\":0.0,"
      + "\"totalNumber\":0}";

  @Test
  public void createOrderTest_WhenCorrectBody_ShouldReturnCreated() throws Exception {
    String orderRequestBody = readResourceFile(completeOrderFileName);
    mockMvc.perform(MockMvcRequestBuilders.post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderRequestBody))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @ParameterizedTest
  @ValueSource(strings = {"order_missing_side.json", "order_bad_side.json", "order_missing_ticker"
      + ".json", "order_missing_volume.json", "order_missing_price.json", "order_empty.json"})
  public void createOrderTest_WhenIncorrectRequest_ShouldReturnBadRequest(String fileName) throws Exception {
    String orderRequestBody = readResourceFile(fileName);
    mockMvc.perform(MockMvcRequestBuilders.post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderRequestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void getOrderByIdTest_WhenExistingId_ShouldReturnOk() throws Exception {
    String orderRequestBody = readResourceFile(completeOrderFileName);
    MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(orderRequestBody))
        .andReturn();

    String orderId = result.getResponse().getContentAsString().replaceAll("\"", "");

    mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", orderId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getOrderByIdTest_WhenNonExistingId_ShouldReturnNotFound() throws Exception {
    String id = "00000000-0000-0000-0000-00000000";
    mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void getOrderByIdTest_WhenNoId_ShouldReturnNotAllowed() throws Exception {
    String id = "";
    mockMvc.perform(MockMvcRequestBuilders.get("/orders{orderId}", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
  }

  @Test
  public void getSummary_WhenMatchingOrder_ShouldReturnCorrectSummary() throws Exception {
    String orderRequestBody = readResourceFile(completeOrderFileName);
    mockMvc.perform(MockMvcRequestBuilders.post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(orderRequestBody));

    String today = LocalDate.now().toString();

    mockMvc.perform(MockMvcRequestBuilders.get("/orders/summary")
            .param("ticker", "SAVE")
            .param("date", today)
            .param("side", "BUY")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("{\"averagePrice\":32.1,\"maxPrice\":32.1,\"minPrice\":32.1,"
            + "\"totalNumber\":1}"));
  }

  @Test
  public void getSummary_WhenNoOrders_ShouldReturnEmptySummary() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/orders/summary")
            .param("ticker", "SAVE")
            .param("date", "2024-05-16")
            .param("side", "BUY")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(emptySummary));
  }

  @Test
  public void getSummary_WhenNoMatchingOrder_ShouldReturnEmptySummary() throws Exception {
    String orderRequestBody = readResourceFile(completeOrderFileName);
    mockMvc.perform(MockMvcRequestBuilders.post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(orderRequestBody));
    mockMvc.perform(MockMvcRequestBuilders.get("/orders/summary")
            .param("ticker", "ABC")
            .param("date", "2024-05-16")
            .param("side", "SELL")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(emptySummary));
  }

  @ParameterizedTest
  @CsvSource({"AAA, incorrect-date, BUY", "AAA, 2016-42-03, BUY", ", 2024-05-16, BUY", "AAA,, "
      + "BUY", "AAA, 2024-05-16,", "AAA, 2024-05-16, SIDE", "AAA, 16-05-2024, SELL"})
  public void getSummary_WhenIncorrectParams_ShouldReturnBadRequest(String ticker, String date,
      String side) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/orders/summary")
            .param("ticker", ticker)
            .param("date", date)
            .param("side", side)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  private String readResourceFile(String filename) throws IOException {
    Resource resource = resourceLoader.getResource("classpath:" + filename);
    InputStream inputStream = resource.getInputStream();
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
      }
    }
    return stringBuilder.toString();
  }
}
