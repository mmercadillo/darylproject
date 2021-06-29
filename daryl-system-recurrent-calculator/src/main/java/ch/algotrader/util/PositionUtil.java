/*     */ package ch.algotrader.util;
/*     */ 
/*     */ import ch.algotrader.entity.Position;
/*     */ import ch.algotrader.entity.Transaction;
/*     */ import ch.algotrader.enumeration.Direction;
/*     */ import ch.algotrader.vo.TradePerformanceVO;
/*     */ import org.apache.commons.math.util.MathUtils;
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
/*     */ public class PositionUtil
/*     */ {
/*     */   public static Position processFirstTransaction(Transaction transaction) {
/*  41 */     double cost = -transaction.getNetValueDouble();
/*     */     
/*  43 */     Position position = new Position(transaction.getStrategy(), transaction.getSecurity());
/*     */     
/*  45 */     position.setQuantity(transaction.getQuantity());
/*  46 */     position.setCost(cost);
/*  47 */     position.setRealizedPL(0.0D);
/*     */     
/*  49 */     return position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TradePerformanceVO processTransaction(Position position, Transaction transaction) {
/*  60 */     long oldQty = position.getQuantity();
/*  61 */     double oldCost = position.getCost();
/*  62 */     double oldRealizedPL = position.getRealizedPL();
/*  63 */     double oldAvgPrice = position.getAveragePrice();
/*     */ 
/*     */     
/*  66 */     long qty = transaction.getQuantity();
/*  67 */     double price = transaction.getPrice().doubleValue();
/*  68 */     double totalCharges = 0.0D;
/*  69 */     double contractSize = 1.0D;
/*     */ 
/*     */     
/*  72 */     long closingQty = (MathUtils.sign(oldQty) != MathUtils.sign(qty)) ? (Math.min(Math.abs(oldQty), Math.abs(qty)) * MathUtils.sign(qty)) : 0L;
/*  73 */     long openingQty = (MathUtils.sign(oldQty) == MathUtils.sign(qty)) ? qty : (qty - closingQty);
/*     */ 
/*     */     
/*  76 */     long newQty = oldQty + qty;
/*  77 */     double newCost = oldCost + openingQty * contractSize * price;
/*  78 */     double newRealizedPL = oldRealizedPL;
/*     */ 
/*     */     
/*  81 */     if (closingQty != 0L) {
/*  82 */       newCost += closingQty * contractSize * oldAvgPrice;
/*  83 */       newRealizedPL += closingQty * contractSize * (oldAvgPrice - price);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     if (openingQty != 0L) {
/*  88 */       newCost += totalCharges * openingQty / qty;
/*     */     }
/*     */     
/*  91 */     if (closingQty != 0L) {
/*  92 */       newRealizedPL -= totalCharges * closingQty / qty;
/*     */     }
/*     */ 
/*     */     
/*  96 */     TradePerformanceVO tradePerformance = null;
/*  97 */     if (Long.signum(position.getQuantity()) * Long.signum(transaction.getQuantity()) == -1) {
/*     */       double cost, value;
/*     */       
/* 100 */       if (Math.abs(transaction.getQuantity()) <= Math.abs(position.getQuantity())) {
/* 101 */         cost = position.getCost() * Math.abs(transaction.getQuantity() / position.getQuantity());
/* 102 */         value = transaction.getNetValueDouble();
/*     */       } else {
/* 104 */         cost = position.getCost();
/* 105 */         value = transaction.getNetValueDouble() * Math.abs(position.getQuantity() / transaction.getQuantity());
/*     */       } 
/*     */       
/* 108 */       double profit = value - cost;
/* 109 */       double profitPct = Direction.LONG.equals(position.getDirection()) ? ((value - cost) / cost) : ((cost - value) / cost);
/*     */       
/* 111 */       tradePerformance = new TradePerformanceVO(profit, profitPct, (profit > 0.0D));
/*     */     } 
/*     */ 
/*     */     
/* 115 */     position.setQuantity(newQty);
/* 116 */     position.setCost(newCost);
/* 117 */     position.setRealizedPL(newRealizedPL);
/*     */     
/* 119 */     return tradePerformance;
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrade\\util\PositionUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */