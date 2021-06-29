/*     */ package ch.algotrader.vo;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class TradePerformanceVO
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5023632083826173592L;
/*     */   private double profit;
/*     */   private boolean setProfit = false;
/*     */   private double profitPct;
/*     */   private boolean setProfitPct = false;
/*     */   private boolean winning;
/*     */   private boolean setWinning = false;
/*     */   
/*     */   public TradePerformanceVO() {}
/*     */   
/*     */   public TradePerformanceVO(double profitIn, double profitPctIn, boolean winningIn) {
/*  75 */     this.profit = profitIn;
/*  76 */     this.setProfit = true;
/*  77 */     this.profitPct = profitPctIn;
/*  78 */     this.setProfitPct = true;
/*  79 */     this.winning = winningIn;
/*  80 */     this.setWinning = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TradePerformanceVO(TradePerformanceVO otherBean) {
/*  91 */     this.profit = otherBean.getProfit();
/*  92 */     this.setProfit = true;
/*  93 */     this.profitPct = otherBean.getProfitPct();
/*  94 */     this.setProfitPct = true;
/*  95 */     this.winning = otherBean.isWinning();
/*  96 */     this.setWinning = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getProfit() {
/* 105 */     return this.profit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProfit(double value) {
/* 114 */     this.profit = value;
/* 115 */     this.setProfit = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSetProfit() {
/* 124 */     return this.setProfit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getProfitPct() {
/* 133 */     return this.profitPct;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProfitPct(double value) {
/* 142 */     this.profitPct = value;
/* 143 */     this.setProfitPct = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSetProfitPct() {
/* 152 */     return this.setProfitPct;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWinning() {
/* 161 */     return this.winning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean getWinning() {
/* 172 */     return this.winning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWinning(boolean value) {
/* 181 */     this.winning = value;
/* 182 */     this.setWinning = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSetWinning() {
/* 191 */     return this.setWinning;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 197 */     StringBuilder builder = new StringBuilder();
/* 198 */     builder.append("TradePerformanceVO [profit=");
/* 199 */     builder.append(this.profit);
/* 200 */     builder.append(", setProfit=");
/* 201 */     builder.append(this.setProfit);
/* 202 */     builder.append(", profitPct=");
/* 203 */     builder.append(this.profitPct);
/* 204 */     builder.append(", setProfitPct=");
/* 205 */     builder.append(this.setProfitPct);
/* 206 */     builder.append(", winning=");
/* 207 */     builder.append(this.winning);
/* 208 */     builder.append(", setWinning=");
/* 209 */     builder.append(this.setWinning);
/* 210 */     builder.append("]");
/*     */     
/* 212 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\vo\TradePerformanceVO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */