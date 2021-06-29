/*    */ package ch.algotrader.entity.strategy;
/*    */ 
/*    */ import ch.algotrader.enumeration.Currency;
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CashBalance
/*    */ {
/*    */   private Currency currency;
/*    */   private BigDecimal amount;
/*    */   private Strategy strategy;
/*    */   
/*    */   public CashBalance(Currency currency, Strategy strategy) {
/* 17 */     this.currency = currency;
/* 18 */     this.strategy = strategy;
/*    */   }
/*    */   
/*    */   public Currency getCurrency() {
/* 22 */     return this.currency;
/*    */   }
/*    */   
/*    */   public BigDecimal getAmount() {
/* 26 */     return this.amount;
/*    */   }
/*    */   
/*    */   public void setAmount(BigDecimal amount) {
/* 30 */     this.amount = amount;
/*    */   }
/*    */   
/*    */   public Strategy getStrategy() {
/* 34 */     return this.strategy;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return "CashBalance [currency=" + this.currency + ", strategy=" + this.strategy + ", amount=" + this.amount + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\strategy\CashBalance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */