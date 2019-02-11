package com.ziffytech.thyrocare;

public class CartDetailModel
{

    Integer total;
    Integer subtotal;
    Integer discount;
    String elementsincart;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getElementsincart() {
        return elementsincart;
    }

    public void setElementsincart(String elementsincart) {
        this.elementsincart = elementsincart;
    }


}
