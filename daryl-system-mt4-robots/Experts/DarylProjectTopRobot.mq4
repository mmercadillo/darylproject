//+------------------------------------------------------------------+
//|                                         DarylProjectTopRobot.mq4 |
//|                             Copyright 2020, Daryl System Project |
//|                                      https://www.darylsystem.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, Daryl System Project."
#property link      "http://www.darylsystem.com"
#property version   "1.02"
#property strict

#include <mql4-http.mqh>
#include <DarylProjectCommon.mqh>
#include <jason.mqh>
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+



double Lotes = 0.01;
bool Parar = false;
int HoraInicio = 0;
int HoraFin = 24;

extern int TopRobots = 2;
string url = "";


int OnInit(){
   EventSetTimer(5);
   
   url = URL_BASE + "/mt/ordenes/" + TopRobots + "/";
   
   return(INIT_SUCCEEDED);
}

//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason){
   //---
   EventKillTimer();
}

//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+

void OnTick(){
   //---
}

long time = -1;
void OnTimer(){
   bool onTime = Hour() >= HoraInicio && Hour() <= HoraFin;
   if(onTime == true){
      
      //Print(url);
      string str = httpGET(url+AccountInfoInteger(ACCOUNT_LOGIN));
      // Object
      CJAVal json;
      
      // Load in and deserialize the data
      json.Deserialize(str);
      Print(str);;
      string listaRobotsActivos = "Robots Activos: \n";
      
      //Cerramos las operaciones q ya no existan en el json
      //Recorremos las oeraciones
      //Print(url);
      if(json.Size() > 0){
         //Print("ENTRAAAAAAAA --- " + json.Size());
         for (int pos = OrdersTotal() - 1; pos >= 0; pos--) {
            if(OrderSelect(pos, SELECT_BY_POS, MODE_TRADES) == true){
               string robot = OrderComment();
               //Recorremos el json buscando el robot
               bool existe = false;
               for(int i = 0; i < json.Size(); i++){
                  string robotJsonBuy = json[i]["robot"].ToStr() + "_" + OP_BUY;
                  string robotJsonSell = json[i]["robot"].ToStr() + "_" + OP_SELL;
                  
                  if(robot == robotJsonBuy || robot == robotJsonSell){
                     existe = true;
                     break;
                  }
               } 
               //No se pq coño no funciona este cacho de código
               //Tiene que recorrer las operaciones y no lo hace bien
               if(existe == false){
                  //Cerramos la operacion
                  if (OrderComment() == robot) {                  
                     if (OrderType() == OP_BUY){
                        if(OrderClose(OrderTicket(), OrderLots(), MarketInfo(OrderSymbol(), MODE_BID), 0.0, Blue) == false){
                           Print("Error al cerrar la operación: ", GetLastError());
                        }
                     }
                     if (OrderType() == OP_SELL){
                        if(!OrderClose(OrderTicket(), OrderLots(), MarketInfo(OrderSymbol(), MODE_ASK), 0.0, Red) == false){
                           Print("Error al cerrar la operación: ", GetLastError());
                        }
                     }  
                  }
               }
            }else{
               //Print("No se ha podido recuperar la operación. Error: ", GetLastError());
            }    
         }
      }
      
      for(int i = 0; i < json.Size(); i++){
         string Comment_Buy = json[i]["robot"].ToStr() + "_" + OP_BUY;
         string Comment_Sell = json[i]["robot"].ToStr() + "_" + OP_SELL;
         string tipoOrden = json[i]["tipoOrden"].ToStr();
         string symbol = json[i]["tipoActivo"].ToStr();
         if(json[i]["robot"].ToStr() != "" && json[i]["tipoOrden"].ToStr() != ""){
            
            bool robotActivo = true;
            
            if(DESACTIVAR_TODOS == true) robotActivo = false;
            
            evaluate(Lotes, onTime, Parar, tipoOrden , Comment_Buy, Comment_Sell, symbol, robotActivo );
            if(robotActivo){
               listaRobotsActivos += (json[i]["robot"].ToStr() + " -- " + tipoOrden + " \n");
            }  
         }
      }
      
      
      
      Comment(listaRobotsActivos);

   }
}

extern bool DESACTIVAR_TODOS = false;
