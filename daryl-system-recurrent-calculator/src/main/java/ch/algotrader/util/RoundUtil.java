/*    */ package ch.algotrader.util;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import org.apache.commons.math.util.MathUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RoundUtil
/*    */ {
/*    */   public static double roundToNextN(double value, double n) {
/* 35 */     return MathUtils.round(value / n, 0) * n;
/*    */   }
/*    */ 
/*    */   
/*    */   public static BigDecimal roundToNextN(BigDecimal value, double n) {
/* 40 */     return getBigDecimal(roundToNextN(value.doubleValue(), n), getDigits(n));
/*    */   }
/*    */ 
/*    */   
/*    */   public static double roundToNextN(double value, double n, int roundingMethod) {
/* 45 */     return MathUtils.round(value / n, 0, roundingMethod) * n;
/*    */   }
/*    */ 
/*    */   
/*    */   public static BigDecimal roundToNextN(BigDecimal value, double n, int roundingMethod) {
/* 50 */     return getBigDecimal(roundToNextN(value.doubleValue(), n, roundingMethod), getDigits(n));
/*    */   }
/*    */ 
/*    */   
/*    */   public static BigDecimal getBigDecimal(double value) {
/* 55 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/* 56 */       return null;
/*    */     }
/* 58 */     BigDecimal decimal = new BigDecimal(value);
/* 59 */     return decimal.setScale(2, 4);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static BigDecimal getBigDecimal(double value, int scale) {
/* 65 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/* 66 */       return null;
/*    */     }
/* 68 */     BigDecimal decimal = new BigDecimal(value);
/* 69 */     return decimal.setScale(scale, 4);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static BigDecimal getBigDecimalNullSafe(Double value) {
/* 75 */     if (value == null) {
/* 76 */       return null;
/*    */     }
/* 78 */     return getBigDecimal(value.doubleValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getDigits(double n) {
/* 83 */     int exponent = -((int)Math.floor(Math.log10(n)));
/* 84 */     int digits = (exponent >= 0) ? exponent : 0;
/* 85 */     return digits;
/*    */   }
/*    */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrade\\util\RoundUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */