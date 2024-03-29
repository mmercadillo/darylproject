//+----------------------------------------------------------------------------+
//|                                                            DarylCommon.mqh |
//+----------------------------------------------------------------------------+
//|                                        Built by Miguel Mercadillo González |
//|                                                       info@darylsystem.com |
//+----------------------------------------------------------------------------+


int timerLap = 5;
const string URL_BASE = "http://darylsystem.ddns.net:9090/daryl";
/////////////////
int CountTradesByComment(string txt, int tipo) {
   //RefreshRates()
   int numTrades = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != Symbol() || OrderComment() != txt) continue;
         if (OrderSymbol() == Symbol() && OrderComment() == txt && OrderType() == tipo ){
            numTrades++;
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (numTrades);
}

int CountTrades(int MagicNumber, int tipo) {
   //RefreshRates()
   int numTrades = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == tipo ){
            numTrades++;
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (numTrades);
}

int CountTradesByComment(string symbol, string txt, int tipo) {
   //RefreshRates()
   int numTrades = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != symbol || OrderComment() != txt) continue;
         if (OrderSymbol() == symbol && OrderComment() == txt && OrderType() == tipo ){
            numTrades++;
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (numTrades);
}

int CountTrades(string symbol, int MagicNumber, int tipo) {
   //RefreshRates()
   int numTrades = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != symbol || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == symbol && OrderMagicNumber() == MagicNumber && OrderType() == tipo ){
            numTrades++;
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (numTrades);
}

double CloseThisSymbolAllByComment(string symbol, string txt) {
   RefreshRates();
   double lot = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() == symbol && OrderComment() == txt) {
            lot = OrderLots();
            if (OrderType() == OP_BUY){
               if(OrderClose(OrderTicket(), OrderLots(), MarketInfo(symbol, MODE_BID), 0.0, Blue) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }
            if (OrderType() == OP_SELL){
               if(!OrderClose(OrderTicket(), OrderLots(), MarketInfo(symbol, MODE_ASK), 0.0, Red) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }  
         }
         //Sleep(1000);
      }  
   }
   return lot;
}

double CloseThisSymbolAll(string symbol, int MagicNumber) {
   RefreshRates();
   double lot = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() == symbol && OrderMagicNumber() == MagicNumber) {
            lot = OrderLots();
            if (OrderType() == OP_BUY){
               if(OrderClose(OrderTicket(), OrderLots(), MarketInfo(symbol, MODE_BID), 0.0, Blue) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }
            if (OrderType() == OP_SELL){
               if(!OrderClose(OrderTicket(), OrderLots(), MarketInfo(symbol, MODE_ASK), 0.0, Red) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }  
         }
         //Sleep(1000);
      }  
   }
   return lot;
}

double CloseThisSymbolAllByComment(string txt) {
   RefreshRates();
   double lot = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() == Symbol() && OrderComment() == txt) {
            lot = OrderLots();
            if (OrderType() == OP_BUY){
               if(OrderClose(OrderTicket(), OrderLots(), Bid, 0.0, Blue) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }
            if (OrderType() == OP_SELL){
               if(!OrderClose(OrderTicket(), OrderLots(), Ask, 0.0, Red) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }  
         }
         //Sleep(1000);
      }  
   }
   return lot;
}

double CloseThisSymbolAll(int MagicNumber) {
   RefreshRates();
   double lot = 0.0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber) {
            lot = OrderLots();
            if (OrderType() == OP_BUY){
               if(OrderClose(OrderTicket(), OrderLots(), Bid, 0.0, Blue) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }
            if (OrderType() == OP_SELL){
               if(!OrderClose(OrderTicket(), OrderLots(), Ask, 0.0, Red) == false){
                  Print("Error al cerrar la operación: ", GetLastError());
               }
            }  
         }
         //Sleep(1000);
      }  
   }
   return lot;
}

void evaluate(double lotSize, bool onTime, bool stop, string str, string CommentBuy, string CommentSell, string symbol, bool robotActivo){
   //string fechaHora = TimeToStr(TimeCurrent(), TIME_DATE|TIME_SECONDS);
   //int ops = CountTradesByComment(CommentBuy, OP_BUY) + CountTradesByComment(CommentSell, OP_SELL);
   //Comment("-"+str+"-"+fechaHora+"-"+ops);
   if(str == "CLOSE" || robotActivo == false){
      if(CountTradesByComment(symbol, CommentBuy, OP_BUY) > 0){
         CloseThisSymbolAllByComment(symbol, CommentBuy);
      }
      //Cerramos las ventas
      if(CountTradesByComment(symbol, CommentSell, OP_SELL) > 0){
         CloseThisSymbolAllByComment(symbol, CommentSell);
      }      
   }
      
   if(str == "SELL"){
      //Cerramos las compras
      if(CountTradesByComment(symbol, CommentBuy, OP_BUY) > 0){
         CloseThisSymbolAllByComment(symbol, CommentBuy);
      }
   }
   if(str == "BUY"){
      //Cerramos las ventas
      if(CountTradesByComment(symbol, CommentSell, OP_SELL) > 0){
         CloseThisSymbolAllByComment(symbol, CommentSell);
      }      
   }

   if(str != ""){   
      if(str == "BUY" && onTime == true && stop == false && robotActivo == true){
         //Cerramos las ventas
         RefreshRates();
         if(CountTradesByComment(symbol, CommentBuy, OP_BUY) == 0){
            
            int res = OrderSend(symbol, OP_BUY, lotSize, MarketInfo(symbol, MODE_ASK), 0, 0, 0, CommentBuy, NULL, 0, 0);
            if(res < 0){
               Print("Operacion no se ha podido dar de alta");
            }
         }
      }
      
      if(str == "SELL" && onTime == true && stop == false && robotActivo == true){
         //Cerramos las compras
         //if(CountTrades(MagicBuy, OP_BUY) > 0) CloseThisSymbolAll(MagicBuy);
         //Sleep(2000);
         RefreshRates();
         if(CountTradesByComment(symbol, CommentSell, OP_SELL) == 0){
            //Vendemos
            //Print("Vendemos");
            
            int res2 = OrderSend(symbol, OP_SELL, lotSize, MarketInfo(symbol, MODE_BID), 0, 0, 0, CommentSell, NULL, 0, 0);
            if(res2 < 0){
               Print("Operacion no se ha podido dar de alta");
            }else{                              
            }    
            
         }

      }
      
   }else{
      Comment("ESPERANDO...");
   }
   int error = GetLastError();
   if(error > 0){
      Print(GetLastError());
   }
}

double CalcProfit() {
   //RefreshRates()
   double profit = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
     
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         int orderticket = OrderTicket();
         if (OrderSymbol() != Symbol()) continue;
         if (OrderSymbol() == Symbol()){
            profit += OrderProfit();
         }  
      }else{
         //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
      }    
   }
   return (profit);
}

double FindLastBuyPrice(int MagicNumber) {
   RefreshRates();
   double price = 0.0;
   int ticket = 0;
   int ticketAux = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {

      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == OP_BUY) {
            ticket = OrderTicket();
            if (ticket > ticketAux) {
               price = OrderOpenPrice();
               ticketAux = ticket;
            }
         }
      }else{
         Print("Error al recuperar la operación: ", GetLastError());
      }  
   }
   return (price);
}

double FindLastSellPrice(int MagicNumber) {
   RefreshRates();
   double price = 0.0;
   int ticket = 0;
   int ticketAux = 0;
   for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
      if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
         if (OrderSymbol() != Symbol() || OrderMagicNumber() != MagicNumber) continue;
         if (OrderSymbol() == Symbol() && OrderMagicNumber() == MagicNumber && OrderType() == OP_SELL) {
            ticket = OrderTicket();
            if (ticket > ticketAux) {
               price = OrderOpenPrice();
               ticketAux = ticket;
            }
         }
      }else{
         Print("Error al recuperar la operación: ", GetLastError());
      }  
   }
   return (price);
}

double AmplitudActual(){

   return MathAbs(iHigh(Symbol(), PERIOD_D1, 0) - iLow(Symbol(), PERIOD_D1, 0));
   
}

double PuntoMedio(int posicion){

   return iLow(Symbol(), PERIOD_D1, posicion) + MathAbs((iHigh(Symbol(), PERIOD_D1, posicion) - iLow(Symbol(), PERIOD_D1, posicion)) /2);
   
}


void drawVerticalLine(int barsBack, color clr) {
   
   string lineName = "Line"+ IntegerToString(Time[barsBack]);
   
   if (ObjectFind(lineName) != 0) {
      ObjectCreate(lineName,OBJ_VLINE,0,Time[barsBack],0);
      ObjectSet(lineName,OBJPROP_COLOR, clr);
      ObjectSet(lineName,OBJPROP_WIDTH,1);
      ObjectSet(lineName,OBJPROP_STYLE,STYLE_DOT);
   }
   
}

void showText(string name, string texto, color clr, int yDistance){
   
   
   ObjectCreate(name, OBJ_LABEL, 0, 0, 0);
   ObjectSetText(name,texto, 15, "Verdana", clr);
   ObjectSet(name, OBJPROP_CORNER, 1);
   ObjectSet(name, OBJPROP_XDISTANCE, 20);
   ObjectSet(name, OBJPROP_YDISTANCE, yDistance);
}