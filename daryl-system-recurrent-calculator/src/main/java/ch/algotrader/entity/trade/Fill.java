/*    */ package ch.algotrader.entity.trade;
/*    */ 
/*    */ import ch.algotrader.enumeration.Side;
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ public class Fill
/*    */ {
/*    */   private Side side;
/*    */   private long quantity;
/*    */   private BigDecimal price;
/*    */   private Order order;
/*    */   
/*    */   public Side getSide() {
/* 14 */     return this.side;
/*    */   }
/*    */   
/*    */   public void setSide(Side sideIn) {
/* 18 */     this.side = sideIn;
/*    */   }
/*    */   
/*    */   public long getQuantity() {
/* 22 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public void setQuantity(long quantityIn) {
/* 26 */     this.quantity = quantityIn;
/*    */   }
/*    */   
/*    */   public BigDecimal getPrice() {
/* 30 */     return this.price;
/*    */   }
/*    */   
/*    */   public void setPrice(BigDecimal priceIn) {
/* 34 */     this.price = priceIn;
/*    */   }
/*    */   
/*    */   public Order getOrder() {
/* 38 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setOrder(Order orderIn) {
/* 42 */     this.order = orderIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return "Fill [side=" + this.side + ", quantity=" + this.quantity + ", price=" + this.price + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\trade\Fill.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */