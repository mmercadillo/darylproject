/*    */ package ch.algotrader.entity.trade;
/*    */ 
/*    */ import ch.algotrader.entity.security.Security;
/*    */ import ch.algotrader.entity.strategy.Strategy;
/*    */ import ch.algotrader.enumeration.Side;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Order
/*    */ {
/*    */   private Side side;
/*    */   private long quantity;
/*    */   private Security security;
/*    */   private Strategy strategy;
/*    */   
/*    */   public Order(Side side, long quantity, Security security, Strategy strategy) {
/* 19 */     this.side = side;
/* 20 */     this.quantity = quantity;
/* 21 */     this.security = security;
/* 22 */     this.strategy = strategy;
/*    */   }
/*    */   
/*    */   public Side getSide() {
/* 26 */     return this.side;
/*    */   }
/*    */   
/*    */   public void setSide(Side side) {
/* 30 */     this.side = side;
/*    */   }
/*    */   
/*    */   public long getQuantity() {
/* 34 */     return this.quantity;
/*    */   }
/*    */   
/*    */   public void setQuantity(long quantity) {
/* 38 */     this.quantity = quantity;
/*    */   }
/*    */   
/*    */   public Security getSecurity() {
/* 42 */     return this.security;
/*    */   }
/*    */   
/*    */   public void setSecurity(Security security) {
/* 46 */     this.security = security;
/*    */   }
/*    */   
/*    */   public Strategy getStrategy() {
/* 50 */     return this.strategy;
/*    */   }
/*    */   
/*    */   public void setStrategy(Strategy strategy) {
/* 54 */     this.strategy = strategy;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "Order [side=" + this.side + ", quantity=" + this.quantity + ", security=" + this.security + ", strategy=" + this.strategy + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\trade\Order.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */