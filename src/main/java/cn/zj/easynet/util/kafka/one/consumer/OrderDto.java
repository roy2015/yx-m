package cn.zj.easynet.util.kafka.one.consumer;

import java.io.Serializable;
import java.math.BigDecimal;


public class OrderDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3219366892580445781L;
    
    private String serialNo;
    private String orderName;
    private Double orderPrice;
    
    public OrderDto() {
        super();
    }

    public OrderDto(String serialNo, String orderName, Double orderPrice) {
        super();
        this.serialNo = serialNo;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
    }

    public String getSerialNo() {
        return serialNo;
    }
    
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    
    public String getOrderName() {
        return orderName;
    }
    
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    
    public Double getOrderPrice() {
        return orderPrice;
    }
    
    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderName == null) ? 0 : orderName.hashCode());
        result = prime * result + ((orderPrice == null) ? 0 : orderPrice.hashCode());
        result = prime * result + ((serialNo == null) ? 0 : serialNo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OrderDto other = (OrderDto) obj;
        if (orderName == null) {
            if (other.orderName != null) return false;
        } else if (!orderName.equals(other.orderName)) return false;
        if (orderPrice == null) {
            if (other.orderPrice != null) return false;
        } else if (!orderPrice.equals(other.orderPrice)) return false;
        if (serialNo == null) {
            if (other.serialNo != null) return false;
        } else if (!serialNo.equals(other.serialNo)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderDto [serialNo=" + serialNo + ", orderName=" + orderName + ", orderPrice=" + orderPrice + "]";
    }
}
