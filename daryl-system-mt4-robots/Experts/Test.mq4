
#property copyright "Malex_Xander"
#property link      ""
#property version   "1.00"
#property strict
#property show_inputs
#include <Custom_Functions.mqh>
extern double Lots = 0.05;
extern double Magic_Number = 1982;


// Estrategia London Breakout

      


void OnTick ()
{
   
   
   
   if (!Check_If_Open__Orders_MNB(Magic_Number)==true && Check_London_Market() == true && DayOfWeek()!=1)
   {
      
      datetime hoy = TimeLocal();
      datetime Dia_Anterior = TimeLocal()-86400;
      string Dia_anterior_String = TimeToString (Dia_Anterior);
      string Mes_dia_anterior = StringSubstr(Dia_anterior_String,5,2);
      string dia_anterior = StringSubstr(Dia_anterior_String,8,2);
      string anio_anterior = StringSubstr(Dia_anterior_String,0,4);
      string hoy_str = TimeToString (hoy);
      string Mes = StringSubstr(hoy_str,5,2);
      string dia = StringSubstr(hoy_str,8,2);
      string anio = StringSubstr(hoy_str,0,4);
      
      
     
      string Comienzo_Asiatico__str = anio_anterior + "." + Mes_dia_anterior + "." + dia_anterior  +  " 23:59";
      datetime Comienzo_Asiatico__DT = StrToTime (Comienzo_Asiatico__str);
      string Cierre_Asiatico__str = anio + "." + Mes + "." + dia  +  " 10:00";
      datetime Cierre_Asiatico__DT = StrToTime (Cierre_Asiatico__str);
      string Time_expiration_str = anio + "." + Mes + "." + dia  +  " 17:00";
      datetime Time_expiration_DT = StrToTime (Time_expiration_str);
      
      int n_Vela_Comienzo = iBarShift(NULL,PERIOD_M15,Comienzo_Asiatico__DT,true);
      int n_Vela_Final = iBarShift(NULL,PERIOD_M15,Cierre_Asiatico__DT,true);
      int n_Velas = n_Vela_Comienzo - n_Vela_Final;
      
      
      
      int Highest_Candle_Asian_Market = iHighest(NULL,PERIOD_M15,MODE_HIGH,n_Velas,n_Vela_Final);
      int Lowest_Candle_Asian_Market = iLowest (NULL,PERIOD_M15,MODE_LOW,n_Velas,n_Vela_Final);
      double Max_Price_Asian_Market =  NormalizeDouble(High [Highest_Candle_Asian_Market],_Digits);
      double Min_Price_Asian_Market =  NormalizeDouble(Low [Lowest_Candle_Asian_Market],_Digits);
      double Rango_Precio = NormalizeDouble ( Max_Price_Asian_Market - Min_Price_Asian_Market,_Digits);
      double Valor_Medio = Rango_Precio/2;
      double Valor_entrada = NormalizeDouble(Min_Price_Asian_Market + Valor_Medio,_Digits);
      double Take_Profit_Value = NormalizeDouble( Valor_Medio *1.2,_Digits);
      double Take_Profit_Price_buys = NormalizeDouble (Valor_entrada + Take_Profit_Value,_Digits);
      double Take_Profit_Price_sells = NormalizeDouble (Valor_entrada- Take_Profit_Value,_Digits);
      
      bool Condicion_Compras = Ask < Min_Price_Asian_Market;
      bool Condicion_Ventas = Bid > Max_Price_Asian_Market;
      
      Comment(" Max precio Asian " + Max_Price_Asian_Market, " Min precio " + Min_Price_Asian_Market);
      
      
         if ( Condicion_Compras == true )
         {
            
            OrderSend (NULL,OP_BUYSTOP,Lots,Valor_entrada,10,Min_Price_Asian_Market,Take_Profit_Price_buys," TRON " + Magic_Number,Magic_Number,Time_expiration_DT,clrRed);
         
         
         
         }
         
         
         else if ( Condicion_Ventas== true)
         
         
         {
         
             OrderSend (NULL,OP_SELLSTOP,Lots,Valor_entrada,10,Max_Price_Asian_Market,Take_Profit_Price_sells," TRON " + Magic_Number,Magic_Number,Time_expiration_DT,clrRed);
         
         }
         

      
      
      
      
   }
   
     


}

el codigo con los extern y eso

y ahi van las funciones

// Funcion para calculat los Pips segun Par de Divisas

double Get_The_Pip_Value_From_Digits(int Digits)

  {

   if(Digits >=4)
     {

      return 0.001;



     }
   else
     {
      return 0.01;
     }
  }

//-----------------------------------

// Funcion para ver si hay ya una opercion abierta por medio del Magic number


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool  Check_If_Open__Orders_MNB(int Magic_Number)
  {
   int Open_Orders = OrdersTotal();

   for(int i=0; i < Open_Orders; i++)
     {
      if(OrderSelect(i,SELECT_BY_POS)== true)
        {
         if(OrderMagicNumber()== Magic_Number)
           {

            return true;

           }


        }



     }

   return false;





  }

//--------------------------------------------------


// Funcion para filtrar la franja horaria de Trading

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool Check_Time_To_Trade()
  {
   string Start_Time_Trade = " 08:00";
   string Stop_Time_Trading = "19:00";


   datetime Start_Time_Trade_TM = StrToTime(Start_Time_Trade);
   datetime Stop_Time_Trading_TM = StrToTime(Stop_Time_Trading);


   bool Trading_Is_Allowed=false;




   if(TimeLocal()> Start_Time_Trade_TM && TimeLocal()< Stop_Time_Trading_TM)
     {
      return true;
     }

   else
     {
      return  false;

     }


   return false;

  }

//-----------


// Funcion para aumentar el lotaje de entrada si pierde la operacion



//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double  Rise_Lots_If_Loss(int Magic_Number)
{
   double ticket= 0;
   double ticket_Anterior =0;
   double Lots= 0.01;
   double pos= OrdersHistoryTotal();
   for(pos-1;pos>=0;pos--)
   {
      if(OrderSelect(pos,SELECT_BY_POS,MODE_HISTORY)== true)
      {
         if (OrderSymbol() != Symbol() || OrderMagicNumber()!= Magic_Number)
         {
          continue;        
         }
         if (OrderSymbol() == Symbol() && OrderMagicNumber()== Magic_Number)
         {
               ticket = OrderTicket();
               if (ticket > ticket_Anterior)
               {
                  ticket_Anterior=ticket;
                  if (OrderProfit()<0 && OrderLots() <= 0.02)
                  {
                    Lots = OrderLots()+0.01;
                  }
               }
         }
      }
   }
      return Lots;
}




//Calcular valor absoluto del Sread

double  Spread_Absolte_Value ()
{
 double Spread = MathAbs(Ask-Bid);
 return Spread;
}

//--------------------------------

// Calcular lotaje segun cuenta 

double OptimalLotSize(double maxRiskPrc, int maxLossInPips)
{

  double accEquity = AccountEquity();
  Alert("accEquity: " + accEquity);
  
  double lotSize = MarketInfo(NULL,MODE_LOTSIZE);
  Alert("lotSize: " + lotSize);
  
  double tickValue = MarketInfo(NULL,MODE_TICKVALUE);
  
  if(Digits <= 3)
  {
   tickValue = tickValue /100;
  }
  
  Alert("tickValue: " + tickValue);
  
  double maxLossDollar = accEquity * maxRiskPrc;
  Alert("maxLossDollar: " + maxLossDollar);
  
  double maxLossInQuoteCurr = maxLossDollar / tickValue;
  Alert("maxLossInQuoteCurr: " + maxLossInQuoteCurr);
  
  double optimalLotSize = NormalizeDouble(maxLossInQuoteCurr /(maxLossInPips * Get_The_Pip_Value_From_Digits(Digits))/lotSize,2);
  
  return optimalLotSize;
 
}


double OptimalLotSize(double maxRiskPrc, double entryPrice, double stopLoss)
{
   int maxLossInPips = MathAbs(entryPrice - stopLoss)/Get_The_Pip_Value_From_Digits( Digits);
   return OptimalLotSize(maxRiskPrc,maxLossInPips);
}


//------------------------------

// Funcion para calcula mercado  Asiatico

bool Check_Asian_Market()
  {
   string Start_Time_Trade = " 22:00";
   string Stop_Time_Trading = "08:00";


   datetime Start_Time_Trade_TM = StrToTime(Start_Time_Trade);
   datetime Stop_Time_Trading_TM = StrToTime(Stop_Time_Trading);


   bool Trading_Is_Allowed=false;




   if(TimeLocal()> Start_Time_Trade_TM && TimeLocal()< Stop_Time_Trading_TM)
     {
      return true;
     }

   else
     {
      return  false;

     }


   return false;

  }
  
  
  //------------------------------

// Funcion para calcula mercado  Londinense




bool Check_London_Market()
  {
   string Start_Time_Trade = " 09:05";
   string Stop_Time_Trading = "12:00";


   datetime Start_Time_Trade_TM = StrToTime(Start_Time_Trade);
   datetime Stop_Time_Trading_TM = StrToTime(Stop_Time_Trading);


   bool Trading_Is_Allowed=false;




   if(TimeLocal()> Start_Time_Trade_TM && TimeLocal()< Stop_Time_Trading_TM)
     {
      return true;
     }

   else
     {
      return  false;

     }


   return false;

  }