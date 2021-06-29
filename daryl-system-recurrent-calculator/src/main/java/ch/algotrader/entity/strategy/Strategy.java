/*    */ package ch.algotrader.entity.strategy;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Strategy
/*    */ {
/*    */   private String name;
/* 14 */   private Set<CashBalance> cashBalances = new HashSet<CashBalance>(0);
/*    */ 
/*    */   
/*    */   public Strategy(String name) {
/* 18 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 22 */     return this.name;
/*    */   }
/*    */   
/*    */   public Set<CashBalance> getCashBalances() {
/* 26 */     return this.cashBalances;
/*    */   }
/*    */   
/*    */   public void setCashBalances(Set<CashBalance> cashBalances) {
/* 30 */     this.cashBalances = cashBalances;
/*    */   }
/*    */   
/*    */   public boolean addCashBalances(CashBalance element) {
/* 34 */     return this.cashBalances.add(element);
/*    */   }
/*    */   
/*    */   public boolean removeCashBalances(CashBalance element) {
/* 38 */     return this.cashBalances.remove(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return "Strategy [name=" + this.name + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\strategy\Strategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */