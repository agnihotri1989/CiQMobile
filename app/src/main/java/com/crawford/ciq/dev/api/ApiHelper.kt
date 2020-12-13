package com.crawford.ciq.dev.api

import com.google.gson.JsonObject

class ApiHelper(val apiService: ApiService) {

//    val bearerotoken: String =
//        "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik0wVTNPRGt3UkVWR1JFTkdPRVpETkRSRFJUQXpOekl3UlVFNFF6SkJOekZFTVRnNE9ETXlNQSJ9.eyJpc3MiOiJodHRwczovL2NyYXdmb3JkLWRldi5hdXRoMC5jb20vIiwic3ViIjoiVlRQN0d0ZHV3QTg5ZjQzcEFxMklPWmhuSTlaMUhhQmdAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vcWEtY21zYXBpLmNyYXdjby5jb20iLCJpYXQiOjE2MDQwNDY2MDUsImV4cCI6MTYwNDEzMzAwNSwiYXpwIjoiVlRQN0d0ZHV3QTg5ZjQzcEFxMklPWmhuSTlaMUhhQmciLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.F34b7rmBNLHRwPkJP9ZLSEngD9anru4TEa-O1Cc9WgJrZjamAlyYl08mXa2L9lm0AfKvZ8rQ4-j4PDKNO7NjyQBZQPBooTnV9vtESNyFv0D9ZyIdMRs4KunkKbqnRouOwvjdsVIL1lkB4Uy3DG-mwmXVMWKSBk3ItOPmCnb55KtGUu2wjuZKhp0LEqni9GnjIn_hq3_wrTIK5-PMaB7okK1ezKGSAPVhBoHnZmmFBhAatPk_RHfw4tI6s8Rxquam6Je2_DHxYenUIuiHYvCGpZvvXdMfWUn6GVIzPHFlzYzeSvj-W-dGBYE0kPzU5JmQpdegsv74beKZlki6tD1UBQ"

//val bearerotoken: String =

    suspend fun getbearertoken(jsonObject: JsonObject) = apiService.getbearertoken(jsonObject)
    suspend fun getClaims(adjsterId: String,claimId:Int,bearertoken:String) =
        apiService.getClaims(adjsterId,claimId, bearertoken)
    suspend fun getinsuredaddress(claimId:Int,bearertoken:String) =
        apiService.getinsuredaddress(claimId, bearertoken)
    suspend fun getclaimantaddress(ClaimantId:Int,ClaimId:Int,bearertoken:String) =
        apiService.getclaimantaddress(ClaimantId,ClaimId, bearertoken)
    suspend fun getmyClaims(adjsterId: String,bearertoken:String) =
        apiService.getmyClaims(adjsterId, bearertoken)
    suspend fun getdockets(ClaimId: Int,bearertoken:String) =
        apiService.getdockets(ClaimId, bearertoken)
    suspend fun getpostdocketresult(jsonbody:JsonObject,bearertoken:String) =
        apiService.getpostdocketresult(jsonbody, bearertoken)
    suspend fun getclaimant(ClaimId: Int,bearertoken:String) =
        apiService.getclaimant(ClaimId, bearertoken)
    suspend fun getuser(NetworkId:String,bearertoken: String)= apiService.getUser(NetworkId,bearertoken)
    suspend fun getClaimSubscription(ClaimId: Int,bearertoken:String) =
        apiService.getClaimSubscription(ClaimId, bearertoken)
    suspend fun gettimecode(bearertoken:String) =
        apiService.gettimecode( bearertoken)
    suspend fun getexpensecode(bearertoken:String) =
        apiService.getexpensecode( bearertoken)
    suspend fun gettimecodedetails(code: String,bearertoken:String) =
        apiService.gettimecodedetails(code, bearertoken)
    suspend fun getnote(claimId: Int,DocketId:Int,bearertoken:String) =
        apiService.getnote(claimId, DocketId,bearertoken)
}