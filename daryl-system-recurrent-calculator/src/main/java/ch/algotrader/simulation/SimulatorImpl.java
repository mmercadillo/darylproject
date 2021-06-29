/*     */ package ch.algotrader.simulation;
/*     */ 
/*     */ import ch.algotrader.entity.Position;
/*     */ import ch.algotrader.entity.Transaction;
/*     */ import ch.algotrader.entity.security.Security;
/*     */ import ch.algotrader.entity.strategy.CashBalance;
/*     */ import ch.algotrader.entity.strategy.Strategy;
/*     */ import ch.algotrader.entity.trade.Fill;
/*     */ import ch.algotrader.entity.trade.LimitOrder;
/*     */ import ch.algotrader.entity.trade.Order;
/*     */ import ch.algotrader.enumeration.Currency;
/*     */ import ch.algotrader.enumeration.Side;
/*     */ import ch.algotrader.enumeration.TransactionType;
/*     */ import ch.algotrader.util.PositionUtil;
/*     */ import ch.algotrader.util.collection.Pair;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections15.MultiMap;
/*     */ import org.apache.commons.collections15.multimap.MultiHashMap;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class SimulatorImpl
/*     */   implements Simulator
/*     */ {
/*  57 */   private static Logger logger = LogManager.getLogger(SimulatorImpl.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final Map<Pair<String, Currency>, CashBalance> cashBalances = new HashMap<Pair<String, Currency>, CashBalance>();
/*  67 */   private final Map<Pair<String, Security>, Position> positionsByStrategyAndSecurity = new HashMap<Pair<String, Security>, Position>();
/*  68 */   private final MultiMap<String, Position> positionsByStrategy = (MultiMap<String, Position>)new MultiHashMap();
/*  69 */   private final MultiMap<Security, Position> positionsBySecurity = (MultiMap<Security, Position>)new MultiHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  77 */     this.positionsByStrategyAndSecurity.clear();
/*  78 */     this.positionsByStrategy.clear();
/*  79 */     this.positionsBySecurity.clear();
/*  80 */     this.cashBalances.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createCashBalance(String strategyName, Currency currency, BigDecimal amount) {
/*  88 */     if (findCashBalanceByStrategyAndCurrency(strategyName, currency) != null) {
/*  89 */       throw new IllegalStateException("cashBalance already exists");
/*     */     }
/*     */     
/*  92 */     Strategy strategy = new Strategy(strategyName);
/*     */     
/*  94 */     CashBalance cashBalance = new CashBalance(currency, strategy);
/*  95 */     cashBalance.setAmount(amount);
/*     */     
/*  97 */     createCashBalance(cashBalance);
/*     */     
/*  99 */     logger.debug("created cashBalance: " + cashBalance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendOrder(LimitOrder order) {
/* 108 */     Validate.notNull(order.getStrategy(), "missing strategy for order " + order);
/* 109 */     Validate.notNull(order.getSecurity(), "missing security for order " + order);
/*     */ 
/*     */     
/* 112 */     Fill fill = new Fill();
/* 113 */     fill.setSide(order.getSide());
/* 114 */     fill.setQuantity(order.getQuantity());
/* 115 */     fill.setPrice(order.getLimit());
/* 116 */     fill.setOrder((Order)order);
/*     */ 
/*     */     
/* 119 */     createTransaction(fill);
/*     */   }
/*     */ 
/*     */   
/*     */   private Transaction createTransaction(Fill fill) {
/* 124 */     Order order = fill.getOrder();
/* 125 */     Security security = order.getSecurity();
/* 126 */     Strategy strategy = order.getStrategy();
/*     */     
/* 128 */     TransactionType transactionType = Side.BUY.equals(fill.getSide()) ? TransactionType.BUY : TransactionType.SELL;
/* 129 */     long quantity = Side.BUY.equals(fill.getSide()) ? fill.getQuantity() : -fill.getQuantity();
/*     */     
/* 131 */     Transaction transaction = new Transaction();
/* 132 */     transaction.setQuantity(quantity);
/* 133 */     transaction.setPrice(fill.getPrice());
/* 134 */     transaction.setType(transactionType);
/* 135 */     transaction.setSecurity(security);
/* 136 */     transaction.setStrategy(strategy);
/* 137 */     transaction.setCurrency(security.getCurrency());
/*     */     
/* 139 */     persistTransaction(transaction);
/*     */     
/* 141 */     return transaction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Position persistTransaction(Transaction transaction) {
/* 151 */     Position position = findPositionByStrategyAndSecurity(transaction.getStrategy().getName(), transaction.getSecurity());
/* 152 */     if (position == null) {
/*     */       
/* 154 */       position = PositionUtil.processFirstTransaction(transaction);
/*     */       
/* 156 */       createPosition(position);
/*     */ 
/*     */       
/* 159 */       transaction.setPosition(position);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 164 */       PositionUtil.processTransaction(position, transaction);
/*     */ 
/*     */       
/* 167 */       transaction.setPosition(position);
/*     */     } 
/*     */ 
/*     */     
/* 171 */     processTransaction(transaction);
/*     */     
/* 173 */     logger.debug("executed transaction: " + transaction);
/*     */     
/* 175 */     return position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processTransaction(Transaction transaction) {
/* 184 */     processAmount(transaction.getStrategy(), transaction.getCurrency(), transaction.getNetValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processAmount(Strategy strategy, Currency currency, BigDecimal amount) {
/* 192 */     CashBalance cashBalance = findCashBalanceByStrategyAndCurrency(strategy.getName(), currency);
/*     */ 
/*     */     
/* 195 */     if (cashBalance == null) {
/*     */       
/* 197 */       cashBalance = new CashBalance(currency, strategy);
/*     */ 
/*     */       
/* 200 */       cashBalance.setAmount(amount);
/*     */       
/* 202 */       createCashBalance(cashBalance);
/*     */     }
/*     */     else {
/*     */       
/* 206 */       cashBalance.setAmount(cashBalance.getAmount().add(amount));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void createPosition(Position position) {
/* 212 */     String name = position.getStrategy().getName();
/* 213 */     Security security = position.getSecurity();
/*     */     
/* 215 */     this.positionsByStrategyAndSecurity.put(new Pair(name, security), position);
/* 216 */     this.positionsByStrategy.put(name, position);
/* 217 */     this.positionsBySecurity.put(security, position);
/*     */   }
/*     */   
/*     */   private void createCashBalance(CashBalance cashBalance) {
/* 221 */     this.cashBalances.put(new Pair(cashBalance.getStrategy().getName(), cashBalance.getCurrency()), cashBalance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Position> findAllPositions() {
/* 228 */     return this.positionsByStrategy.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Position findPositionByStrategyAndSecurity(String strategyName, Security security) {
/* 235 */     return this.positionsByStrategyAndSecurity.get(new Pair(strategyName, security));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Position> findPositionsByStrategy(String strategyName) {
/* 242 */     return this.positionsByStrategy.get(strategyName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Position> findPositionsBySecurity(Security security) {
/* 249 */     return this.positionsBySecurity.get(security);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CashBalance findCashBalanceByStrategyAndCurrency(String strategyName, Currency currency) {
/* 256 */     return this.cashBalances.get(new Pair(strategyName, currency));
/*     */   }
/*     */ }


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\simulation\SimulatorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */