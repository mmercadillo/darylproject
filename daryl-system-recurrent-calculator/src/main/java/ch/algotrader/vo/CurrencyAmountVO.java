/*     */ package ch.algotrader.vo;
/*     */ 
/*     */ import ch.algotrader.enumeration.Currency;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CurrencyAmountVO
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -287820727140021514L;
/*     */   private Currency currency;
/*     */   private BigDecimal amount;
/*     */   
/*     */   public CurrencyAmountVO() {}
/*     */   
/*     */   public CurrencyAmountVO(Currency currencyIn, BigDecimal amountIn) {
/*  57 */     this.currency = currencyIn;
/*  58 */     this.amount = amountIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CurrencyAmountVO(CurrencyAmountVO otherBean) {
/*  69 */     this.currency = otherBean.getCurrency();
/*  70 */     this.amount = otherBean.getAmount();
/*     */   }
/*     */ 
/*     */   
/*     */   public Currency getCurrency() {
/*  75 */     return this.currency;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrency(Currency value) {
/*  80 */     this.currency = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getAmount() {
/*  85 */     return this.amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAmount(BigDecimal value) {
/*  90 */     this.amount = value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     StringBuilder builder = new StringBuilder();
/*  97 */     builder.append("CurrencyAmountVO [currency=");
/*  98 */     builder.append(this.currency);
/*  99 */     builder.append(", amount=");
/* 100 */     builder.append(this.amount);
/* 101 */     builder.append("]");
/*     */     
/* 103 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\vo\CurrencyAmountVO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */