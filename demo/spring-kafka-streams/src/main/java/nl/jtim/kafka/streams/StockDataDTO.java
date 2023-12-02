package nl.jtim.kafka.streams;

import java.util.List;

public class StockDataDTO {
    private String stockCode;
    private StockInfoDTO data;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public StockInfoDTO getData() {
        return data;
    }

    public void setData(StockInfoDTO data) {
        this.data = data;
    }
}

class StockInfoDTO {
    private List<BuyDataDTO> BUY;
    private List<SellDataDTO> SELL;

    public List<BuyDataDTO> getBUY() {
        return BUY;
    }

    public void setBUY(List<BuyDataDTO> BUY) {
        this.BUY = BUY;
    }

    public List<SellDataDTO> getSELL() {
        return SELL;
    }

    public void setSELL(List<SellDataDTO> SELL) {
        this.SELL = SELL;
    }
}


class BuyDataDTO {
    Double price;
    Integer quantity;

    public BuyDataDTO(Double price, Integer quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

class SellDataDTO {
    Double price;
    Integer quantity;

    public SellDataDTO(Double price, Integer quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}