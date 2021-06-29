package ch.algotrader.simulation;

import ch.algotrader.entity.Position;
import ch.algotrader.entity.security.Security;
import ch.algotrader.entity.strategy.CashBalance;
import ch.algotrader.entity.trade.LimitOrder;
import ch.algotrader.enumeration.Currency;
import java.math.BigDecimal;
import java.util.Collection;

public interface Simulator {
  void clear();
  
  void createCashBalance(String paramString, Currency paramCurrency, BigDecimal paramBigDecimal);
  
  void sendOrder(LimitOrder paramLimitOrder);
  
  Collection<Position> findAllPositions();
  
  Position findPositionByStrategyAndSecurity(String paramString, Security paramSecurity);
  
  Collection<Position> findPositionsByStrategy(String paramString);
  
  Collection<Position> findPositionsBySecurity(Security paramSecurity);
  
  CashBalance findCashBalanceByStrategyAndCurrency(String paramString, Currency paramCurrency);
}


/* Location:              C:\Users\mmercadi\Downloads\JRecurrentMDFA-master\JRecurrentMDFA-master\jars\algotrader-simulator-0.0.1-SNAPSHOT.jar!\ch\algotrader\simulation\Simulator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */