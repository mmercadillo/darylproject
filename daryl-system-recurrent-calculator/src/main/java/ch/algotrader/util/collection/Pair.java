/*     */ package ch.algotrader.util.collection;
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
/*     */ public final class Pair<First, Second>
/*     */   implements Serializable
/*     */ {
/*     */   private First first;
/*     */   private Second second;
/*     */   private static final long serialVersionUID = -4168417618011472714L;
/*     */   
/*     */   public Pair(First first, Second second) {
/*  42 */     this.first = first;
/*  43 */     this.second = second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public First getFirst() {
/*  51 */     return this.first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Second getSecond() {
/*  59 */     return this.second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFirst(First first) {
/*  67 */     this.first = first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecond(Second second) {
/*  75 */     this.second = second;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  80 */     if (this == obj) {
/*  81 */       return true;
/*     */     }
/*     */     
/*  84 */     if (!(obj instanceof Pair)) {
/*  85 */       return false;
/*     */     }
/*     */     
/*  88 */     Pair<?, ?> other = (Pair<?, ?>)obj;
/*     */     
/*  90 */     if ((this.first == null) ? (other.first == null) : this.first.equals(other.first)) if ((this.second == null) ? (other.second == null) : this.second.equals(other.second));  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return ((this.first == null) ? 0 : this.first.hashCode()) ^ ((this.second == null) ? 0 : this.second.hashCode());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "Pair [" + this.first + ':' + this.second + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrade\\util\collection\Pair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */