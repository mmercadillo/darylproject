/*    */ package ch.algotrader.entity.security;
/*    */ 
/*    */ import ch.algotrader.enumeration.Currency;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Security
/*    */ {
/*    */   private String symbol;
/*    */   private Currency currency;
/*    */   
/*    */   public Security(String symbol, Currency currency) {
/* 15 */     this.symbol = symbol;
/* 16 */     this.currency = currency;
/*    */   }
/*    */   
/*    */   public String getSymbol() {
/* 20 */     return this.symbol;
/*    */   }
/*    */   
/*    */   public Currency getCurrency() {
/* 24 */     return this.currency;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 29 */     return "Security [symbol=" + this.symbol + ", currency=" + this.currency + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\security\Security.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */