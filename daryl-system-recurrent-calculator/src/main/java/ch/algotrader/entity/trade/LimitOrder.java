/*    */ package ch.algotrader.entity.trade;
/*    */ 
/*    */ import ch.algotrader.entity.security.Security;
/*    */ import ch.algotrader.entity.strategy.Strategy;
/*    */ import ch.algotrader.enumeration.Side;
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ public class LimitOrder
/*    */   extends Order
/*    */ {
/*    */   private BigDecimal limit;
/*    */   
/*    */   public LimitOrder(Side side, long quantity, Security security, Strategy strategy, BigDecimal limit) {
/* 15 */     super(side, quantity, security, strategy);
/* 16 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BigDecimal getLimit() {
/* 22 */     return this.limit;
/*    */   }
/*    */   
/*    */   public void setLimit(BigDecimal limit) {
/* 26 */     this.limit = limit;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return "LimitOrder [toString()=" + super.toString() + ", limit=" + this.limit + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\trade\LimitOrder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */