/*     */ package ch.algotrader.entity;
/*     */ 
/*     */ import ch.algotrader.entity.security.Security;
/*     */ import ch.algotrader.entity.strategy.Strategy;
/*     */ import ch.algotrader.enumeration.Currency;
/*     */ import ch.algotrader.enumeration.TransactionType;
/*     */ import ch.algotrader.util.RoundUtil;
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
/*     */ public class Transaction
/*     */ {
/*     */   private long quantity;
/*     */   private BigDecimal price;
/*     */   private Currency currency;
/*     */   private TransactionType type;
/*     */   private Position position;
/*     */   private Security security;
/*     */   private Strategy strategy;
/*     */   
/*     */   public long getQuantity() {
/*  95 */     return this.quantity;
/*     */   }
/*     */   
/*     */   public void setQuantity(long quantity) {
/*  99 */     this.quantity = quantity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getPrice() {
/* 106 */     return this.price;
/*     */   }
/*     */   
/*     */   public void setPrice(BigDecimal price) {
/* 110 */     this.price = price;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Currency getCurrency() {
/* 117 */     return this.currency;
/*     */   }
/*     */   
/*     */   public void setCurrency(Currency currency) {
/* 121 */     this.currency = currency;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransactionType getType() {
/* 128 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(TransactionType type) {
/* 132 */     this.type = type;
/*     */   }
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
/*     */   public Position getPosition() {
/* 159 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setPosition(Position position) {
/* 163 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Security getSecurity() {
/* 170 */     return this.security;
/*     */   }
/*     */   
/*     */   public void setSecurity(Security security) {
/* 174 */     this.security = security;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Strategy getStrategy() {
/* 182 */     return this.strategy;
/*     */   }
/*     */   
/*     */   public void setStrategy(Strategy strategy) {
/* 186 */     this.strategy = strategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrade() {
/* 191 */     if (TransactionType.BUY.equals(getType()) || TransactionType.SELL.equals(getType())) {
/* 192 */       return true;
/*     */     }
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getGrossValue() {
/* 200 */     return RoundUtil.getBigDecimal(getNetValueDouble(), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getGrossValueDouble() {
/* 205 */     if (isTrade()) {
/* 206 */       return -getQuantity() * getPrice().doubleValue();
/*     */     }
/* 208 */     return getQuantity() * getPrice().doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getNetValue() {
/* 214 */     return RoundUtil.getBigDecimal(getNetValueDouble(), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getNetValueDouble() {
/* 219 */     return getGrossValueDouble();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 224 */     return "Transaction [quantity=" + this.quantity + ", price=" + this.price + ", currency=" + this.currency + ", type=" + this.type + ", security=" + this.security + ", strategy=" + this.strategy + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\Transaction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */