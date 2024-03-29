//+------------------------------------------------------------------+
//|                                           GuardarCierresFull.mq4 |
//|                             Copyright 2020, Daryl System Project |
//|                                      https://www.darylsystem.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2020, MetaQuotes Software Corp."
#property link      "https://www.mql5.com"
#property version   "1.20"
#property strict

#include <mql4-mysql.mqh>



//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+

int OnInit(){
   EventSetTimer(3000);
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason){
   EventKillTimer();
}

long time = -1;
void OnTimer(){

      Comment("GUARDANDO OPERACIONES: " + TimeToStr(TimeCurrent()));
      saveOrdersToMySQL();
      time = iTime(NULL, PERIOD_M1, 0);

}

//+------------------------------------------------------------------+
//Checkeamos la fecha de la operacion
datetime fechaInicio=D'2020.11.23 15:00:00';
int saveOrdersToMySQL(){
   
   
   string  host     = "192.168.0.120";
   string  user     = "daryl";
   string  pass     = "12101492";
   string  dbName   = "daryl";
   
   int     port     = 3306;
   int     socket   = 0;
   int     client   = 0;
   
   int     dbConnectId = 0;
   bool    goodConnect = false;
   
   


       //+-------------------------------------------------------------------
    //| Fetch multiple columns in multiple rows
    //+-------------------------------------------------------------------  
    int contador = 0;

    Print("Buscando nuevas operaciones entre -> " + OrdersHistoryTotal());
    
   goodConnect = init_MySQL(dbConnectId, host, user, pass, dbName, port, socket, client);
    
   if ( !goodConnect ) {
      Print("Error...");
      return (1); // bad connect
   }
    
    for (int pos = OrdersHistoryTotal() - 1; pos >= 0; pos--) {

      //if(contador >= 10) break;
      if(OrderSelect(pos, SELECT_BY_POS, MODE_HISTORY) == true){
      
         //Print("Cheando fecha ... " + OrderCloseTime() + " con " + fechaInicio);
         
         if(fechaInicio > OrderCloseTime()) continue;
         //else Print("MENOR -- SE DA DE ALTA");
         
         string query = StringConcatenate(
                                          "SELECT `fcierre`, `comentario` ",
                                          "FROM `historico_operaciones` WHERE ", 
                                          "`comentario` = '"+OrderComment()+"' AND `fcierre` = '"+OrderCloseTime()+"'" );
         string data[][2];   // important: second dimension size must be equal to the number of columns
         int result = MySQL_FetchArray(dbConnectId, query, data);
         if ( result == 0 ) {
            
            Print(OrderCloseTime() + " - " + OrderComment() + " NO EXISTE - ALTA");
            
            string insertQuery = "INSERT INTO `historico_operaciones` (ticket, cierre, fcierre, comentario, profit, fapertura, apertura, swap, comision) " + 
                        "VALUES " + 
                        "("+OrderTicket()+","+OrderClosePrice()+",\'"+OrderCloseTime()+"\', \'"+OrderComment()+"\',"+OrderProfit()+", \'"+OrderOpenTime()+"\', "+OrderOpenPrice()+","+OrderSwap()+","+OrderCommission()+");";
            
            if ( MySQL_Query(dbConnectId, insertQuery) ) {
               Print("ALTA OK");
               contador++;
            }
            
         }else{
            //Print(OrderCloseTime() + " - " + OrderComment() + " YA EXISTE");
         } 
         
        
      }
    
    
    }
  deinit_MySQL(dbConnectId);
   return 0;

}
       
void OnTick(){   
}
//+------------------------------------------------------------------+
