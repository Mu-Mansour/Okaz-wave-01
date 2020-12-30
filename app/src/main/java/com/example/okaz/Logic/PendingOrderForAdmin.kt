package com.example.okaz.Logic

data class PendingOrderForAdmin(val Admin:String?
,val  PhoneToContact:String?,val   WhoOrdered:String?,val id:String?
,val  price:String?,val theAddress:String?,val theReceiver:String?,val   Items:Map<String,OrderItem>?) {
constructor():this(null,null,null,null,null,null,null,null,)
}