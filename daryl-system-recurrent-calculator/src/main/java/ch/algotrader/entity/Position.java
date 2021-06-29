/*     */ package ch.algotrader.entity;
/*     */ 
/*     */ import ch.algotrader.entity.security.Security;
/*     */ import ch.algotrader.entity.strategy.Strategy;
/*     */ import ch.algotrader.enumeration.Direction;
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
/*     */ public class Position
/*     */ {
/*     */   private long quantity;
/*     */   private double cost;
/*     */   private double realizedPL;
/*     */   private Strategy strategy;
/*     */   private Security security;
/*     */   
/*     */   public Position(Strategy strategy, Security security) {
/*  38 */     this.strategy = strategy;
/*  39 */     this.security = security;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getQuantity() {
/*  46 */     return this.quantity;
/*     */   }
/*     */   
/*     */   public void setQuantity(long quantity) {
/*  50 */     this.quantity = quantity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCost() {
/*  57 */     return this.cost;
/*     */   }
/*     */   
/*     */   public void setCost(double cost) {
/*  61 */     this.cost = cost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRealizedPL() {
/*  68 */     return this.realizedPL;
/*     */   }
/*     */   
/*     */   public void setRealizedPL(double realizedPL) {
/*  72 */     this.realizedPL = realizedPL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Strategy getStrategy() {
/*  80 */     return this.strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Security getSecurity() {
/*  87 */     return this.security;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAveragePrice() {
/*  92 */     return getCost() / getQuantity();
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getDirection() {
/*  97 */     if (getQuantity() < 0L)
/*  98 */       return Direction.SHORT; 
/*  99 */     if (getQuantity() > 0L) {
/* 100 */       return Direction.LONG;
/*     */     }
/* 102 */     return Direction.FLAT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     return "Position [strategy=" + this.strategy + ", security=" + this.security + ", quantity=" + this.quantity + ", cost=" + this.cost + ", realizedPL=" + this.realizedPL + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\entity\Position.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */